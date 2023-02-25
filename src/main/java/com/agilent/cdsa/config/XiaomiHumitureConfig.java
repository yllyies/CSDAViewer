package com.agilent.cdsa.config;

import com.agilent.cdsa.dto.XiaomiHumitureDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "xiaomi.humiture")
public class XiaomiHumitureConfig {

  private static List<XiaomiHumitureDto> list;

  public static List<XiaomiHumitureDto> getList() {
    return XiaomiHumitureConfig.list;
  }

  public void setList(List<XiaomiHumitureDto> list) {
    XiaomiHumitureConfig.list = list;
  }
}
