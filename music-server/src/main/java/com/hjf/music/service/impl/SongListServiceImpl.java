package com.hjf.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjf.music.dao.ListSongDao;
import com.hjf.music.dao.SongListDao;
import com.hjf.music.entity.*;
import com.hjf.music.service.ISongListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hjf
 * @create 2020-11-16 12:56
 */
@Service
public class SongListServiceImpl implements ISongListService {

    @Autowired
    private SongListDao songListDao;

    @Autowired
    private ListSongDao listSongDao;

    /**
     * 添加SongList
     * @param songList
     * @return
     */
    @Override
    public int insert(SongList songList) {
        int insert = songListDao.insert(songList);
        return insert;
    }

    /**
     * 根据id删除SongList
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        int delete = songListDao.deleteById(id);
        return delete;
    }

    /**
     * 根据id更新SongList
     * @param songList
     * @return
     */
    @Override
    public int updateById(SongList songList) {
        int update = songListDao.updateById(songList);
        return update;
    }

    /**
     * 根据id查询SongList
     * @param id
     * @return
     */
    @Override
    public SongList selectById(Integer id) {
        SongList songList = songListDao.selectById(id);
        return songList;
    }

    /**
     * 有条件的查询list<SongList>
     * @param queryInfo
     * @return
     */
    @Override
    public PageInfo<SongList> selectList(QueryInfo queryInfo) {
        PageHelper.startPage(queryInfo.getPageNum(), queryInfo.getPageSize());
        QueryWrapper<SongList> wrapper = new QueryWrapper<>();
        wrapper.like("title",queryInfo.getQuery());
        List<SongList> oldSongLists = songListDao.selectList(wrapper);
        PageInfo<SongList> pageInfo = new PageInfo<>(oldSongLists);

        List<SongList> songLists = pageInfo.getList();
        for (int i = 0; i < songLists.size(); i++) {
            SongList songList = songLists.get(i);
            QueryWrapper<ListSong> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("song_list_id",songList.getId());
            Integer count = listSongDao.selectCount(queryWrapper);
            songList.setSongCount(count);
            songLists.set(i,songList);
        }
        pageInfo.setList(songLists);
        return pageInfo;
    }

    /**
     * 批量删除SongList
     * @param ids
     * @return
     */
    @Override
    public int deleteSome(List<Integer> ids) {
        int i = songListDao.deleteBatchIds(ids);
        return i;
    }

    /**
     * 批量查找SongList
     * @param ids
     * @return
     */
    @Override
    public List<SongList> getListSongList(List<Integer> ids) {
        List<SongList> songLists = songListDao.selectBatchIds(ids);
        return songLists;
    }
}
