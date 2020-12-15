package com.hjf.music.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjf.music.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author hjf
 * @create 2020-11-20 11:25
 */
@Repository
public interface UserDao extends BaseMapper<User> {
}
