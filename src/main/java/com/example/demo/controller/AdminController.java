package com.example.demo.controller;

import com.example.demo.dto.UsersDto;
import com.example.demo.service.UsermanagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UsermanagementService usermanagementService;
    @GetMapping("/admin/main")
    public String adminmain(Model model){
        int membercount=usermanagementService.countmembers();
        int boardcount=usermanagementService.countboard();
        model.addAttribute("membercount",membercount);
        model.addAttribute("boardcount",boardcount);
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
    @PostMapping("/admin/usermanagement")
    @ResponseBody
    public String rolechg(UsersDto dto){
        int n=usermanagementService.roleupdate(dto);
        if (n>0){
            return "변경이 완료 되었습니다.";
        }else {
            return "변경이 실패 하였습니다.";
        }
    }
    @DeleteMapping("/admin/usermanagement/{member_id}")
    @ResponseBody
    public String userdel(@PathVariable("member_id") int member_id){
        int n=usermanagementService.admindelete(member_id);
        if(n>0){
            return "회원이 삭제되었습니다.";
        }else {
            return "회원 삭제에 실패하였습니다.";
        }
    }
}
