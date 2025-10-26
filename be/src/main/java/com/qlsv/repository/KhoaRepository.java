package com.qlsv.repository;

import com.qlsv.entity.Khoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhoaRepository extends JpaRepository<Khoa, String> {
    
    Optional<Khoa> findByMaKhoa(String maKhoa);
    
    List<Khoa> findByTenKhoaContainingIgnoreCase(String tenKhoa);
    
    @Query("SELECT k FROM Khoa k LEFT JOIN FETCH k.danhSachLop WHERE k.maKhoa = :maKhoa")
    Optional<Khoa> findByMaKhoaWithLop(String maKhoa);
    
    @Query("SELECT k FROM Khoa k LEFT JOIN FETCH k.danhSachSinhVien WHERE k.maKhoa = :maKhoa")
    Optional<Khoa> findByMaKhoaWithSinhVien(String maKhoa);
    
    boolean existsByMaKhoa(String maKhoa);
}
