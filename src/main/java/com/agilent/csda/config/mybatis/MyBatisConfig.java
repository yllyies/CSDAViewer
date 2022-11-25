package com.agilent.csda.config.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Config
 */
@Configuration
@MapperScan({"com.agilent.csda.acl.mapper"})
public class MyBatisConfig {
}

