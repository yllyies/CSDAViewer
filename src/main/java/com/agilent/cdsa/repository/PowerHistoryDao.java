package com.agilent.cdsa.repository;

import com.agilent.cdsa.model.PowerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerHistoryDao extends JpaRepository<PowerHistory, String>, JpaSpecificationExecutor<PowerHistory>
{

    List<PowerHistory> findByIdIn(List<String> ids);

    List<PowerHistory> findByIp(String ip);

    List<PowerHistory> findByInstrumentId(String ip);
}
