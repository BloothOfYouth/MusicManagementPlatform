package com.hjf.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjf.music.dao.UserDao;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.User;
import com.hjf.music.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hjf
 * @create 2020-11-20 11:28
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    /**
     * 添加User
     * @param user
     * @return
     */
    @Override
    public int insert(User user) {
        int insert = userDao.insert(user);
        return insert;
    }

    /**
     * 根据id删除User
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        int delete = userDao.deleteById(id);
        return delete;
    }

    /**
     * 根据id更新User
     * @param user
     * @return
     */
    @Override
    public int updateById(User user) {
        int update = userDao.updateById(user);
        return update;
    }

    /**
     * 根据id查询User
     * @param id
     * @return
     */
    @Override
    public User selectById(Integer id) {
        User user = userDao.selectById(id);
        return user;
    }

    /**
     * 有条件的查询list<User>
     * @param queryInfo
     * @return
     */
    @Override
    public PageInfo<User> selectList(QueryInfo queryInfo) {
        PageHelper.startPage(queryInfo.getPageNum(), queryInfo.getPageSize());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name",queryInfo.getQuery());
        List<User> users = userDao.selectList(wrapper);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    /**
     * 批量删除User
     * @param ids
     * @return
     */
    @Override
    public int deleteSome(List<Integer> ids) {
        int i = userDao.deleteBatchIds(ids);
        return i;
    }

    /**
     * 批量查找User
     * @param ids
     * @return
     */
    @Override
    public List<User> getListUser(List<Integer> ids) {
        List<User> users = userDao.selectBatchIds(ids);
        return users;
    }
}
