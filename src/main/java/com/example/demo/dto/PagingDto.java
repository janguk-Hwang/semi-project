package com.example.demo.dto;

import com.example.demo.Page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagingDto {
    private List<BoardDto> list;
    private PageInfo pageinfo;
    public List<BoardDto> getList() {
        return list;
    }
    public PageInfo getPageinfo() {
        return pageinfo;
    }
}
