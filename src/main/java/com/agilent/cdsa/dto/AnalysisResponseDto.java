package com.agilent.cdsa.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "仪器统计查询返回值 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponseDto implements Serializable {

    private static final long serialVersionUID = -5485560989823458520L;

    private List<String> instrumentNames;
    private List<Integer> yearRange;
    private List<String> projectNames;
    private List<String> creators;
    private List<String> barLabels;
    private List<ChartDatasetDto> barDatasets;
    private List<String> lineLabels;
    private List<ChartDatasetDto> lineDatasets;
    private List<String> doughnutLabels;
    private List<Long> doughnutDatasets;
    private List<TableDatasetDto> tableDatasets;
    private AnalysisRequestDto requestParams;
}
