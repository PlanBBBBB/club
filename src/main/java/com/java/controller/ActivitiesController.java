package com.java.controller;

import com.java.config.CacheHandle;
import com.java.dto.ActivePageDto;
import com.java.dto.IdDto;
import com.java.entity.Activities;
import com.java.entity.Users;
import com.java.service.ActivitiesService;
import com.java.service.UsersService;
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
 * 活动信息
 */
@Controller
@RequestMapping("/activities")
@Api(tags = "活动信息")
public class ActivitiesController {

    protected static final Logger Log = LoggerFactory.getLogger(ActivitiesController.class);


    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private UsersService usersService;

    @Resource
    private ActivitiesService activitiesService;

    @PostMapping("")
    @ApiOperation("活动信息首页")
    public String index() {
        return "pages/Activities";
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation("查找指定活动信息")
    public R getInfo(@RequestBody IdDto idDto) {
        Log.info("查找指定活动信息，ID：{}", idDto.getId());
        Activities activities = activitiesService.getOne(idDto.getId());
        return R.successData(activities);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查找活动信息")
    public R getPageInfos(@RequestBody ActivePageDto activePageDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(activePageDto.getToken()));
        if (user.getType() == 0) {
            Log.info("分页查找活动信息，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，社团名称：{}，活动名称：{}", activePageDto.getPageIndex(),
                    activePageDto.getPageSize(), activePageDto.getTeamName(), activePageDto.getActiveName());
            PageData page = activitiesService.getPageAll(activePageDto.getPageIndex(),
                    activePageDto.getPageSize(), activePageDto.getTeamName(), activePageDto.getActiveName());
            return R.successData(page);
        } else {
            Log.info("分页查找活动信息，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，社团名称：{}，活动名称：{}", activePageDto.getPageIndex(),
                    activePageDto.getPageSize(), activePageDto.getTeamName(), activePageDto.getActiveName());
            PageData page = activitiesService.getPageByUserId(activePageDto.getPageIndex(),
                    activePageDto.getPageSize(), user.getId(), activePageDto.getTeamName(), activePageDto.getActiveName());
            return R.successData(page);
        }
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加活动信息")
    public R addInfo(@RequestBody Activities activities) {
        activities.setId(IDUtils.makeIDByCurrent());
        Log.info("添加活动信息，传入参数：{}", activities);
        activitiesService.add(activities);
        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改活动信息")
    public R updInfo(@RequestBody Activities activities) {
        Log.info("修改活动信息，传入参数：{}", activities);
        activitiesService.update(activities);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除活动信息")
    public R delInfo(@RequestBody IdDto idDto) {
        Log.info("删除活动信息, ID:{}", idDto.getId());
        Activities activities = activitiesService.getOne(idDto.getId());
        activitiesService.delete(activities);
        return R.success();
    }
}