package com.example.demo.controller;

import com.example.demo.service.AdminBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminBoardController {
    private final AdminBoardService adminBoardService;

    @GetMapping("/admin/board")
    public String adminboard(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                             @RequestParam(value = "field",required = false)String field,
                             @RequestParam(value = "keyword",required = false)String keyword,
                             @RequestParam(value = "board_type",defaultValue = "공지사항")String board_type,
                             Model model){
        List<String> boardselectlist=adminBoardService.boardselectlist();
        Map<String,Object> map =adminBoardService.boardlist(pageNum,board_type,field,keyword);

        model.addAttribute("list",map.get("list"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("field",field);
        model.addAttribute("keyword",keyword);
        model.addAttribute("board_type",board_type);
        model.addAttribute("boardselectlist",boardselectlist);
        return "admin/board";
    }
    @DeleteMapping("/admin/board/{board_id}")
    @ResponseBody
    public String admin_board_delete(@PathVariable("board_id")int board_id){
        int n=adminBoardService.adminboarddelete(board_id);
        return n>0?"삭제성공":"삭제실패";
    }
}
