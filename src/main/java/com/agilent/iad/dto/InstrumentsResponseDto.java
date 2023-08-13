package com.agilent.iad.dto;

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

    @ApiModelProperty("温湿度，“30.5 ℃, 40%RH”")
    String humiture;
    @ApiModelProperty("仪器列表数据")
    List<InstrumentDto> dataSource;
//    @ApiModelProperty("仪器合计数量")
//    private String instrumentTotal;
//    @ApiModelProperty("仪器使用数量")
//    private String instrumentRunning;
//    @ApiModelProperty("仪器待机数量")
//    private String instrumentIdel;
//    @ApiModelProperty("仪器未使用数量")
//    private String instrumentNotConnect;
    /* 统计数据*/
    @ApiModelProperty("仪器合计数量")
    private ChartItemDto instrumentTotal;
    @ApiModelProperty("仪器使用数量")
    private ChartItemDto instrumentRunning;
    @ApiModelProperty("仪器待机数量")
    private ChartItemDto instrumentIdel;
    @ApiModelProperty("仪器未使用数量")
    private ChartItemDto instrumentUnused;
    @ApiModelProperty("在线人数")
    private ChartItemDto userCount;
    @ApiModelProperty("session数量")
    private ChartItemDto sessionCount;
    @ApiModelProperty("仪器AQ数量")
    private ChartItemDto aqCount;
    @ApiModelProperty("仪器DA数量")
    private ChartItemDto daCount;
    @ApiModelProperty("并发数量")
    private ChartItemDto concurrentCount;
    @ApiModelProperty("服务器CPU占用")
    private ChartItemDto cpuPercentage;
    @ApiModelProperty("服务器内存占用")
    private ChartItemDto memoryPercentage;

//    @ApiModelProperty("System Total，仪器总数")
//    String systemTotal;
//    @ApiModelProperty("Running，运行中总数")
//    String runningCount;
//    @ApiModelProperty("notReadyCount，未就绪总数")
//    String notReadyCount;
//    @ApiModelProperty("idleCount，空闲总数")
//    String idleCount;
//    @ApiModelProperty("errorCount，错误总数")
//    String errorCount;
//    @ApiModelProperty("offlineCount，离线总数")
//    String offlineCount;
//    @ApiModelProperty("暂不使用，unknownCount，未知总数")
//    String unknownCount;
//    @ApiModelProperty("温湿度显示，暂不使用")
//    String humiture;
}
