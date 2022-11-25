package com.agilent.csda.acl.mapper;

import com.agilent.csda.acl.dto.UserRolesDto;
import com.agilent.csda.acl.model.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRolesMapper {

    int insert(UserRole record);

    UserRolesDto selectByUserId(@Param("userId") Long userId);

    List<UserRolesDto> selectGroupByUserId();

    int updateByUserIdAndRoleId(UserRole record);

    int deleteByUserId(@Param("userId") Long userId);
}