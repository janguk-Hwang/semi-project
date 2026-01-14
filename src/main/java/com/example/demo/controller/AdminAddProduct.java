package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            return "admin/addproduct";
        }

        // ✅ AOP용 List 리턴값은 컨트롤러에서 굳이 받을 필요 없음
        adminProductService.createProduct(dto);

        // ✅ fileinfo 저장에 필요하므로 productId는 다시 조회
        int productId = adminProductService.product_select_id(dto.getProduct_name());

        List<MultipartFile> files = dto.getFile1();
        if (files != null && !files.isEmpty()) {
            for (int idx = 0; idx < files.size(); idx++) {
                MultipartFile file = files.get(idx);
                if (file == null || file.isEmpty()) continue;

                String orgname = file.getOriginalFilename();
                if (orgname == null || !orgname.contains(".")) continue;

                String ext = orgname.substring(orgname.lastIndexOf("."));
                String savename = UUID.randomUUID() + ext;

                file.transferTo(new File(UPLOADPATH + savename));

                String role = (idx == 0) ? "THUMB" : "DETAIL";
                adminProductService.insert_img(
                        new AdminFileinfoRequestDto(
                                0, orgname, savename,
                                "PRODUCT", productId, role, null
                        )
                );
            }
        }

        redirectAttributes.addFlashAttribute("msg", "상품등록이 완료되었습니다!");
        return "redirect:/admin/product";
    }


}

