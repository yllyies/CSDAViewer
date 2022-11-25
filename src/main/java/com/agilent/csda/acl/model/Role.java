package com.agilent.csda.acl.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACl_ROLE")
public class Role implements Serializable
{
    /**
     *
     */
    private static final long	serialVersionUID	= 1L;

    @Id
    @Column(nullable = false)
    private Long				id;

    @Column(nullable = false)
    private String				name;

    @Column(nullable = false)
    private Integer				level;

    @Column(nullable = false)
    private String				authority;

    @Column(nullable = false)
    private String				status;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getAuthority()
    {
        return authority;
    }

    public void setAuthority(String authority)
    {
        this.authority = authority;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", level=").append(level);
        sb.append(", authority=").append(authority);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }

}