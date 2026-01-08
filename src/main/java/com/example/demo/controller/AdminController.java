package com.example.demo.controller;

import com.example.demo.dto.UsersDto;
import com.example.demo.service.UsermanagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UsermanagementService usermanagementService;
    @GetMapping("/admin/main")
    public String adminmain(){
        return "admin/adminmain";
    }
    @GetMapping("/admin/usermanagement")
    public String usermanagementpage(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                     @RequestParam(value = "field", required = false)String field,
                                     @RequestParam(value = "keyword",required = false)String keyword,
                                     Model model){

        Map<String,Object> map=usermanagementService.userlist(pageNum,field,keyword);
        model.addAttribute("list",map.get("list"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("field",field);
        model.addAttribute("keyword",keyword);
        return "admin/management";
    }
    @GetMapping("/admin/rolechg")
    @ResponseBody
    public String rolechg(UsersDto dto){
        int n=usermanagementService.roleupdate(dto);
        if (n>0){
            return dto.getId()+"님의 권한이 변경되었습니다";
        }else {
            return "권한 변경에 실패하였습니다.";
        }
    }
}
