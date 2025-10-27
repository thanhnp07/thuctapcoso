package com.qlsv.controller;

import com.qlsv.repository.KhoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/khoa")
@RequiredArgsConstructor
@Slf4j
public class KhoaWebController {
    
    private final KhoaRepository khoaRepository;
    
    @GetMapping
    public String listKhoa(Model model) {
        model.addAttribute("danhSachKhoa", khoaRepository.findAll());
        return "khoa/list";
    }
}
