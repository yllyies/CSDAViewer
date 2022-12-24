package com.agilent.csda.phase1.controller;


import com.agilent.csda.phase1.model.User;
import com.agilent.csda.phase1.service.UserService;
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


@Api(tags = "UserController")
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @ApiOperation("listPage")
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public ModelAndView listPage(@RequestParam(value = "pageNum", defaultValue = "1") @ApiParam("index") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") @ApiParam("pageSize") Integer pageSize) {
        List<User> users = userService.doFindPage(pageNum, pageSize);
        return new ModelAndView("/user/user_list", "allUsers", users);
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
}

