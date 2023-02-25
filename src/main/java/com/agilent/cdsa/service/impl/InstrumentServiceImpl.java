package com.agilent.cdsa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.agilent.cdsa.common.CodeListConstant;
import com.agilent.cdsa.common.util.HttpUtil;
import com.agilent.cdsa.common.util.PythonUtil;
import com.agilent.cdsa.dto.CdsadaResponseDto;
import com.agilent.cdsa.dto.InstrumentDto;
import com.agilent.cdsa.model.PowerHistory;
import com.agilent.cdsa.repository.PowerHistoryDao;
import com.agilent.cdsa.repository.ProjectDao;
import com.agilent.cdsa.service.InstrumentService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<InstrumentDto> doFindInstrumentsByPost() {
        List<InstrumentDto> result = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        JSONObject jsonObj = JSONObject.fromObject(new HashMap<>());
        HttpEntity<String> request = new HttpEntity<>(jsonObj.toString(), headers);
        CdsadaResponseDto response = HttpUtil.httpRequest(instrumentInfoUrl, HttpMethod.GET, request, new ParameterizedTypeReference<>(){});
        if (response != null && CollUtil.isNotEmpty(response.getArray())) {
            result.addAll(response.getArray());
        }
        return result;
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
                    return CodeListConstant.INSTRUMENT_STATE_PRE_RUN;
                }
            }
            return CodeListConstant.INSTRUMENT_STATE_UNKNOWN;
        }
    }
}
