package com.qlsv.controller;

import com.qlsv.dto.KhoaDTO;
import com.qlsv.dto.LopDTO;
import com.qlsv.service.KhoaService;
import com.qlsv.service.LopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/lop")
@RequiredArgsConstructor
@Slf4j
public class LopWebController {
    
    private final LopService lopService;
    private final KhoaService khoaService;
    
    @GetMapping
    public String listLop(Model model) {
        log.info("Hiển thị danh sách lớp");
        model.addAttribute("danhSachLop", lopService.getAllLop());
        return "lop/list";
    }
    
    @GetMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String showForm(Model model) {
        log.info("Hiển thị form tạo lớp");
        List<KhoaDTO> danhSachKhoa = khoaService.getAllKhoa();
        model.addAttribute("lop", new LopDTO());
        model.addAttribute("danhSachKhoa", danhSachKhoa);
        return "lop/form";
    }
    
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Hiển thị form edit lớp id: {}", id);
        try {
            LopDTO lop = lopService.getLopById(id);
            List<KhoaDTO> danhSachKhoa = khoaService.getAllKhoa();
            model.addAttribute("lop", lop);
            model.addAttribute("danhSachKhoa", danhSachKhoa);
            return "lop/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lớp không tồn tại");
            return "redirect:/lop";
        }
    }
    
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveLop(@Valid @ModelAttribute LopDTO lopDTO, RedirectAttributes redirectAttributes) {
        log.info("Lưu lớp: {}", lopDTO.getMaLop());
        try {
            if (lopDTO.getId() == null) {
                lopService.createLop(lopDTO);
            } else {
                lopService.updateLop(lopDTO.getId(), lopDTO);
            }
            redirectAttributes.addFlashAttribute("success", "Lưu lớp thành công");
            return "redirect:/lop";
        } catch (Exception e) {
            log.error("Lỗi khi lưu lớp", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu lớp: " + e.getMessage());
            return "redirect:/lop/form";
        }
    }
    
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteLop(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Xóa lớp id: {}", id);
        try {
            lopService.deleteLop(id);
            redirectAttributes.addFlashAttribute("success", "Xóa lớp thành công");
            return "redirect:/lop";
        } catch (Exception e) {
            log.error("Lỗi khi xóa lớp", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa lớp: " + e.getMessage());
            return "redirect:/lop";
        }
    }
}
