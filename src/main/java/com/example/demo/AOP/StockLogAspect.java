package com.example.demo.AOP;


import com.example.demo.mapper.StockLogTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StockLogAspect {
    private final StockLogTransactionMapper stockLogTransactionMapper;
}
