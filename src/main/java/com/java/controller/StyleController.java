package com.java.controller;

import com.java.config.CacheHandle;
import com.java.dto.ActivePageDto;
import com.java.dto.IdDto;
import com.java.entity.Style;
import com.java.entity.Users;
import com.java.service.StyleService;
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
 * 风采信息
 */
@Controller
@RequestMapping("/style")
@Api(tags = "风采信息")
public class StyleController {

    protected static final Logger Log = LoggerFactory.getLogger(ActivitiesController.class);

    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private UsersService usersService;

    @Resource
    private StyleService styleService;

    @PostMapping("")
    @ApiOperation("风采信息首页")
    public String index() {
        return "pages/Style";
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation("查找指定风采信息")
    public R getInfo(@RequestBody IdDto idDto) {
        Log.info("查找指定风采信息，ID：{}", idDto.getId());
        Style style = styleService.getOne(idDto.getId());
        return R.successData(style);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查找风采信息")
    public R getPageInfos(@RequestBody ActivePageDto activePageDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(activePageDto.getToken()));
        if (user.getType() == 0) {
            Log.info("分页查找风采信息，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，社团名称：{}，风采名称：{}", activePageDto.getPageIndex(),
                    activePageDto.getPageSize(), activePageDto.getTeamName(), activePageDto.getActiveName());
            PageData page = styleService.getPageAll(activePageDto.getPageIndex(),
                    activePageDto.getPageSize(), activePageDto.getTeamName(), activePageDto.getActiveName());
            return R.successData(page);
        } else {
            Log.info("分页查找风采信息，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，社团名称：{}，风采名称：{}", activePageDto.getPageIndex(),
                    activePageDto.getPageSize(), activePageDto.getTeamName(), activePageDto.getActiveName());
            PageData page = styleService.getPageByUserId(activePageDto.getPageIndex(),
                    activePageDto.getPageSize(), user.getId(), activePageDto.getTeamName(), activePageDto.getActiveName());
            return R.successData(page);
        }
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加风采信息")
    public R addInfo(@RequestBody Style style) {
        style.setId(IDUtils.makeIDByCurrent());
        Log.info("添加风采信息，传入参数：{}", style);
        styleService.add(style);
        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改风采信息")
    public R updInfo(@RequestBody Style style) {
        Log.info("修改风采信息，传入参数：{}", style);
        styleService.update(style);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除风采信息")
    public R delInfo(@RequestBody IdDto idDto) {
        Log.info("删除风采信息, ID:{}", idDto.getId());
        Style style = styleService.getOne(idDto.getId());
        styleService.delete(style);
        return R.success();
    }
}