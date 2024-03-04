package com.java.controller;

import com.java.config.CacheHandle;
import com.java.dto.AddApplyLogsDto;
import com.java.dto.ApplylogsOrUserPageDto;
import com.java.dto.IdDto;
import com.java.entity.ApplyLogs;
import com.java.entity.Users;
import com.java.service.ApplyLogsService;
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
 * 申请记录
 */
@Controller
@RequestMapping("/applyLogs")
@Api(tags = "申请记录")
public class ApplyLogsController {

    protected static final Logger Log = LoggerFactory.getLogger(ApplyLogsController.class);

    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private UsersService usersService;

    @Resource
    private ApplyLogsService applyLogsService;

    @PostMapping("")
    @ApiOperation("跳转到申请记录页面")
    public String index() {
        return "pages/ApplyLogs";
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation("获取申请记录信息")
    public R getInfo(@RequestBody IdDto idDto) {
        Log.info("查找指定申请记录，ID：{}", idDto.getId());
        ApplyLogs applyLogs = applyLogsService.getOne(idDto.getId());
        return R.successData(applyLogs);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页获取申请记录信息")
    public R getPageInfos(@RequestBody ApplylogsOrUserPageDto applylogsPageDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(applylogsPageDto.getToken()));
        if (user.getType() == 0) {
            Log.info("分页查看全部申请记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", applylogsPageDto.getPageIndex(),
                    applylogsPageDto.getPageIndex(), applylogsPageDto.getTeamName(), applylogsPageDto.getUserName());
            PageData page = applyLogsService.getPageInfo(applylogsPageDto.getPageIndex(),
                    applylogsPageDto.getPageIndex(), null, applylogsPageDto.getTeamName(), applylogsPageDto.getUserName());
            return R.successData(page);
        } else if (user.getType() == 1) {
            Log.info("团队管理员查看申请记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", applylogsPageDto.getPageIndex(),
                    applylogsPageDto.getPageIndex(), applylogsPageDto.getTeamName(), applylogsPageDto.getUserName());
            PageData page = applyLogsService.getManPageInfo(applylogsPageDto.getPageIndex(),
                    applylogsPageDto.getPageIndex(), user.getId(), applylogsPageDto.getTeamName(), applylogsPageDto.getUserName());
            return R.successData(page);

        } else {
            Log.info("分页用户相关申请记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", applylogsPageDto.getPageIndex(),
                    applylogsPageDto.getPageIndex(), applylogsPageDto.getTeamName(), applylogsPageDto.getUserName());
            PageData page = applyLogsService.getPageInfo(applylogsPageDto.getPageIndex(),
                    applylogsPageDto.getPageIndex(), user.getId(), applylogsPageDto.getTeamName(), null);
            return R.successData(page);
        }
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加申请记录")
    public R addInfo(@RequestBody AddApplyLogsDto addApplyLogsDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(addApplyLogsDto.getToken()));
        if (applyLogsService.isApply(user.getId(), addApplyLogsDto.getTeamId())) {
            addApplyLogsDto.setId(IDUtils.makeIDByCurrent());
            addApplyLogsDto.setUserId(user.getId());
            addApplyLogsDto.setCreateTime(DateUtils.getNowDate());
            Log.info("添加申请记录，传入参数：{}", addApplyLogsDto);
            applyLogsService.add(addApplyLogsDto);
            return R.success();
        } else {
            return R.warn("申请审核中，请耐心等待");
        }
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改申请记录")
    public R updInfo(@RequestBody ApplyLogs applyLogs) {

        Log.info("修改申请记录，传入参数：{}", applyLogs);

        applyLogsService.update(applyLogs);

        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除申请记录")
    public R delInfo(@RequestBody IdDto idDto) {

        Log.info("删除申请记录, ID:{}", idDto.getId());

        ApplyLogs applyLogs = applyLogsService.getOne(idDto.getId());

        applyLogsService.delete(applyLogs);

        return R.success();
    }
}