package com.agilent.cdsa.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.agilent.cdsa.common.util.PythonUtil;
import com.agilent.cdsa.model.PowerHistory;
import com.agilent.cdsa.service.InstrumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

//@Configuration
//@EnableScheduling
@Slf4j
public class SaticScheduleTask {
    @Autowired
    private InstrumentService instrumentService;

    @Scheduled(cron="${time.cron}")
    private void configureTasks() {
        List<PowerHistory> powerHistories = PythonUtil.doGetInstrumentPower();
        if (CollUtil.isNotEmpty(powerHistories)) {
            Timestamp now = DateUtil.parse(DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN)).toTimestamp();
            // 过滤功率为0的数据，此时默认关机
            List<PowerHistory> filter = powerHistories.stream().filter(pw -> null != pw.getPower() && pw.getPower() != 0d).collect(Collectors.toList());
            instrumentService.doBatchReplace(filter, now);
        } else {
            log.error("获取仪器信息失败 PythonUtil.doGetInstrumentPower 未获取任何数据");
        }
    }
}