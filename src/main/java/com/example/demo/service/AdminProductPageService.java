package com.example.demo.service;

import com.example.demo.Page.PageInfo;
import com.example.demo.dto.AdminProductRequestDto;
import com.example.demo.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminProductPageService {
    private final AdminMapper adminMapper;

    public Map<String,Object> product_list(int pageNum,String field,String keyword){
        Map<String,Object> map=new HashMap<>();
        map.put("field",field);
        map.put("keyword",keyword);
        int totalRowCount=adminMapper.product_count(map);
        PageInfo info=new PageInfo(pageNum,10,5,totalRowCount);
        map.put("startRow",info.getStartRow());
        map.put("endRow",info.getEndRow());
        List<AdminProductRequestDto> list=adminMapper.product_list(map);
        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",info);
        return result;

    }
}
