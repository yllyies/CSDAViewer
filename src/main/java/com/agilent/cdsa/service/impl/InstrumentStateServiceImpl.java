package com.agilent.cdsa.service.impl;

import com.agilent.cdsa.model.InstrumentState;
import com.agilent.cdsa.repository.InstrumentStateDao;
import com.agilent.cdsa.service.InstrumentStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lifang
 * @since 2019-09-01
 */
@Service
public class InstrumentStateServiceImpl implements InstrumentStateService {
    @Autowired
    private InstrumentStateDao instrumentStateDao;

    @Override
    public List<InstrumentState> doFindByIds(List<BigDecimal> ids) {
        return instrumentStateDao.findByInstrumentIdIn(ids);
    }
}
