package com.agilent.cdsa.phase1.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * ALTER TABLE cdsa.rslt CHANGE IsSqxRslt is_sqx_rslt bit(1) NULL COMMENT '是否序列结果集，可能是跟测试无关。';
 */
@Entity
@Table(name = "rslt")
@NamedEntityGraph(name = "Rslt.Graph", attributeNodes = {@NamedAttributeNode("project"), @NamedAttributeNode("dxList")})
public class Rslt implements Serializable
{
    private static final long serialVersionUID = -1175451183014155708L;

    @Id
    @Column(nullable = false)
    private BigDecimal id;

    @Column
    private BigDecimal nodeId;

    @Column
    private String name;

    @Column
    private String cmPath;

    @Column
    private String creator;

    @Column
    private String created;

    @Column(name = "created_date")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Timestamp createdDate;

    @Column
    private Boolean isSqxRslt;

    @Column(name = "project_id", insertable = false, updatable = false)
    private BigDecimal projectId;

    @Column
    private String cmFullPath;

    @Column
    private String instrumentName;

    @Column
    private String sequenceName;

    @ManyToOne(targetEntity = Project.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(targetEntity = Dx.class, fetch = FetchType.LAZY)
    @JoinTable(name = "dx", joinColumns = {@JoinColumn(name = "parent_node_id")}, inverseJoinColumns = {@JoinColumn(name ="node_id")})
    private List<Dx> dxList;

    /** 前端展示字段 */
    @Transient
    @JsonProperty
    private Integer totalTime;

    @Transient
    @JsonProperty
    private String location;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getNodeId() {
        return nodeId;
    }

    public void setNodeId(BigDecimal nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCmPath() {
        return cmPath;
    }

    public void setCmPath(String cmPath) {
        this.cmPath = cmPath;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getSqxRslt() {
        return isSqxRslt;
    }

    public void setSqxRslt(Boolean sqxRslt) {
        isSqxRslt = sqxRslt;
    }

    public BigDecimal getProjectId() {
        return projectId;
    }

    public void setProjectId(BigDecimal projectId) {
        this.projectId = projectId;
    }

    public String getCmFullPath() {
        return cmFullPath;
    }

    public void setCmFullPath(String cmFullPath) {
        this.cmFullPath = cmFullPath;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Dx> getDxList() {
        return dxList;
    }

    public void setDxList(List<Dx> dxList) {
        this.dxList = dxList;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}