package com.example.demo.mapper;

import com.example.demo.dto.AdminBoardRequestDto;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.User_rolesDto;
import com.example.demo.dto.UsersDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    int count(Map<String,Object> map);
    List<UsersDto>userlist(Map<String,Object> map);
    int admindelete(int member_id);
    int roleupdate(UsersDto dto);
    User_rolesDto isadmin(int member_id);
    int countmembers();
    int countboard();
    int board_management_count(Map<String,Object> map);
    List<BoardDto> boardlist(Map<String,Object> map);
    BoardDto boardprev(Map<String,Object> map);
    List<String> boardselectlist();
    List<AdminBoardRequestDto> boardrequestlist(Map<String,Object> map);
    int adminboarddelete(int board_id);
}
