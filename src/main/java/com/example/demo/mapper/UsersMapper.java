package com.example.demo.mapper;

import com.example.demo.dto.UsersDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UsersMapper {
    int insertUser(UsersDto dto);
    UsersDto selectOne(String id);
    UsersDto isUser(UsersDto dto);
}
