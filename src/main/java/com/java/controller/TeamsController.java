package com.java.controller;

import com.java.config.CacheHandle;
import com.java.dto.IdDto;
import com.java.dto.TeamsPageDto;
import com.java.entity.Teams;
import com.java.entity.Users;
import com.java.service.TeamsService;
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
import java.util.List;

/**
 * 社团信息
 */
@Controller
@RequestMapping("/teams")
@Api(tags = "社团信息")
public class TeamsController {

    protected static final Logger Log = LoggerFactory.getLogger(TeamsController.class);

    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private UsersService usersService;

    @Resource
    private TeamsService teamsService;

    @PostMapping("")
    @ApiOperation("跳转到社团信息页面")
    public String index() {

        return "pages/Teams";
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation("查找指定社团信息")
    public R getInfo(@RequestBody IdDto idDto) {

        Log.info("查找指定社团信息，ID：{}", idDto.getId());

        Teams teams = teamsService.getOne(idDto.getId());

        return R.successData(teams);
    }

    @PostMapping("/all")
    @ResponseBody
    @ApiOperation("获取全部的社团")
    public R getAll() {

        Log.info("获取全部的社团");

        List<Teams> list = teamsService.getAll();

        return R.successData(list);
    }

    @PostMapping("/man")
    @ResponseBody
    @ApiOperation("获取指定社团管理员相关的社团列表")
    public R getListByManId(@RequestBody IdDto idDto) {
        Log.info("获取指定社团管理员相关的社团列表");
        List<Teams> list = teamsService.getListByManId(idDto.getId());
        return R.successData(list);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页获取社团信息")
    public R getPageInfos(@RequestBody TeamsPageDto teamsPageDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(teamsPageDto.getToken()));
        if (user.getType() == 1) {
            teamsPageDto.setManager(user.getId());
        }
        Log.info("分页查找社团信息，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，附加参数：{},{},{}", teamsPageDto.getPageIndex(),
                teamsPageDto.getPageSize(), teamsPageDto.getName(),teamsPageDto.getTypeId(),teamsPageDto.getManager());
        PageData page = teamsService.getPageInfo(teamsPageDto.getPageIndex(),
                teamsPageDto.getPageSize(), teamsPageDto.getName(),teamsPageDto.getTypeId(),teamsPageDto.getManager());
        return R.successData(page);
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加社团信息")
    public R addInfo(@RequestBody Teams teams) {

        teams.setId(IDUtils.makeIDByCurrent());
        teams.setCreateTime(DateUtils.getNowDate("yyyy-MM-dd"));

        Log.info("添加社团信息，传入参数：{}", teams);

        teamsService.add(teams);

        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改社团信息")
    public R updInfo(@RequestBody Teams teams) {

        Log.info("修改社团信息，传入参数：{}", teams);

        teamsService.update(teams);

        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除社团信息")
    public R delInfo(@RequestBody IdDto idDto) {

        Log.info("删除社团信息, ID:{}", idDto.getId());

        Teams teams = teamsService.getOne(idDto.getId());

        teamsService.delete(teams);

        return R.success();
    }
}