package com.agilent.cdsa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.agilent.cdsa.common.util.PythonUtil;
import com.agilent.cdsa.model.PowerHistory;
import com.agilent.cdsa.repository.PowerHistoryDao;
import com.agilent.cdsa.repository.ProjectDao;
import com.agilent.cdsa.service.PowerHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lifang
 * @since 2019-09-01
 */
@Service
@Slf4j
public class PowerHistoryServiceImpl implements PowerHistoryService {
    @Autowired
    private ProjectDao projectDao;
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

    @Override
    public String doMachineLearnPower(String instrumentId) {
        List<PowerHistory> powerHistories = powerHistoryDao.findByInstrumentId(instrumentId);
        if (CollUtil.isEmpty(powerHistories)) {
            return "";
        }
        List<Double> powerList = powerHistories.stream().map(PowerHistory::getPower).collect(Collectors.toList());
        String s = PythonUtil.execPyByListReturnString("instrument_power.py", powerList);
        return "" + s;
    }
}
