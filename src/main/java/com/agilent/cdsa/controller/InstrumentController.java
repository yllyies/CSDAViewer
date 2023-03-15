package com.agilent.cdsa.controller;

import cn.hutool.core.map.MapUtil;
import com.agilent.cdsa.common.CodeListConstant;
import com.agilent.cdsa.common.util.PythonUtil;
import com.agilent.cdsa.dto.InstrumentDto;
import com.agilent.cdsa.dto.XiaomiHumitureDto;
import com.agilent.cdsa.service.InstrumentService;
import com.agilent.cdsa.service.InstrumentStateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "仪器总览")
@Controller
@RequestMapping("instrument")
public class InstrumentController {

    @Autowired
    private InstrumentService instrumentService;
    @Autowired
    private InstrumentStateService instrumentStateService;

    @ApiOperation("仪器总览界面主查询")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("instrument/index");
        List<InstrumentDto> agilentInstruments = instrumentService.doFindInstrumentsByRemote();
        List<InstrumentDto> result = new ArrayList<>(agilentInstruments);
        List<InstrumentDto> thirdPartyInstruments = instrumentService.doFindThirdPartyInstruments();
        result.addAll(thirdPartyInstruments);

        modelAndView.getModel().put("dataSource", result); // 仪器状态结果集
        Map<String, Long> stateToCountMap = result.stream().collect(Collectors.groupingBy(InstrumentDto::getInstrumentState, Collectors.counting()));
        modelAndView.getModel().put("systemTotal", result.size()); // 总数

        if (stateToCountMap.containsKey(CodeListConstant.INSTRUMENT_STATE_RUNNING)) {
            modelAndView.getModel().put("runningCount", MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_RUNNING) == null ? "0": MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_RUNNING));
        }
        if (stateToCountMap.containsKey(CodeListConstant.INSTRUMENT_STATE_NOT_READY)) {
            modelAndView.getModel().put("notReadyCount", MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_NOT_READY) == null ? "0": MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_NOT_READY));
        }
        if (stateToCountMap.containsKey(CodeListConstant.INSTRUMENT_STATE_IDLE)) {
            modelAndView.getModel().put("idleCount", MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_IDLE) == null ? "0": MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_IDLE));
        }
        if (stateToCountMap.containsKey(CodeListConstant.INSTRUMENT_STATE_ERROR)) {
            modelAndView.getModel().put("errorCount", MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_ERROR) == null ? "0": MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_ERROR));
        }
        if (stateToCountMap.containsKey(CodeListConstant.INSTRUMENT_STATE_NOT_CONNECT)) {
            modelAndView.getModel().put("offlineCount", MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_NOT_CONNECT) == null ? "0": MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_NOT_CONNECT));
        }
        if (stateToCountMap.containsKey(CodeListConstant.INSTRUMENT_STATE_UNKNOWN)) {
            modelAndView.getModel().put("unknownCount", MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_UNKNOWN) == null ? "0": MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_UNKNOWN));
        }
        // 获取温湿度信息
        XiaomiHumitureDto xiaomiHumitureDto = PythonUtil.doGetHumitureInfo();
        if (CodeListConstant.NONE.equals(xiaomiHumitureDto.getTemperature()) && CodeListConstant.NONE.equals(xiaomiHumitureDto.getHumidity())) {
            modelAndView.getModel().put("humiture", "温湿度：None ℃， None %RH");
        } else {
            modelAndView.getModel().put("humiture", xiaomiHumitureDto.getDesc());
        }

        return modelAndView;
    }
}
