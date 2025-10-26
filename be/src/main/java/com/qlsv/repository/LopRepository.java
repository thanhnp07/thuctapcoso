package com.qlsv.repository;

import com.qlsv.entity.Lop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LopRepository extends JpaRepository<Lop, String> {
    
    Optional<Lop> findByMaLop(String maLop);
    
    List<Lop> findByTenLopContainingIgnoreCase(String tenLop);
    
    List<Lop> findByKhoa_MaKhoa(String maKhoa);
    
    @Query("SELECT l FROM Lop l LEFT JOIN FETCH l.danhSachSinhVien WHERE l.maLop = :maLop")
    Optional<Lop> findByMaLopWithSinhVien(String maLop);
    
    @Query("SELECT l FROM Lop l JOIN FETCH l.khoa WHERE l.maLop = :maLop")
    Optional<Lop> findByMaLopWithKhoa(String maLop);
    
    boolean existsByMaLop(String maLop);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.lop.maLop = :maLop")
    Long countSinhVienByMaLop(String maLop);
}
