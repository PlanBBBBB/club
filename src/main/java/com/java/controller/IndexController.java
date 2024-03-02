package com.java.controller;

import com.java.config.CacheHandle;
import com.java.entity.Notices;
import com.java.entity.Users;
import com.java.service.NoticesService;
import com.java.service.UsersService;
import com.java.utils.IDUtils;
import com.java.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
@Api(tags = "首页")
public class IndexController extends BaseController {

    private static final Logger Log = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private UsersService usersService;

    @Resource
    private CacheHandle cacheHandle;

    @Resource
    private NoticesService noticesService;

    @PostMapping("/sys/notices")
    @ResponseBody
    @ApiOperation("获取系统通知")
    public R getNoticeList(String token){

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        if(user.getType() == 0){

            List<Notices> list = noticesService.getSysNotices();

            return R.successData(list);
        }else if(user.getType() == 1){

            List<Notices> list = noticesService.getManNotices(user.getId());

            return R.successData(list);
        }else{

            List<Notices> list = noticesService.getMemNotices(user.getId());

            return R.successData(list);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation("用户登录")
    public R login(String userName, String passWord, HttpSession session){

        Log.info("用户登录，用户名：{}， 用户密码：{}", userName, passWord);

        Users user = usersService.getUserByUserName(userName);

        if(user == null) {

            return R.error("输入的用户名不存在");
        }else {

            if(passWord.equals(user.getPassWord().trim())) {


                String token = IDUtils.makeIDByUUID();
                cacheHandle.addUserCache(token, user.getId());

                return R.success("登录成功", token);
            }else {

                return R.error("输入的密码错误");
            }
        }
    }

    @PostMapping("/exit")
    @ResponseBody
    @ApiOperation("用户退出")
    public R exit(String token) {

        Log.info("用户退出系统并移除登录信息");

        cacheHandle.removeUserCache(token);

        return R.success();
    }

    @PostMapping("/getInfo")
    @ResponseBody
    @ApiOperation("获取用户信息")
    public R info(String token){

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        return R.successData(user);
    }

    @PostMapping("/changeInfo")
    @ResponseBody
    @ApiOperation("修改用户信息")
    public R info(Users user){

        Log.info("修改用户信息，{}", user);

        usersService.update(user);

        return R.success();
    }

    @PostMapping("/checkPwd")
    @ResponseBody
    @ApiOperation("验证原始密码")
    public R checkPwd(String oldPwd, String token) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        if(oldPwd.equals(user.getPassWord())) {

            return R.success();
        }else {

            return R.warn("原始密码和输入密码不一致");
        }
    }

    @PostMapping("/pwd")
    @ResponseBody
    @ApiOperation("修改用户密码")
    public R pwd(String token, String password) {

        Log.info("修改用户密码，{}", password);

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        user.setPassWord(password);
        usersService.update(user);

        return R.success();
    }
}