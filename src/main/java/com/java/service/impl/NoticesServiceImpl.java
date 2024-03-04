package com.java.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.NoticesDao;
import com.java.dao.TeamsDao;
import com.java.entity.Notices;
import com.java.entity.Teams;
import com.java.service.NoticesService;
import com.java.utils.StringUtils;
import com.java.vo.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("noticesService")
public class NoticesServiceImpl implements NoticesService {

    @Resource
    private NoticesDao noticesDao;

	@Resource
	private TeamsDao teamsDao;

    @Override
    public void add(Notices notices) {
        noticesDao.insert(notices);
    }

    @Override
    public void update(Notices notices) {
        noticesDao.updateById(notices);
    }

    @Override
    public void delete(Notices notices) {
        noticesDao.deleteById(notices);
    }

    @Override
    public Notices getOne(String id) {
        return noticesDao.selectById(id);
    }

    @Override
    public List<Notices> getSysNotices(){
        return noticesDao.qrySysNotices();
    }

    @Override
    public List<Notices> getManNotices(String manId){
        return noticesDao.qryManNotices(manId);
    }

    @Override
    public List<Notices> getMemNotices(String memId){
        return noticesDao.qryMemNotices(memId);
    }

    @Override
    public PageData getPageAll(Long pageIndex, Long pageSize, String title, String teamName){
        Page<Map<String, Object>>  page = noticesDao.qryPageAll(new Page<>(pageIndex, pageSize), title, teamName);
        return parsePage(page);
    }

    @Override
    public PageData getPageById(Long pageIndex, Long pageSize, String userId, String title, String teamName){
        Page<Map<String, Object>>  page = noticesDao.qryPageById(new Page<>(pageIndex, pageSize), userId, title, teamName);
        return parsePage(page);
    }

    /**
     * 查询列表结果转换
     */
    public List<Map<String, Object>> parseList(List<Notices> notices){
        List<Map<String, Object>> resl = new ArrayList<>();
        for (Notices notice : notices) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("id", notice.getId());
            temp.put("title", notice.getTitle());
            temp.put("detail", notice.getDetail());
            temp.put("createTime", notice.getCreateTime());
            if(StringUtils.isNotNullOrEmpty(notice.getTeamId())){
                Teams teams = teamsDao.selectById(notice.getTeamId());
                temp.put("teamId", notice.getTeamId());
                temp.put("teamName", teams.getName());
                temp.put("isTeam", true);
            }else{
                temp.put("isTeam", false);
            }
            resl.add(temp);
        }
        return resl;
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Map<String, Object>> p) {
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), p.getRecords());
    }
}