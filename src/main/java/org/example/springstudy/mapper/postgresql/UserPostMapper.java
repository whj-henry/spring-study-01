package org.example.springstudy.mapper.postgresql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.springstudy.entity.User;

@Mapper
public interface UserPostMapper extends BaseMapper<User> {
}
