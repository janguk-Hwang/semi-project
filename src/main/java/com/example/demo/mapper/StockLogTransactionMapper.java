package com.example.demo.mapper;

import com.example.demo.dto.Stock_logDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface StockLogTransactionMapper {
    int lockproductstock(int product_id);
    int lockoptionstock(int option_id);
    int updateproductstock(Map<String,Object> map);
    int updateoptionstock(Map<String,Object> map);
    int insertstocklog(Stock_logDto dto);
    int select_option_id_by_name(Map<String,Object> map);
}
