package com.agilent.cdsa.repository;

import com.agilent.cdsa.model.InstrumentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InstrumentStateDao extends JpaRepository<InstrumentState, String>, JpaSpecificationExecutor<InstrumentState> {

    List<InstrumentState> findByInstrumentIdIn(List<BigDecimal> ids);

    List<InstrumentState> findByInstrumentNameIn(List<String> instrumentNames);
}
