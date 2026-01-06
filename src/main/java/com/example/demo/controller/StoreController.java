package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreController {
    @GetMapping("/store")
    public String storeHome(){
        return "store/storePage";
    }

    @GetMapping("/store/storeItem")
    public String storeItemPage(){
        return "store/storeItemPage";
    }
}
