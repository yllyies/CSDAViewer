package com.agilent.iad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置文件中配置的智能插座信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XiaomiHumitureDto {
    private String did;
    private String name;
    private String temperature;
    private String humidity;
    private String desc;

    public XiaomiHumitureDto(String temperature, String humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
        // 自动拼接温湿度信息
        this.desc = "温湿度：" + temperature + " ℃， " + humidity + " %RH";
    }
}
