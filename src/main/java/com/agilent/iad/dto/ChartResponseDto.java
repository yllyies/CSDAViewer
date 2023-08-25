package com.agilent.iad.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "仪表盘返回DTO")
@Data
public class ChartResponseDto implements Serializable {

    private static final long serialVersionUID = -8566622414626116740L;

//    @ApiModelProperty("柱状图-当天仪器运行时间前10，label是仪器名称，值是运行时间（分钟）")
//    private List<ChartItemDto> barChartDay;

    @ApiModelProperty("柱状图-最近7天仪器运行时间，label是仪器名称，值是运行时间（分钟）")
    private List<ChartItemDto> barChartWeek;

//    @ApiModelProperty("饼图-当天仪器运行时间前10，label是仪器名称，值是运行时间（分钟）")
//    private List<ChartItemDto> doughnutChartDay;

//    @ApiModelProperty("饼图-最近7天仪器运行时间前10，label是仪器名称，值是运行时间（分钟）")
//    private List<ChartItemDto> doughnutChartWeek;

//    @ApiModelProperty("水波图-剩余可用仪器台数")
//    private ChartItemDto liquidChartWorking;

//    @ApiModelProperty("线图-最近7天完成序列的数量")
//    private List<ChartItemDto> lineChartRunSequenceCount;

    @ApiModelProperty("柱状图-从0点到24点完成序列的数量")
    private List<String> barChartRunSequenceCountDay;

//    @ApiModelProperty("柱状图-最近7天完成序列的数量")
//    private List<String> barChartRunSequenceCountWeek;

//    @ApiModelProperty("线图-异常信息预览：仪器连接信息，序列错误...")
//    private List<ChartItemDto> lineChartErrorMessage;
}
