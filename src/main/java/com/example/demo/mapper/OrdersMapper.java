package com.example.demo.mapper;

import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderDetailItemDto;
import com.example.demo.dto.OrderListDto;
import com.example.demo.dto.OrdersDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {
    int updateOrderConfirmed(Map<String,Object> map);
    boolean isConfirmableOrder(Map<String,Object> map);
    int updateOrderStatusToCancelled(Map<String, Object> map);
    boolean isCancelableOrder(Map<String, Object> map);
    OrdersDto selectByOrderId(int order_id);
    int insertOrder(OrdersDto dto);
    int selectCurrentOrderId();
    List<OrderListDto> selectOrderListPaging(Map<String,Object> map);
    int countOrdersByMember(int member_id);
    OrderDetailDto selectOrderDetail(Map<String, Object> map);
    List<OrderDetailItemDto> selectOrderDetailItems(int order_id);
}
