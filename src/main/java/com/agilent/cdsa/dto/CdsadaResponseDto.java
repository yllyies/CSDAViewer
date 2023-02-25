package com.agilent.cdsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "CDSA DA WebAPI仪器状态的返回值DTO")
@Data
public class CdsadaResponseDto implements Serializable {
    @ApiModelProperty("响应数据")
    private List<InstrumentDto> array;
}
