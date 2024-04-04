package org.example.springstudy.controller;

import org.example.springstudy.entity.User;
import org.example.springstudy.mapper.postgresql.UserPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("/test")
public class TestController {
    @Autowired()
    private UserPostMapper userMapper;
    @GetMapping
    public Mono<User> test(){
        User user = userMapper.selectById(1);
        return Mono.justOrEmpty(user);
    }
}
