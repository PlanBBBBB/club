package com.java.controller;

import com.java.entity.TeamTypes;
import com.java.service.TeamTypesService;
import com.java.utils.DateUtils;
import com.java.utils.IDUtils;
import com.java.vo.PageData;
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

import java.util.List;

/**
 * 系统请求响应控制器
 * 社团类型
 */
@Controller
@RequestMapping("/teamTypes")
@Api(tags = "社团类型管理")
public class TeamTypesController extends BaseController {

    protected static final Logger Log = LoggerFactory.getLogger(TeamTypesController.class);

    @Autowired
    private TeamTypesService teamTypesService;

    @PostMapping("")
    @ApiOperation("社团类型页面")
    public String index() {

        return "pages/TeamTypes";
    }

    @PostMapping("/all")
    @ResponseBody
    @ApiOperation("查看全部的社团类型信息")
    public R getAll(){

        Log.info("查看全部的社团类型信息");

        List<TeamTypes> list = teamTypesService.getAll();

        return R.successData(list);
    }

    @PostMapping("/info")
    @ResponseBody
    @ApiOperation("查找指定社团类型")
    public R getInfo(String id) {

        Log.info("查找指定社团类型，ID：{}", id);

        TeamTypes teamTypes = teamTypesService.getOne(id);

        return R.successData(teamTypes);
    }

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查找社团类型")
    public R getPageInfos(Long pageIndex, Long pageSize,
                          TeamTypes teamTypes) {

        Log.info("分页查找社团类型，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，附加参数：{}", pageIndex,
                pageSize, teamTypes);

        PageData page = teamTypesService.getPageInfo(pageIndex, pageSize, teamTypes);

        return R.successData(page);
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加社团类型")
    public R addInfo(TeamTypes teamTypes) {

        teamTypes.setId(IDUtils.makeIDByCurrent());
        teamTypes.setCreateTime(DateUtils.getNowDate());

        Log.info("添加社团类型，传入参数：{}", teamTypes);

        teamTypesService.add(teamTypes);

        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    @ApiOperation("修改社团类型")
    public R updInfo(TeamTypes teamTypes) {

        Log.info("修改社团类型，传入参数：{}", teamTypes);

        teamTypesService.update(teamTypes);

        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除社团类型")
    public R delInfo(String id) {

        if(teamTypesService.isRemove(id)){

            Log.info("删除社团类型, ID:{}", id);

            TeamTypes teamTypes = teamTypesService.getOne(id);

            teamTypesService.delete(teamTypes);

            return R.success();
        }else{

            return R.warn("存在关联社团，无法移除");
        }
    }
}