package com.qlsv.controller;

import com.qlsv.dto.ApiResponse;
import com.qlsv.dto.LoginRequest;
import com.qlsv.dto.LoginResponse;
import com.qlsv.entity.TaiKhoan;
import com.qlsv.repository.TaiKhoanRepository;
import com.qlsv.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        log.info("Đăng nhập: {}", request.getUsername());
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsernameWithSinhVien(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        
        // Update last login
        taiKhoan.setLastLogin(LocalDateTime.now());
        taiKhoanRepository.save(taiKhoan);
        
        LoginResponse response = LoginResponse.builder()
                .token(token)
                .username(taiKhoan.getUsername())
                .vaiTro(taiKhoan.getVaiTro())
                .maSV(taiKhoan.getSinhVien() != null ? taiKhoan.getSinhVien().getMaSV() : null)
                .hoTen(taiKhoan.getSinhVien() != null ? taiKhoan.getSinhVien().getHoTen() : null)
                .build();
        
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
}
