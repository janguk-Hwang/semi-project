package com.example.demo.service;

import com.example.demo.cunstomanotation.StockLoggable;
import com.example.demo.dto.*;
import com.example.demo.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final AdminMapper adminMapper;

    /**
     * 상품 생성 + 옵션 생성까지 한 트랜잭션에서 처리
     * 그리고 AOP가 stock_log에 넣을 수 있도록 StockChangeDto 리스트를 리턴
     */
    @StockLoggable
    @Transactional
    public List<StockChangeDto> createProduct(AdminProductDto dto) {

        // 1) dto.stock 계산 + has_option 세팅
        int sumstock = 0;
        boolean hasOpt = (dto.getOptions() != null && !dto.getOptions().isEmpty());

        if (hasOpt) {
            for (Product_optionDto p : dto.getOptions()) {
                // 혹시 null 들어올 수 있으니 안전하게
                if (p != null && p.getStock() != null) sumstock += p.getStock();
            }
            dto.setStock(sumstock);
            dto.setHas_option("Y");
        } else {
            dto.setHas_option("N");
            // 옵션 없으면 dto.getStock()가 null일 수도 있으니 안전하게
            if (dto.getStock() == null) dto.setStock(0);
        }

        // 2) product insert
        ProductDto productDto = new ProductDto();
        productDto.setProduct_name(dto.getProduct_name());
        productDto.setProduct_type(dto.getProduct_type());
        productDto.setPrice(dto.getPrice());
        productDto.setDescription(dto.getDescription());
        productDto.setStock(dto.getStock());
        productDto.setArtist_id(dto.getArtist_id());
        productDto.setHas_option(dto.getHas_option());

        adminMapper.product_insert(productDto);

        // 3) product_id 확보
        int productId = adminMapper.product_select_id(dto.getProduct_name());
        dto.setProduct_id(productId);

        // 4) option insert + (product_id + option_name)로 option_id 조회해서 StockChangeDto 만들기
        List<StockChangeDto> changes = new ArrayList<>();

        if (hasOpt) {
            for (Product_optionDto opt : dto.getOptions()) {
                if (opt == null) continue;

                String optionName = opt.getOption_name();
                Integer optStock = opt.getStock();
                if (optStock == null) optStock = 0;

                // 4-1) option insert
                adminMapper.product_option_insert(
                        new ProductOptionDto(0, optionName, optStock, productId)
                );

                // 4-2) 방금 insert한 option_id 조회 (이미 mapper 만들어둔 상태라고 하셨으니 그걸 호출)
                // 예: select_option_id_by_name(Map) / select_option_id(Map) 등
                Map<String, Object> m = new HashMap<>();
                m.put("product_id", productId);
                m.put("option_name", optionName);

                // ✅ 여기 메소드명은 "본인이 만든 mapper 메소드명"에 맞춰주세요.
                // (당신이 이전에 쓰던 이름이 select_option_id(option_map) 이었으니 그걸 우선 사용)
                Integer optionId = adminMapper.select_option_id(m);

                // 5) AOP가 받아서 stock_log insert 하게 "로그용 변화" 리턴
                changes.add(new StockChangeDto(
                        productId,
                        optionId,       // ✅ 이제 null 아님
                        "STOCK_IN",
                        optStock
                ));
            }
        } else {
            // 옵션 없는 상품은 product 기준으로 1줄
            changes.add(new StockChangeDto(
                    productId,
                    null,
                    "STOCK_IN",
                    dto.getStock()
            ));
        }

        return changes;
    }

    public int product_select_id(String product_name) {
        return adminMapper.product_select_id(product_name);
    }

    public int insert_img(AdminFileinfoRequestDto dto) {
        return adminMapper.insert_img(dto);
    }

    public MusicianDto musician_allselect() {
        return adminMapper.musician_allselect();
    }
}
