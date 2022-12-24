package com.agilent.csda.phase1.dto;

import com.agilent.csda.common.CodeListConstant;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "请求参数DTO")
@Data
public class AnalysisRequestDto implements Serializable {
    // 1.数据维度： 仪器、项目、人员；过滤条件：如选择项目后，过滤参与人员
    private String viewType = CodeListConstant.INSTRUMENT_VIEW;
    private String instrumentNames;
    private String projectNames;
    private String creatorNames;
    // 2.选择查询时间，查询粒度
    private String timeUnit;
    private String daterange;
    // 选择显示运行时间或针数 TODO
    private String yAxisUnit;

}
