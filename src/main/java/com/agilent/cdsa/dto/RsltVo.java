package com.agilent.cdsa.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel("统计分析页面，用于获取 查询条件下拉框范围的VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RsltVo {

    private String instrumentName;
    private String projectName;
    private String creator;
    private Date createdDate;

    public RsltVo(String instrumentName, String projectName, String creator) {
        this.instrumentName = instrumentName;
        this.projectName = projectName;
        this.creator = creator;
    }
}
