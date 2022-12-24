package com.agilent.csda.phase1.service;

import com.agilent.csda.phase1.dto.AnalysisRequestDto;

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
