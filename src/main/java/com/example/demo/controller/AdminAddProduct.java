package com.example.demo.controller;

import com.example.demo.dto.AdminProductDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductOptionDto;
import com.example.demo.dto.Stock_logDto;
import com.example.demo.service.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AdminAddProduct {
    private final AdminProductService adminProductService;
    @GetMapping("/admin/product/new")
    public String addproductForm(Model model){
        model.addAttribute("musician",adminProductService.musician_allselect());
        return "admin/addproduct";
    }
    @PostMapping("/admin/product/new")
    public String addProduct(@Valid AdminProductDto dto,
                             BindingResult bindingResult){
        int product=adminProductService.product_insert(dto);
        if (product<=0){
            return "admin/addproduct";
        }
        int stocklog=adminProductService.stock_insert(dto);

        int productoption=adminProductService.product_option_insert(dto);


    }
}
