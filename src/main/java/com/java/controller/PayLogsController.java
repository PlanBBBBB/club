package com.java.controller;

import com.java.config.CacheHandle;
import com.java.dto.ApplylogsOrUserPageDto;
import com.java.dto.IdDto;
import com.java.entity.PayLogs;
import com.java.entity.Users;
import com.java.service.PayLogsService;
import com.java.service.UsersService;
import com.java.utils.DateUtils;
import com.java.utils.IDUtils;
import com.java.vo.PageData;
import com.java.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 缴费记录
 */
@Controller
@RequestMapping("/paylogs")
@Api(tags = "缴费记录")
@Slf4j
public class PayLogsController {

    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private UsersService usersService;

    @Resource
    private PayLogsService paylogsService;


    @PostMapping("")
    @ApiOperation("缴费记录页面")
    public String index() {
        return "pages/PayLogs";
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation("查找指定缴费记录")
    public R getInfo(@RequestBody IdDto idDto) {
        log.info("查找指定缴费记录，ID：{}", idDto.getId());
        PayLogs paylogs = paylogsService.getOne(idDto.getId());
        return R.successData(paylogs);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查找缴费记录")
    public R getPageInfos(@RequestBody ApplylogsOrUserPageDto applylogsOrUserPageDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(applylogsOrUserPageDto.getToken()));
        if (user.getType() == 0) {
            log.info("分页查看全部缴费记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", applylogsOrUserPageDto.getPageIndex(),
                    applylogsOrUserPageDto.getPageSize(), applylogsOrUserPageDto.getTeamName(), applylogsOrUserPageDto.getUserName());
            PageData page = paylogsService.getPageInfo(applylogsOrUserPageDto.getPageIndex(),
                    applylogsOrUserPageDto.getPageSize(), null, applylogsOrUserPageDto.getTeamName(), applylogsOrUserPageDto.getUserName());
            return R.successData(page);
        } else if (user.getType() == 1) {
            log.info("团队管理员查看缴费记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", applylogsOrUserPageDto.getPageIndex(),
                    applylogsOrUserPageDto.getPageSize(), applylogsOrUserPageDto.getTeamName(), applylogsOrUserPageDto.getUserName());
            PageData page = paylogsService.getManPageInfo(applylogsOrUserPageDto.getPageIndex(),
                    applylogsOrUserPageDto.getPageSize(), user.getId(), applylogsOrUserPageDto.getTeamName(), applylogsOrUserPageDto.getUserName());
            return R.successData(page);
        } else {
            log.info("分页用户相关缴费记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", applylogsOrUserPageDto.getPageIndex(),
                    applylogsOrUserPageDto.getPageSize(), applylogsOrUserPageDto.getTeamName(), applylogsOrUserPageDto.getUserName());
            PageData page = paylogsService.getPageInfo(applylogsOrUserPageDto.getPageIndex(),
                    applylogsOrUserPageDto.getPageSize(), user.getId(), applylogsOrUserPageDto.getTeamName(), null);
            return R.successData(page);
        }
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加缴费记录")
    public R addInfo(@RequestBody PayLogs paylogs) {
        paylogs.setId(IDUtils.makeIDByCurrent());
        paylogs.setCreateTime(DateUtils.getNowDate());
        log.info("添加缴费记录，传入参数：{}", paylogs);
        paylogsService.add(paylogs);
        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改缴费记录")
    public R updInfo(@RequestBody PayLogs paylogs) {
        log.info("修改缴费记录，传入参数：{}", paylogs);
        paylogsService.update(paylogs);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除缴费记录")
    public R delInfo(@RequestBody IdDto idDto) {
        log.info("删除缴费记录, ID:{}", idDto.getId());
        PayLogs paylogs = paylogsService.getOne(idDto.getId());
        paylogsService.delete(paylogs);
        return R.success();
    }
}