package com.agilent.csda.acl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ACL_USER_ROLE")
public class UserRole  implements Serializable {
    @Id
    @Column(name = "acl_user_id", nullable = false)
    private Long aclUserId;

    @Column(name = "acl_role_id", nullable = false)
    private Long aclRoleId;

    public Long getAclUserId() {
        return aclUserId;
    }

    public void setAclUserId(Long aclUserId) {
        this.aclUserId = aclUserId;
    }

    public Long getAclRoleId() {
        return aclRoleId;
    }

    public void setAclRoleId(Long aclRoleId) {
        this.aclRoleId = aclRoleId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", aclUserId=").append(aclUserId);
        sb.append(", aclRoleId=").append(aclRoleId);
        sb.append("]");
        return sb.toString();
    }
}