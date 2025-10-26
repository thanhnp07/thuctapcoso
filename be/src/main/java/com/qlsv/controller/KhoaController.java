package com.qlsv.controller;

import com.qlsv.dto.ApiResponse;
import com.qlsv.entity.Khoa;
import com.qlsv.repository.KhoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khoa")
@RequiredArgsConstructor
@Slf4j
public class KhoaController {
    
    private final KhoaRepository khoaRepository;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<Khoa>>> getAllKhoa() {
        List<Khoa> danhSachKhoa = khoaRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách khoa thành công", danhSachKhoa));
    }
    
    @GetMapping("/{maKhoa}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Khoa>> getKhoaById(@PathVariable String maKhoa) {
        return khoaRepository.findById(maKhoa)
                .<ResponseEntity<ApiResponse<Khoa>>>map(khoa -> ResponseEntity.ok(ApiResponse.success("Lấy thông tin khoa thành công", khoa)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
