package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequestDto {
    //users
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String phone;
    private String addr;

}
