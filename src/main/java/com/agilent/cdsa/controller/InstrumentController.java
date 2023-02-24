package com.agilent.cdsa.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.agilent.cdsa.common.CodeListConstant;
import com.agilent.cdsa.common.util.PythonUtil;
import com.agilent.cdsa.dto.InstrumentDto;
import com.agilent.cdsa.dto.XiaomiHumitureDto;
import com.agilent.cdsa.service.InstrumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "仪器总览")
@Controller
@RequestMapping("instrument")
public class InstrumentController {

    @Autowired
    private InstrumentService instrumentService;

    @ApiOperation("仪器总览界面主查询")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("instrument/index");
        List<InstrumentDto> instruments = instrumentService.doFindInstrumentsByPost();
        List<InstrumentDto> thirdPartyInstruments = instrumentService.doFindThirdPartyInstruments();
        instruments.addAll(thirdPartyInstruments);
        modelAndView.getModel().put("dataSource", instruments); // 仪器状态结果集
        Map<String, Long> stateToCountMap = instruments.stream().collect(Collectors.groupingBy(InstrumentDto::getInstrumentState, Collectors.counting()));
        modelAndView.getModel().put("systemTotal", instruments.size()); // 总数
        modelAndView.getModel().put("runningCount", MapUtil.getAny(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_PRE_RUN, CodeListConstant.INSTRUMENT_STATE_RUNNING).values().size()); // 运行
        modelAndView.getModel().put("idleCount", MapUtil.getAny(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_IDLE, CodeListConstant.INSTRUMENT_STATE_NOT_READY, CodeListConstant.INSTRUMENT_STATE_SLEEP).values().size()); // 空闲
        modelAndView.getModel().put("errorCount", 0); // 错误
        modelAndView.getModel().put("offlineCount", MapUtil.getAny(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_NOT_CONNECT).values().size()); // 离线
        modelAndView.getModel().put("unknownCount", MapUtil.getAny(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_UNKNOWN).values().size()); // 未知
        // 获取温湿度信息
        List<XiaomiHumitureDto> xiaomiHumitureDtos = PythonUtil.doGetHumitureInfo();
        if (CollUtil.isNotEmpty(xiaomiHumitureDtos)) {
            modelAndView.getModel().put("humiture", xiaomiHumitureDtos.get(0).getDesc());
        }
        return modelAndView;
    }
}
