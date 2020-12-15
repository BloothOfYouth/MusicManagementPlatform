package com.hjf.music;

import com.hjf.music.dao.ListSongDao;
import com.hjf.music.dao.SingerDao;
import com.hjf.music.dao.SongDao;
import com.hjf.music.dao.SongListDao;
import com.hjf.music.entity.ListSong;
import com.hjf.music.entity.Singer;
import com.hjf.music.entity.Song;
import com.hjf.music.entity.SongList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class MusicServerApplicationTests {

    @Autowired
    private SongDao songDao;

    @Autowired
    private ListSongDao listSongDao;

    @Autowired
    private SingerDao singerDao;

    @Autowired
    private SongListDao songListDao;

    @Test
    void contextLoads() {
        Integer[] ids = {5,6,7};
        List<Integer> list = Arrays.asList(ids);
        List<Song> songs = songDao.selectBatchIds(list);
        songs.forEach(song -> {
            System.out.println(song);
        });
    }

    @Test
    void contextLoads1() {
        List<ListSong> listSongs = new ArrayList<>();
        ListSong listSong = new ListSong();
        ListSong listSong1 = new ListSong();
        listSong.setSongListId(4);
        listSong.setSongId(1);
        listSong1.setSongListId(4);
        listSong1.setSongId(2);
        listSongs.add(listSong);
        listSongs.add(listSong1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("listSongs",listSongs);
        int i = listSongDao.insertSome(map);
        System.out.println("i = " + i);
    }

    @Test
    void contextLoads2() {
        Integer[] ids = {1,2};
        List<Integer> songIds = Arrays.asList(ids);
        HashMap<String, Object> map = new HashMap<>();
        map.put("songIds",songIds);
        List<Song> songs = songDao.selectBySongIds(map);
        songs.forEach(song -> {
            System.out.println("song = " + song);
        });
    }

    @Test
    void contextLoads3() {
        List<Integer> list = listSongDao.selectSongIdsBySongListId(5);
        System.out.println(list);
        list.forEach(integer -> {
            System.out.println(integer);
        });
    }

    @Test
    void contextLoads4() {
        List<Singer> singers = singerDao.selectSingers("");
        singers.forEach(singer -> {
            System.out.println(singer);
        });
    }

    @Test
    void contextLoads5() {
        List<SongList> songLists = songListDao.selectSongLists("");
        songLists.forEach(songList -> {
            System.out.println(songList);
        });
    }

}
