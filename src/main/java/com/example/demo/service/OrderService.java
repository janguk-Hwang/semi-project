package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.*;
import com.example.demo.pagination.PageInfo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrdersMapper ordersMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final ProductOptionMapper productOptionMapper;
    private final CartMapper cartMapper;

    //주문취소
    public void cancelOrder(int order_id, int member_id){
        // 1. 주문취소 가능여부
        Map<String,Object> map=new HashMap<>();
        map.put("order_id",order_id);
        map.put("member_id",member_id);
        boolean cancelable=ordersMapper.isCancelableOrder(map);
        if (!cancelable){
            throw new IllegalStateException("취소할 수 없는 주문입니다.");
        }
        // 2. 주문상품 목록 조회
        List<OrderItemDto> items=orderItemMapper.selectOrderItemsByOrderId(order_id);
        if(items==null || items.isEmpty()){
            throw new IllegalStateException("주문 상품 정보가 존재하지 않습니다.");
        }
        // 3. 재고복구
        for(OrderItemDto item:items){
            StockUpdateDto stockDto=new StockUpdateDto();
            stockDto.setQuantity(item.getQuantity());
            //옵션 있는 상품
            if(item.getOption_id()!=null){
                // 3-1. 옵션 재고 복구
                stockDto.setOption_id(item.getOption_id());
                int optionUpdated=productOptionMapper.increaseOptionStock(stockDto);
                if(optionUpdated==0){
                    throw new IllegalStateException("옵션 재고 복구 실패");
                }
                // 3-2. 상품 재고 복구
                stockDto.setProduct_id(item.getProduct_id());
                int productUpdated=productMapper.increaseProductStock(stockDto);
                if(productUpdated==0){
                    throw new IllegalStateException("상품 재고 복구 실패");
                }
            } else {
                //옵션 없는 상품
                stockDto.setProduct_id(item.getProduct_id());
                int productUpdated=productMapper.increaseProductStock(stockDto);
                if(productUpdated==0){
                    throw new IllegalStateException("상품 재고 복구 실패");
                }
            }
        }
        // 4. 주문 상태 변경
        int updated=ordersMapper.updateOrderStatusToCancelled(map);
        if(updated==0){
            throw new IllegalStateException("주문 상태 변경 실패");
        }
    }

    //주문취소 가능여부 체크
    public boolean isCancelableOrder(int order_id, int member_id){
        OrdersDto order=ordersMapper.selectByOrderId(order_id);
        if(order==null){
            return false; //주문없음
        }
        if(order.getMember_id()!=member_id){
            return false; //본인주문 아님
        }
        if(!"ORDERED".equals(order.getOrder_status())){
            return false; //이미 취소 또는 배송중/완료
        }
        return true;
    }

    //주문 상세 조회
    public OrderDetailDto getOrderDetail(int order_id, int member_id){
        Map<String,Object> map=new HashMap<>();
        map.put("order_id",order_id);
        map.put("member_id",member_id);
        // 1. 주문 기본 정보 조회(본인 주문만)
        OrderDetailDto order=ordersMapper.selectOrderDetail(map);
        if(order==null){
            throw new IllegalArgumentException("존재하지 않거나 접근 권한이 없는 주문입니다.");
        }
        // 2. 주문 상품 목록 조회
        List<OrderDetailItemDto> items=ordersMapper.selectOrderDetailItems(order_id);
        order.setItems(items);

        return order;
    }

    //마이페이지 주문내역 조회 + 페이징처리
    public Map<String,Object> getOrderListPaging(int member_id,int pageNum){
        int totalRowCount=ordersMapper.countOrdersByMember(member_id);
        PageInfo pageInfo=new PageInfo(pageNum,6,5,totalRowCount);

        Map<String,Object> map=new HashMap<>();
        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());
        map.put("member_id",member_id);
        List<OrderListDto> list=ordersMapper.selectOrderListPaging(map);

        Map<String,Object> result=new HashMap<>();
        result.put("orderList",list);
        result.put("pageInfo",pageInfo);
        result.put("totalCount",totalRowCount);

        return result;
    }

    //장바구니 상품 주문 관련 공통 로직 -> 총주문금액 계산, 주문상품리스트, 배송정보
    private OrderPageDto buildOrderPage(List<OrderPageItemDto> items,UsersDto loginUser){
        int orderTotalPrice = 0;
        //총주문금액 계산
        for (OrderPageItemDto item : items){
            int itemTotal=item.getUnit_price()*item.getQuantity();
            item.setTotal_price(itemTotal);
            orderTotalPrice+=itemTotal;
        }
        OrderPageDto pageDto=new OrderPageDto();
        pageDto.setItems(items); //주문할 상품들 List에 담기
        pageDto.setOrder_total_price(orderTotalPrice); //총 주문금액 담기
        //배송정보
        pageDto.setReceiver(loginUser.getName());
        pageDto.setPhone(loginUser.getPhone());
        pageDto.setAddress(loginUser.getAddr());

        return pageDto;
    }
    //전체 장바구니 상품 주문페이지
    public OrderPageDto makeOrderPageFromCartAll(int member_id, UsersDto loginUser){
        List<OrderPageItemDto> items=orderItemMapper.selectOrderItemByMember(member_id);
        if (items==null || items.isEmpty()){
            throw new IllegalArgumentException("장바구니에 상품이 없습니다.");
        }
        List<Integer> cartIds=cartMapper.selectCartIdsByMember(member_id);
        OrderPageDto pageDto=buildOrderPage(items,loginUser);
        pageDto.setCartIds(cartIds);

        return pageDto;
    }
    //선택 장바구니 상품 주문페이지
    public OrderPageDto makeOrderPageFromCart(int member_id, List<Integer> cartIds, UsersDto loginUser){
        List<OrderPageItemDto> items=orderItemMapper.selectOrderItemsByCart(member_id,cartIds);
        //주문할 상품 체크를 안했을 경우
        if(items==null || items.isEmpty()){
            throw new IllegalArgumentException("주문할 상품이 없습니다.");
        }
        return buildOrderPage(items,loginUser);
    }

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

    //단건, 다건 주문페이지에서 주문하기 통합
    public void confirmOrder(int member_id, OrderConfirmRequestDto dto){
        List<OrderRequestDto> items=dto.getItems();
        if(items==null || items.isEmpty()){
            throw new IllegalStateException("주문할 상품이 없습니다.");
        }
        int totalPrice=0;
        // 1. 재고차감
        for (OrderRequestDto item:items){
            StockUpdateDto stockUpdateDto=new StockUpdateDto();
            stockUpdateDto.setQuantity(item.getQuantity());

            int updated;
            if(item.getOption_id()!=null){
                stockUpdateDto.setOption_id(item.getOption_id());
                updated=productMapper.decreaseOptionStock(stockUpdateDto);
                if (updated==0) throw new IllegalStateException("옵션 재고 부족");

                updated=productMapper.decreaseProductStockByOption(stockUpdateDto);
                if (updated==0) throw new IllegalStateException("상품 재고 부족");
            } else {
                stockUpdateDto.setProduct_id(item.getProduct_id());
                updated=productMapper.decreaseProductStock(stockUpdateDto);
                if (updated==0) throw new IllegalStateException("상품 재고 부족");
            }
            totalPrice+=item.getPrice()*item.getQuantity();
        }
        // 2. orders생성
        OrdersDto order=new OrdersDto();
        order.setMember_id(member_id);
        order.setOrder_status("ORDERED");
        order.setTotal_price(totalPrice);
        order.setAddress(dto.getAddress());
        ordersMapper.insertOrder(order);
        // 3. order_id 조회
        int order_id=ordersMapper.selectCurrentOrderId();
        // 4. order_item생성
        for (OrderRequestDto item:items){
            OrderItemDto oi=new OrderItemDto();
            oi.setOrder_id(order_id);
            oi.setProduct_id(item.getProduct_id());
            oi.setOption_id(item.getOption_id());
            oi.setQuantity(item.getQuantity());
            oi.setPrice(item.getPrice());
            orderItemMapper.insertOrder(oi);
        }
        // 5. cart주문시 장바구니 비우기
        if (dto.getCartIds()!=null && !dto.getCartIds().isEmpty()){
            cartMapper.deleteCartItemsWhenOrders(dto.getCartIds());
        }
    }
    //상품상세(단일상품) 주문페이지에서 주문하기
    public void orderDirect(int member_id, OrderRequestDto orderRequestDto){
        //재고차감
        StockUpdateDto stockUpdateDto=new StockUpdateDto();
        stockUpdateDto.setQuantity(orderRequestDto.getQuantity());
        int updated;
        if(orderRequestDto.getOption_id()!=null){
            stockUpdateDto.setOption_id(orderRequestDto.getOption_id());
            updated=productMapper.decreaseOptionStock(stockUpdateDto);
            if(updated==0){
                throw new IllegalStateException("옵션 재고가 부족합니다.");
            }
            updated=productMapper.decreaseProductStockByOption(stockUpdateDto);
            if(updated==0){
                throw new IllegalStateException("상품 재고가 부족합니다.");
            }
        }else {
            stockUpdateDto.setProduct_id(orderRequestDto.getProduct_id());
            updated=productMapper.decreaseProductStock(stockUpdateDto);
            if(updated==0){
                throw new IllegalStateException("상품 재고가 부족합니다.");
            }
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
