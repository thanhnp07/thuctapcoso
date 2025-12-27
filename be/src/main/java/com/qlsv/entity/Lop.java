package com.qlsv.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "lop")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Lop {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "ma_lop", length = 20, unique = true, nullable = false)
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
    
    @Column(name = "id_khoa", nullable = false)
    @NotNull(message = "Khoa không được để trống")
    private Long idKhoa;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khoa", insertable = false, updatable = false)
    private Khoa khoa;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
