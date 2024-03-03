package com.java.service;


import com.java.entity.Users;
import com.java.vo.PageData;

/**
 * 业务层处理
 * 系统用户
 */
public interface UsersService extends BaseService<Users, String> {

    /**
     * 检查指定的用户是否可以删除
     * 当用户不是社团成员可以进行删除
     *
     * @param userId 用户ID
     * @return
     */
    public Boolean isRemove(String userId);

    /**
     * 依据用户名获取用户信息
     *
     * @param userName 用户账号
     * @return
     */
    public Users getUserByUserName(String userName);

    /**
     * 分页查询系统用户信息
     */
    public PageData getPageInfo(Long pageIndex, Long pageSize, String name, String userName, String phone);
}