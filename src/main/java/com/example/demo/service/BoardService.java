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

        pageNum = Math.max(1,pageNum);
        Map<String,Object> map = new HashMap<>();
        map.put("field",field);
        map.put("keyword",keyword);
        int totalRowCount = mapper.count(map);
        PageInfo pageInfo = new PageInfo(pageNum,
                5,
                5,
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
}

