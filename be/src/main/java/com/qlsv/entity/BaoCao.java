package com.qlsv.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "bao_cao", indexes = {
    @Index(name = "idx_ngay_lap", columnList = "ngay_lap")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class BaoCao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_bc")
    private Long maBaoCao;
    
    @Column(name = "loai", nullable = false, length = 50)
    @NotBlank(message = "Loại báo cáo không được để trống")
    private String loai; // "DANH_SACH_SINH_VIEN", "THONG_KE_THEO_KHOA", etc.
    
    @Column(name = "ngay_lap", nullable = false)
    @CreatedDate
    private LocalDateTime ngayLap;
    
    @Column(name = "dinh_dang", nullable = false, length = 10)
    @Pattern(regexp = "EXCEL|PDF", message = "Định dạng phải là EXCEL hoặc PDF")
    private String dinhDang;
    
    @Column(name = "ten_file", length = 200)
    private String tenFile;
    
    @Column(name = "duong_dan_file", columnDefinition = "TEXT")
    private String duongDanFile;
    
    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
    
    @Column(name = "tieu_chi_loc", columnDefinition = "TEXT")
    private String tieuChiLoc; // JSON string chứa các tiêu chí lọc
    
    @Column(name = "ma_sv", length = 20)
    private String maSV; // Người tạo báo cáo
}
