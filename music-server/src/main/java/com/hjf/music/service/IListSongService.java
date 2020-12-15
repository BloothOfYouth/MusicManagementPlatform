package com.hjf.music.service;

import com.github.pagehelper.PageInfo;
import com.hjf.music.entity.ListSong;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.Song;

import java.util.List;

/**
 * @author hjf
 * @create 2020-11-18 11:30
 */
public interface IListSongService {
    /**
     * 添加ListSong
     * @param listSong
     * @return
     */
    int insert(ListSong listSong);

    /**
     * 根据id删除ListSong对应的(歌曲与歌单的关系)
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 有条件的查询歌单的list<Song>
     * @param queryInfo
     * @param songListId
     * @return
     */
    PageInfo<Song> selectList(QueryInfo queryInfo, Integer songListId);

    /**
     * 批量删除ListSong
     * @param ids
     * @return
     */
    int deleteSome(List<Integer> ids);

    /**
     * 批量添加ListSong
     * @param listSongs
     * @return
     */
    int insertSome(List<ListSong> listSongs);
}
