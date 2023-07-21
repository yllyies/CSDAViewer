package com.agilent.cdsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "统计分析查询返回值 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponseDto implements Serializable {

    private static final long serialVersionUID = -5485560989823458520L;

    @ApiModelProperty("所有仪器名称列表")
    private List<String> instrumentNames;
    @ApiModelProperty("所有时间列表")
    private List<Integer> yearRange;
    @ApiModelProperty("所有项目名称列表")
    private List<String> projectNames;
    @ApiModelProperty("所有序列提交人名称列表")
    private List<String> creators;
    @ApiModelProperty("柱状图 label集合")
    private List<String> barLabels;
    @ApiModelProperty("柱状图 dataset")
    private List<ChartDatasetDto> barDatasets;
    @ApiModelProperty("线性图 label集合")
    private List<String> lineLabels;
    @ApiModelProperty("线性图 dataset")
    private List<ChartDatasetDto> lineDatasets;
    @ApiModelProperty("饼图 label集合")
    private List<String> doughnutLabels;
    @ApiModelProperty("饼图 dataset")
    private List<Long> doughnutDatasets;
    @ApiModelProperty("表单 dataset")
    private List<TableDatasetDto> tableDatasets;
    @ApiModelProperty("")
    private AnalysisRequestDto requestParams;
}
