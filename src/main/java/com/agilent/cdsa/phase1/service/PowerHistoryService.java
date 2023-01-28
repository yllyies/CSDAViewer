package com.agilent.cdsa.phase1.service;

import com.agilent.cdsa.phase1.model.PowerHistory;

import java.util.List;

/**
 * @author lifang
 * @since 2022-11-28
 */
public interface PowerHistoryService {

    List<PowerHistory> doFindByIpOrderByCreatedDateDesc(String ip);

    List<PowerHistory> doFindAll();

    void doCreate(PowerHistory entity);

    void doBatchCreate(List<PowerHistory> entities);


}
