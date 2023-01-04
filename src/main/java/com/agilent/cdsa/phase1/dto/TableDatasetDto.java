package com.agilent.cdsa.phase1.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "展示表格的数据结果集")
@Data
public class TableDatasetDto implements Serializable {
    @ApiModelProperty(value = "仪器名称")
    private String instrumentName;
    @ApiModelProperty(value = "时间")
    private String date;
    @ApiModelProperty(value = "运行时间")
    private Long time;
    @ApiModelProperty(value = "比率")
    private String ratio;

    public TableDatasetDto() {
    }

    public TableDatasetDto(String instrumentName, String date, Long time, String ratio) {
        this.instrumentName = instrumentName;
        this.date = date;
        this.time = time;
        this.ratio = ratio;
    }
}
