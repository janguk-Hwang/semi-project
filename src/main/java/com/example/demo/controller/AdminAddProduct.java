package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AdminAddProduct {
    private final AdminProductService adminProductService;
    private final String UPLOADPATH = "c:\\web\\team1\\upload\\";

    @GetMapping("/admin/product/new")
    public String addproductForm(Model model) {
        model.addAttribute("musician", adminProductService.musician_allselect());
        return "admin/addproduct";
    }

    @PostMapping("/admin/product/new")
    public String addProduct(@Valid AdminProductDto dto,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            //  addproduct에서 musician 리스트 쓰면 여기서 다시 model에 넣어야 함
            // model.addAttribute("musician", musicianService.list());
            return "admin/addproduct";
        }

        // 1) 상품/재고/옵션 먼저 저장 (productId 확보 목적)
        // product_insert가 "생성된 product_id"를 리턴한다고 가정
        adminProductService.product_insert(dto);
        int productId = adminProductService.product_select_id(dto.getProduct_name());

        // dto에 product_id가 필요하면 세팅
        dto.setProduct_id(productId);
        adminProductService.product_option_insert(dto);
        //재고 로그 는 트랜잭션 처리 해서 사용 나중에 변경예정
        adminProductService.stock_insert(dto);


        // 2) 파일 저장 + fileinfo insert
        List<MultipartFile> files = dto.getFile1();
        if (files != null && !files.isEmpty()) {

            for (int idx = 0; idx < files.size(); idx++) {
                MultipartFile file = files.get(idx);
                if (file == null || file.isEmpty()) continue;

                String orgname = file.getOriginalFilename();
                if (orgname == null || !orgname.contains(".")) continue;

                String ext = orgname.substring(orgname.lastIndexOf("."));
                String savename = UUID.randomUUID() + ext;

                File saved = new File(UPLOADPATH + savename);
                file.transferTo(saved);

                String role = (idx == 0) ? "THUMB" : "DETAIL";

                adminProductService.insert_img(
                        new AdminFileinfoRequestDto(
                                0, orgname, savename,
                                "PRODUCT", productId, role, null
                        )
                );
            }
        }
        redirectAttributes.addFlashAttribute("msg","상품등록이 완료되었습니다!");
        return "redirect:/admin/product";
        }
    }

