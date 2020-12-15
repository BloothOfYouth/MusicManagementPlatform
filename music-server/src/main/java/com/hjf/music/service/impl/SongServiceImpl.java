package com.hjf.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjf.music.dao.SongDao;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.Song;
import com.hjf.music.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hjf
 * @create 2020-11-05 19:33
 */
@Service
public class SongServiceImpl implements ISongService {

    @Autowired
    private SongDao songDao;

    /**
     * 添加Song
     * @param song
     * @return
     */
    @Override
    public int insert(Song song) {
        int insert = songDao.insert(song);
        return insert;
    }

    /**
     * 根据id删除Song
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        int delete = songDao.deleteById(id);
        return delete;
    }

    /**
     * 根据id更新Song
     * @param song
     * @return
     */
    @Override
    public int updateById(Song song) {
        int update = songDao.updateById(song);
        return update;
    }

    /**
     * 根据id查询Song
     * @param id
     * @return
     */
    @Override
    public Song selectById(Integer id) {
        Song song = songDao.selectById(id);
        return song;
    }

    /**
     * 根据singerId有条件的查询list<Song>
     * @param queryInfo
     * @param singerId
     * @return
     */
    @Override
    public PageInfo<Song> selectList(QueryInfo queryInfo, Integer singerId) {
        PageHelper.startPage(queryInfo.getPageNum(), queryInfo.getPageSize());
        QueryWrapper<Song> wrapper = new QueryWrapper<>();
        wrapper.eq("singer_id",singerId);
        wrapper.like("name",queryInfo.getQuery());
        List<Song> songs = songDao.selectList(wrapper);
        PageInfo<Song> pageInfo = new PageInfo<>(songs);
        return pageInfo;
    }

    /**
     * 有条件的查询list<Song>
     * @param queryInfo
     * @return
     */
    @Override
    public PageInfo<Song> selectList(QueryInfo queryInfo) {
        PageHelper.startPage(queryInfo.getPageNum(), queryInfo.getPageSize());
        List<Song> songs = songDao.selectAllSongs(queryInfo.getQuery());
        PageInfo<Song> pageInfo = new PageInfo<>(songs);
        return pageInfo;
    }

    /**
     * 批量删除Song
     * @param ids
     * @return
     */
    @Override
    public int deleteSome(List<Integer> ids) {
        int i = songDao.deleteBatchIds(ids);
        return i;
    }

    /**
     * 批量查找Song
     * @param ids
     * @return
     */
    @Override
    public List<Song> getListSong(List<Integer> ids) {
        List<Song> songs = songDao.selectBatchIds(ids);
        return songs;
    }
}
