package com.agilent.csda.acl.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ALTER TABLE cdsa.rslt CHANGE IsSqxRslt Is_sqx_rslt bit(1) NULL COMMENT '是否序列结果集，可能是跟测试无关。';
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

    @ManyToOne(targetEntity = Project.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(targetEntity = Dx.class, fetch = FetchType.EAGER)
    @JoinTable(name = "dx", joinColumns = {@JoinColumn(name = "parent_node_id")}, inverseJoinColumns = {@JoinColumn(name ="node_id")})
    private List<Dx> dxList;

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
}