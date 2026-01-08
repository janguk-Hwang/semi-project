package com.example.demo.controller;

import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ListController {
    public final BoardService service;
    @GetMapping("/board/boardlist")
    public String list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                       @RequestParam(value = "field", required = false)String field,
                       @RequestParam(value = "keyword", required = false)String keyword,
                       Model model){
        System.out.println("list");
        Map<String,Object> map = service.selectAll (pageNum,field,keyword);
        model.addAttribute("list",map.get ("list"));
        model.addAttribute("pageinfo",map.get("pageinfo"));
        model.addAttribute("field",field);
        model.addAttribute("keyword",keyword);
        return "board/boardlist";

    }
}
