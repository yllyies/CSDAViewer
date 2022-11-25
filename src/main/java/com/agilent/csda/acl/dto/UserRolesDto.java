package com.agilent.csda.acl.dto;

import com.agilent.csda.acl.model.Role;
import com.agilent.csda.acl.model.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lifang
 * @since 2019-09-05
 */
@ApiModel(value = "user-role relationship dto")
public class UserRolesDto implements UserDetails {

    @ApiModelProperty(value = "User ID")
    private Long aclUserId;

    @ApiModelProperty(value = "User Name")
    private String aclUserName;

    @ApiModelProperty(value = "User Model")
    private User aclUser;

    @ApiModelProperty(value = "Role ID List")
    private List<Long> aclRoleIdList;

    @ApiModelProperty(value = "Role Model List")
    private List<Role> aclRoleList;

    public Long getAclUserId() {
        return aclUserId;
    }

    public void setAclUserId(Long aclUserId) {
        this.aclUserId = aclUserId;
    }

    public String getAclUserName() {
        return aclUserName;
    }

    public void setAclUserName(String aclUserName) {
        this.aclUserName = aclUserName;
    }

    public User getAclUser() {
        return aclUser;
    }

    public void setAclUser(User aclUser) {
        this.aclUser = aclUser;
    }

    public List<Long> getAclRoleIdList() {
        return aclRoleIdList;
    }

    public void setAclRoleIdList(List<Long> aclRoleIdList) {
        this.aclRoleIdList = aclRoleIdList;
    }

    public List<Role> getAclRoleList() {
        return aclRoleList;
    }

    public void setAclRoleList(List<Role> aclRoleList) {
        this.aclRoleList = aclRoleList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return aclRoleList.stream()
                .filter(permission -> permission.getAuthority() != null)
                .map(permission -> new SimpleGrantedAuthority(permission.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return aclUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.aclUserName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return aclUser.getStatus().equals(1);
        return true;
    }
}
