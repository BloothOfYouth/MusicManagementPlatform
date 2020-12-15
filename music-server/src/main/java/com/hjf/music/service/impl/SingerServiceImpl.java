package com.hjf.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjf.music.dao.SingerDao;
import com.hjf.music.dao.SongDao;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.Singer;
import com.hjf.music.entity.Song;
import com.hjf.music.service.ISingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hjf
 * @create 2020-10-29 22:17
 */
@Service
public class SingerServiceImpl implements ISingerService {

    @Autowired
    private SingerDao singerDao;

    @Autowired
    private SongDao songDao;

    /**
     * 添加Singer
     * @param singer
     * @return
     */
    @Override
    public int insert(Singer singer) {
        int insert = singerDao.insert(singer);
        return insert;
    }

    /**
     * 根据id删除Singer
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        int delete = singerDao.deleteById(id);
        return delete;
    }

    /**
     * 根据id更新Singer
     * @param singer
     * @return
     */
    @Override
    public int updateById(Singer singer) {
        int update = singerDao.updateById(singer);
        return update;
    }

    /**
     * 根据id查询Singer
     * @param id
     * @return
     */
    @Override
    public Singer selectById(Integer id) {
        Singer singer = singerDao.selectById(id);
        return singer;
    }

    /**
     * 有条件的查询list<Singer>
     * @param queryInfo
     * @return
     */
    @Override
    public PageInfo<Singer> selectList(QueryInfo queryInfo) {
        PageHelper.startPage(queryInfo.getPageNum(), queryInfo.getPageSize());
        QueryWrapper<Singer> wrapper = new QueryWrapper<>();
        wrapper.like("name",queryInfo.getQuery());
        List<Singer> oldSingers = singerDao.selectList(wrapper);
        PageInfo<Singer> pageInfo = new PageInfo<>(oldSingers);

        List<Singer> singers = pageInfo.getList();
        for (int i = 0; i < singers.size(); i++) {
            Singer singer = singers.get(i);
            QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("singer_id",singer.getId());
            Integer count = songDao.selectCount(queryWrapper);
            singer.setSongCount(count);
            singers.set(i,singer);
        }
        pageInfo.setList(singers);

        return pageInfo;
    }

    /**
     * 批量删除Singer
     * @param ids
     * @return
     */
    @Override
    public int deleteSome(List<Integer> ids) {
        int i = singerDao.deleteBatchIds(ids);
        return i;
    }

    /**
     * 批量查找Singer
     * @param ids
     * @return
     */
    @Override
    public List<Singer> getListSinger(List<Integer> ids) {
        List<Singer> singers = singerDao.selectBatchIds(ids);
        return singers;
    }
}
