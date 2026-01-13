package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.MusicianDto;
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
    @Autowired
    private ProductService productService;
    @Autowired
    private MusicianService musicianService;

    @Test
    public void selecttype(){
        musicianService.selectArtist().forEach(l-> System.out.println(l));
    }
    @Test
    public void selectArtistById(){
        MusicianDto dto=musicianService.selectArtistById(2);
        System.out.println("dto============>"+dto);
    }
//    @Test
//    public void deleteCart(){
//        int n=cartService.deleteCartItem(62,61);
//        System.out.println("n====>"+n);
//    }
}
