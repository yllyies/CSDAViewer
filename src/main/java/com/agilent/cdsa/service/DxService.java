package com.agilent.cdsa.service;

import com.agilent.cdsa.dto.AnalysisRequestDto;

import java.util.Map;

/**
 * @author lifang
 * @since 2022-11-28
 */
public interface DxService {

    /**
     * 按条件查询
     *
     * @return
     */
    Map<String, Object> doQuery(AnalysisRequestDto analysisRequestDto);
}
