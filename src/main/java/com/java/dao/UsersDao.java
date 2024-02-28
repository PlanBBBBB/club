package com.java.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.entity.Users;
import org.springframework.stereotype.Repository;

/**
 * 数据层处理接口
 * 系统用户
 */
@Repository("usersDao")
public interface UsersDao extends BaseMapper<Users> {
	

}