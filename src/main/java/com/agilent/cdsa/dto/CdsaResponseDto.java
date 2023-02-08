package com.agilent.cdsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "CDSA 后端程序接口调用返回对象DTO")
@Data
public class CdsaResponseDto implements Serializable {
    private static final long serialVersionUID = 8820539914383260172L;

    @ApiModelProperty("请求参数，用于保留查询条件")
    private List<InstrumentDto> instrument_state;
}