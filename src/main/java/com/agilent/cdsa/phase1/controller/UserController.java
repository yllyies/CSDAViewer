package com.agilent.cdsa.phase1.controller;


import com.agilent.cdsa.phase1.model.User;
import com.agilent.cdsa.phase1.service.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Api(tags = "UserController")
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

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

