package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMapper mapper;

    public int insertAccount(AccountDto dto){
        return mapper.insertAccount(dto);
    }

}
