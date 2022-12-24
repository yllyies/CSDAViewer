package com.agilent.csda.phase1.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "调用远程接口获取的仪器信息DTO")
@Data
public class InstrumentDto implements Serializable {
    @ApiModelProperty("仪器ID，主键：object 表ID 仪器唯一索引")
    private String instrumentId;
    @ApiModelProperty("仪器状态")
    private String instrumentState;
    @ApiModelProperty("仪器名称")
    private String instrumentName;
    @ApiModelProperty("仪器类型")
    private String instrumentType;
    @ApiModelProperty("项目")
    private String projectName;
    @ApiModelProperty("操作用户")
    private String operationUsername;
    @ApiModelProperty("登录用户")
    private String loginUsername;
    @ApiModelProperty("序列名称")
    private String sequenceName;
    @ApiModelProperty("提交时间")
    private String commitTime;
    @ApiModelProperty("当前样品")
    private String currentSample;
    @ApiModelProperty("仪器描述")
    private String instrumentDescription;
    @ApiModelProperty("仪器运行时间")
    private Long runtime;
    @ApiModelProperty("显示当前序列的运行状态")
    private String sequenceInfo;
}
