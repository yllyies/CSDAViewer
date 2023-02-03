package com.agilent.cdsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@ApiModel(value = "响应参数DTO")
@Data
public class ResponseDto implements Serializable {
    @ApiModelProperty("请求参数，用于保留查询条件")
    private AnalysisRequestDto requestDto;
    @ApiModelProperty("响应数据")
    private Map<String, Object> data;
    @ApiModelProperty("是否成功")
    private Boolean success;
}
