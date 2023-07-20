package com.agilent.cdsa.service;

import java.util.Map;

/**
 * @author lifang
 * @since 2023-07-19
 */
public interface RsltService {

    /**
     * 查询所有仪器信息
     *
     * @return
     */
    Map<String, Object> doFindInstruments();
}