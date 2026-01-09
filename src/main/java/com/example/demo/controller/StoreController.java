package com.example.demo.controller;

import com.example.demo.dto.ProductListDto;
import com.example.demo.service.MusicianService;
import com.example.demo.service.ProductOptionService;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StoreController {
    private final ProductService productService;
    private final MusicianService musicianService;
    private final ProductOptionService productOptionService;

    @GetMapping("/store/storeItem/{product_id}")
    public String storeProductDetail(@PathVariable int product_id,Model model){
        Map<String,Object> result=productOptionService.getProductDetail(product_id);

        model.addAttribute("product",result.get("product"));
        model.addAttribute("options",result.get("options"));
        model.addAttribute("detailImages",result.get("detailImages"));

        return "store/storeItemPage";
    }

    //전체상품보기
    @GetMapping("/store")
    public String storeHome(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                            @RequestParam(value = "mode", required = false) String mode,
                            Model model){

        Map<String,Object> result=productService.allProductList(null,pageNum);

        model.addAttribute("products",result.get("products"));
        model.addAttribute("pageInfo",result.get("pageInfo"));
        model.addAttribute("totalCount",result.get("totalCount"));
        model.addAttribute("product_type","all");
        model.addAttribute("mode",mode);

        if("artist".equals(mode)){
            model.addAttribute("artists",musicianService.selectArtist());
        }

        return "store/storePage";
    }

    //카테고리별 상품보기 - ALBUM,MD
    @GetMapping("/store/category/{product_type}")
    public String storeCategoryPage(
            @PathVariable String product_type,
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
            Model model){

        Map<String,Object> result=productService.allProductList(product_type,pageNum);

        model.addAttribute("products",result.get("products"));
        model.addAttribute("totalCount",productService.countAll());
        model.addAttribute("pageInfo",result.get("pageInfo"));
        model.addAttribute("product_type",product_type);

        return "store/storePage";
    }

    //아티스트별 상품목록 보기
    @GetMapping("/store/artist/{artist_id}")
    public String storeByArtist(@PathVariable int artist_id,
                                @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                Model model){
        Map<String,Object> result=productService.productListByArtist(artist_id,pageNum);

        model.addAttribute("products",result.get("products"));
        model.addAttribute("pageInfo", result.get("pageInfo"));
        model.addAttribute("totalCount",result.get("totalCount"));
        model.addAttribute("mode","artist");
        model.addAttribute("artist_id",artist_id);
        model.addAttribute("artists",musicianService.selectArtist());

        String artist_name=musicianService.selectArtistById(artist_id).getArtist_name();
        model.addAttribute("artist_name",artist_name);

        return "store/storePage";
    }

    //아티스트 카테고리 보기
    @GetMapping("/store/artist")
    public String storeArtistCategory(Model model){
        model.addAttribute("mode","artist");
        model.addAttribute("artists",musicianService.selectArtist());

        return "store/storePage";
    }

    @GetMapping("/store/storeItem")
    public String storeItemPage(){
        return "store/storeItemPage";
    }

}
