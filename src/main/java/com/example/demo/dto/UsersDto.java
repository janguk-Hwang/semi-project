package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersDto {
    private Integer member_id;
    private String id;
    private String pwd;
    private String email;
    private String name;
    private String phone;
    private Date sign_in_date;
    private String role;
    private String profile;
    private Integer gender;
    private String addr;
}
