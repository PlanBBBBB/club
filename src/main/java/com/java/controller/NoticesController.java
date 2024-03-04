package com.java.controller;

import com.java.config.CacheHandle;
import com.java.dto.IdDto;
import com.java.dto.NoticesPageDto;
import com.java.entity.Notices;
import com.java.entity.Users;
import com.java.service.NoticesService;
import com.java.service.UsersService;
import com.java.utils.DateUtils;
import com.java.utils.IDUtils;
import com.java.utils.StringUtils;
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
 * 通知记录
 */
@Controller
@RequestMapping("/notices")
@Api(tags = "通知记录")
public class NoticesController {

    protected static final Logger Log = LoggerFactory.getLogger(NoticesController.class);

    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private NoticesService noticesService;

    @Resource
    private UsersService usersService;

    @PostMapping("")
    @ApiOperation("跳转到通知记录页面")
    public String index() {
        return "pages/Notices";
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation("查找指定通知记录")
    public R getInfo(@RequestBody IdDto idDto) {
        Log.info("查找指定通知记录，ID：{}", idDto.getId());
        Notices notices = noticesService.getOne(idDto.getId());
        return R.successData(notices);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查找通知记录")
    public R getPageInfos(@RequestBody NoticesPageDto noticesPageDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(noticesPageDto.getToken()));
        if (user.getType() == 0) {
            Log.info("分页查找指通知记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，通知标题：{}，社团名称：{}", noticesPageDto.getPageIndex(),
                    noticesPageDto.getPageSize(), noticesPageDto.getTitle(), noticesPageDto.getTeamName());
            PageData page = noticesService.getPageAll(noticesPageDto.getPageIndex(),
                    noticesPageDto.getPageSize(), noticesPageDto.getTitle(), noticesPageDto.getTeamName());
            return R.successData(page);
        } else {
            Log.info("分页查找指定用户相关通知记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，通知标题：{}，社团名称：{}", noticesPageDto.getPageIndex(),
                    noticesPageDto.getPageSize(), noticesPageDto.getTitle(), noticesPageDto.getTeamName());
            PageData page = noticesService.getPageById(noticesPageDto.getPageIndex(),
                    noticesPageDto.getPageSize(), user.getId(), noticesPageDto.getTitle(), noticesPageDto.getTeamName());
            return R.successData(page);
        }
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加通知记录")
    public R addInfo(@RequestBody Notices notices) {
        notices.setId(IDUtils.makeIDByCurrent());
        notices.setCreateTime(DateUtils.getNowDate("yyyy-MM-dd"));
        if (StringUtils.isNullOrEmpty(notices.getTeamId())) {
            notices.setTeamId(null);
        }
        Log.info("添加通知记录，传入参数：{}", notices);
        noticesService.add(notices);
        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改通知记录")
    public R updInfo(@RequestBody Notices notices) {
        Log.info("修改通知记录，传入参数：{}", notices);
        noticesService.update(notices);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除通知记录")
    public R delInfo(@RequestBody IdDto idDto) {
        Log.info("删除通知记录, ID:{}", idDto.getId());
        Notices notices = noticesService.getOne(idDto.getId());
        noticesService.delete(notices);
        return R.success();
    }
}