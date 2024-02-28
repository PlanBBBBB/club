package com.java.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.entity.TeamTypes;
import org.springframework.stereotype.Repository;

/**
 * 数据层处理接口
 * 社团类型
 */
@Repository("teamTypesDao")
public interface TeamTypesDao extends BaseMapper<TeamTypes> {
	

}