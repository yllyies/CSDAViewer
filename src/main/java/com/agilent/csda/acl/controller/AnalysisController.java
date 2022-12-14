package com.agilent.csda.acl.controller;

import com.agilent.csda.acl.dto.AnalysisRequestDto;
import com.agilent.csda.acl.service.DxService;
import com.agilent.csda.acl.service.RsltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Api(tags = "统计分析")
@Controller
@RequestMapping("analysis")
public class AnalysisController {

    @Autowired
    private RsltService rsltService;
    @Autowired
    private DxService dxService;

    @ApiOperation("定义UI图2页主要查询接口")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list() {
        Map<String, Object> resultMap = rsltService.doFindAll();
        ModelAndView modelAndView = new ModelAndView("analysis/index");
        modelAndView.getModel().putAll(resultMap);
        return modelAndView;
    }

    @ApiOperation("定义UI图2：按时间粒度查询")
    @PostMapping("/query")
    public ModelAndView query(AnalysisRequestDto analysisRequestDto) {
        Map<String, Object> resultMap = dxService.doQuery(analysisRequestDto);
        ModelAndView modelAndView = new ModelAndView("analysis/index");
        modelAndView.getModel().putAll(resultMap);
        return modelAndView;
    }
}