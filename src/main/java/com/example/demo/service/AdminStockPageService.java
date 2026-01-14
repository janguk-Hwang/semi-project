package com.example.demo.service;

import com.example.demo.cunstomanotation.StockLoggable;
import com.example.demo.dto.StockChangeDto;
import com.example.demo.mapper.StockLogTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminStockPageService {
    private final StockLogTransactionMapper stockLogTransactionMapper;

    @Transactional
    @StockLoggable
    public StockChangeDto stockIn(Integer product_id, Integer option_id, Integer qty){
        if(qty<=0) throw new IllegalArgumentException("수량 1이상");

        if(option_id == null){
            int cur= stockLogTransactionMapper.lockproductstock(product_id);
            int next= cur+qty;
            stockLogTransactionMapper.updateproductstock(Map.of("product_id",product_id, "newstock",next));
        }else {
            int cur=stockLogTransactionMapper.lockoptionstock(option_id);
            int next= cur+qty;
            stockLogTransactionMapper.updateoptionstock(Map.of("option_id",option_id,"newstock",next));
        }
        return new StockChangeDto(product_id,option_id,"STOCK_IN",qty);
    }
    @Transactional
    @StockLoggable
    public StockChangeDto stockOut(Integer product_id,Integer option_id,Integer qty){
        if(qty<=0) throw new IllegalArgumentException("수량 1 이상");
        if(option_id==null){
            int cur=stockLogTransactionMapper.lockproductstock(product_id);
            if(cur<qty) throw new IllegalStateException("재고 부족");
            stockLogTransactionMapper.updateproductstock(Map.of("product_id",product_id,"newstock",cur-qty));
        } else {
            int cur=stockLogTransactionMapper.lockoptionstock(option_id);
            if(cur<qty) throw new IllegalStateException("옵션 재고 부족");
            stockLogTransactionMapper.updateoptionstock(Map.of("option_id",option_id,"newstock",cur-qty));
        }
        return new StockChangeDto(product_id,option_id,"STOCK_OUT",qty);
    }
}
