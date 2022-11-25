package com.agilent.csda.acl.controller;


import com.agilent.csda.acl.model.Role;
import com.agilent.csda.acl.service.RoleService;
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

import java.util.List;


@Api(tags = "RoleController")
@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @ApiOperation("listPage")
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public ModelAndView listPage(@RequestParam(value = "pageNum", defaultValue = "1") @ApiParam("index") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") @ApiParam("pageSize") Integer pageSize) {
        List<Role> roles = roleService.doFindPage(pageNum, pageSize);
        return new ModelAndView("/role/role_list", "allRoles", roles);
    }

    @PostMapping("/create")
    @ApiOperation("create new role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addRole(Role role) {
        int count = roleService.doCreate(role);
        if (count == 1) {
            LOGGER.debug("addRole success :id={}", role.getId());
        } else {
            LOGGER.debug("addRole failed :id={}", role.getId());
        }
        return "redirect:/role/listPage";
    }

    @RequestMapping(value = "/toUpdate/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView toUpdate(@PathVariable("id") Long id) {
        Role role;
        if (id != null) {
            role = roleService.doFindById(id);
        } else {
            role = roleService.doFindAll().get(0);
        }
        return new ModelAndView("/role/role_update", "role", role);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(Role role) {
        int count = roleService.doUpdate(role);
        if (count == 1) {
            LOGGER.debug("deleteRole success :id={}", role.getId());
        } else {
            LOGGER.debug("deleteRole failed :id={}", role.getId());
        }
        return "redirect:/role/listPage";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        int count = roleService.doDelete(id);
        if (count == 1) {
            LOGGER.debug("deleteRole success :id={}", id);
        } else {
            LOGGER.debug("deleteRole failed :id={}", id);
        }
        return "redirect:/role/listPage";
    }

}

