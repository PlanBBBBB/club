package com.java.controller;

import com.java.dto.IdDto;
import com.java.dto.UsersPageDto;
import com.java.entity.Users;
import com.java.service.UsersService;
import com.java.utils.DateUtils;
import com.java.utils.IDUtils;
import com.java.vo.PageData;
import com.java.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 系统用户
 */
@Controller
@RequestMapping("/users")
@Api(tags = "系统用户")
public class UsersController {

    protected static final Logger Log = LoggerFactory.getLogger(UsersController.class);

    @Resource
    private UsersService usersService;

    @PostMapping("")
    @ApiOperation("系统用户首页")
    public String index() {
        return "pages/Users";
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation("查找指定系统用户")
    public R getInfo(@RequestBody IdDto idDto) {
        Log.info("查找指定系统用户，ID：{}", idDto.getId());
        Users users = usersService.getOne(idDto.getId());
        return R.successData(users);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查找系统用户")
    public R getPageInfos(@RequestBody UsersPageDto usersPageDto) {
        Log.info("分页查找系统用户，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，附加参数：{},{},{}", usersPageDto.getPageIndex(),
                usersPageDto.getPageSize(), usersPageDto.getName(), usersPageDto.getUserName(), usersPageDto.getPhone());
        PageData page = usersService.getPageInfo(usersPageDto.getPageIndex(),
                usersPageDto.getPageSize(), usersPageDto.getName(), usersPageDto.getUserName(), usersPageDto.getPhone());
        return R.successData(page);
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加系统用户")
    public R addInfo(@RequestBody Users users) {
        if (usersService.getUserByUserName(users.getUserName()) == null) {
            users.setId(IDUtils.makeIDByCurrent());
            users.setCreateTime(DateUtils.getNowDate());
            Log.info("添加系统用户，传入参数：{}", users);
            usersService.add(users);
            return R.success();
        } else {
            return R.warn("用户账号已存在，请重新输入");
        }
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改系统用户")
    public R updInfo(@RequestBody Users users) {
        Log.info("修改系统用户，传入参数：{}", users);
        usersService.update(users);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除系统用户")
    public R delInfo(@RequestBody IdDto idDto) {
        if (usersService.isRemove(idDto.getId())) {
            Log.info("删除系统用户, ID:{}", idDto.getId());
            Users users = usersService.getOne(idDto.getId());
            usersService.delete(users);
            return R.success();
        } else {
            return R.warn("用户存在关联社团，无法移除");
        }
    }
}