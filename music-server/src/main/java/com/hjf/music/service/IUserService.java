package com.hjf.music.service;

import com.github.pagehelper.PageInfo;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.User;

import java.util.List;

/**
 * @author hjf
 * @create 2020-11-20 11:26
 */
public interface IUserService {
    /**
     * 添加User
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 根据id删除User
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 根据id更新User
     * @param user
     * @return
     */
    int updateById(User user);

    /**
     * 根据id查询User
     * @param id
     * @return
     */
    User selectById(Integer id);

    /**
     * 有条件的查询list<User>
     * @param queryInfo
     * @return
     */
    PageInfo<User> selectList(QueryInfo queryInfo);

    /**
     * 批量删除User
     * @param ids
     * @return
     */
    int deleteSome(List<Integer> ids);

    /**
     * 批量查找User
     * @param ids
     * @return
     */
    List<User> getListUser(List<Integer> ids);
}
