package org.example.springstudy.controller;

import jakarta.annotation.Resource;
import org.example.springstudy.entity.User;
import org.example.springstudy.mapper.mysql.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;

@RestController("/test")
public class TestController {
    @Resource
    private UserMapper userMapper;
    @GetMapping
    public String test(){
        User user = userMapper.selectById(1);
        return user.toString();
    }
}
