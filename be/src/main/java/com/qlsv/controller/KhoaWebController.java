package com.qlsv.controller;

import com.qlsv.dto.KhoaDTO;
import com.qlsv.service.KhoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/khoa")
@RequiredArgsConstructor
@Slf4j
public class KhoaWebController {
    
    private final KhoaService khoaService;
    
    @GetMapping
    public String listKhoa(Model model) {
        log.info("Hiển thị danh sách khoa");
        model.addAttribute("danhSachKhoa", khoaService.getAllKhoa());
        return "khoa/list";
    }
    
    @GetMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String showForm(Model model) {
        log.info("Hiển thị form tạo khoa");
        model.addAttribute("khoa", new KhoaDTO());
        return "khoa/form";
    }
    
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Hiển thị form edit khoa id: {}", id);
        try {
            KhoaDTO khoa = khoaService.getKhoaById(id);
            model.addAttribute("khoa", khoa);
            return "khoa/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Khoa không tồn tại");
            return "redirect:/khoa";
        }
    }
    
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveKhoa(@Valid @ModelAttribute KhoaDTO khoaDTO, RedirectAttributes redirectAttributes) {
        log.info("Lưu khoa: {}", khoaDTO.getMaKhoa());
        try {
            if (khoaDTO.getId() == null) {
                khoaService.createKhoa(khoaDTO);
            } else {
                khoaService.updateKhoa(khoaDTO.getId(), khoaDTO);
            }
            redirectAttributes.addFlashAttribute("success", "Lưu khoa thành công");
            return "redirect:/khoa";
        } catch (Exception e) {
            log.error("Lỗi khi lưu khoa", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu khoa: " + e.getMessage());
            return "redirect:/khoa/form";
        }
    }
    
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteKhoa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Xóa khoa id: {}", id);
        try {
            khoaService.deleteKhoa(id);
            redirectAttributes.addFlashAttribute("success", "Xóa khoa thành công");
            return "redirect:/khoa";
        } catch (Exception e) {
            log.error("Lỗi khi xóa khoa", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa khoa: " + e.getMessage());
            return "redirect:/khoa";
        }
    }
}
