package com.agilent.csda.acl.service;

import com.agilent.csda.acl.dto.AnalysisRequestDto;
import com.agilent.csda.acl.model.Dx;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lifang
 * @since 2022-11-28
 */
public interface DxService {

    /**
     * 按时间区间查询
     *
     * @return
     */
    List<Dx> doFindBetweenDate(Date startDate, Date endDate);

    /**
     * 按条件查询
     *
     * @return
     */
    Map<String, Object> doQuery(AnalysisRequestDto analysisRequestDto);
}
