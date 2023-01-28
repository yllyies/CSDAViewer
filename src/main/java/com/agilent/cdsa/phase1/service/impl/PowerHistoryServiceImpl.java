package com.agilent.cdsa.phase1.service.impl;

import com.agilent.cdsa.phase1.model.PowerHistory;
import com.agilent.cdsa.phase1.repository.PowerHistoryDao;
import com.agilent.cdsa.phase1.service.PowerHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lifang
 * @since 2019-09-01
 */
@Service
@Slf4j
public class PowerHistoryServiceImpl implements PowerHistoryService {
    @Autowired
    private PowerHistoryDao powerHistoryDao;

    @Override
    public List<PowerHistory> doFindByIpOrderByCreatedDateDesc(String ip) {
        return powerHistoryDao.findByIp(ip);
    }

    @Override
    public List<PowerHistory> doFindAll() {
        return powerHistoryDao.findAll(Sort.by(Sort.Direction.ASC,"createdDate"));
    }

    @Override
    public void doCreate(PowerHistory instrument) {
        powerHistoryDao.saveAndFlush(instrument);
    }

    @Override
    public void doBatchCreate(List<PowerHistory> instruments) {
        powerHistoryDao.saveAll(instruments);
        powerHistoryDao.flush();
    }
}
