package com.example.demo.service;

import com.example.demo.Page.PageInfo;
import com.example.demo.dto.BoardDto;
import com.example.demo.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper mapper;

    public int insert(BoardDto dto){
        return mapper.insert(dto);
    }

    public Map<String,Object> selectAll(int pageNum,String field,String keyword) {


        Map<String,Object> map = new HashMap<>();
        map.put("field",field);
        map.put("keyword",keyword);
        int totalRowCount = mapper.count(map);
        PageInfo pageInfo = new PageInfo(pageNum,
                5,
                3,
                totalRowCount);
        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<BoardDto> list = mapper.selectAll(map);
        Map<String,Object> result = new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);
        System.out.println(list);
        return result;
    }
    public BoardDto detail(int num){
        return mapper.detail(num);
    }
    public BoardDto prev(int num){
        return mapper.prev(num);
    }
    public BoardDto next(int num){
        return mapper.next(num);
    }
    public int like_count(int num){
        return mapper.like_count(num);
    }
    public int read_count(int num){
        return mapper.read_count(num);
    }
}

