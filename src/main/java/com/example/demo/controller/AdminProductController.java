package com.example.demo.controller;

import com.example.demo.service.AdminProductPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminProductController {
    private final AdminProductPageService adminProductPageService;

    @GetMapping("/admin/product")
    public String adminproduct(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                     @RequestParam(value = "field", required = false)String field,
                                     @RequestParam(value = "keyword",required = false)String keyword,
                                     Model model){

        Map<String,Object> map=adminProductPageService.product_list(pageNum,field,keyword);
        model.addAttribute("list",map.get("list"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("field",field);
        model.addAttribute("keyword",keyword);
        return "admin/product";
    }
}
