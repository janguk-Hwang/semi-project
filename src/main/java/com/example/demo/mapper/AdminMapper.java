package com.example.demo.mapper;

import com.example.demo.dto.*;
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
    int product_insert(ProductDto dto);
    int stock_insert(Stock_logDto dto);
    int product_option_insert(ProductOptionDto dto);
    int product_select_id(String product_name);
    MusicianDto musician_allselect();
    int insert_img(AdminFileinfoRequestDto dto);
    int select_option_id(int product_id);
}
