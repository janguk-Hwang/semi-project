package com.example.demo.service;

import com.example.demo.dto.AdminBoardRequestDto;
import com.example.demo.dto.BoardDto;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.Page.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminBoardService {
    private final AdminMapper adminMapper;

    public Map<String,Object> boardlist(int pageNum,String Board_type,String field,String keyword){
        Map<String,Object> map=new HashMap<>();
        map.put("board_type",Board_type);
        map.put("field",field);
        map.put("keyword",keyword);
        int count=adminMapper.board_management_count(map);
        PageInfo info=new PageInfo(pageNum,10,5,count);
        map.put("startRow",info.getStartRow());
        map.put("endRow",info.getEndRow());
        List<AdminBoardRequestDto> list=adminMapper.boardrequestlist(map);
        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",info);
        return result;
    }
    public List<String> boardselectlist(){
        return adminMapper.boardselectlist();
    }
    public int adminboarddelete(int board_id){
        return adminMapper.adminboarddelete(board_id);
    }

}
