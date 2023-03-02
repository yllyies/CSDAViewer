package com.agilent.cdsa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.agilent.cdsa.common.CodeListConstant;
import com.agilent.cdsa.common.util.HttpUtil;
import com.agilent.cdsa.common.util.PythonUtil;
import com.agilent.cdsa.dto.InstrumentDto;
import com.agilent.cdsa.model.InstrumentState;
import com.agilent.cdsa.model.PowerHistory;
import com.agilent.cdsa.repository.PowerHistoryDao;
import com.agilent.cdsa.repository.ProjectDao;
import com.agilent.cdsa.service.InstrumentService;
import com.agilent.cdsa.service.InstrumentStateService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仪器服务方法实现
 *
 * @author lifang
 * @since 2019-09-01
 */
@Service
@Slf4j
public class InstrumentServiceImpl implements InstrumentService {
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private PowerHistoryDao powerHistoryDao;
    @Autowired
    private InstrumentStateService instrumentStateService;
    @Value("${cdsada.interface-url}")
    private String instrumentInfoUrl;

    @Override
    public List<PowerHistory> doFindByIpOrderByCreatedDateDesc(String ip) {
        return powerHistoryDao.findByIp(ip);
    }

    @Override
    public List<PowerHistory> doFindAll() {
        return powerHistoryDao.findAll(Sort.by(Sort.Direction.ASC,"createdDate"));
    }

    @Override
    public void doBatchCreate(List<PowerHistory> instruments) {
        powerHistoryDao.saveAll(instruments);
        powerHistoryDao.flush();
    }

    @Override
    public List<InstrumentDto> doFindThirdPartyInstruments() {
        List<InstrumentDto> result = new ArrayList<>();
        // 获取第三方仪器信息实时功率
        List<PowerHistory> thirdPartyPowerInfo = PythonUtil.doGetInstrumentPower();
        // 查询所有仪器功率历史记录
        List<String> ips = thirdPartyPowerInfo.stream().map(PowerHistory::getIp).distinct().collect(Collectors.toList());
        List<PowerHistory> powerHistoryList = powerHistoryDao.findByIpInOrderByCreatedDateDesc(ips);
        Map<String, List<Double>> ipToPowerHistoriesMap = powerHistoryList.stream().collect(
                Collectors.groupingBy(PowerHistory::getIp, Collectors.mapping(PowerHistory::getPower, Collectors.toList())));

        for (PowerHistory powerHistory : thirdPartyPowerInfo) {
            InstrumentDto instrumentDto = new InstrumentDto();
            instrumentDto.setInstrumentName(powerHistory.getInstrumentName());
            if (ipToPowerHistoriesMap.containsKey(powerHistory.getIp())) {
                String powerLevel = calPowerLevel(powerHistory.getPower(),
                        PythonUtil.doGetInstrumentPowerLevel(ipToPowerHistoriesMap.get(powerHistory.getIp())));
                instrumentDto.setInstrumentState(powerLevel);
                result.add(instrumentDto);
            } else {
                instrumentDto.setInstrumentState(CodeListConstant.INSTRUMENT_STATE_UNKNOWN);
                result.add(instrumentDto);
            }
        }
        return result;
    }

    @Override
    public List<InstrumentDto> doFindInstrumentsByRemote() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        JSONObject jsonObj = JSONObject.fromObject(new HashMap<>());
        HttpEntity<String> request = new HttpEntity<>(jsonObj.toString(), headers);
        String responseStr = HttpUtil.httpRequest(instrumentInfoUrl, HttpMethod.GET, request);

        if (StrUtil.isBlank(responseStr)) {
            log.warn("get data from cdsa extra program api without response -------" + responseStr);
            return new ArrayList<>();
        }
        log.info("cdsa extra program response:" + responseStr);
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(responseStr);
        InstrumentDto[] instrumentStateList = jsonObject.get("array", InstrumentDto[].class);
        List<InstrumentDto> result = Arrays.asList(instrumentStateList);

        if (CollUtil.isEmpty(result)) {
            log.warn("get data from cdsa extra program api without response");
            return new ArrayList<>();
        }
        log.info("get data from cdsa extra program api, total:" + result.size());
        // 处理仪器运行时间，收集 Prerun Running 状态的数据
        List<BigDecimal> instrumentIds = result.stream().map(InstrumentDto::getInstrumentId).map(BigDecimal::new).collect(Collectors.toList());
        List<InstrumentState> instrumentStates = instrumentStateService.doFindByIds(instrumentIds);
        Map<String, Long> instrumentIdToCountMap = instrumentStates.stream().filter(item -> StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_RUNNING) ||
                StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_PRERUN)).collect(Collectors.groupingBy(is -> StrUtil.toString(is.getInstrumentId()), Collectors.mapping(InstrumentState::getInstrumentRuntime, Collectors.counting())));

        this.processDisplayField(result, instrumentIdToCountMap);
        return result;
    }

    /**
     * 处理前端需要展示的冗余字段，
     *
     * @param result 需要返回的仪器信息
     * @param instrumentIdToCountMap 仪器ID对应数量
     */
    private void processDisplayField(List<InstrumentDto> result, Map<String, Long> instrumentIdToCountMap) {
        // 处理运行时间和状态颜色
        for (InstrumentDto instrumentDto : result) {
            // 处理运行时间（分钟）
            if (instrumentIdToCountMap.containsKey(instrumentDto.getInstrumentId())) {
                instrumentDto.setRuntimeString(MapUtil.getLong(instrumentIdToCountMap, instrumentDto.getInstrumentId()) + "分钟");
            }
            // 处理序列运行
            if (null != instrumentDto.getSampleTotal()) {
                instrumentDto.setSequenceInfo(instrumentDto.getCurrentSample() == null ? "" : instrumentDto.getCurrentSample() + " / " + instrumentDto.getSampleTotal());
            }
            // 处理进度条
            if (null != instrumentDto.getCurrentSample() && null != instrumentDto.getSampleTotal()) {
                instrumentDto.setProgressBarWidth(NumberUtil.formatPercent((float) instrumentDto.getCurrentSample() / (float) instrumentDto.getSampleTotal() * 100, 2) + "%");
            }
            // 处理仪器状态颜色
            String instrumentState = instrumentDto.getInstrumentState();
            if (StrUtil.isBlank(instrumentState)) {
                continue;
            }
            switch (instrumentState) {
                case CodeListConstant.INSTRUMENT_STATE_NOT_CONNECT :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_NOT_CONNECT);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_IDLE :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_IDLE);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_ERROR :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_ERROR);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_PRERUN :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_PRERUN);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_RUNNING :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_RUNNING);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_NOT_READY :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_NOT_READY);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_MAINTENANCE_DUE :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_MAINTENANCE_DUE);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_SLEEP :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_SLEEP);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_OFFLINE :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_OFFLINE);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_UNKNOWN :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_UNKNOWN);
                    break;
                default:
                    instrumentDto.setColor("transparent");
            }
        }
    }

    /**
     * 根据实时功率和机器学习仪器计算的档位，划分出仪器当前状态
     *
     * @param power 仪器实时功率
     * @param powerLevelStr 机器学习的功率档位
     * @return 第三方仪器状态
     */
    private String calPowerLevel(Double power, String powerLevelStr) {
        if (!JSONUtil.isJsonArray(powerLevelStr)) {
            return CodeListConstant.INSTRUMENT_STATE_UNKNOWN;
        }
        JSONArray jsonArray = JSONUtil.parseArray(powerLevelStr);
        if (CollUtil.isEmpty(jsonArray)) {
            return CodeListConstant.INSTRUMENT_STATE_UNKNOWN;
        } else {
            Double[] powerLevels = jsonArray.toArray(Double[]::new);
            if (power == null) {
                return CodeListConstant.INSTRUMENT_STATE_UNKNOWN;
            }
            if (power == 0) {
                return CodeListConstant.INSTRUMENT_STATE_OFFLINE;
            } else if (powerLevels[powerLevels.length - 1] - power < 5 && powerLevels[powerLevels.length - 1] - power > -5) {
                return CodeListConstant.INSTRUMENT_STATE_RUNNING;
            }
            for (int i = 1; i < powerLevels.length - 1; i++) {
                if (powerLevels[powerLevels.length - i] - power < 5 && powerLevels[powerLevels.length - i] - power > -5) {
                    return CodeListConstant.INSTRUMENT_STATE_PRERUN;
                }
            }
            return CodeListConstant.INSTRUMENT_STATE_UNKNOWN;
        }
    }
}
