package com.qlsv.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "khoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Khoa {
    
    @Id
    @Column(name = "ma_khoa", length = 20)
    @NotBlank(message = "Mã khoa không được để trống")
    @Size(max = 20, message = "Mã khoa không quá 20 ký tự")
    private String maKhoa;
    
    @Column(name = "ten_khoa", nullable = false, length = 200)
    @NotBlank(message = "Tên khoa không được để trống")
    @Size(max = 200, message = "Tên khoa không quá 200 ký tự")
    private String tenKhoa;
    
    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
