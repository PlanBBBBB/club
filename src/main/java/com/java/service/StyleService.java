package com.java.service;



import com.java.entity.Style;
import com.java.vo.PageData;


/**
 * 业务层处理
 * 报名记录
 */
public interface StyleService extends BaseService<Style, String> {

    /**
     * 分页查询风采信息信息
     * @param pageIndex 当前页码
     * @param pageSize 每页数据量
     * @param activeName 风采名称
     * @param teamName 团队名称
     * @return
     */
    public PageData getPageAll(Long pageIndex, Long pageSize, String activeName, String teamName);


    /**
     * 分页查询指定成员相关风采信息信息
     * @param pageIndex 当前页码
     * @param pageSize 每页数据量
     * @param userId 指定成员ID
     * @param activeName 风采名称
     * @param teamName 团队名称
     * @return
     */
    public PageData getPageByUserId(Long pageIndex, Long pageSize, String userId, String activeName, String teamName);
}