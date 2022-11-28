package com.agilent.csda.acl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ALTER TABLE cdsa.rslt CHANGE IsSqxRslt Is_sqx_rslt bit(1) NULL COMMENT '是否序列结果集，可能是跟测试无关。';
 */
@Entity
@Table(name = "dx")
public class Dx implements Serializable
{
    private static final long serialVersionUID = -3041174739979095752L;

    @Id
    @Column(nullable = false, name = "node_id")
    private BigDecimal nodeId;

    @Column(name = "parent_node_id")
    private BigDecimal parentNodeId;

    @Column(length = 1024)
    private String name;

    @Column(length = 30)
    private String uploaded;

    @Column
    private Integer methodSetTime;

    @Column
    private Integer collectedTime;

    @Column
    private String injectionSource;

    @Column
    private Float injectionVolume;

    @Column
    private BigDecimal filePath;

    public BigDecimal getNodeId() {
        return nodeId;
    }

    public void setNodeId(BigDecimal nodeId) {
        this.nodeId = nodeId;
    }

    public BigDecimal getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(BigDecimal parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public Integer getMethodSetTime() {
        return methodSetTime;
    }

    public void setMethodSetTime(Integer methodSetTime) {
        this.methodSetTime = methodSetTime;
    }

    public Integer getCollectedTime() {
        return collectedTime;
    }

    public void setCollectedTime(Integer collectedTime) {
        this.collectedTime = collectedTime;
    }

    public String getInjectionSource() {
        return injectionSource;
    }

    public void setInjectionSource(String injectionSource) {
        this.injectionSource = injectionSource;
    }

    public Float getInjectionVolume() {
        return injectionVolume;
    }

    public void setInjectionVolume(Float injectionVolume) {
        this.injectionVolume = injectionVolume;
    }

    public BigDecimal getFilePath() {
        return filePath;
    }

    public void setFilePath(BigDecimal filePath) {
        this.filePath = filePath;
    }
}