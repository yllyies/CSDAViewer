package com.agilent.iad.controller;


import com.agilent.iad.common.dto.CommonResult;
import com.agilent.iad.model.Bulletin;
import com.agilent.iad.service.BulletinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "公告管理")
@Controller
@RequestMapping("bulletin")
@Slf4j
public class BulletinController {

    @Autowired
    private BulletinService bulletinService;

    @ApiOperation("v2:公告查询，按更新时间降排")
    @GetMapping(value = "/api/list")
    @ResponseBody
    public CommonResult<List<Bulletin>> apiList() {
        return CommonResult.success(bulletinService.doFindAll());
    }

    @ApiOperation("v2:公告保存，根据提交的数据是否有ID进行新增或保存")
    @PostMapping(value = "/api/save")
    @ResponseBody
    public CommonResult<Bulletin> apiSave(@RequestBody Bulletin bulletin) {
        return CommonResult.success(bulletinService.doSave(bulletin));
    }

    @ApiOperation("v2:公告删除，根据提交的id删除")
    @PostMapping(value = "/api/delete")
    @ResponseBody
    public CommonResult<String> apiDelete(@RequestBody Bulletin bulletin) {
        bulletinService.doDelete(bulletin.getId());
        return CommonResult.success("success");
    }
}

