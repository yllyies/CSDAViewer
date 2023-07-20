package com.agilent.cdsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "调用远程接口获取的仪器信息DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentDto implements Serializable {

    private static final long serialVersionUID = -4865203808778094585L;

    @ApiModelProperty("仪器ID，主键：object 表ID 仪器唯一索引")
    private String instrumentId;
    @ApiModelProperty("仪器状态")
    private String instrumentState;
    @ApiModelProperty("仪器名称")
    private String instrumentName;
    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("操作用户，实时SDK获取")
    private String commitUser;
    @ApiModelProperty("登录用户")
    private String acqUser;
    @ApiModelProperty("序列名称")
    private String sequenceName;
    @ApiModelProperty("序列提交时间")
    private String updateTime;
    @ApiModelProperty("当前样品名称")
    private String sampleName;
    @ApiModelProperty("当前样品第几针")
    private Integer currentSample;
    @ApiModelProperty("序列一共多少针")
    private Integer sampleTotal;
    @ApiModelProperty("仪器描述")
    private String instrumentDescription;
    @ApiModelProperty("仪器运行时间，数据库获取")
    private Long runtime;
    @ApiModelProperty("显示当前序列的运行状态，当前第几帧/一共有几针，如 5 / 10")
    private String sequenceInfo;
    /* 前端显示特殊处理 */
    @ApiModelProperty("仪器状态颜色")
    private String color;
    @ApiModelProperty("仪器运行时间字符串")
    private String runtimeString;
    @ApiModelProperty("进度条百分比长度")
    private String progressBarWidth;
    @ApiModelProperty("序列已运行时间，根据当前时间与提交时间的差值计算")
    private String executeTime;
}
