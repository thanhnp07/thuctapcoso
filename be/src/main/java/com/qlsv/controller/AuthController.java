package com.qlsv.controller;

import com.qlsv.dto.ApiResponse;
import com.qlsv.dto.LoginRequest;
import com.qlsv.entity.TaiKhoan;
import com.qlsv.repository.SinhVienRepository;
import com.qlsv.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final TaiKhoanRepository taiKhoanRepository;
    private final SinhVienRepository sinhVienRepository;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest request) {
        log.info("Đăng nhập: {}", request.getUsername());
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        
        // Update last login
        taiKhoan.setLastLogin(LocalDateTime.now());
        taiKhoanRepository.save(taiKhoan);
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", taiKhoan.getUsername());
        response.put("vaiTro", taiKhoan.getVaiTro());
        response.put("maSV", taiKhoan.getMaSV());
        
        // Lấy thông tin sinh viên nếu có
        if (taiKhoan.getMaSV() != null) {
            sinhVienRepository.findByMaSV(taiKhoan.getMaSV()).ifPresent(sv -> {
                response.put("hoTen", sv.getHoTen());
            });
        }
        
        log.info("Đăng nhập thành công: {}", request.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody TaiKhoan taiKhoan) {
        log.info("Đăng ký tài khoản mới: {}", taiKhoan.getUsername());
        
        if (taiKhoanRepository.existsByUsername(taiKhoan.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Username đã tồn tại"));
        }
        
        taiKhoan.setPasswordHash(passwordEncoder.encode(taiKhoan.getPasswordHash()));
        taiKhoanRepository.save(taiKhoan);
        
        log.info("Đăng ký thành công: {}", taiKhoan.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công", null));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        SecurityContextHolder.clearContext();
        log.info("Đăng xuất thành công");
        return ResponseEntity.ok(ApiResponse.success("Đăng xuất thành công", null));
    }
}
