package com.qlsv.repository;

import com.qlsv.entity.SinhVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, String> {
    
    Optional<SinhVien> findByMaSV(String maSV);
    
    List<SinhVien> findByHoTenContainingIgnoreCase(String hoTen);
    
    List<SinhVien> findByLop_MaLop(String maLop);
    
    List<SinhVien> findByKhoa_MaKhoa(String maKhoa);
    
    List<SinhVien> findByGioiTinh(String gioiTinh);
    
    List<SinhVien> findByTrangThai(String trangThai);
    
    @Query("SELECT sv FROM SinhVien sv JOIN FETCH sv.lop JOIN FETCH sv.khoa WHERE sv.maSV = :maSV")
    Optional<SinhVien> findByMaSVWithLopAndKhoa(String maSV);
    
    // Tìm kiếm đa tiêu chí
    @Query("SELECT sv FROM SinhVien sv WHERE " +
           "(:maSV IS NULL OR sv.maSV = :maSV) AND " +
           "(:hoTen IS NULL OR LOWER(sv.hoTen) LIKE LOWER(CONCAT('%', :hoTen, '%'))) AND " +
           "(:maLop IS NULL OR sv.lop.maLop = :maLop) AND " +
           "(:maKhoa IS NULL OR sv.khoa.maKhoa = :maKhoa) AND " +
           "(:gioiTinh IS NULL OR sv.gioiTinh = :gioiTinh) AND " +
           "(:trangThai IS NULL OR sv.trangThai = :trangThai)")
    Page<SinhVien> searchSinhVien(
        @Param("maSV") String maSV,
        @Param("hoTen") String hoTen,
        @Param("maLop") String maLop,
        @Param("maKhoa") String maKhoa,
        @Param("gioiTinh") String gioiTinh,
        @Param("trangThai") String trangThai,
        Pageable pageable
    );
    
    // Thống kê
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.khoa.maKhoa = :maKhoa")
    Long countByKhoa(String maKhoa);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.lop.maLop = :maLop")
    Long countByLop(String maLop);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.gioiTinh = :gioiTinh")
    Long countByGioiTinh(String gioiTinh);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.trangThai = :trangThai")
    Long countByTrangThai(String trangThai);
    
    @Query("SELECT AVG(sv.gpa) FROM SinhVien sv WHERE sv.khoa.maKhoa = :maKhoa")
    Double avgGpaByKhoa(String maKhoa);
    
    @Query("SELECT AVG(sv.gpa) FROM SinhVien sv WHERE sv.lop.maLop = :maLop")
    Double avgGpaByLop(String maLop);
    
    // Tìm sinh viên sinh nhật trong khoảng
    List<SinhVien> findByNgaySinhBetween(LocalDate startDate, LocalDate endDate);
    
    boolean existsByMaSV(String maSV);
    
    boolean existsByEmail(String email);
}
