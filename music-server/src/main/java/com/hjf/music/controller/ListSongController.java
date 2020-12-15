package com.hjf.music.controller;

import com.github.pagehelper.PageInfo;
import com.hjf.music.dao.ListSongDao;
import com.hjf.music.entity.ListSong;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.Song;
import com.hjf.music.model.ResultMap;
import com.hjf.music.service.IListSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author hjf
 * @create 2020-11-18 14:52
 */
@RestController
@RequestMapping("/listSong")
public class ListSongController {

    @Autowired
    private IListSongService listSongService;

    @Autowired
    private ListSongDao listSongDao;

    @Autowired
    private ResultMap resultMap;

    /**
     * 根据歌单id查询歌曲
     * 模糊及分页查询
     * @param queryInfo
     * @return
     */
    @PostMapping("/get/{songListId}")
    public PageInfo<Song> getAllSongs(@RequestBody QueryInfo queryInfo, @PathVariable Integer songListId){
        PageInfo<Song> pageInfo = listSongService.selectList(queryInfo,songListId);
        return pageInfo;
    }

    /**
     * 添加一个或多个ListSong
     * @param params
     * @return
     */
    @PostMapping("/addSome")
    public ResultMap addSomeListSong(@RequestBody Map params){
        List<ListSong> listSongs = (List<ListSong>) params.get("listSongs");
        int insert = listSongService.insertSome(listSongs);
        if (insert > 0) {
            return resultMap.success().code(200).message("歌曲添加成功");
        }
        return resultMap.fail().code(401).message("歌曲添加失败");

    }

    /**
     * 通过songListId查询歌曲的SongId集合
     * @return
     */
    @GetMapping("/getSongIds/{songListId}")
    public ResultMap getSongIdsBySongListId(@PathVariable Integer songListId){
        List<Integer> songIds = listSongDao.selectSongIdsBySongListId(songListId);
        return resultMap.success().code(200).message(songIds);
    }

    /**
     * 删除一个ListSong
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResultMap deleteSong(@PathVariable Integer id){
        int delete = listSongService.deleteById(id);
        if (delete > 0) {
            return resultMap.success().code(200).message("歌曲删除成功");
        }
        return resultMap.fail().code(401).message("歌曲删除失败");
    }

    /**
     * 批量删除歌曲
     * @param params
     * @return
     */
    @PostMapping("/delSome")
    public ResultMap delSome(@RequestBody Map params) {
        List<Integer> list = (List<Integer>) params.get("ids");
        int i = listSongService.deleteSome(list);
        if (i > 0) {
            return resultMap.success().code(200).message("批量删除成功");
        }
        return resultMap.fail().code(401).message("批量删除失败");
    }

}
