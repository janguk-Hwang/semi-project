package com.example.demo.Controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService service;
    @GetMapping("/boardinsert")
    public String insertForm(){
        return "board/boardinsert";
    }
    public String insert(BoardDto dto, Model model){
        int n =service.insert(dto);
        if(n>0){
            model.addAttribute("result","success");
        }else {
            model.addAttribute("result","failure");
        }
        return "home";
    }
}
