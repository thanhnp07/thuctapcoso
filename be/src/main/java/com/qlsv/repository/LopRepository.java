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
    
    List<Lop> findByMaKhoa(String maKhoa);
    
    boolean existsByMaLop(String maLop);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.maLop = :maLop")
    Long countSinhVienByMaLop(String maLop);
}
