package com.agilent.cdsa.service;

import com.agilent.cdsa.model.InstrumentState;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lifang
 * @since 2023-02-07
 */
public interface InstrumentStateService {

    List<InstrumentState> doFindByInstrumentIdIn(List<BigDecimal> instrumentIds);

}
