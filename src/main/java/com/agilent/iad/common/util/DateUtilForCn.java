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
     * hutool转换
     */
    public static String parseTimeZoneUsToCn(String dateStr, String datePattern) {
        // 美国时区
        TimeZone usTimeZone = TimeZone.getTimeZone("Greenwich");
        TimeZone cnTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        sdf.setTimeZone(usTimeZone);
        DateTime usTime = DateUtil.parse(dateStr, sdf);
        sdf.setTimeZone(cnTimeZone);
        return DateUtil.format(usTime, sdf);

    }
}
