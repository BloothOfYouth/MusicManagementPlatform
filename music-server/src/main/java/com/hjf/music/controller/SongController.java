package com.hjf.music.controller;

import com.github.pagehelper.PageInfo;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.Song;
import com.hjf.music.model.ResultMap;
import com.hjf.music.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hjf
 * @create 2020-11-05 19:38
 */
@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private ISongService songService;

    @Autowired
    private ResultMap resultMap;

    /**
     * 默认歌曲图片
     */
    private static final String defaultSongPic = "/img/songPic/tubiao.jpg";

    /**
     * 根据歌手id查询歌曲
     * 模糊及分页查询
     * @param queryInfo
     * @return
     */
    @PostMapping("/get/{singerId}")
    public PageInfo<Song> getAllSongsBySingerId(@RequestBody QueryInfo queryInfo,@PathVariable Integer singerId){
        PageInfo<Song> pageInfo = songService.selectList(queryInfo,singerId);
        return pageInfo;
    }

    /**
     * 模糊及分页查询
     * @param queryInfo
     * @return
     */
    @PostMapping("/getAll")
    public PageInfo<Song> getAllSongs(@RequestBody QueryInfo queryInfo){
        PageInfo<Song> pageInfo = songService.selectList(queryInfo);
        return pageInfo;
    }

    /**
     * 查询一个歌曲
     * @param id
     * @return
     */
    @GetMapping("/getOne/{id}")
    public ResultMap getOneSong(@PathVariable Integer id){
        Song song = songService.selectById(id);
        if (song != null) {
            return resultMap.success().code(200).message(song);
        }
        return resultMap.fail().code(401).message("歌曲查找失败");
    }

    /**
     * 添加一个歌曲
     * @return
     */
    @PostMapping("/add")
    public ResultMap addSong(HttpServletRequest request, @RequestParam("file")MultipartFile multipartFile){
        //上传歌曲文件
        if (multipartFile.isEmpty()) {
            return resultMap.fail().code(401).message("歌曲上传失败");
        }
        // 文件名 = 当前时间到毫秒 + 原来的文件名
        String fileName = System.currentTimeMillis() + multipartFile.getOriginalFilename(); // 防止文件名重复
        // 文件路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"song"
                +System.getProperty("file.separator");
        // 如果文件路径不存在，新增该路径
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        // 实际的文件地址
        File dest = new File(filePath + fileName);
        // 存储到数据库里的相对文件地址
        String storeUrlPath = "/song/" + fileName;
        try {
            multipartFile.transferTo(dest);
            Song song = new Song();
            song.setSingerId(Integer.parseInt(request.getParameter("singerId").trim()));
            song.setName(request.getParameter("name").trim());
            song.setIntroduction(request.getParameter("introduction").trim());
            song.setCreateTime(new Date());
            song.setUpdateTime(new Date());
            song.setLyric(request.getParameter("lyric"));
            song.setPic(defaultSongPic);// 设置默认歌曲图片
            song.setUrl(storeUrlPath);
            int insert = songService.insert(song);
            if (insert > 0) {
                return resultMap.success().code(200).message("歌曲"+song.getName()+"添加成功");
            }
            return resultMap.fail().code(401).message("歌曲"+song.getName()+"添加失败");

        } catch (IOException e) {
            return resultMap.fail().code(401).message("图片更新失败");
        }
    }

    /**
     * 更新一个歌曲
     * @param song
     * @return
     */
    @PutMapping("/update")
    public ResultMap updateSong(@RequestBody Song song){
        int update = songService.updateById(song);
        if (update > 0) {
            return resultMap.success().code(200).message("歌曲"+song.getName()+"更新成功");
        }
        return resultMap.fail().code(401).message("歌曲"+song.getName()+"更新失败");
    }

    /**
     * 删除一个歌曲
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResultMap deleteSong(@PathVariable Integer id){
        Song song = songService.selectById(id);
        deleteSongUrlFile(song); // 删除歌曲Mp3文件
        deleteSongPicFile(song); // 删除歌曲图片文件

        int delete = songService.deleteById(id);
        if (delete > 0) {
            return resultMap.success().code(200).message("歌曲[ "+song.getName()+" ]删除成功");
        }
        return resultMap.fail().code(401).message("歌曲[ "+song.getName()+" ]删除失败");
    }

    /**
     * 批量删除歌曲
     * @param params
     * @return
     */
    @PostMapping("/delSome")
    public ResultMap delSome(@RequestBody Map params) {
        List<Integer> list = (List<Integer>) params.get("ids");
        List<Song> listSong = songService.getListSong(list);
        listSong.forEach(song -> {
            deleteSongUrlFile(song); // 删除歌曲Mp3文件
            deleteSongPicFile(song); // 删除歌曲图片文件
        });

        int i = songService.deleteSome(list);
        if (i > 0) {
            return resultMap.success().code(200).message("批量删除成功");
        }
        return resultMap.fail().code(401).message("批量删除失败");
    }

    /**
     * 更新歌曲的图片
     * @param multipartFile
     * @param id
     * @return
     */
    @PostMapping("/updateSongPic/{id}")
    public ResultMap updateSongPic(@RequestParam("file") MultipartFile multipartFile, @PathVariable int id){
        //上传图片文件
        if (multipartFile.isEmpty()) {
            return resultMap.fail().code(401).message("图片上传失败");
        }
        // 文件名 = 当前时间到毫秒 + 原来的文件名
        String fileName = System.currentTimeMillis() + multipartFile.getOriginalFilename(); // 防止文件名重复
        // 文件路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"img"
                +System.getProperty("file.separator")+"songPic"+System.getProperty("file.separator");
        // 如果文件路径不存在，新增该路径
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        // 实际的文件地址
        File dest = new File(filePath + fileName);
        // 存储到数据库里的相对文件地址
        String storeAvatorPath = "/img/songPic/" + fileName;
        try {
            multipartFile.transferTo(dest);
            Song song = songService.selectById(id);
            //如果当前歌手的图片不是默认图片，更新时，删除旧图片
            deleteSongPicFile(song);

            song.setPic(storeAvatorPath); //数据库更新新的图片的地址
            song.setUpdateTime(new Date()); // 设置更新时间
            int update = songService.updateById(song);
            if (update > 0) {
                return resultMap.success().code(200).message("图片更新成功");
            }
            return resultMap.fail().code(401).message("图片更新失败");
        } catch (IOException e) {
            return resultMap.fail().code(401).message("图片更新失败");
        }
    }

    /**
     * 更新歌曲
     * @param multipartFile
     * @param id
     * @return
     */
    @PostMapping("/updateSongUrl/{id}")
    public ResultMap updateSongUrl(@RequestParam("file") MultipartFile multipartFile, @PathVariable int id){
        //上传图片文件
        if (multipartFile.isEmpty()) {
            return resultMap.fail().code(401).message("歌曲上传失败");
        }
        // 文件名 = 当前时间到毫秒 + 原来的文件名
        String fileName = System.currentTimeMillis() + multipartFile.getOriginalFilename(); // 防止文件名重复
        // 文件路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"song"
                +System.getProperty("file.separator");
        // 如果文件路径不存在，新增该路径
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        // 实际的文件地址
        File dest = new File(filePath + fileName);
        // 存储到数据库里的相对文件地址
        String storeAvatorPath = "/song/" + fileName;
        try {
            multipartFile.transferTo(dest);
            Song song = songService.selectById(id);
            // 删除原来歌曲Mp3文件
            deleteSongUrlFile(song);

            song.setUrl(storeAvatorPath); //数据库更新新的Mp3的地址
            int update = songService.updateById(song);
            if (update > 0) {
                return resultMap.success().code(200).message("歌曲更新成功");
            }
            return resultMap.fail().code(401).message("歌曲更新失败");
        } catch (IOException e) {
            return resultMap.fail().code(401).message("歌曲更新失败");
        }
    }

    /**
     * 删除歌曲Mp3文件
     * @param song
     */
    private void deleteSongUrlFile(Song song) {
        // 歌曲文件路径
        String songFilePath = System.getProperty("user.dir")+song.getUrl();
        File songFile = new File(songFilePath);
        if (songFile.exists()) {
            songFile.delete();
        }
    }

    /**
     * 删除歌曲图片文件
     * @param song
     */
    private void deleteSongPicFile(Song song) {
        // 歌曲图片地址（图片地址不等于默认图片地址时删掉歌曲图片）
        if (!song.getPic().equals(defaultSongPic)) {
            String songPicPath = System.getProperty("user.dir") + song.getPic();
            File songPicFile = new File(songPicPath);
            if (songPicFile.exists()) {
                songPicFile.delete();
            }
        }
    }

}
