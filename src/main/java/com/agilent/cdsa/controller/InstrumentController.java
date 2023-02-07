package com.agilent.cdsa.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.agilent.cdsa.common.CodeListConstant;
import com.agilent.cdsa.dto.InstrumentDto;
import com.agilent.cdsa.service.PowerHistoryService;
import com.agilent.cdsa.service.RsltService;
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
    private RsltService rsltService;
    @Autowired
    private PowerHistoryService powerHistoryService;

    @ApiOperation("仪器总览界面主查询")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("instrument/index");
        List<InstrumentDto> instrumentDtos = rsltService.doFindInstrumentsByPost();
        // test ML 仪器功率
//        String level = powerHistoryService.doMachineLearnPower("17");

        InstrumentDto instrumentDto = new InstrumentDto();
        instrumentDto.setInstrumentId("17");
        instrumentDto.setInstrumentName("instrument 17");
        instrumentDto.setInstrumentState(CodeListConstant.INSTRUMENT_STATE_RUNNING);
        instrumentDto.setColor("green");
        instrumentDto.setSequenceInfo("5 / 10");
        instrumentDto.setInstrumentDescription("当前档位：5");
        instrumentDtos.add(instrumentDto);
        modelAndView.getModel().put("dataSource", instrumentDtos); // 仪器状态结果集
        if (CollUtil.isEmpty(instrumentDtos)) {
            return modelAndView;
        }
        Map<String, Long> stateToCountMap = instrumentDtos.stream().collect(Collectors.groupingBy(InstrumentDto::getInstrumentState, Collectors.counting()));
        modelAndView.getModel().put("systemTotal", instrumentDtos.size()); // 总数
        modelAndView.getModel().put("runningCount", MapUtil.getAny(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_PRE_RUN, CodeListConstant.INSTRUMENT_STATE_RUNNING).values().size()); // 运行
        modelAndView.getModel().put("idleCount", MapUtil.getAny(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_IDLE, CodeListConstant.INSTRUMENT_STATE_NOT_READY, CodeListConstant.INSTRUMENT_STATE_SLEEP).values().size()); // 空闲
        modelAndView.getModel().put("errorCount", 0); // 错误
        modelAndView.getModel().put("offlineCount", MapUtil.getAny(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_NOT_CONNECT).values().size()); // 离线
        modelAndView.getModel().put("unknownCount", MapUtil.getAny(stateToCountMap, CodeListConstant.INSTRUMENT_STATE_OFFLINE).values().size()); // 未知
        return modelAndView;
    }
}
