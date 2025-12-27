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
public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {
    
    Optional<SinhVien> findByMaSV(String maSV);
    
    List<SinhVien> findByHoTenContainingIgnoreCase(String hoTen);
    
    List<SinhVien> findByIdLop(Long idLop);
    
    List<SinhVien> findByIdKhoa(Long idKhoa);
    
    List<SinhVien> findByGioiTinh(String gioiTinh);
    
    List<SinhVien> findByTrangThai(String trangThai);
    
    // Tìm kiếm đa tiêu chí với JPQL query (hỗ trợ sort tự động)
    @Query("SELECT sv FROM SinhVien sv WHERE " +
           "(:maSV IS NULL OR sv.maSV = :maSV) AND " +
           "(:hoTen IS NULL OR LOWER(CAST(sv.hoTen AS string)) LIKE LOWER(CONCAT('%', CAST(:hoTen AS string), '%'))) AND " +
           "(:idLop IS NULL OR sv.idLop = :idLop) AND " +
           "(:idKhoa IS NULL OR sv.idKhoa = :idKhoa) AND " +
           "(:gioiTinh IS NULL OR sv.gioiTinh = :gioiTinh) AND " +
           "(:trangThai IS NULL OR sv.trangThai = :trangThai)")
    Page<SinhVien> searchSinhVien(
        @Param("maSV") String maSV,
        @Param("hoTen") String hoTen,
        @Param("idLop") Long idLop,
        @Param("idKhoa") Long idKhoa,
        @Param("gioiTinh") String gioiTinh,
        @Param("trangThai") String trangThai,
        Pageable pageable
    );
    
    // Tìm kiếm với keyword (tìm theo mã SV HOẶC họ tên HOẶC email)
    @Query("SELECT sv FROM SinhVien sv WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "  sv.maSV LIKE CONCAT('%', :keyword, '%') OR " +
           "  LOWER(sv.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "  LOWER(sv.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:idLop IS NULL OR sv.idLop = :idLop) AND " +
           "(:idKhoa IS NULL OR sv.idKhoa = :idKhoa) AND " +
           "(:gioiTinh IS NULL OR :gioiTinh = '' OR sv.gioiTinh = :gioiTinh)")
    Page<SinhVien> searchByKeyword(
        @Param("keyword") String keyword,
        @Param("idLop") Long idLop,
        @Param("idKhoa") Long idKhoa,
        @Param("gioiTinh") String gioiTinh,
        Pageable pageable
    );
    
    // Tìm kiếm với JOIN FETCH cho export (không phân trang)
    @Query("SELECT DISTINCT sv FROM SinhVien sv " +
           "LEFT JOIN FETCH sv.lop " +
           "LEFT JOIN FETCH sv.khoa " +
           "WHERE (:keyword IS NULL OR :keyword = '' OR " +
           "  sv.maSV LIKE CONCAT('%', :keyword, '%') OR " +
           "  LOWER(sv.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "  LOWER(sv.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:idLop IS NULL OR sv.idLop = :idLop) AND " +
           "(:idKhoa IS NULL OR sv.idKhoa = :idKhoa) AND " +
           "(:gioiTinh IS NULL OR :gioiTinh = '' OR sv.gioiTinh = :gioiTinh)")
    List<SinhVien> searchByKeywordWithRelationships(
        @Param("keyword") String keyword,
        @Param("idLop") Long idLop,
        @Param("idKhoa") Long idKhoa,
        @Param("gioiTinh") String gioiTinh
    );
    
    // Thống kê
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.idKhoa = :idKhoa")
    Long countByKhoa(Long idKhoa);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.idLop = :idLop")
    Long countByLop(Long idLop);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.gioiTinh = :gioiTinh")
    Long countByGioiTinh(String gioiTinh);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.trangThai = :trangThai")
    Long countByTrangThai(String trangThai);
    
    @Query("SELECT AVG(sv.gpa) FROM SinhVien sv WHERE sv.idKhoa = :idKhoa")
    Double avgGpaByKhoa(Long idKhoa);
    
    @Query("SELECT AVG(sv.gpa) FROM SinhVien sv WHERE sv.idLop = :idLop")
    Double avgGpaByLop(Long idLop);
    
    // Tìm sinh viên sinh nhật trong khoảng
    List<SinhVien> findByNgaySinhBetween(LocalDate startDate, LocalDate endDate);
    
    // Fetch tất cả với relationships cho export
    @Query("SELECT sv FROM SinhVien sv LEFT JOIN FETCH sv.lop LEFT JOIN FETCH sv.khoa")
    List<SinhVien> findAllWithRelationships();
    
    boolean existsByMaSV(String maSV);
    
    boolean existsByEmail(String email);
}
