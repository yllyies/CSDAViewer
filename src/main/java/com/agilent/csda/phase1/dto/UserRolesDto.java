package com.agilent.csda.phase1.dto;

import com.agilent.csda.phase1.model.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return new BCryptPasswordEncoder().encode(aclUser.getPassword());
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
