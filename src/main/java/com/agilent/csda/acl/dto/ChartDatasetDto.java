package com.agilent.csda.acl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "展示图表的数据结果集， {lables:['ICQ001','ICQ002','ICQ003'], datasetMap:{'ICQ001': ['1000(Q1)','1500(Q2)','2000(Q3)'],'ICQ002': ['1000(Q1)','1500(Q2)','2000(Q3)'],'ICQ003': ['1000(Q1)','1500(Q2)','2000(Q3)']}}")
@Data
public class ChartDatasetDto implements Serializable {

    @ApiModelProperty(value = "datasets-item label")
    private String label;

    @ApiModelProperty(value = "datasets-item backgroundColor")
    private String backgroundColor;
    @ApiModelProperty(value = "datasets-item borderColor")
    private String borderColor;

    @ApiModelProperty(value = "datasets-item label")
    private List<Long> data;

    public ChartDatasetDto() {
    }

    public ChartDatasetDto(String label, String backgroundColor, List<Long> data) {
        this.label = label;
        this.backgroundColor = backgroundColor;
        this.data = data;
    }

    public ChartDatasetDto(String label, String backgroundColor, String borderColor, List<Long> data) {
        this.label = label;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.data = data;
    }
}
