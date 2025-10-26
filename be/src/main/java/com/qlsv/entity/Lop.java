package com.qlsv.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lop")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Lop {
    
    @Id
    @Column(name = "ma_lop", length = 20)
    @NotBlank(message = "Mã lớp không được để trống")
    @Size(max = 20, message = "Mã lớp không quá 20 ký tự")
    private String maLop;
    
    @Column(name = "ten_lop", nullable = false, length = 200)
    @NotBlank(message = "Tên lớp không được để trống")
    @Size(max = 200, message = "Tên lớp không quá 200 ký tự")
    private String tenLop;
    
    @Column(name = "khoa_hoc", length = 20)
    private String khoaHoc; // Ví dụ: "2020-2024"
    
    @Column(name = "si_so")
    private Integer siSo;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_khoa", nullable = false)
    private Khoa khoa;
    
    @OneToMany(mappedBy = "lop", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<SinhVien> danhSachSinhVien = new HashSet<>();
}
