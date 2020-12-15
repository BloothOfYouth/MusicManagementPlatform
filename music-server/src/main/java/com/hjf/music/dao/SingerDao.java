package com.hjf.music.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjf.music.entity.Singer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hjf
 * @create 2020-10-29 22:10
 */
@Repository
public interface SingerDao extends BaseMapper<Singer> {
    /**
     * 模糊查询List<Singer>
     * 对Singer封装songCount(有多少个歌曲)
     * @param name
     * @return
     */
    List<Singer> selectSingers(String name);
}
