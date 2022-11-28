package com.agilent.csda.acl.controller;

import cn.hutool.core.util.StrUtil;
import com.agilent.csda.acl.dao.RsltDao;
import com.agilent.csda.acl.model.Rslt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Rslt Controller")
@Controller
@RequestMapping("labroom")
public class MenuController {

    @Autowired
    private RsltDao rsltDao;

    @ApiOperation("UI2")
    @RequestMapping(value = "labroom_list", method = RequestMethod.GET)
    public ModelAndView labroomList() {
        List<Rslt> all = rsltDao.findAll();
        List<String> instrumentNames = all.stream().map(Rslt::getInstrumentName).distinct().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        List<String> projectNames = all.stream().map(rslt -> rslt.getProject().getName()).distinct().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        List<String> creators = all.stream().map(rslt -> rslt.getCreator()).distinct().filter(StrUtil::isNotBlank).collect(Collectors.toList());

        ModelAndView modelAndView = new ModelAndView("labroom/labroom_list");
        modelAndView.getModel().put("customParams", "helloworld");
        modelAndView.getModel().put("instrumentNames", instrumentNames);
        modelAndView.getModel().put("projectNames", projectNames);
        modelAndView.getModel().put("creators", creators);
        return modelAndView;
    }
}
