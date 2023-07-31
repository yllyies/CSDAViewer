package com.agilent.iad.config;

import com.agilent.iad.dto.XiaomiSocketDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "xiaomi.socket")
public class XiaomiSocketConfig {

  private static List<XiaomiSocketDto> list;//必须是static修饰，不然获取不到配置的值

  public static List<XiaomiSocketDto> getList() {
    return XiaomiSocketConfig.list;
  }

  public void setList(List<XiaomiSocketDto> list) {
    XiaomiSocketConfig.list = list;
  }
}
