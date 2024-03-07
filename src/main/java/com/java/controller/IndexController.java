package com.java.controller;

import com.java.config.CacheHandle;
import com.java.dto.LoginDto;
import com.java.dto.PwdDto;
import com.java.dto.TokenDto;
import com.java.entity.Notices;
import com.java.entity.Users;
import com.java.service.NoticesService;
import com.java.service.UsersService;
import com.java.utils.IDUtils;
import com.java.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/")
@Api(tags = "首页")
@Slf4j
public class IndexController {

    @Resource
    private UsersService usersService;

    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private NoticesService noticesService;

    @PostMapping("/sys/notices")
    @ResponseBody
    @ApiOperation("获取系统通知")
    public R getNoticeList(@RequestBody TokenDto tokenDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(tokenDto.getToken()));
        if (user.getType() == 0) {
            List<Notices> list = noticesService.getSysNotices();
            return R.successData(list);
        } else if (user.getType() == 1) {
            List<Notices> list = noticesService.getManNotices(user.getId());
            return R.successData(list);
        } else {
            List<Notices> list = noticesService.getMemNotices(user.getId());
            return R.successData(list);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation("用户登录")
    public R login(@RequestBody LoginDto loginDto) {
        log.info("用户登录，用户名：{}， 用户密码：{}", loginDto.getUserName(), loginDto.getPassWord());
        Users user = usersService.getUserByUserName(loginDto.getUserName());
        if (user == null) {
            return R.error("输入的用户名不存在");
        } else {
            if (loginDto.getPassWord().equals(user.getPassWord().trim())) {
                String token = IDUtils.makeIDByUUID();
                cacheHandle.addUserCache(token, user.getId());
                return R.success("登录成功", token);
            } else {
                return R.error("输入的密码错误");
            }
        }
    }

    @PostMapping("/exit")
    @ResponseBody
    @ApiOperation("用户退出")
    public R exit(@RequestBody TokenDto tokenDto) {
        log.info("用户退出系统并移除登录信息");
        cacheHandle.removeUserCache(tokenDto.getToken());
        return R.success();
    }

    @PostMapping("/getInfo")
    @ResponseBody
    @ApiOperation("获取用户信息")
    public R info(@RequestBody TokenDto tokenDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(tokenDto.getToken()));
        return R.successData(user);
    }

    @PostMapping("/changeInfo")
    @ResponseBody
    @ApiOperation("修改用户信息")
    public R info(@RequestBody Users user) {
        log.info("修改用户信息，{}", user);
        usersService.update(user);
        return R.success();
    }

    @PostMapping("/checkPwd")
    @ResponseBody
    @ApiOperation("验证原始密码")
    public R checkPwd(@RequestBody PwdDto pwdDto) {
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(pwdDto.getToken()));
        if (pwdDto.getPassword().equals(user.getPassWord())) {
            return R.success();
        } else {
            return R.warn("原始密码和输入密码不一致");
        }
    }

    @PostMapping("/pwd")
    @ResponseBody
    @ApiOperation("修改用户密码")
    public R pwd(@RequestBody PwdDto pwdDto) {
        log.info("修改用户密码，{}", pwdDto.getPassword());
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(pwdDto.getToken()));
        user.setPassWord(pwdDto.getPassword());
        usersService.update(user);
        return R.success();
    }
}