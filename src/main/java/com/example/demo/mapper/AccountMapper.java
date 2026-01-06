package com.example.demo.mapper;

import com.example.demo.dto.AccountDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {
    int insertAccount(AccountDto dto);
}
