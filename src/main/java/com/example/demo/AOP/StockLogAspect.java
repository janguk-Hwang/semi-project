package com.example.demo.AOP;

import com.example.demo.dto.StockChangeDto;
import com.example.demo.dto.Stock_logDto;
import com.example.demo.mapper.StockLogTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class StockLogAspect {

    private final StockLogTransactionMapper stockLogTransactionMapper;

    @AfterReturning(
            value = "@annotation(com.example.demo.cunstomanotation.StockLoggable)",
            returning = "result"
    )
    public void writeStockLog(Object result) {

        // 1) 단건 처리
        if (result instanceof StockChangeDto sc) {
            insertOne(sc);
            return;

        }

        // 2) 여러 건(List) 처리
        if (result instanceof List<?> list) {
            for (Object o : list) {
                if (o instanceof StockChangeDto sc2) {
                    insertOne(sc2);

                }
            }
        }
    }

    private void insertOne(StockChangeDto sc) {
        Stock_logDto log = new Stock_logDto();
        log.setChange_type(sc.getChangetype());
        log.setQuantity(sc.getQuantity());
        log.setOption_id(sc.getOption_id());   // Integer 그대로
        log.setProduct_id(sc.getProduct_id()); // Integer 그대로

        stockLogTransactionMapper.insertstocklog(log);

    }
}
