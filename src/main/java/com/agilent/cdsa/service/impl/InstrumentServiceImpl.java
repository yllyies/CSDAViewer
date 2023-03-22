package com.agilent.cdsa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.agilent.cdsa.common.CodeListConstant;
import com.agilent.cdsa.common.util.HttpUtil;
import com.agilent.cdsa.common.util.PythonUtil;
import com.agilent.cdsa.dto.InstrumentDto;
import com.agilent.cdsa.model.PowerHistory;
import com.agilent.cdsa.repository.PowerHistoryDao;
import com.agilent.cdsa.repository.ProjectDao;
import com.agilent.cdsa.service.InstrumentService;
import com.agilent.cdsa.service.InstrumentStateService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
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
    public void doBatchSave(List<PowerHistory> instruments) {
        powerHistoryDao.saveAll(instruments);
        powerHistoryDao.flush();
    }

    @Override
    public void doBatchReplace(List<PowerHistory> powerHistories, Timestamp nowDate) {
        // 往前推三天，查询是否存在数据；
        List<String> ips = powerHistories.stream().map(PowerHistory::getIp).distinct().collect(Collectors.toList());
        Timestamp beforeDate = DateUtil.offsetDay(nowDate, -3).toTimestamp();
        List<PowerHistory> dbData = powerHistoryDao.findByIpInAndCreatedDateEquals(ips, beforeDate);
        Map<String, PowerHistory> dbDataMap = dbData.stream().collect(Collectors.toMap(PowerHistory::getIp, Function.identity(), (k1, k2) -> k2));
        // 遍历待保存数据。存在则更新，不存在则新增；
        List<PowerHistory> toSaveList = new ArrayList<>();
        for (PowerHistory powerHistory : powerHistories) {
            if (dbDataMap.containsKey(powerHistory.getIp())) {
                PowerHistory toSave = dbDataMap.get(powerHistory.getIp());
                toSave.setPower(powerHistory.getPower());
                toSave.setCreatedDate(nowDate);
                toSaveList.add(toSave);
            } else {
                powerHistory.setCreatedDate(nowDate);
                toSaveList.add(powerHistory);
            }
        }
        this.doBatchSave(toSaveList);
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
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(responseStr);
        InstrumentDto[] instrumentStateList = jsonObject.get("instrument_state", InstrumentDto[].class);
        List<InstrumentDto> result = Arrays.asList(instrumentStateList);

        if (CollUtil.isEmpty(result)) {
            log.warn("get data from cdsa extra program api without response");
            return new ArrayList<>();
        }
        // 处理仪器实时状态前端展示字段
        this.processDisplayField(result);
        return result;
    }

    /**
     * 处理前端需要展示的冗余字段：
     * <ol>
     *     <li>序列运行时间，根据序列提交时间与当前时间的差值计算</li>
     *     <li>仪器运行时间，根据数据库 instrument_state表，统计PreRun和Running状态的数据求和获取</li>
     *     <li>序列运行状态，当前针 / 共计多少针计算</li>
     *     <li>仪器运行状态进度条， 根据 当前针 / 共计多少针 计算百分比</li>
     *     <li>仪器运行状态颜色，如 Idle为空闲</li>
     * </ol>
     *
     * @param result 需要返回的仪器信息
     */
    private void processDisplayField(List<InstrumentDto> result) {
        for (InstrumentDto instrumentDto : result) {
            // 处理序列运行时间（分钟）
            if (StrUtil.isNotBlank(instrumentDto.getUpdateTime())) {
                instrumentDto.setExecuteTime(DateUtil.formatBetween(DateUtil.date(),
                        DateUtil.parse(instrumentDto.getUpdateTime(), CodeListConstant.ISO_DATETIME_FORMAT), BetweenFormater.Level.SECOND));
            }
            // 处理序列运行状态
            if (null != instrumentDto.getSampleTotal() && instrumentDto.getSampleTotal() != 0) {
                instrumentDto.setSequenceInfo((instrumentDto.getCurrentSample() == null ? "0" : instrumentDto.getCurrentSample()) + " / " + instrumentDto.getSampleTotal());
            }
            // 处理进度条
            if (null != instrumentDto.getCurrentSample() && null != instrumentDto.getSampleTotal() && instrumentDto.getSampleTotal() != 0) {
                instrumentDto.setProgressBarWidth(NumberUtil.formatPercent((float) instrumentDto.getCurrentSample() / (float) instrumentDto.getSampleTotal(), 2));
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
