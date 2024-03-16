package com.java.controller;

import com.java.config.CacheHandle;
import com.java.dto.ApplylogsOrUserPageDto;
import com.java.dto.IdDto;
import com.java.dto.MemberAddDto;
import com.java.entity.Members;
import com.java.entity.Users;
import com.java.service.MembersService;
import com.java.service.UsersService;
import com.java.vo.PageData;
import com.java.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 成员信息
 */
@Controller
@RequestMapping("/members")
@Api(tags = "成员信息")
@Slf4j
public class MembersController {

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
    public R getInfo(@RequestBody IdDto idDto) {
        log.info("查找指定成员信息，ID：{}", idDto.getId());
        Members members = membersService.getOne(idDto.getId());
        return R.successData(members);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查找成员信息")
    public R getPageInfos(@RequestBody ApplylogsOrUserPageDto applylogsOrUserPageDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(applylogsOrUserPageDto.getToken()));
        if (user.getType() == 0) {
            log.info("分页查找成员信息，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", applylogsOrUserPageDto.getPageIndex(),
                    applylogsOrUserPageDto.getPageIndex(), applylogsOrUserPageDto.getTeamName(), applylogsOrUserPageDto.getUserName());
            PageData page = membersService.getPageAll(applylogsOrUserPageDto.getPageIndex(),
                    applylogsOrUserPageDto.getPageIndex(), applylogsOrUserPageDto.getTeamName(), applylogsOrUserPageDto.getUserName());
            return R.successData(page);
        } else {
            log.info("分页查找成员信息，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", applylogsOrUserPageDto.getPageIndex(),
                    applylogsOrUserPageDto.getPageIndex(), applylogsOrUserPageDto.getTeamName(), applylogsOrUserPageDto.getUserName());
            PageData page = membersService.getPageByManId(applylogsOrUserPageDto.getPageIndex(),
                    applylogsOrUserPageDto.getPageIndex(), user.getId(), applylogsOrUserPageDto.getTeamName(), applylogsOrUserPageDto.getUserName());
            return R.successData(page);
        }
    }


    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加成员信息")
    public R addInfo(@RequestBody MemberAddDto memberAddDto) {
        membersService.add(memberAddDto);
        return R.success();
    }


    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改成员信息")
    public R updInfo(@RequestBody Members members) {
        membersService.update(members);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除成员信息")
    public R delInfo(@RequestBody IdDto idDto) {
        Members members = membersService.getOne(idDto.getId());
        if (membersService.isManager(members.getTeamId(), members.getUserId())) {
            return R.warn("社团管理员无法移除");
        } else {
            log.info("删除成员信息, ID:{}", idDto.getId());
            membersService.delete(members);
            return R.success();
        }
    }
}