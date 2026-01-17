package com.example.demo.mapper;

import com.example.demo.dto.FileinfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileInfoMapper {
    List<FileinfoDto> selectProductDetailImg(int product_id);
}
