package com.agilent.csda.acl.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ACL_USER")
public class User implements Serializable
{
    /**
     *
     */
    private static final long	serialVersionUID	= 1L;

    @Id
    @Column(nullable = false)
    private Long				id;

    @Column(nullable = false)
    private String				title;

    @Column(nullable = false)
    private String				name;

    @Column(nullable = false)
    private String				email;

    @Column(nullable = false)
    private String				authtype;

    @Column(nullable = false)
    private String				password;

    @Column(nullable = false)
    private String				saml;

    @Column(nullable = false)
    private String				status;

    @ManyToMany
    @JoinTable(
            name = "ACL_USER_ROLE",
            joinColumns = @JoinColumn(name = "acl_user_id"),
            inverseJoinColumns = @JoinColumn(name = "acl_role_id")
    )
    private Set<Role>			roles;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAuthtype()
    {
        return authtype;
    }

    public void setAuthtype(String authtype)
    {
        this.authtype = authtype;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getSaml()
    {
        return saml;
    }

    public void setSaml(String saml)
    {
        this.saml = saml;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }
}