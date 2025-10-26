package com.qlsv.service;

import com.qlsv.dto.SinhVienDTO;
import com.qlsv.entity.Khoa;
import com.qlsv.entity.Lop;
import com.qlsv.entity.SinhVien;
import com.qlsv.exception.ResourceNotFoundException;
import com.qlsv.repository.KhoaRepository;
import com.qlsv.repository.LopRepository;
import com.qlsv.repository.SinhVienRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SinhVienService {
    
    private final SinhVienRepository sinhVienRepository;
    private final LopRepository lopRepository;
    private final KhoaRepository khoaRepository;
    
    public List<SinhVienDTO> getAllSinhVien() {
        return sinhVienRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public SinhVienDTO getSinhVienById(String maSV) {
        SinhVien sinhVien = sinhVienRepository.findByMaSVWithLopAndKhoa(maSV)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với mã: " + maSV));
        return convertToDTO(sinhVien);
    }
    
    public SinhVienDTO createSinhVien(SinhVienDTO dto) {
        log.info("Tạo sinh viên mới: {}", dto.getMaSV());
        
        if (sinhVienRepository.existsByMaSV(dto.getMaSV())) {
            throw new IllegalArgumentException("Mã sinh viên đã tồn tại: " + dto.getMaSV());
        }
        
        if (dto.getEmail() != null && sinhVienRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng: " + dto.getEmail());
        }
        
        Lop lop = lopRepository.findByMaLop(dto.getMaLop())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp: " + dto.getMaLop()));
        
        Khoa khoa = khoaRepository.findByMaKhoa(dto.getMaKhoa())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoa: " + dto.getMaKhoa()));
        
        SinhVien sinhVien = SinhVien.builder()
                .maSV(dto.getMaSV())
                .hoTen(dto.getHoTen())
                .ngaySinh(dto.getNgaySinh())
                .gioiTinh(dto.getGioiTinh())
                .email(dto.getEmail())
                .soDienThoai(dto.getSoDienThoai())
                .diaChi(dto.getDiaChi())
                .gpa(dto.getGpa())
                .trangThai(dto.getTrangThai() != null ? dto.getTrangThai() : "DANG_HOC")
                .lop(lop)
                .khoa(khoa)
                .build();
        
        SinhVien saved = sinhVienRepository.save(sinhVien);
        log.info("Đã tạo sinh viên: {}", saved.getMaSV());
        return convertToDTO(saved);
    }
    
    public SinhVienDTO updateSinhVien(String maSV, SinhVienDTO dto) {
        log.info("Cập nhật sinh viên: {}", maSV);
        
        SinhVien sinhVien = sinhVienRepository.findByMaSV(maSV)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên: " + maSV));
        
        if (dto.getEmail() != null && !dto.getEmail().equals(sinhVien.getEmail()) 
                && sinhVienRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng: " + dto.getEmail());
        }
        
        if (dto.getMaLop() != null && !dto.getMaLop().equals(sinhVien.getLop().getMaLop())) {
            Lop lop = lopRepository.findByMaLop(dto.getMaLop())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp: " + dto.getMaLop()));
            sinhVien.setLop(lop);
        }
        
        if (dto.getMaKhoa() != null && !dto.getMaKhoa().equals(sinhVien.getKhoa().getMaKhoa())) {
            Khoa khoa = khoaRepository.findByMaKhoa(dto.getMaKhoa())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoa: " + dto.getMaKhoa()));
            sinhVien.setKhoa(khoa);
        }
        
        if (dto.getHoTen() != null) sinhVien.setHoTen(dto.getHoTen());
        if (dto.getNgaySinh() != null) sinhVien.setNgaySinh(dto.getNgaySinh());
        if (dto.getGioiTinh() != null) sinhVien.setGioiTinh(dto.getGioiTinh());
        if (dto.getEmail() != null) sinhVien.setEmail(dto.getEmail());
        if (dto.getSoDienThoai() != null) sinhVien.setSoDienThoai(dto.getSoDienThoai());
        if (dto.getDiaChi() != null) sinhVien.setDiaChi(dto.getDiaChi());
        if (dto.getGpa() != null) sinhVien.setGpa(dto.getGpa());
        if (dto.getTrangThai() != null) sinhVien.setTrangThai(dto.getTrangThai());
        
        SinhVien updated = sinhVienRepository.save(sinhVien);
        log.info("Đã cập nhật sinh viên: {}", updated.getMaSV());
        return convertToDTO(updated);
    }
    
    public void deleteSinhVien(String maSV) {
        log.info("Xóa sinh viên: {}", maSV);
        
        SinhVien sinhVien = sinhVienRepository.findByMaSV(maSV)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên: " + maSV));
        
        sinhVienRepository.delete(sinhVien);
        log.info("Đã xóa sinh viên: {}", maSV);
    }
    
    public Page<SinhVienDTO> searchSinhVien(String maSV, String hoTen, String maLop, 
                                            String maKhoa, String gioiTinh, String trangThai, 
                                            Pageable pageable) {
        log.info("Tìm kiếm sinh viên với tiêu chí: maSV={}, hoTen={}, maLop={}, maKhoa={}", 
                maSV, hoTen, maLop, maKhoa);
        
        Page<SinhVien> result = sinhVienRepository.searchSinhVien(
                maSV, hoTen, maLop, maKhoa, gioiTinh, trangThai, pageable);
        
        return result.map(this::convertToDTO);
    }
    
    // Overload cho web controller với keyword search
    public Page<SinhVienDTO> searchSinhVien(String keyword, String maKhoa, String maLop, 
                                            String gioiTinh, Pageable pageable) {
        // keyword có thể chứa maSV, hoTen, hoặc email
        return searchSinhVien(keyword, keyword, maLop, maKhoa, gioiTinh, null, pageable);
    }
    
    public List<SinhVienDTO> getSinhVienByLop(String maLop) {
        return sinhVienRepository.findByLop_MaLop(maLop).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<SinhVienDTO> getSinhVienByKhoa(String maKhoa) {
        return sinhVienRepository.findByKhoa_MaKhoa(maKhoa).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<Khoa> getAllKhoa() {
        return khoaRepository.findAll();
    }
    
    public List<Lop> getAllLop() {
        return lopRepository.findAll();
    }
    
    private SinhVienDTO convertToDTO(SinhVien sinhVien) {
        return SinhVienDTO.builder()
                .maSV(sinhVien.getMaSV())
                .hoTen(sinhVien.getHoTen())
                .ngaySinh(sinhVien.getNgaySinh())
                .gioiTinh(sinhVien.getGioiTinh())
                .email(sinhVien.getEmail())
                .soDienThoai(sinhVien.getSoDienThoai())
                .diaChi(sinhVien.getDiaChi())
                .gpa(sinhVien.getGpa())
                .trangThai(sinhVien.getTrangThai())
                .maLop(sinhVien.getLop().getMaLop())
                .tenLop(sinhVien.getLop().getTenLop())
                .maKhoa(sinhVien.getKhoa().getMaKhoa())
                .tenKhoa(sinhVien.getKhoa().getTenKhoa())
                .build();
    }
}
