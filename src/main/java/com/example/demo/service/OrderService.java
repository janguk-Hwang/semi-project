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
        OrderPageDto dto=new OrderPageDto();
        //상품기본정보
        ProductDetailDto product=productMapper.selectProduct(product_id);
        if(product==null){
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
        dto.setProduct_id(product_id);
        dto.setProduct_name(product.getProduct_name());
        dto.setSavefilename(product.getSavefilename());
        dto.setUnit_price(product.getPrice());
        dto.setQuantity(quantity);

        //옵션 있는 경우
        if(option_id!=null){
            ProductOptionDto option=productOptionMapper.selectOptionById(option_id);
            if(option==null){
                throw new IllegalArgumentException("존재하지 않는 옵션입니다.");
            }
            dto.setOption_id(option_id);
            dto.setOption_name(option.getOption_name());
        }
        int totalPrice=product.getPrice()*quantity;
        dto.setTotal_price(totalPrice);
        dto.setOrder_total_price(totalPrice);
        //배송정보
        dto.setReceiver(loginUser.getName());
        dto.setPhone(loginUser.getPhone());
        dto.setAddress(loginUser.getAddr());

        return dto;
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
