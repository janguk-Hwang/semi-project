package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.UsersDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsersServiceTest {
    @Autowired
    private UsersService usersService;
    @Autowired
    private AccountService accountService;

    @Test
    public void insertUser(){
        int n=usersService.insertUser(new UsersDto(0004,"user04","1234","user04@test.com","이영희","010-1111-1111",
                null,null,"",1));
        System.out.println("n===>"+n);
    }
    @Test
    public void insertAccount(){
        int n=accountService.insertAccount(new AccountDto(0001,"000000","홍길동","국민",1));
        System.out.println("n===>"+n);
    }
}
