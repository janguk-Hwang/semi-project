package com.example.demo.mapper;

import com.example.demo.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
   int insert (BoardDto boardDto);
   List<BoardDto> selectAll(Map<String, Object> map);
   int count(Map<String, Object> map);
}
