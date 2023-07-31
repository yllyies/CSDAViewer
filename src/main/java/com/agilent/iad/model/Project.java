package com.agilent.iad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "project")
public class Project implements Serializable
{

    private static final long serialVersionUID = 3905628661906255711L;

    @Id
    @Column(nullable = false)
    private BigDecimal id;

    @Column
    private String name;

    @Column
    private String cpPath;

    @Column
    private String cmPath;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpPath() {
        return cpPath;
    }

    public void setCpPath(String cpPath) {
        this.cpPath = cpPath;
    }

    public String getCmPath() {
        return cmPath;
    }

    public void setCmPath(String cmPath) {
        this.cmPath = cmPath;
    }
}