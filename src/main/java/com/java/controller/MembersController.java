package com.java.controller;

import com.java.config.CacheHandle;
import com.java.entity.Members;
import com.java.entity.Users;
import com.java.service.MembersService;
import com.java.service.UsersService;
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
 * 成员信息
 */
@Controller
@RequestMapping("/members")
@Api(tags = "成员信息")
public class
MembersController extends BaseController {

    protected static final Logger Log = LoggerFactory.getLogger(MembersController.class);

    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private UsersService usersService;

    @Resource
    private MembersService membersService;

    @PostMapping("")
    @ApiOperation("跳转至成员信息页面")
    public String index() {

        return "pages/Members";
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation("查找指定成员信息")
    public R getInfo(String id) {

        Log.info("查找指定成员信息，ID：{}", id);

        Members members = membersService.getOne(id);

        return R.successData(members);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查找成员信息")
    public R getPageInfos(Long pageIndex, Long pageSize,
                          String token, String teamName, String userName) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        if (user.getType() == 0) {

            Log.info("分页查找成员信息，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = membersService.getPageAll(pageIndex, pageSize, teamName, userName);

            return R.successData(page);
        } else {

            Log.info("分页查找成员信息，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = membersService.getPageByManId(pageIndex, pageSize, user.getId(), teamName, userName);

            return R.successData(page);
        }
    }


    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加成员信息")
    public R addInfo(Members members) {

        members.setId(IDUtils.makeIDByCurrent());

        Log.info("添加成员信息，传入参数：{}", members);

        membersService.add(members);

        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改成员信息")
    public R updInfo(Members members) {

        Log.info("修改成员信息，传入参数：{}", members);

        membersService.update(members);

        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除成员信息")
    public R delInfo(String id) {

        Members members = membersService.getOne(id);

        if(membersService.isManager(members.getTeamId(), members.getUserId())){

            return R.warn("社团管理员无法移除");
        }else{

            Log.info("删除成员信息, ID:{}", id);

            membersService.delete(members);

            return R.success();
        }
    }
}