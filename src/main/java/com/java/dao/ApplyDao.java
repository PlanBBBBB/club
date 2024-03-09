package com.java.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.entity.Apply;
import org.springframework.stereotype.Repository;

/**
 * 数据层处理接口
 * 申请加入社团
 */
@Repository("applyDao")
public interface ApplyDao extends BaseMapper<Apply> {
}
