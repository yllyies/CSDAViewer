package com.agilent.csda.acl.controller;

import com.agilent.csda.acl.service.RsltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Api(tags = "仪器总览")
@Controller
@RequestMapping("instrument")
public class InstrumentController {

    @Autowired
    private RsltService rsltService;

    @ApiOperation("定义UI图1页主要查询接口")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list() {
        Map<String, Object> resultMap = rsltService.doFindInstruments();
        ModelAndView modelAndView = new ModelAndView("instrument/index");
        modelAndView.getModel().putAll(resultMap);
        return modelAndView;
    }
}
