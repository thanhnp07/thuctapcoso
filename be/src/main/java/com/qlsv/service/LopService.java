package com.qlsv.service;

import com.qlsv.dto.LopDTO;
import com.qlsv.entity.Khoa;
import com.qlsv.entity.Lop;
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
public class LopService {
    
    private final LopRepository lopRepository;
    private final KhoaRepository khoaRepository;
    private final SinhVienRepository sinhVienRepository;
    
    public List<LopDTO> getAllLop() {
        return lopRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public LopDTO getLopById(Long id) {
        Lop lop = lopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp với id: " + id));
        return convertToDTO(lop);
    }
    
    public LopDTO getLopByMaLop(String maLop) {
        Lop lop = lopRepository.findByMaLop(maLop)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp với mã: " + maLop));
        return convertToDTO(lop);
    }
    
    public List<LopDTO> getLopByKhoa(Long idKhoa) {
        return lopRepository.findByIdKhoa(idKhoa).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public LopDTO createLop(LopDTO dto) {
        log.info("Tạo lớp mới: {}", dto.getMaLop());
        
        if (lopRepository.existsByMaLop(dto.getMaLop())) {
            throw new IllegalArgumentException("Mã lớp đã tồn tại: " + dto.getMaLop());
        }
        
        Khoa khoa = khoaRepository.findById(dto.getIdKhoa())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoa với id: " + dto.getIdKhoa()));
        
        Lop lop = Lop.builder()
                .maLop(dto.getMaLop())
                .tenLop(dto.getTenLop())
                .khoaHoc(dto.getKhoaHoc())
                .siSo(dto.getSiSo())
                .idKhoa(dto.getIdKhoa())
                .khoa(khoa)
                .build();
        
        Lop saved = lopRepository.save(lop);
        log.info("Đã tạo lớp: {}", saved.getMaLop());
        return convertToDTO(saved);
    }
    
    public LopDTO updateLop(Long id, LopDTO dto) {
        log.info("Cập nhật lớp id: {}", id);
        
        Lop lop = lopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp: " + id));
        
        // Kiểm tra trùng mã lớp nếu thay đổi
        if (dto.getMaLop() != null && !dto.getMaLop().equals(lop.getMaLop())) {
            if (lopRepository.existsByMaLop(dto.getMaLop())) {
                throw new IllegalArgumentException("Mã lớp đã tồn tại: " + dto.getMaLop());
            }
            lop.setMaLop(dto.getMaLop());
        }
        
        // Cập nhật khoa nếu thay đổi
        if (dto.getIdKhoa() != null && !dto.getIdKhoa().equals(lop.getIdKhoa())) {
            Khoa khoa = khoaRepository.findById(dto.getIdKhoa())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoa với id: " + dto.getIdKhoa()));
            lop.setIdKhoa(dto.getIdKhoa());
            lop.setKhoa(khoa);
        }
        
        if (dto.getTenLop() != null) lop.setTenLop(dto.getTenLop());
        if (dto.getKhoaHoc() != null) lop.setKhoaHoc(dto.getKhoaHoc());
        if (dto.getSiSo() != null) lop.setSiSo(dto.getSiSo());
        
        Lop updated = lopRepository.save(lop);
        log.info("Đã cập nhật lớp: {}", updated.getMaLop());
        return convertToDTO(updated);
    }
    
    public void deleteLop(Long id) {
        log.info("Xóa lớp id: {}", id);
        
        Lop lop = lopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp: " + id));
        
        // Kiểm tra còn sinh viên không
        Long soLuongSinhVien = sinhVienRepository.countByLop(id);
        if (soLuongSinhVien > 0) {
            throw new IllegalStateException("Không thể xóa lớp còn " + soLuongSinhVien + " sinh viên");
        }
        
        lopRepository.delete(lop);
        log.info("Đã xóa lớp: {}", lop.getMaLop());
    }
    
    private LopDTO convertToDTO(Lop lop) {
        Khoa khoa = lop.getKhoa();
        Long soLuongSinhVien = sinhVienRepository.countByLop(lop.getId());
        
        return LopDTO.builder()
                .id(lop.getId())
                .maLop(lop.getMaLop())
                .tenLop(lop.getTenLop())
                .khoaHoc(lop.getKhoaHoc())
                .siSo(lop.getSiSo())
                .idKhoa(lop.getIdKhoa())
                .maKhoa(khoa != null ? khoa.getMaKhoa() : null)
                .tenKhoa(khoa != null ? khoa.getTenKhoa() : null)
                .soLuongSinhVien(soLuongSinhVien)
                .build();
    }
}
