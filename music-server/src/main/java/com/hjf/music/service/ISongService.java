package com.hjf.music.service;

import com.github.pagehelper.PageInfo;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.Song;

import java.util.List;

/**
 * @author hjf
 * @create 2020-11-05 19:31
 */
public interface ISongService {
    /**
     * 添加Song
     * @param song
     * @return
     */
    int insert(Song song);

    /**
     * 根据id删除Song
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 根据id更新Song
     * @param song
     * @return
     */
    int updateById(Song song);

    /**
     * 根据id查询Song
     * @param id
     * @return
     */
    Song selectById(Integer id);

    /**
     * 根据singerId有条件的查询list<Song>
     * @param queryInfo
     * @param singerId
     * @return
     */
    PageInfo<Song> selectList(QueryInfo queryInfo, Integer singerId);

    /**
     * 有条件的查询list<Song>
     * @param queryInfo
     * @return
     */
    PageInfo<Song> selectList(QueryInfo queryInfo);

    /**
     * 批量删除Song
     * @param ids
     * @return
     */
    int deleteSome(List<Integer> ids);

    /**
     * 批量查找Song
     * @param ids
     * @return
     */
    List<Song> getListSong(List<Integer> ids);
}
