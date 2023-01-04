package com.agilent.cdsa.phase1.controller;


import com.agilent.cdsa.phase1.model.User;
import com.agilent.cdsa.phase1.service.UserService;
import com.agilent.cdsa.common.UserInfoContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation("index")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView list() {
        return new ModelAndView("instrument/index");
    }


    @RequestMapping(value = "check", method = RequestMethod.POST)
    public String check(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> map) {
        User user = userService.doFindByName(username);
        if (user != null && user.getPassword().equals(password)) {
            UserInfoContext.setUser(user);
            return "redirect:index";
        } else {
            map.put("msg", "error name or password");
            return "error/500";
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

