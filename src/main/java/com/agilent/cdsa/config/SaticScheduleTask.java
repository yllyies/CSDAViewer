package com.agilent.cdsa.config;

import cn.hutool.core.date.DateUtil;
import com.agilent.cdsa.common.util.PythonUtil;
import com.agilent.cdsa.model.PowerHistory;
import com.agilent.cdsa.service.PowerHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

//@Configuration
//@EnableScheduling
public class SaticScheduleTask {
    @Autowired
    private PowerHistoryService powerHistoryService;

    //3.添加定时任务
    @Scheduled(cron="${time.cron}")
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        String power = PythonUtil.execPyReturnString("instrument_status.py");
        String ip = "192.168.120.44";
        String token = "19bc2330c330c048b5721195523906d7";
        powerHistoryService.doCreate(new PowerHistory(null, "17", ip, token, Double.valueOf(power), DateUtil.date().toTimestamp()));
    }
}