package com.agilent.cdsa.service;

import com.agilent.cdsa.model.InstrumentState;

import java.util.List;

/**
 * @author lifang
 * @since 2023-02-07
 */
public interface InstrumentStateService {

    /**
     * 根据ID集合查询
     *
     * @param instrumentIds 仪器ID集合
     * @return 符合条件的结果即
     */
//    List<InstrumentState> doFindByInstrumentIdIn(List<BigDecimal> instrumentIds);

    /**
     * 根据名称集合查询
     *
     * @param instrumentNames 仪器名称集合
     * @return 符合条件的结果即
     */
    List<InstrumentState> doFindByInstrumentNameIn(List<String> instrumentNames);

}
