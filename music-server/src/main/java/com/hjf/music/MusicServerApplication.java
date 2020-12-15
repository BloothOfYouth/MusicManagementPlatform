package com.hjf.music;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hjf.music.dao")
public class MusicServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicServerApplication.class, args);
    }

}
