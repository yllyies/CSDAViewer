package com.agilent.cdsa.dto;

import com.agilent.cdsa.common.CodeListConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "请求参数DTO")
@Data
public class AnalysisRequestDto implements Serializable {
    @ApiModelProperty("数据维度： 仪器、项目、人员；过滤条件：InstrumentView,ProjectView,CreatorView")
    private String viewType = CodeListConstant.INSTRUMENT_VIEW;
    @ApiModelProperty("仪器名称，通过英文逗号分割")
    private String instrumentNames;
    @ApiModelProperty("项目名称，通过英文逗号分割")
    private String projectNames;
    @ApiModelProperty("人员名称，通过英文逗号分割")
    private String creatorNames;
    @ApiModelProperty("选择查询时间，查询粒度，如 年、月、日")
    private String timeUnit;
    @ApiModelProperty("时间区间，通过日历选择器获取，以逗号分割如 2021,2022,2023")
    private String daterange;
    @ApiModelProperty("暂不使用，选择显示运行时间或针数")
    private String yAxisUnit;

}
