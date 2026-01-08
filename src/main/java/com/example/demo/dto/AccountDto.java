package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDto {
    private Integer account_id;
    private String account;
    private String name;
    private String bank;
    private Integer member_id;
}
