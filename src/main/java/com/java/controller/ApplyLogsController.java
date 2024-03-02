package com.java.controller;

import com.java.config.CacheHandle;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 系统请求响应控制器
 * 申请记录
 */
@Controller
@RequestMapping("/applyLogs")
@Api(tags = "申请记录")
public class ApplyLogsController extends BaseController {

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
    public R getInfo(String id) {

        Log.info("查找指定申请记录，ID：{}", id);

        ApplyLogs applyLogs = applyLogsService.getOne(id);

        return R.successData(applyLogs);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页获取申请记录信息")
    public R getPageInfos(Long pageIndex, Long pageSize,
                          String token, String teamName, String userName) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        if (user.getType() == 0) {

            Log.info("分页查看全部申请记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = applyLogsService.getPageInfo(pageIndex, pageSize, null, teamName, userName);

            return R.successData(page);

        } else if (user.getType() == 1) {

            Log.info("团队管理员查看申请记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = applyLogsService.getManPageInfo(pageIndex, pageSize, user.getId(), teamName, userName);

            return R.successData(page);

        } else {

            Log.info("分页用户相关申请记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = applyLogsService.getPageInfo(pageIndex, pageSize, user.getId(), teamName, null);

            return R.successData(page);
        }
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加申请记录")
    public R addInfo(String token, ApplyLogs applyLogs) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        if(applyLogsService.isApply(user.getId(), applyLogs.getTeamId())){

            applyLogs.setId(IDUtils.makeIDByCurrent());
            applyLogs.setUserId(user.getId());
            applyLogs.setCreateTime(DateUtils.getNowDate());

            Log.info("添加申请记录，传入参数：{}", applyLogs);

            applyLogsService.add(applyLogs);

            return R.success();
        }else{

            return R.warn("申请审核中，请耐心等待");
        }
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改申请记录")
    public R updInfo(ApplyLogs applyLogs) {

        Log.info("修改申请记录，传入参数：{}", applyLogs);

        applyLogsService.update(applyLogs);

        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除申请记录")
    public R delInfo(String id) {

        Log.info("删除申请记录, ID:{}", id);

        ApplyLogs applyLogs = applyLogsService.getOne(id);

        applyLogsService.delete(applyLogs);

        return R.success();
    }
}