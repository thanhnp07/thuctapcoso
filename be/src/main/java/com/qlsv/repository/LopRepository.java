package com.qlsv.repository;

import com.qlsv.entity.Lop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LopRepository extends JpaRepository<Lop, Long> {
    
    Optional<Lop> findByMaLop(String maLop);
    
    List<Lop> findByTenLopContainingIgnoreCase(String tenLop);
    
    List<Lop> findByIdKhoa(Long idKhoa);
    
    boolean existsByMaLop(String maLop);
    
    Long countByIdKhoa(Long idKhoa);
    
    @Query("SELECT COUNT(sv) FROM SinhVien sv WHERE sv.idLop = :idLop")
    Long countSinhVienByIdLop(Long idLop);
}
