package com.qlsv.controller;

import com.qlsv.dto.SinhVienDTO;
import com.qlsv.service.SinhVienService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sinhvien")
@RequiredArgsConstructor
@Slf4j
public class SinhVienWebController {
    
    private final SinhVienService sinhVienService;
    
    @GetMapping
    public String listSinhVien(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String maKhoa,
            @RequestParam(required = false) String maLop,
            @RequestParam(required = false) String gioiTinh,
            Model model) {
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("maSV").ascending());
            Page<SinhVienDTO> sinhVienPage = sinhVienService.searchSinhVien(keyword, maKhoa, maLop, gioiTinh, pageable);
            
            model.addAttribute("sinhVienPage", sinhVienPage);
            model.addAttribute("keyword", keyword);
            model.addAttribute("maKhoa", maKhoa);
            model.addAttribute("maLop", maLop);
            model.addAttribute("gioiTinh", gioiTinh);
            
            // Load danh sách khoa và lớp cho filter
            model.addAttribute("danhSachKhoa", sinhVienService.getAllKhoa());
            model.addAttribute("danhSachLop", sinhVienService.getAllLop());
            
            return "sinhvien/list";
        } catch (Exception e) {
            log.error("Lỗi khi tải danh sách sinh viên", e);
            model.addAttribute("error", "Không thể tải danh sách sinh viên");
            return "error";
        }
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("sinhVien", null);
        model.addAttribute("danhSachKhoa", sinhVienService.getAllKhoa());
        model.addAttribute("danhSachLop", sinhVienService.getAllLop());
        return "sinhvien/form";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String maSV, Model model) {
        try {
            SinhVienDTO sinhVien = sinhVienService.getSinhVienById(maSV);
            model.addAttribute("sinhVien", sinhVien);
            model.addAttribute("danhSachKhoa", sinhVienService.getAllKhoa());
            model.addAttribute("danhSachLop", sinhVienService.getAllLop());
            return "sinhvien/form";
        } catch (Exception e) {
            log.error("Không tìm thấy sinh viên: " + maSV, e);
            return "redirect:/sinhvien?error=notfound";
        }
    }
    
    @GetMapping("/view/{id}")
    public String viewSinhVien(@PathVariable("id") String maSV, Model model) {
        try {
            SinhVienDTO sinhVien = sinhVienService.getSinhVienById(maSV);
            model.addAttribute("sinhVien", sinhVien);
            return "sinhvien/view";
        } catch (Exception e) {
            log.error("Không tìm thấy sinh viên: " + maSV, e);
            return "redirect:/sinhvien?error=notfound";
        }
    }
}
