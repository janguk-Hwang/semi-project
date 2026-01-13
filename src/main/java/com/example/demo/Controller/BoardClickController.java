package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BoardClickController {
    private final BoardService service;
@GetMapping("/board/detail")
    public String Click(@RequestParam int num, Model model){
        BoardDto detail=service.detail(num);
        BoardDto prev=service.prev(num);
        BoardDto next=service.next(num);
        service.read_count(num);
//        service.like_count(num);
        model.addAttribute("detail",detail);
        model.addAttribute("prev",prev);
        model.addAttribute("next",next);
        return "board/detail";
    }
}
