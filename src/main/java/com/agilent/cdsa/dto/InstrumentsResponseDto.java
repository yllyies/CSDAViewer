package com.agilent.cdsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "仪器实时状态返回值DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentsResponseDto implements Serializable {

    private static final long serialVersionUID = -3245381352482608240L;

    @ApiModelProperty("仪器列表数据")
    List<InstrumentDto> dataSource;
    @ApiModelProperty("System Total，仪器总数")
    String systemTotal;
    @ApiModelProperty("Running，运行中总数")
    String runningCount;
    @ApiModelProperty("notReadyCount，未就绪总数")
    String notReadyCount;
    @ApiModelProperty("idleCount，空闲总数")
    String idleCount;
    @ApiModelProperty("errorCount，错误总数")
    String errorCount;
    @ApiModelProperty("offlineCount，离线总数")
    String offlineCount;
    @ApiModelProperty("暂不使用，unknownCount，未知总数")
    String unknownCount;
    @ApiModelProperty("温湿度显示，暂不使用")
    String humiture;
}
