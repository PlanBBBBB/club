package com.java.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.entity.ActiveLogs;
import org.springframework.stereotype.Repository;

/**
 * 数据层处理接口
 * 报名记录
 */
@Repository("activeLogsDao")
public interface ActiveLogsDao extends BaseMapper<ActiveLogs> {
}