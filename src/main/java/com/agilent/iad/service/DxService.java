package com.agilent.iad.service;

import com.agilent.iad.dto.AnalysisRequestDto;
import com.agilent.iad.dto.AnalysisResponseDto;
import com.agilent.iad.dto.ChartResponseDto;

import java.util.Map;

/**
 * @author lifang
 * @since 2023-07-19
 */
public interface DxService {

    /**
     * 按条件查询
     *
     * @return
     */
    AnalysisResponseDto doQuery2(AnalysisRequestDto analysisRequestDto);

    /**
     * 按条件查询
     *
     * @return
     */
    Map<String, Object> doQuery(AnalysisRequestDto analysisRequestDto);

    /**
     * 根据时间区间，统计仪器相关图表数据
     *
     * @param dateRange 时间区间
     * @return 仪表盘返回DTO
     */
    ChartResponseDto doFindInstrumentCharts(String dateRange);
}
