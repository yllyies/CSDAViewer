package com.agilent.iad.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.agilent.iad.common.CodeListConstant;
import com.agilent.iad.common.dto.CommonResult;
import com.agilent.iad.dto.InstrumentDto;
import com.agilent.iad.dto.InstrumentsResponseDto;
import com.agilent.iad.service.InstrumentService;
import com.agilent.iad.service.InstrumentStateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

/*    @ApiOperation("仪器总览界面主查询")
    @ApiIgnore
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("instrument/index");
        List<InstrumentDto> agilentInstruments = instrumentService.doFindInstrumentsByRemote();
        List<InstrumentDto> result = new ArrayList<>(agilentInstruments);
        // 小米智能插座获取数据
//        List<InstrumentDto> thirdPartyInstruments = instrumentService.doFindThirdPartyInstruments();
//        result.addAll(thirdPartyInstruments);

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
        /*XiaomiHumitureDto xiaomiHumitureDto = PythonUtil.doGetHumitureInfo();
        if (CodeListConstant.NONE.equals(xiaomiHumitureDto.getTemperature()) && CodeListConstant.NONE.equals(xiaomiHumitureDto.getHumidity())) {
            modelAndView.getModel().put("humiture", "温湿度：None");
        } else {
            modelAndView.getModel().put("humiture", xiaomiHumitureDto.getDesc());
        }*//*

        return modelAndView;
    }*/

    @ApiOperation("v2:仪器总览界面主查询接口")
    @GetMapping(value = "/api/list")
    @ResponseBody
    public CommonResult<InstrumentsResponseDto> apiList() {
        InstrumentsResponseDto result = new InstrumentsResponseDto();
        List<InstrumentDto> agilentInstruments = instrumentService.doFindInstrumentsByRemote();
        result.setDataSource(agilentInstruments);
        Map<String, Long> stateToCountMap = agilentInstruments.stream().collect(Collectors.groupingBy(InstrumentDto::getInstrumentState, Collectors.counting()));
        result.setSystemTotal(StrUtil.toString(agilentInstruments.size()));
        // 预运行、运行
        Long runCount = 0L;
        if (MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_RUNNING) != null) {
            runCount += MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_RUNNING);
        }
        if (MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_PRERUN) != null) {
            runCount += MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_PRERUN);
        }
        result.setRunningCount(String.valueOf(runCount));
        result.setNotReadyCount(MapUtil.getStr(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_NOT_READY) == null ? "0":
                MapUtil.getStr(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_NOT_READY));
        result.setIdleCount(MapUtil.getStr(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_IDLE) == null ? "0":
                MapUtil.getStr(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_IDLE));
        result.setErrorCount(MapUtil.getStr(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_ERROR) == null ? "0":
                MapUtil.getStr(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_ERROR));
        // 未连接、离线
        Long offlineCount = 0L;
        if (MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_OFFLINE) != null) {
            offlineCount += MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_OFFLINE);
        }
        if (MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_NOT_CONNECT) != null) {
            offlineCount += MapUtil.getLong(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_NOT_CONNECT);
        }
        result.setOfflineCount(String.valueOf(offlineCount));
        return CommonResult.success(result);
    }
}
