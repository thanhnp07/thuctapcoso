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
        SinhVien sinhVien = sinhVienRepository.findByMaSV(maSV)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với mã: " + maSV));
        return convertToDTO(sinhVien);
    }
    
    public SinhVienDTO getSinhVienByIdEntity(Long id) {
        SinhVien sinhVien = sinhVienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với id: " + id));
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
        
        // Validate khoa và lớp tồn tại (using ID)
        Khoa khoa = khoaRepository.findById(dto.getIdKhoa())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoa với id: " + dto.getIdKhoa()));
        
        Lop lop = lopRepository.findById(dto.getIdLop())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp với id: " + dto.getIdLop()));
        
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
                .idLop(dto.getIdLop())
                .idKhoa(dto.getIdKhoa())
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
        
        if (dto.getIdLop() != null && !dto.getIdLop().equals(sinhVien.getIdLop())) {
            Lop lop = lopRepository.findById(dto.getIdLop())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp với id: " + dto.getIdLop()));
            sinhVien.setIdLop(dto.getIdLop());
            sinhVien.setLop(lop);
        }
        
        if (dto.getIdKhoa() != null && !dto.getIdKhoa().equals(sinhVien.getIdKhoa())) {
            Khoa khoa = khoaRepository.findById(dto.getIdKhoa())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoa với id: " + dto.getIdKhoa()));
            sinhVien.setIdKhoa(dto.getIdKhoa());
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
    
    public Page<SinhVienDTO> searchSinhVien(String maSV, String hoTen, Long idLop, 
                                            Long idKhoa, String gioiTinh, String trangThai, 
                                            Pageable pageable) {
        log.info("Tìm kiếm sinh viên với tiêu chí: maSV={}, hoTen={}, idLop={}, idKhoa={}", 
                maSV, hoTen, idLop, idKhoa);
        
        // Normalize empty strings to null to avoid JPQL parameter issues
        maSV = (maSV != null && maSV.trim().isEmpty()) ? null : maSV;
        hoTen = (hoTen != null && hoTen.trim().isEmpty()) ? null : hoTen;
        gioiTinh = (gioiTinh != null && gioiTinh.trim().isEmpty()) ? null : gioiTinh;
        trangThai = (trangThai != null && trangThai.trim().isEmpty()) ? null : trangThai;
        
        Page<SinhVien> result = sinhVienRepository.searchSinhVien(
                maSV, hoTen, idLop, idKhoa, gioiTinh, trangThai, pageable);
        
        return result.map(this::convertToDTO);
    }
    
    // Overload cho web controller với keyword search
    public Page<SinhVienDTO> searchSinhVien(String keyword, Long idKhoa, Long idLop, 
                                            String gioiTinh, Pageable pageable) {
        // keyword tìm kiếm theo MÃ SV hoặc HỌ TÊN (OR logic)
        // Normalize empty strings to null
        keyword = (keyword != null && keyword.trim().isEmpty()) ? null : keyword;
        gioiTinh = (gioiTinh != null && gioiTinh.trim().isEmpty()) ? null : gioiTinh;
        
        Page<SinhVien> result = sinhVienRepository.searchByKeyword(
                keyword, idLop, idKhoa, gioiTinh, pageable);
        
        return result.map(this::convertToDTO);
    }
    
    public List<SinhVienDTO> getSinhVienByLop(Long idLop) {
        return sinhVienRepository.findByIdLop(idLop).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<SinhVienDTO> getSinhVienByKhoa(Long idKhoa) {
        return sinhVienRepository.findByIdKhoa(idKhoa).stream()
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
        // Lấy thông tin lớp và khoa từ relationship (LAZY loading)
        Lop lop = sinhVien.getLop();
        Khoa khoa = sinhVien.getKhoa();
        
        return SinhVienDTO.builder()
                .id(sinhVien.getId())
                .maSV(sinhVien.getMaSV())
                .hoTen(sinhVien.getHoTen())
                .ngaySinh(sinhVien.getNgaySinh())
                .gioiTinh(sinhVien.getGioiTinh())
                .email(sinhVien.getEmail())
                .soDienThoai(sinhVien.getSoDienThoai())
                .diaChi(sinhVien.getDiaChi())
                .gpa(sinhVien.getGpa())
                .trangThai(sinhVien.getTrangThai())
                .idLop(sinhVien.getIdLop())
                .maLop(lop != null ? lop.getMaLop() : null)
                .tenLop(lop != null ? lop.getTenLop() : null)
                .idKhoa(sinhVien.getIdKhoa())
                .maKhoa(khoa != null ? khoa.getMaKhoa() : null)
                .tenKhoa(khoa != null ? khoa.getTenKhoa() : null)
                .createdAt(sinhVien.getCreatedAt())
                .updatedAt(sinhVien.getUpdatedAt())
                .build();
    }
}
