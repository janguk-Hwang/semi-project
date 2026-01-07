package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.SignupRequestDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.mapper.UsersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UsersMapper usersMapper;
    private final AccountMapper accountMapper;

    @Transactional
    public void signup(SignupRequestDto dto){
        //users
        UsersDto usersDto=new UsersDto();
        usersDto.setId(dto.getId());
        usersDto.setPwd(dto.getPwd());
        usersDto.setName(dto.getName());
        usersDto.setAddr(dto.getAddr());
        usersDto.setPhone(dto.getPhone());
        usersDto.setEmail(dto.getEmail());
        usersDto.setGender(dto.getGender());
        usersDto.setRole("USER");
        usersMapper.insertUser(usersDto);

        Integer member_id=accountMapper.selectmid(dto.getId());

        //account
        AccountDto accountDto=new AccountDto();
        accountDto.setName(dto.getAccountName());
        accountDto.setBank(dto.getBank());
        accountDto.setAccount(dto.getAccount());
        accountDto.setMember_id(member_id);
        accountMapper.insertAccount(accountDto);
    }
}
