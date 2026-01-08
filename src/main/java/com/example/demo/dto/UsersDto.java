package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UsersDto {
    private int member_id;
    private String id;
    private String pwd;
    private String email;
    private String name;
    private String phone;
    private Date sign_in_date;
    private String role;
    private String profile;
    private int gender;

}
