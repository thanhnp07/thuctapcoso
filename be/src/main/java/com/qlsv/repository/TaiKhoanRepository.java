package com.qlsv.repository;

import com.qlsv.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {
    
    Optional<TaiKhoan> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    List<TaiKhoan> findByVaiTro(String vaiTro);
    
    List<TaiKhoan> findByIsActive(Boolean isActive);
    
    Optional<TaiKhoan> findByMaSV(String maSV);
}
