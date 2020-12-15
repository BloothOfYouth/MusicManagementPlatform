package com.hjf.music.controller;

import com.github.pagehelper.PageInfo;
import com.hjf.music.entity.QueryInfo;
import com.hjf.music.entity.User;
import com.hjf.music.model.ResultMap;
import com.hjf.music.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author hjf
 * @create 2020-11-20 11:38
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ResultMap resultMap;

    /**
     * 默认用户头像
     */
    private static final String defaultUserAvator = "/avatorImages/default.jpg";


    /**
     * 模糊及分页查询
     * @param queryInfo
     * @return
     */
    @PostMapping("/get")
    public PageInfo<User> getAllUsers(@RequestBody QueryInfo queryInfo){
        PageInfo<User> pageInfo = userService.selectList(queryInfo);
        return pageInfo;
    }

    /**
     * 查询一个用户
     * @param id
     * @return
     */
    @GetMapping("/getOne/{id}")
    public ResultMap getOneUser(@PathVariable Integer id){
        User user = userService.selectById(id);
        if (user != null) {
            return resultMap.success().code(200).message(user);
        }
        return resultMap.fail().code(401).message("用户查找失败");
    }

    /**
     * 添加一个用户
     * @param user
     * @return
     */
    @PostMapping("/add")
    public ResultMap addUser(@RequestBody User user){
        user.setAvator(defaultUserAvator);// 设置默认用户图片
        int insert = userService.insert(user);
        if (insert > 0) {
            return resultMap.success().code(200).message("用户"+user.getName()+"添加成功");
        }
        return resultMap.fail().code(401).message("用户"+user.getName()+"添加失败");
    }

    /**
     * 更新一个用户
     * @param user
     * @return
     */
    @PutMapping("/update")
    public ResultMap updateUser(@RequestBody User user){
        int update = userService.updateById(user);
        if (update > 0) {
            return resultMap.success().code(200).message("用户"+user.getName()+"更新成功");
        }
        return resultMap.fail().code(401).message("用户"+user.getName()+"更新失败");
    }

    /**
     * 删除一个用户
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResultMap deleteUser(@PathVariable Integer id){
        User user = userService.selectById(id);
        deleteUserAvatorFile(user); // 删除用户图片文件

        int delete = userService.deleteById(id);
        if (delete > 0) {
            return resultMap.success().code(200).message("用户[ "+user.getName()+" ]删除成功");
        }
        return resultMap.fail().code(401).message("用户[ "+user.getName()+" ]删除失败");
    }

    /**
     * 更新用户的图片
     * @param multipartFile
     * @param id
     * @return
     */
    @PostMapping("/updateUserAvator/{id}")
    public ResultMap updateUserAvator(@RequestParam("file") MultipartFile multipartFile, @PathVariable int id){
        //上传图片文件
        if (multipartFile.isEmpty()) {
            return resultMap.fail().code(401).message("图片上传失败");
        }
        if (!multipartFile.getContentType().equals("image/jpeg")) {
            return resultMap.fail().code(401).message("上传的文件类型错误");
        }

        // 文件名 = 当前时间到毫秒 + 原来的文件名
        String fileName = System.currentTimeMillis() + multipartFile.getOriginalFilename(); // 防止文件名重复
        // 文件路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"avatorImages"
                +System.getProperty("file.separator");
        // 如果文件路径不存在，新增该路径
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        // 实际的文件地址
        File dest = new File(filePath + fileName);
        // 存储到数据库里的相对文件地址
        String storeAvatorPath = "/avatorImages/" + fileName;
        try {
            multipartFile.transferTo(dest);
            User user = userService.selectById(id);
            deleteUserAvatorFile(user); //如果当前用户的图片不是默认图片，更新时，删除旧图片

            user.setAvator(storeAvatorPath); //数据库更新新的图片的地址
            int update = userService.updateById(user);
            if (update > 0) {
                return resultMap.success().code(200).message("图片更新成功");
            }
            return resultMap.fail().code(401).message("图片更新失败");
        } catch (IOException e) {
            return resultMap.fail().code(401).message("图片更新失败");
        }
    }

    /**
     * 批量删除用户
     * @param params
     * @return
     */
    @PostMapping("/delSome")
    public ResultMap delSome(@RequestBody Map params) {
        List<Integer> list = (List<Integer>) params.get("ids");
        List<User> listUser = userService.getListUser(list);
        listUser.forEach(user -> {
            deleteUserAvatorFile(user); // 删除用户图片文件
        });

        int i = userService.deleteSome(list);
        if (i > 0) {
            return resultMap.success().code(200).message("批量删除成功");
        }
        return resultMap.fail().code(401).message("批量删除失败");
    }

    /**
     * 删除用户图片文件
     * @param user
     */
    private void deleteUserAvatorFile(User user) {
        // 用户图片地址（图片地址不等于默认图片地址时删掉用户图片）
        if (!user.getAvator().equals(defaultUserAvator)) {
            String songAvatorPath = System.getProperty("user.dir") + user.getAvator();
            File songAvatorFile = new File(songAvatorPath);
            if (songAvatorFile.exists()) {
                songAvatorFile.delete();
            }
        }
    }

}
