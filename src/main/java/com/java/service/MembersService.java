package com.java.service;


import com.java.dto.MemberAddDto;
import com.java.entity.Members;
import com.java.vo.PageData;

/**
 * 业务层处理
 * 成员信息
 */
public interface MembersService {

    /**
     * 指定用户是否是管理员
     *
     * @param userId 指定用户ID
     * @param teamId 团队ID
     * @return
     */
    public Boolean isManager(String teamId, String userId);

    /**
     * 分页查询成员信息信息
     *
     * @param pageIndex 当前页码
     * @param pageSize  每页数据量
     * @param teamName  社团名称
     * @param userName  成员姓名
     * @return
     */
    public PageData getPageAll(Long pageIndex, Long pageSize, String teamName, String userName);

    /**
     * 分页查询成员信息信息
     *
     * @param pageIndex 当前页码
     * @param pageSize  每页数据量
     * @param manId     管理员ID
     * @param teamName  社团名称
     * @param userName  成员姓名
     * @return
     */
    public PageData getPageByManId(Long pageIndex, Long pageSize, String manId, String teamName, String userName);

    void add(MemberAddDto memberAddDto);

    void update(Members members);

    Members getOne(String id);

    void delete(Members members);

}