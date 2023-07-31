package com.agilent.iad.service;

import com.agilent.iad.dto.AnalysisRequestDto;
import com.agilent.iad.dto.AnalysisResponseDto;

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
}
