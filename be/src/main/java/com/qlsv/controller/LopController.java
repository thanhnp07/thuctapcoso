package com.qlsv.controller;

import com.qlsv.dto.ApiResponse;
import com.qlsv.entity.Lop;
import com.qlsv.repository.LopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lop")
@RequiredArgsConstructor
@Slf4j
public class LopController {
    
    private final LopRepository lopRepository;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<Lop>>> getAllLop() {
        List<Lop> danhSachLop = lopRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách lớp thành công", danhSachLop));
    }
    
    @GetMapping("/khoa/{maKhoa}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<Lop>>> getLopByKhoa(@PathVariable String maKhoa) {
        List<Lop> danhSachLop = lopRepository.findByKhoa_MaKhoa(maKhoa);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách lớp theo khoa thành công", danhSachLop));
    }
    
    @GetMapping("/{maLop}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Lop>> getLopById(@PathVariable String maLop) {
        return lopRepository.findById(maLop)
                .<ResponseEntity<ApiResponse<Lop>>>map(lop -> ResponseEntity.ok(ApiResponse.success("Lấy thông tin lớp thành công", lop)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
