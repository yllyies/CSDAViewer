package com.agilent.iad.controller;


import cn.hutool.core.util.StrUtil;
import com.agilent.iad.common.UserInfoContext;
import com.agilent.iad.common.dto.CommonResult;
import com.agilent.iad.model.User;
import com.agilent.iad.service.UserService;
import de.schlichtherle.license.LicenseContentException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Api(tags = "菜单跳转控制器")
@Controller
public class ACLController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ACLController.class);

    @ApiIgnore
    @ApiOperation("登录")
    @RequestMapping(value = {"/", "/login"}, method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("login");
    }

    @ApiIgnore
    @ApiOperation("重定向")
    @RequestMapping(value="/toRedirect",method = { RequestMethod.POST, RequestMethod.GET })
    public  ModelAndView toRedirect(HttpServletRequest request, RedirectAttributes attr){
        ModelAndView  model = new ModelAndView("redirect:/login");
        Object exception = request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (exception instanceof BadCredentialsException) {
            attr.addFlashAttribute("message", "用户名或密码不正确");
        } else if (exception instanceof LicenseContentException) {
            attr.addFlashAttribute("message", ((LicenseContentException) exception).getLocalizedMessage());
        } else if (exception instanceof CredentialsExpiredException) {
            attr.addFlashAttribute("message", "证书已过期");
        } else {
            attr.addFlashAttribute("message", ((Exception) exception).getLocalizedMessage());
        }
        return model;
    }

    @ApiIgnore
    @ApiOperation("默认页面入口")
    @GetMapping(value = "/index")
    public ModelAndView list() {
        return new ModelAndView("instrument/index");
    }

    @ApiIgnore
    @ApiOperation("校验用户名密码是否正确")
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public String check(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> map) {
        User user = userService.doFindByName(username);
        if (user != null && user.getPassword().equals(password)) {
            UserInfoContext.setUser(user);
            return "redirect:login";
        } else {
            map.put("msg", "用户名或密码不正确");
            return "error/500";
        }
    }

    @ApiIgnore
    @ApiOperation("登出")
    @GetMapping("/logout")
    public ModelAndView logoutView(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ModelAndView("redirect:/");
    }

    @ApiOperation("登录API")
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<?> apiLogin(@RequestBody User user) {
        if (null == user || StrUtil.isBlank(user.getName()) || StrUtil.isBlank(user.getPassword())) {
            return CommonResult.failed("用户名或密码不正确");
        }
        User dbUser = userService.doFindByName(user.getName());
        if (dbUser != null && user.getPassword().equals(dbUser.getPassword())) {
            UserInfoContext.setUser(user);
            return CommonResult.success("success", "用户已登录");
        } else {
            return CommonResult.failed("用户名或密码不正确");
        }
    }

    @ApiOperation("登出")
    @GetMapping("/api/logout")
    @ResponseBody
    public CommonResult<?> apiLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return CommonResult.success("success", "用户已登出");
    }
}

