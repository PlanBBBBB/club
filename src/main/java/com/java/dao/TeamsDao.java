package com.java.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.entity.Teams;
import org.springframework.stereotype.Repository;

/**
 * 数据层处理接口
 * 社团信息
 */
@Repository("teamsDao")
public interface TeamsDao extends BaseMapper<Teams> {
	

}