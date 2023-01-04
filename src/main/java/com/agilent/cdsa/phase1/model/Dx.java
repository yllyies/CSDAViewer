package com.agilent.cdsa.phase1.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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

    @Column(name = "uploaded_date")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Timestamp uploadedDate;

    @ManyToOne(targetEntity = Rslt.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_node_id", insertable = false, updatable = false)
    private Rslt Rslt;

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

    public Timestamp getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(Timestamp uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Rslt getRslt() {
        return Rslt;
    }

    public void setRslt(Rslt rslt) {
        Rslt = rslt;
    }
}