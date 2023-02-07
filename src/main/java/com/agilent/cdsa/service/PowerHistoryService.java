package com.agilent.cdsa.service;

import com.agilent.cdsa.model.PowerHistory;

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

    String doMachineLearnPower(String instrumentId);
}
