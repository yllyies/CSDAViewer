package com.agilent.csda.acl.controller;


import com.agilent.csda.acl.dto.UserRolesDto;
import com.agilent.csda.acl.model.Role;
import com.agilent.csda.acl.model.User;
import com.agilent.csda.acl.model.UserRole;
import com.agilent.csda.acl.service.RoleService;
import com.agilent.csda.acl.service.UserRolesService;
import com.agilent.csda.acl.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Api(tags = "UserRolesController")
@Controller
@RequestMapping("userRoles")
public class UserRolesController {

    @Autowired
    private UserRolesService userRolesService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRolesController.class);

    @ApiOperation("listPage")
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public ModelAndView listPage(@RequestParam(value = "pageNum", defaultValue = "1") @ApiParam("index") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") @ApiParam("pageSize") Integer pageSize) {
        List<UserRolesDto> userRolesDtos = userRolesService.doFindDtoPage(pageNum, pageSize);
        List<Role> allRoles = roleService.doFindAll();
        Map<Long, String> userId2NameMap = userService.doFindAll().stream().collect(Collectors.toMap(User::getId, User::getName));
        for (UserRolesDto userRolesDto : userRolesDtos) {
            List<Long> roleIdList = userRolesDto.getAclRoleList().stream().map(Role::getId).collect(Collectors.toList());
            userRolesDto.setAclRoleIdList(roleIdList);
            userRolesDto.setAclRoleList(allRoles);
            userRolesDto.setAclUserName(userId2NameMap.get(userRolesDto.getAclUserId()));
        }
        return new ModelAndView("/user_to_role/user_to_role_list", "allUserRoles", userRolesDtos);
    }

    @RequestMapping(value = "/toCreate", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView toCreate() {
        ModelAndView modelAndView = new ModelAndView("/user_to_role/user_to_role_create");
        modelAndView.addObject("roleList", roleService.doFindAll());
        modelAndView.addObject("userList", userService.doFindAll());
        return modelAndView;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUserRoles(UserRolesDto userRolesDto) {
        UserRole userRoles = new UserRole();
        userRoles.setAclUserId(userRolesDto.getAclUserId());
        for (Long aclRoleId : userRolesDto.getAclRoleIdList()) {
            userRoles.setAclRoleId(aclRoleId);
            userRolesService.doCreate(userRoles);
        }

        int count = userRolesService.doCreate(userRoles);
        if (count == 1) {
            LOGGER.debug("addUserRoles success");
        } else {
            LOGGER.debug("addUserRoles failed");
        }
        return "redirect:/userRoles/listPage";
    }

    @RequestMapping(value = "/toUpdate/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView toUpdate(@PathVariable("id") Long id) {
        UserRolesDto userRolesDto = userRolesService.doFindByUserId(id);
        User user = userService.doFindById(userRolesDto.getAclUserId());

        ModelAndView modelAndView = new ModelAndView("/user_to_role/user_to_role_update");
        modelAndView.addObject("roleList", roleService.doFindAll());
        modelAndView.addObject("roleIdList", userRolesDto.getAclRoleList().stream().map(Role::getId).collect(Collectors.toList()));
        modelAndView.addObject("userList", Arrays.asList(user));
        return modelAndView;
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(UserRolesDto userRolesDto) {
        userRolesService.doDelete(userRolesDto.getAclUserId());
        UserRole userRoles = new UserRole();
        userRoles.setAclUserId(userRolesDto.getAclUserId());
        for (Long aclRoleId : userRolesDto.getAclRoleIdList()) {
            userRoles.setAclRoleId(aclRoleId);
            userRolesService.doCreate(userRoles);
        }

        int count = userRolesService.doCreate(userRoles);
        if (count == 1) {
            LOGGER.debug("addUserRoles success");
        } else {
            LOGGER.debug("addUserRoles failed");
        }
        return "redirect:/userRoles/listPage";
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("userId") Long userId) {
        int count = userRolesService.doDelete(userId);
        if (count == 1) {
            LOGGER.debug("delete success");
        } else {
            LOGGER.debug("delete failed");
        }
        return "redirect:/userRoles/listPage";
    }
}

