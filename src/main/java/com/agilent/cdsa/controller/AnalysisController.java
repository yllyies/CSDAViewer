package com.agilent.cdsa.controller;

import com.agilent.cdsa.dto.AnalysisRequestDto;
import com.agilent.cdsa.dto.AnalysisResponseDto;
import com.agilent.cdsa.service.DxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Api(tags = "统计分析")
@Controller
@RequestMapping("analysis")
public class AnalysisController {

    @Autowired
    private DxService dxService;

    @ApiOperation("根据仪器、项目、人员多个维度查询仪器使用情况")
    @RequestMapping(value = "/query", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView query(AnalysisRequestDto analysisRequestDto) {
        Map<String, Object> resultMap = dxService.doQuery(analysisRequestDto);
        ModelAndView modelAndView = new ModelAndView("analysis/index");
        modelAndView.getModel().putAll(resultMap);
        return modelAndView;
    }

    @ApiOperation("v2:根据仪器、项目、人员多个维度查询仪器使用情况 不返回页面信息")
    @PostMapping(value = "/api/query")
    @ResponseBody
    public AnalysisResponseDto apiQuery(AnalysisRequestDto analysisRequestDto) {
        AnalysisResponseDto analysisResponseDto = dxService.doQuery2(analysisRequestDto);
        return analysisResponseDto;
    }
}
