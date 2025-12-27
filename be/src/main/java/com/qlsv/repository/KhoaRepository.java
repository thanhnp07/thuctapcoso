package com.qlsv.repository;

import com.qlsv.entity.Khoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhoaRepository extends JpaRepository<Khoa, Long> {
    
    Optional<Khoa> findByMaKhoa(String maKhoa);
    
    List<Khoa> findByTenKhoaContainingIgnoreCase(String tenKhoa);
    
    boolean existsByMaKhoa(String maKhoa);
}
