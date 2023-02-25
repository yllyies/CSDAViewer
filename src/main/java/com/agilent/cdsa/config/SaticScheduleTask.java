package com.agilent.cdsa.config;

import cn.hutool.core.collection.CollUtil;
import com.agilent.cdsa.common.util.PythonUtil;
import com.agilent.cdsa.model.PowerHistory;
import com.agilent.cdsa.service.InstrumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
public class SaticScheduleTask {
    @Autowired
    private InstrumentService instrumentService;

    @Scheduled(cron="${time.cron}")
    private void configureTasks() {
        List<PowerHistory> powerHistories = PythonUtil.doGetInstrumentPower();
        if (CollUtil.isNotEmpty(powerHistories)) {
            instrumentService.doBatchCreate(powerHistories);
        }
    }
}