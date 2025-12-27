package com.qlsv.controller;

import com.qlsv.service.KhoaService;
import com.qlsv.service.LopService;
import com.qlsv.service.SinhVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final SinhVienService sinhVienService;
    private final KhoaService khoaService;
    private final LopService lopService;
    
    @GetMapping("/")
    public String index() {
        // Check if user is authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String login() {
        // If already logged in, redirect to dashboard
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/dashboard";
        }
        return "login";
    }
    
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        // Add user information to model
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("authorities", authentication.getAuthorities());
        }
        
        // Load statistics for dashboard
        var allStudents = sinhVienService.getAllSinhVien();
        var allKhoa = khoaService.getAllKhoa();
        var allLop = lopService.getAllLop();
        
        model.addAttribute("totalSinhVien", allStudents.size());
        model.addAttribute("totalKhoa", allKhoa.size());
        model.addAttribute("totalLop", allLop.size());
        
        // Calculate GPA average
        double avgGpa = allStudents.stream()
            .filter(s -> s.getGpa() != null)
            .mapToDouble(s -> s.getGpa())
            .average()
            .orElse(0.0);
        model.addAttribute("avgGpa", Math.round(avgGpa * 100.0) / 100.0);
        
        // Statistics by Khoa for chart
        var statsByKhoa = allStudents.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                s -> s.getTenKhoa() != null ? s.getTenKhoa() : "N/A",
                java.util.stream.Collectors.counting()
            ));
        model.addAttribute("statsByKhoa", statsByKhoa);
        
        // Statistics by Gender for chart
        var statsByGender = allStudents.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                s -> s.getGioiTinh() != null ? s.getGioiTinh() : "Khác",
                java.util.stream.Collectors.counting()
            ));
        model.addAttribute("statsByGender", statsByGender);
        
        // Load recent students (5 latest)
        var recentStudents = sinhVienService.searchSinhVien(null, null, null, null, 
                PageRequest.of(0, 5, Sort.by("id").descending()));
        model.addAttribute("recentStudents", recentStudents.getContent());
        
        return "dashboard";
    }
}
