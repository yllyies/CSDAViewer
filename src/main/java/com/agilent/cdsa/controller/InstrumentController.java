package com.agilent.cdsa.controller;

import com.agilent.cdsa.dto.InstrumentDto;
import com.agilent.cdsa.service.RsltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Api(tags = "仪器总览")
@Controller
@RequestMapping("instrument")
public class InstrumentController {

    @Autowired
    private RsltService rsltService;

    @ApiOperation("仪器总览界面主查询")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("instrument/index");
        List<InstrumentDto> instrumentDtos = rsltService.doFindInstrumentsByPost();
        modelAndView.getModel().put("dataSource", instrumentDtos);
        return modelAndView;
    }
}
