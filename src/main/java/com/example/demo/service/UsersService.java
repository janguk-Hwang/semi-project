package com.example.demo.service;

import com.example.demo.dto.UsersDto;
import com.example.demo.mapper.UsersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersMapper mapper;

    public int insertUser(UsersDto dto){
        return mapper.insertUser(dto);
    }
    public UsersDto isMembers(Map<String,Object> map){
        return mapper.isMembers(map);
    }
}
