package com.java.controller;

import com.java.config.CacheHandle;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 系统请求响应控制器
 * 缴费记录
 */
@Controller
@RequestMapping("/payLogs")
@Api(tags = "缴费记录")
public class PayLogsController extends BaseController {

    protected static final Logger Log = LoggerFactory.getLogger(PayLogsController.class);

    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private UsersService usersService;

    @Resource
    private PayLogsService payLogsService;


    @PostMapping("")
    @ApiOperation("缴费记录页面")
    public String index() {

        return "pages/PayLogs";
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation("查找指定缴费记录")
    public R getInfo(String id) {

        Log.info("查找指定缴费记录，ID：{}", id);

        PayLogs payLogs = payLogsService.getOne(id);

        return R.successData(payLogs);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查找缴费记录")
    public R getPageInfos(Long pageIndex, Long pageSize,
                          String token, String teamName, String userName) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        if (user.getType() == 0) {

            Log.info("分页查看全部缴费记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = payLogsService.getPageInfo(pageIndex, pageSize, null, teamName, userName);

            return R.successData(page);
        } else if (user.getType() == 1) {

            Log.info("团队管理员查看缴费记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = payLogsService.getManPageInfo(pageIndex, pageSize, user.getId(), teamName, userName);

            return R.successData(page);
        } else {

            Log.info("分页用户相关缴费记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = payLogsService.getPageInfo(pageIndex, pageSize, user.getId(), teamName, null);

            return R.successData(page);
        }
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加缴费记录")
    public R addInfo( PayLogs payLogs) {

        payLogs.setId(IDUtils.makeIDByCurrent());
        payLogs.setCreateTime(DateUtils.getNowDate());

        Log.info("添加缴费记录，传入参数：{}", payLogs);

        payLogsService.add(payLogs);

        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改缴费记录")
    public R updInfo(PayLogs payLogs) {

        Log.info("修改缴费记录，传入参数：{}", payLogs);

        payLogsService.update(payLogs);

        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除缴费记录")
    public R delInfo(String id) {

        Log.info("删除缴费记录, ID:{}", id);

        PayLogs payLogs = payLogsService.getOne(id);

        payLogsService.delete(payLogs);

        return R.success();
    }
}