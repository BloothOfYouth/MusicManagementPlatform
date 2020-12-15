package com.hjf.music.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hjf.music.dao.SingerDao;
import com.hjf.music.dao.SongDao;
import com.hjf.music.dao.SongListDao;
import com.hjf.music.dao.UserDao;
import com.hjf.music.entity.Singer;
import com.hjf.music.entity.Song;
import com.hjf.music.entity.SongList;
import com.hjf.music.entity.User;
import com.hjf.music.model.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author hjf
 * @create 2020-11-20 21:55
 */
@RestController
@RequestMapping("/info")
public class InfoController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SongDao songDao;

    @Autowired
    private SingerDao singerDao;

    @Autowired
    private SongListDao songListDao;

    @Autowired
    private ResultMap resultMap;

    @GetMapping("/getAllCount")
    public ResultMap getAllCount() {
        Integer userCount = userDao.selectCount(null);
        Integer girlsCount = userDao.selectCount(new QueryWrapper<User>().eq("sex", 0));
        Integer boysCount = userDao.selectCount(new QueryWrapper<User>().eq("sex", 1));

        Integer songCount = songDao.selectCount(null);
        Integer singerCount = singerDao.selectCount(null);
        Integer nv = singerDao.selectCount(new QueryWrapper<Singer>().eq("sex", 0));
        Integer nan = singerDao.selectCount(new QueryWrapper<Singer>().eq("sex", 1));
        Integer zuhe = singerDao.selectCount(new QueryWrapper<Singer>().eq("sex", 2));
        Integer buming = singerDao.selectCount(new QueryWrapper<Singer>().eq("sex", 3));
        HashMap<String, Integer> singerSex = new HashMap<>();
        singerSex.put("nv",nv);
        singerSex.put("nan",nan);
        singerSex.put("zuhe",zuhe);
        singerSex.put("buming",buming);

        Integer songListCount = songListDao.selectCount(null);
        Integer huayu = songListDao.selectCount(new QueryWrapper<SongList>().eq("style", "华语"));
        Integer yueyu = songListDao.selectCount(new QueryWrapper<SongList>().eq("style", "粤语"));
        Integer omei = songListDao.selectCount(new QueryWrapper<SongList>().eq("style", "欧美"));
        Integer rihan = songListDao.selectCount(new QueryWrapper<SongList>().eq("style", "日韩"));
        Integer BGM = songListDao.selectCount(new QueryWrapper<SongList>().eq("style", "BGM"));
        Integer qingyinyue = songListDao.selectCount(new QueryWrapper<SongList>().eq("style", "轻音乐"));
        Integer yueqi = songListDao.selectCount(new QueryWrapper<SongList>().eq("style", "乐器"));
        HashMap<String, Integer> style = new HashMap<>();
        style.put("huayu",huayu);
        style.put("yueyu",yueyu);
        style.put("omei",omei);
        style.put("rihan",rihan);
        style.put("BGM",BGM);
        style.put("qingyinyue",qingyinyue);
        style.put("yueqi",yueqi);


        HashMap<String, Object> map = new HashMap<>();
        map.put("userCount",userCount);
        map.put("songCount",songCount);
        map.put("singerCount",singerCount);
        map.put("songListCount",songListCount);
        map.put("girlsCount",girlsCount);
        map.put("boysCount",boysCount);
        map.put("style",style);
        map.put("singerSex",singerSex);
        return resultMap.success().code(200).message(map);
    }

}
