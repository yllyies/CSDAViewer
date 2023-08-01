package com.agilent.iad.controller;

import com.agilent.iad.common.dto.CommonResult;
import com.agilent.iad.dto.AnalysisRequestDto;
import com.agilent.iad.dto.AnalysisResponseDto;
import com.agilent.iad.dto.ChartResponseDto;
import com.agilent.iad.service.DxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import org.springframework.web.bind.annotation.*;

@Api(tags = "统计分析")
@Controller
@RequestMapping("analysis")
public class AnalysisController {

    @Autowired
    private DxService dxService;

    /*@ApiIgnore
    @ApiOperation("根据仪器、项目、人员多个维度查询仪器使用情况")
    @RequestMapping(value = "/query", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView query(AnalysisRequestDto analysisRequestDto) {
        Map<String, Object> resultMap = dxService.doQuery(analysisRequestDto);
        ModelAndView modelAndView = new ModelAndView("analysis/index");
        modelAndView.getModel().putAll(resultMap);
        return modelAndView;
    }*/

    @ApiOperation("v2:根据仪器、项目、人员多个维度查询仪器使用情况 不返回页面信息")
    @PostMapping(value = "/api/query")
    @ResponseBody
    public CommonResult<AnalysisResponseDto> apiQuery(@RequestBody AnalysisRequestDto analysisRequestDto) {
        AnalysisResponseDto analysisResponseDto = dxService.doQuery2(analysisRequestDto);
        return CommonResult.success(analysisResponseDto);
    }

    @ApiOperation("v2: 所有仪器信息的仪表盘接口")
    @GetMapping(value = "/api/charts")
    @ResponseBody
    public CommonResult<ChartResponseDto> apiCharts(@ApiParam("查询时间，2023-07-31,2023-08-01") @RequestParam(value = "dateRange", required = false) String dateRange) {
        ChartResponseDto chartResponseDto = dxService.doFindInstrumentCharts(dateRange);
        return CommonResult.success(chartResponseDto);
    }
}
