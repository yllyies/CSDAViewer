package com.agilent.csda.acl.controller;

import com.agilent.csda.acl.model.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Api(tags = "RoleController")
@Controller
@RequestMapping("menu")
public class MenuController {

    @ApiOperation("list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        List<String> menuList = Arrays.asList("Lab GC Room", "Lab LC Room");
        return new ModelAndView("/index", "menuList", menuList);
    }
}
