package com.qlsv.controller;

import com.qlsv.repository.LopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lop")
@RequiredArgsConstructor
@Slf4j
public class LopWebController {
    
    private final LopRepository lopRepository;
    
    @GetMapping
    public String listLop(Model model) {
        model.addAttribute("danhSachLop", lopRepository.findAll());
        return "lop/list";
    }
}
