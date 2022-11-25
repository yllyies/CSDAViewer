package com.agilent.csda.acl.controller;


import com.agilent.csda.acl.model.User;
import com.agilent.csda.acl.service.UserService;
import com.agilent.csda.common.UserInfoContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Api(tags = "ACLController")
@Controller
public class ACLController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ACLController.class);

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public String login() {
        return "login";
    }

//    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
//    public String index() {
//        return "index";
//    }

    @ApiOperation("index")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView list() {
        List<String> menuList = Arrays.asList("Lab GC Room", "Lab LC Room");
        return new ModelAndView("/index", "menuList", menuList);
    }

    @RequestMapping(value = "user/user_list", method = RequestMethod.GET)
    public String userList() {
        return "user/user_list";
    }

    @RequestMapping(value = "user/user_create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String userCreate() {
        return "user/user_create";
    }

    @RequestMapping(value = "user/user_update", method = RequestMethod.GET)
    public String userUpdate() {
        return "user/user_update";
    }

    @RequestMapping(value = "role/role_list", method = RequestMethod.GET)
    public String roleList() {
        return "role/role_list";
    }

    @RequestMapping(value = "role/role_create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String roleCreate() {
        return "role/role_create";
    }

    @RequestMapping(value = "role/role_update", method = RequestMethod.GET)
    public String roleUpdate() {
        return "role/role_update";
    }

    @ApiOperation("menu/labroom_list")
    @RequestMapping(value = "menu/labroom_list", method = RequestMethod.GET)
    public ModelAndView labroomList() {
        List<String> menuList = Arrays.asList("Lab GC Room", "Lab LC Room");
        return new ModelAndView("labroom/labroom_list", "menuList", menuList);
    }

    @RequestMapping(value = "check", method = RequestMethod.POST)
    public String check(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> map) {
        User user = userService.doFindByName(username);
        if (user != null && user.getPassword().equals(password)) {
            UserInfoContext.setUser(user);
            return "redirect:/index";
        } else {
            map.put("msg", "error name or password");
            return "error/page_500";
        }
    }

    /**
     * logout
     */
    @RequestMapping("/logout")
    public ModelAndView logoutView(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return new ModelAndView("redirect:/");
    }
}

