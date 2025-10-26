package com.qlsv.repository;

import com.qlsv.entity.BaoCao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BaoCaoRepository extends JpaRepository<BaoCao, Long> {
    
    List<BaoCao> findByLoai(String loai);
    
    List<BaoCao> findByDinhDang(String dinhDang);
    
    List<BaoCao> findBySinhVien_MaSV(String maSV);
    
    List<BaoCao> findByNgayLapBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT bc FROM BaoCao bc ORDER BY bc.ngayLap DESC")
    List<BaoCao> findAllOrderByNgayLapDesc();
    
    @Query("SELECT COUNT(bc) FROM BaoCao bc WHERE bc.loai = :loai")
    Long countByLoai(String loai);
}
