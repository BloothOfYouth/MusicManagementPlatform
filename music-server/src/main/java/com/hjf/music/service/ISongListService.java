package com.hjf.music.service;

import com.github.pagehelper.PageInfo;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.SongList;

import java.util.List;

/**
 * @author hjf
 * @create 2020-11-16 12:53
 */
public interface ISongListService {
    /**
     * 添加SongList
     * @param songList
     * @return
     */
    int insert(SongList songList);

    /**
     * 根据id删除SongList
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 根据id更新SongList
     * @param songList
     * @return
     */
    int updateById(SongList songList);

    /**
     * 根据id查询SongList
     * @param id
     * @return
     */
    SongList selectById(Integer id);

    /**
     * 有条件的查询list<SongList>
     * @param queryInfo
     * @return
     */
    PageInfo<SongList> selectList(QueryInfo queryInfo);

    /**
     * 批量删除SongList
     * @param ids
     * @return
     */
    int deleteSome(List<Integer> ids);

    /**
     * 批量查找SongList
     * @param ids
     * @return
     */
    List<SongList> getListSongList(List<Integer> ids);
}
