package com.agilent.iad.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "柱状图最小元素DTO")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartItemDto implements Serializable {
    private static final long serialVersionUID = -6949143138977042468L;

    @ApiModelProperty("图表最小单位-标签")
    private String label;
    @ApiModelProperty("图表最小单位-类型")
    private String type;
    @ApiModelProperty("图表最小单位-值")
    private Long value;
}
