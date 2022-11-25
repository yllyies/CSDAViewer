package com.agilent.csda.acl.controller;


import com.agilent.csda.acl.model.User;
import com.agilent.csda.acl.model.UserRole;
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
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Api(tags = "UserController")
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRolesService userRolesService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @ApiOperation("listPage")
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public ModelAndView listPage(@RequestParam(value = "pageNum", defaultValue = "1") @ApiParam("index") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") @ApiParam("pageSize") Integer pageSize) {
        List<User> users = userService.doFindPage(pageNum, pageSize);
        return new ModelAndView("/user/user_list", "allUsers", users);
    }

    @PostMapping("/create")
    public String addUser(User user) {
        User dbUser = userService.doFindByName(user.getName());
        Assert.isNull(dbUser, "UserName Exist!");
        int count = userService.doCreate(user);
        if (count == 1) {
            LOGGER.debug("addUser success :id={}", user.getId());
        } else {
            LOGGER.debug("addUser failed :id={}", user.getId());
        }
        // default grant
        UserRole userRoles = new UserRole();
        userRoles.setAclUserId(userService.doFindByName(user.getName()).getId());
        userRoles.setAclRoleId(2L);
        userRolesService.doCreate(userRoles);
        return "redirect:/login";
    }

    @RequestMapping(value = "/toUpdate/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView toUpdate(@PathVariable("id") Long id) {
        User user = null;
        if (id != null) {
            user = userService.doFindById(id);
        } else {
            user = userService.doFindAll().get(0);
        }
        return new ModelAndView("/user/user_update", "user", user);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(User user) {
        int count = userService.doUpdate(user);
        if (count == 1) {
            LOGGER.debug("deleteUser success :id={}", user.getId());
        } else {
            LOGGER.debug("deleteUser failed :id={}", user.getId());
        }
        return "redirect:/user/listPage";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        int count = userService.doDelete(id);
        if (count == 1) {
            LOGGER.debug("deleteUser success :id={}", id);
        } else {
            LOGGER.debug("deleteUser failed :id={}", id);
        }
        return "redirect:/user/listPage";
    }
}

