package com.java.controller;

import com.java.config.CacheHandle;
import com.java.dto.AddActivelogsDto;
import com.java.dto.IdDto;
import com.java.entity.ActiveLogs;
import com.java.entity.Users;
import com.java.service.ActiveLogsService;
import com.java.service.UsersService;
import com.java.utils.DateUtils;
import com.java.utils.IDUtils;
import com.java.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 报名记录
 */
@Controller
@RequestMapping("/activeLogs")
@Api(tags = "报名记录")
@Slf4j
public class ActiveLogsController {

    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private UsersService usersService;

    @Resource
    private ActiveLogsService activeLogsService;

    @PostMapping("")
    @ApiOperation(value = "报名记录页面")
    public String index() {
        return "pages/ActiveLogs";
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation(value = "获取指定报名记录")
    public R getInfo(@RequestBody IdDto idDto) {
        log.info("查找指定报名记录，ID：{}", idDto.getId());
        ActiveLogs activeLogs = activeLogsService.getOne(idDto.getId());
        return R.successData(activeLogs);
    }

    @PostMapping("/list")
    @ResponseBody
    @ApiOperation(value = "获取指定活动的报名记录")
    public R getList(@RequestBody IdDto idDto) {
        log.info("获取指定活动的报名记录，活动ID：{}", idDto.getId());
        List<Map<String, Object>> list = activeLogsService.getListByActiveId(idDto.getId());
        return R.successData(list);
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation(value = "添加报名记录")
    public R addInfo(@RequestBody AddActivelogsDto addActivelogsDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(addActivelogsDto.getToken()));
        if (activeLogsService.isActive(addActivelogsDto.getActiveId(), user.getId())) {
            addActivelogsDto.setId(IDUtils.makeIDByCurrent());
            addActivelogsDto.setUserId(user.getId());
            addActivelogsDto.setCreateTime(DateUtils.getNowDate());
            log.info("添加报名记录，传入参数：{}", addActivelogsDto);
            activeLogsService.add(addActivelogsDto);
            return R.success();
        } else {
            return R.warn("该活动您已参与，请勿重复报名");
        }
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation(value = "修改报名记录")
    public R updInfo(@RequestBody ActiveLogs activeLogs) {
        log.info("修改报名记录，传入参数：{}", activeLogs);
        activeLogsService.update(activeLogs);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation(value = "删除报名记录")
    public R delInfo(@RequestBody IdDto idDto) {
        log.info("删除报名记录, ID:{}", idDto.getId());
        ActiveLogs activeLogs = activeLogsService.getOne(idDto.getId());
        activeLogsService.delete(activeLogs);
        return R.success();
    }
}