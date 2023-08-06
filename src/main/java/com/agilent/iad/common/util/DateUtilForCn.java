package com.agilent.iad.common.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 转换时间工具类，从美国时区转换到中国时区
 *
 */
@Component
@Slf4j
public class DateUtilForCn {

    /**
     * 将特殊格式的日期字符串，从UTC0转换为UTC+8，并格式化
     *
     * @param dateStr
     * @param fromDatePattern
     * @param toDatePattern
     * @return
     */
    public static String parseTimeZoneUsToCn(String dateStr, String fromDatePattern, String toDatePattern) {
        // 时区设置
        TimeZone usTimeZone = TimeZone.getTimeZone("Greenwich");
        TimeZone cnTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        SimpleDateFormat sdf = new SimpleDateFormat(fromDatePattern);
        sdf.setTimeZone(usTimeZone);
        DateTime usTime = DateUtil.parse(dateStr, sdf);
        sdf.setTimeZone(cnTimeZone);
        return DateUtil.format(usTime, toDatePattern);
    }
}
