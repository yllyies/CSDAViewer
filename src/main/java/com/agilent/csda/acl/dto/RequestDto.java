package com.agilent.csda.acl.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "请求参数DTO")
@Data
public class RequestDto implements Serializable {
    // 1.选择年度、季度范围；2.选择仪器；3.选择项目；4.选择人员；5.选择同比、环比；6.选择Y轴为时间或针数
    private String timeUnit;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    // 1.选择年度、季度范围；2.选择仪器；3.选择项目；4.选择人员；5.选择同比、环比；6.选择Y轴为时间或针数
    private String daterange;
    private String instrumentNames;
    private String projectNames;
    private String adminNames;
    private String isLinkRelative; // "on" 或 null
    private String yAxisUnit;
    private String viewType;
}
