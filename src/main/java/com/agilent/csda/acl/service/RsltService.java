package com.agilent.csda.acl.service;

import com.agilent.csda.acl.dto.RequestDto;
import com.agilent.csda.acl.model.Rslt;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author lifang
 * @since 2022-11-28
 */
public interface RsltService {

    /**
     * 查询所有数据
     *
     * @return
     */
    Map<String, Object> doFindAll();

    /**
     * 按条件查询
     *
     * @return
     */
    Map<String, Object> doQuery(RequestDto requestDto);

    /**
     * 分页查询
     *
     * @return
     */
    Page<Rslt> doFindPage(int pageNum, int pageSize);

    /**
     * 查询所有仪器信息
     *
     * @return
     */
    Map<String, Object> doFindInstruments();
}
