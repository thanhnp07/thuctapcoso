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
    
    List<SinhVien> findByMaLop(String maLop);
    
    List<SinhVien> findByMaKhoa(String maKhoa);
    
    List<SinhVien> findByGioiTinh(String gioiTinh);
    
    List<SinhVien> findByTrangThai(String trangThai);
    
    // Tìm kiếm đa tiêu chí với JPQL query (hỗ trợ sort tự động)
    @Query("SELECT sv FROM SinhVien sv WHERE " +
           "(:maSV IS NULL OR sv.maSV = :maSV) AND " +
           "(:hoTen IS NULL OR LOWER(CAST(sv.hoTen AS string)) LIKE LOWER(CONCAT('%', CAST(:hoTen AS string), '%'))) AND " +
           "(:maLop IS NULL OR sv.maLop = :maLop) AND " +
           "(:maKhoa IS NULL OR sv.maKhoa = :maKhoa) AND " +
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
    
    // Tìm kiếm với keyword (tìm theo mã SV HOẶC họ tên HOẶC email)
    @Query("SELECT sv FROM SinhVien sv WHERE " +
           "(:keyword IS NULL OR " +
           "  CAST(sv.maSV AS string) LIKE CONCAT('%', CAST(:keyword AS string), '%') OR " +
           "  LOWER(CAST(sv.hoTen AS string)) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR " +
           "  LOWER(CAST(sv.email AS string)) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))) AND " +
           "(:maLop IS NULL OR sv.maLop = :maLop) AND " +
           "(:maKhoa IS NULL OR sv.maKhoa = :maKhoa) AND " +
           "(:gioiTinh IS NULL OR sv.gioiTinh = :gioiTinh)")
    Page<SinhVien> searchByKeyword(
        @Param("keyword") String keyword,
        @Param("maLop") String maLop,
        @Param("maKhoa") String maKhoa,
        @Param("gioiTinh") String gioiTinh,
        Pageable pageable
    );
    
    // Thống kê
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.maKhoa = :maKhoa")
    Long countByKhoa(String maKhoa);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.maLop = :maLop")
    Long countByLop(String maLop);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.gioiTinh = :gioiTinh")
    Long countByGioiTinh(String gioiTinh);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.trangThai = :trangThai")
    Long countByTrangThai(String trangThai);
    
    @Query("SELECT AVG(sv.gpa) FROM SinhVien sv WHERE sv.maKhoa = :maKhoa")
    Double avgGpaByKhoa(String maKhoa);
    
    @Query("SELECT AVG(sv.gpa) FROM SinhVien sv WHERE sv.maLop = :maLop")
    Double avgGpaByLop(String maLop);
    
    // Tìm sinh viên sinh nhật trong khoảng
    List<SinhVien> findByNgaySinhBetween(LocalDate startDate, LocalDate endDate);
    
    boolean existsByMaSV(String maSV);
    
    boolean existsByEmail(String email);
}
