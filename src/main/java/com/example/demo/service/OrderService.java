package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrdersMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ProductOptionMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrdersMapper ordersMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final ProductOptionMapper productOptionMapper;

    //주문페이지에 필요한 정보
    public OrderPageDto makeOrderPage(int product_id, Integer option_id, int quantity, UsersDto loginUser){
        //상품기본정보
        ProductDetailDto product=productMapper.selectProduct(product_id);
        if(product==null){
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
        OrderPageItemDto item=new OrderPageItemDto();
        item.setProduct_id(product_id);
        item.setProduct_name(product.getProduct_name());
        item.setSavefilename(product.getSavefilename());
        item.setUnit_price(product.getPrice());
        item.setQuantity(quantity);

        //옵션 있는 경우
        if(option_id!=null){
            ProductOptionDto option=productOptionMapper.selectOptionById(option_id);
            if(option==null){
                throw new IllegalArgumentException("존재하지 않는 옵션입니다.");
            }
            item.setOption_id(option_id);
            item.setOption_name(option.getOption_name());
        }
        //금액계산
        int itemTotalPrice=product.getPrice()*quantity;
        item.setTotal_price(itemTotalPrice);
        //OrderPageDto 구성
        OrderPageDto pageDto=new OrderPageDto();
        pageDto.setItems(List.of(item));
        pageDto.setOrder_total_price(itemTotalPrice);

        //배송정보(USers테이블 기반)
        pageDto.setReceiver(loginUser.getName());
        pageDto.setPhone(loginUser.getPhone());
        pageDto.setAddress(loginUser.getAddr());

        return pageDto;
    }

    //주문페이지 주문하기
    public void orderDirect(int member_id, OrderRequestDto orderRequestDto){
        //재고차감
        StockUpdateDto stockUpdateDto=new StockUpdateDto();
        stockUpdateDto.setQuantity(orderRequestDto.getQuantity());
        int updated;
        if(orderRequestDto.getOption_id()!=null){
            stockUpdateDto.setOption_id(orderRequestDto.getOption_id());
            updated=productMapper.decreaseOptionStock(stockUpdateDto);
        }else {
            stockUpdateDto.setProduct_id(orderRequestDto.getProduct_id());
            updated=productMapper.decreaseProductStock(stockUpdateDto);
        }
        //재고부족시 -> update실패
        if(updated==0){
            throw new IllegalStateException("재고가 부족합니다.");
        }
        //orders생성
        OrdersDto ordersDto=new OrdersDto();
        ordersDto.setMember_id(member_id);
        ordersDto.setOrder_status("ORDERED");
        ordersDto.setTotal_price(orderRequestDto.getPrice()*orderRequestDto.getQuantity());
        ordersDto.setAddress(orderRequestDto.getAddress());
        ordersMapper.insertOrder(ordersDto);
        //orders 주문번호 조회
        int order_id=ordersMapper.selectCurrentOrderId();
        //order_item생성
        OrderItemDto itemDto=new OrderItemDto();
        itemDto.setOrder_id(order_id);
        itemDto.setQuantity(orderRequestDto.getQuantity());
        itemDto.setProduct_id(orderRequestDto.getProduct_id());
        itemDto.setOption_id(orderRequestDto.getOption_id());
        itemDto.setPrice(orderRequestDto.getPrice());
        orderItemMapper.insertOrder(itemDto);
    }
}
