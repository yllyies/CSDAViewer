package com.agilent.csda.acl.service;

import com.agilent.csda.acl.dto.UserRolesDto;
import com.agilent.csda.acl.model.UserRole;

import java.util.List;

/**
 * @author lifang
 * @since 2019-09-01
 */
public interface UserRolesService {

    int doCreate(UserRole userRoles);

    List<UserRolesDto> doFindDtoPage(int pageNum, int pageSize);

    UserRolesDto doFindByUserId(Long userId);

    int doUpdate(UserRole userRoles);

    int doDelete(Long userId);
}
