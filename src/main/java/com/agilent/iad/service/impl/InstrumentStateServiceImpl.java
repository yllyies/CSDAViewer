package com.agilent.iad.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.agilent.iad.model.InstrumentState;
import com.agilent.iad.repository.InstrumentStateDao;
import com.agilent.iad.service.InstrumentStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lifang
 * @since 2023-07-19
 */
@Service
public class InstrumentStateServiceImpl implements InstrumentStateService {
    @Autowired
    private InstrumentStateDao instrumentStateDao;

//    @Override
//    public List<InstrumentState> doFindByInstrumentIdIn(List<BigDecimal> instrumentIds) {
//        if (CollUtil.isEmpty(instrumentIds)) {
//            return new ArrayList<>();
//        }
//        return instrumentStateDao.findByInstrumentIdIn(instrumentIds);
//    }

    @Override
    public List<InstrumentState> doFindByInstrumentNameIn(List<String> instrumentNames) {
        if (CollUtil.isEmpty(instrumentNames)) {
            return new ArrayList<>();
        }
        return instrumentStateDao.findByInstrumentNameIn(instrumentNames);
    }
}
