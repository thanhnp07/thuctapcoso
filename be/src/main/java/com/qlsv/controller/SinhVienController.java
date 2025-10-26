package com.qlsv.controller;

import com.qlsv.dto.ApiResponse;
import com.qlsv.dto.SinhVienDTO;
import com.qlsv.service.SinhVienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sinhvien")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SinhVienController {
    
    private final SinhVienService sinhVienService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<SinhVienDTO>>> getAllSinhVien() {
        log.info("Lấy danh sách tất cả sinh viên");
        List<SinhVienDTO> sinhVienList = sinhVienService.getAllSinhVien();
        return ResponseEntity.ok(ApiResponse.success(sinhVienList));
    }
    
    @GetMapping("/{maSV}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<SinhVienDTO>> getSinhVienById(@PathVariable String maSV) {
        log.info("Lấy thông tin sinh viên: {}", maSV);
        SinhVienDTO sinhVien = sinhVienService.getSinhVienById(maSV);
        return ResponseEntity.ok(ApiResponse.success(sinhVien));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SinhVienDTO>> createSinhVien(@Valid @RequestBody SinhVienDTO dto) {
        log.info("Tạo sinh viên mới: {}", dto.getMaSV());
        SinhVienDTO created = sinhVienService.createSinhVien(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm sinh viên thành công", created));
    }
    
    @PutMapping("/{maSV}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SinhVienDTO>> updateSinhVien(
            @PathVariable String maSV, 
            @Valid @RequestBody SinhVienDTO dto) {
        log.info("Cập nhật sinh viên: {}", maSV);
        SinhVienDTO updated = sinhVienService.updateSinhVien(maSV, dto);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật sinh viên thành công", updated));
    }
    
    @DeleteMapping("/{maSV}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSinhVien(@PathVariable String maSV) {
        log.info("Xóa sinh viên: {}", maSV);
        sinhVienService.deleteSinhVien(maSV);
        return ResponseEntity.ok(ApiResponse.success("Xóa sinh viên thành công", null));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Page<SinhVienDTO>>> searchSinhVien(
            @RequestParam(required = false) String maSV,
            @RequestParam(required = false) String hoTen,
            @RequestParam(required = false) String maLop,
            @RequestParam(required = false) String maKhoa,
            @RequestParam(required = false) String gioiTinh,
            @RequestParam(required = false) String trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "maSV") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        
        log.info("Tìm kiếm sinh viên với tiêu chí: maSV={}, hoTen={}, maLop={}, maKhoa={}", 
                maSV, hoTen, maLop, maKhoa);
        
        Sort sort = direction.equalsIgnoreCase("ASC") ? 
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<SinhVienDTO> result = sinhVienService.searchSinhVien(
                maSV, hoTen, maLop, maKhoa, gioiTinh, trangThai, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/lop/{maLop}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<SinhVienDTO>>> getSinhVienByLop(@PathVariable String maLop) {
        log.info("Lấy danh sách sinh viên theo lớp: {}", maLop);
        List<SinhVienDTO> sinhVienList = sinhVienService.getSinhVienByLop(maLop);
        return ResponseEntity.ok(ApiResponse.success(sinhVienList));
    }
    
    @GetMapping("/khoa/{maKhoa}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<SinhVienDTO>>> getSinhVienByKhoa(@PathVariable String maKhoa) {
        log.info("Lấy danh sách sinh viên theo khoa: {}", maKhoa);
        List<SinhVienDTO> sinhVienList = sinhVienService.getSinhVienByKhoa(maKhoa);
        return ResponseEntity.ok(ApiResponse.success(sinhVienList));
    }
}
