package com.agilent.cdsa.phase1.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.agilent.cdsa.common.util.PythonUtil;
import com.agilent.cdsa.phase1.dto.InstrumentDto;
import com.agilent.cdsa.phase1.service.RsltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "仪器总览")
@Controller
@RequestMapping("instrument")
public class InstrumentController {

    @Autowired
    private RsltService rsltService;

    @ApiOperation("定义UI图1页主要查询接口")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list() {
//        List<InstrumentDto> instrumentDtos = rsltService.doFindInstrumentsByPost();
        List<InstrumentDto> instrumentDtos = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            instrumentDtos.add(new InstrumentDto(String.valueOf(i), "Not Connected", "green", "LC-" + i, "LC", "Project-" + i,
                    "admin", "admin", "Sequence-" + i, "12-22 10:10:30", "Sample-"  + i, "Lab" + i, (long) i, DateUtil.secondToTime(i), "7 / 10"));
        }
        ModelAndView modelAndView = new ModelAndView("instrument/index");
        modelAndView.getModel().put("dataSource", instrumentDtos);
        return modelAndView;
    }

    @ApiOperation("定义UI图1页主要查询接口")
    @RequestMapping(value = "list2", method = RequestMethod.GET)
    public String list2(Model model) {
//        List<InstrumentDto> instrumentDtos = rsltService.doFindInstrumentsByPost();
        List<InstrumentDto> instrumentDtos = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            instrumentDtos.add(new InstrumentDto(String.valueOf(i), "Not Connected", "green", "LC-" + i, "LC", "Project-" + i,
                    "admin", "admin", "Sequence-" + i, "12-22 10:10:30", "Sample-"  + i, "Lab" + i, (long) i, DateUtil.secondToTime(i), "7 / 10"));
        }
        String message = PythonUtil.execPythonFileWithReturn("instrument_status.py");
        instrumentDtos.add(new InstrumentDto("4", "", StrUtil.isNotBlank(message) ? "green": "", "监控仪器001", "", "", "", "", "", null, null, "功率(W)：" + message, 1000l, DateUtil.secondToTime(1000), null));
        model.addAttribute("dataSource", instrumentDtos);
        return "instrument/index :: #datatable-div";
    }
}
