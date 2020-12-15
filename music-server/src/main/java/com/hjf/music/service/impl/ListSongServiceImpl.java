package com.hjf.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjf.music.dao.ListSongDao;
import com.hjf.music.dao.SongDao;
import com.hjf.music.entity.ListSong;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.Song;
import com.hjf.music.service.IListSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author hjf
 * @create 2020-11-18 11:33
 */
@Service
public class ListSongServiceImpl implements IListSongService {

    @Autowired
    private ListSongDao listSongDao;

    @Autowired
    private SongDao songDao;

    /**
     * 添加ListSong
     * @param listSong
     * @return
     */
    @Override
    public int insert(ListSong listSong) {
        int insert = listSongDao.insert(listSong);
        return insert;
    }

    /**
     * 根据id删除ListSong对应的(歌曲与歌单的关系)
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        int delete = listSongDao.deleteById(id);
        return delete;
    }

    /**
     * 有条件的查询歌单的list<Song>
     * @param queryInfo
     * @param songListId
     * @return
     */
    @Override
    public PageInfo<Song> selectList(QueryInfo queryInfo, Integer songListId) {
        PageHelper.startPage(queryInfo.getPageNum(), queryInfo.getPageSize());
        QueryWrapper<ListSong> listSongQueryWrapper = new QueryWrapper<>();
        listSongQueryWrapper.eq("song_list_id",songListId);
        List<ListSong> listSongs = listSongDao.selectList(listSongQueryWrapper);
        PageInfo<ListSong> olderInfo = new PageInfo<>(listSongs);

        List<Integer> songIds = new ArrayList<>();
        List<ListSong> list = olderInfo.getList();
        list.forEach(item -> {
            songIds.add(item.getSongId());
        });

        PageInfo<Song> pageInfo = new PageInfo<>();
        if (songIds.size()!=0) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("songIds",songIds);
            map.put("name",queryInfo.getQuery());
            map.put("songListId",songListId);
            List<Song> songs = songDao.selectBySongIds(map);
            pageInfo.setList(songs);
        }
        pageInfo.setPageNum(olderInfo.getPageNum());
        pageInfo.setPageSize(olderInfo.getPageSize());
        pageInfo.setTotal(olderInfo.getTotal());

        return pageInfo;
    }

    /**
     * 批量删除ListSong
     * @param ids
     * @return
     */
    @Override
    public int deleteSome(List<Integer> ids) {
        int i = listSongDao.deleteBatchIds(ids);
        return i;
    }

    /**
     * 批量添加ListSong
     * @param listSongs
     * @return
     */
    @Override
    public int insertSome(List<ListSong> listSongs) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("listSongs",listSongs);
        int i = listSongDao.insertSome(map);
        return i;
    }
}
