package com.example.demo.controller;

import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
public class DataURLController {
    private String fileDir="c:/web/team1/upload/";

    @GetMapping("/images/{filename}")
    public UrlResource showImage(@PathVariable("filename") String fileName) throws MalformedURLException {
        return new UrlResource("file:"+fileDir+fileName);
    }
}
