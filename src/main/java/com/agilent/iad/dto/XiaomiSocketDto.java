package com.agilent.iad.dto;

import lombok.Data;

/**
 * 配置文件中配置的智能插座信息
 */
@Data
public class XiaomiSocketDto {
    private String ip;
    private String token;
    private String instrumentName;
    /**
     * used when return
     */
    private String power;
    /**
     * used when return
     */
    private String message;
}
