package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.MusicianDto;
import com.example.demo.dto.ProductDto;
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
    @Autowired
    private ProductOptionService productOptionService;

    @Test
    public void selecttype(){
        musicianService.selectArtist().forEach(l-> System.out.println(l));
    }
    @Test
    public void selectArtistById(){
        MusicianDto dto=musicianService.selectArtistById(2);
        System.out.println("dto============>"+dto);
    }

}
