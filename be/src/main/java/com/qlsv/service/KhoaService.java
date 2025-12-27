package com.qlsv.service;

import com.qlsv.dto.KhoaDTO;
import com.qlsv.entity.Khoa;
import com.qlsv.exception.ResourceNotFoundException;
import com.qlsv.repository.KhoaRepository;
import com.qlsv.repository.LopRepository;
import com.qlsv.repository.SinhVienRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KhoaService {
    
    private final KhoaRepository khoaRepository;
    private final LopRepository lopRepository;
    private final SinhVienRepository sinhVienRepository;
    
    public List<KhoaDTO> getAllKhoa() {
        return khoaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public KhoaDTO getKhoaById(Long id) {
        Khoa khoa = khoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoa với id: " + id));
        return convertToDTO(khoa);
    }
    
    public KhoaDTO getKhoaByMaKhoa(String maKhoa) {
        Khoa khoa = khoaRepository.findByMaKhoa(maKhoa)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoa với mã: " + maKhoa));
        return convertToDTO(khoa);
    }
    
    public KhoaDTO createKhoa(KhoaDTO dto) {
        log.info("Tạo khoa mới: {}", dto.getMaKhoa());
        
        if (khoaRepository.existsByMaKhoa(dto.getMaKhoa())) {
            throw new IllegalArgumentException("Mã khoa đã tồn tại: " + dto.getMaKhoa());
        }
        
        Khoa khoa = Khoa.builder()
                .maKhoa(dto.getMaKhoa())
                .tenKhoa(dto.getTenKhoa())
                .moTa(dto.getMoTa())
                .build();
        
        Khoa saved = khoaRepository.save(khoa);
        log.info("Đã tạo khoa: {}", saved.getMaKhoa());
        return convertToDTO(saved);
    }
    
    public KhoaDTO updateKhoa(Long id, KhoaDTO dto) {
        log.info("Cập nhật khoa id: {}", id);
        
        Khoa khoa = khoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoa: " + id));
        
        // Kiểm tra trùng mã khoa nếu thay đổi
        if (dto.getMaKhoa() != null && !dto.getMaKhoa().equals(khoa.getMaKhoa())) {
            if (khoaRepository.existsByMaKhoa(dto.getMaKhoa())) {
                throw new IllegalArgumentException("Mã khoa đã tồn tại: " + dto.getMaKhoa());
            }
            khoa.setMaKhoa(dto.getMaKhoa());
        }
        
        if (dto.getTenKhoa() != null) khoa.setTenKhoa(dto.getTenKhoa());
        if (dto.getMoTa() != null) khoa.setMoTa(dto.getMoTa());
        
        Khoa updated = khoaRepository.save(khoa);
        log.info("Đã cập nhật khoa: {}", updated.getMaKhoa());
        return convertToDTO(updated);
    }
    
    public void deleteKhoa(Long id) {
        log.info("Xóa khoa id: {}", id);
        
        Khoa khoa = khoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoa: " + id));
        
        // Kiểm tra còn lớp hay sinh viên không
        Long soLuongLop = lopRepository.countByIdKhoa(id);
        if (soLuongLop > 0) {
            throw new IllegalStateException("Không thể xóa khoa còn " + soLuongLop + " lớp");
        }
        
        Long soLuongSinhVien = sinhVienRepository.countByKhoa(id);
        if (soLuongSinhVien > 0) {
            throw new IllegalStateException("Không thể xóa khoa còn " + soLuongSinhVien + " sinh viên");
        }
        
        khoaRepository.delete(khoa);
        log.info("Đã xóa khoa: {}", khoa.getMaKhoa());
    }
    
    private KhoaDTO convertToDTO(Khoa khoa) {
        Long soLuongLop = lopRepository.countByIdKhoa(khoa.getId());
        Long soLuongSinhVien = sinhVienRepository.countByKhoa(khoa.getId());
        
        return KhoaDTO.builder()
                .id(khoa.getId())
                .maKhoa(khoa.getMaKhoa())
                .tenKhoa(khoa.getTenKhoa())
                .moTa(khoa.getMoTa())
                .soLuongLop(soLuongLop)
                .soLuongSinhVien(soLuongSinhVien)
                .build();
    }
}
