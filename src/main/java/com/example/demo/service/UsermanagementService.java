package com.example.demo.service;

import com.example.demo.dto.User_rolesDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.pageinfo.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsermanagementService {
    private final AdminMapper adminMapper;

    public Map<String,Object> userlist(int pageNum,String field,String keyword){
        Map<String,Object> map=new HashMap<>();
        map.put("field",field);
        map.put("keyword",keyword);
        int totalRowCount=adminMapper.count(map);
        PageInfo info=new PageInfo(pageNum,10,10,totalRowCount);
        map.put("startRow",info.getStartRow());
        map.put("endRow",info.getEndRow());
        List<UsersDto> list=adminMapper.userlist(map);
        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",info);
        return result;

    }
    public User_rolesDto isadmin(int member_id){
       return adminMapper.isadmin(member_id);
    }
    public int roleupdate(UsersDto dto){
        return adminMapper.roleupdate(dto);
    }
    public int admindelete(int member_id){
        return adminMapper.admindelete(member_id);
    }
}
