package com.qlsv.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sinh_vien", indexes = {
    @Index(name = "idx_ho_ten", columnList = "ho_ten"),
    @Index(name = "idx_ma_lop", columnList = "ma_lop"),
    @Index(name = "idx_ma_khoa", columnList = "ma_khoa")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SinhVien {
    
    @Id
    @Column(name = "ma_sv", length = 20)
    @NotBlank(message = "Mã sinh viên không được để trống")
    @Size(max = 20, message = "Mã sinh viên không quá 20 ký tự")
    private String maSV;
    
    @Column(name = "ho_ten", nullable = false, length = 100)
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không quá 100 ký tự")
    private String hoTen;
    
    @Column(name = "ngay_sinh", nullable = false)
    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate ngaySinh;
    
    @Column(name = "gioi_tinh", length = 10)
    @Pattern(regexp = "Nam|Nữ|Khác", message = "Giới tính phải là: Nam, Nữ, hoặc Khác")
    private String gioiTinh;
    
    @Column(name = "email", length = 100)
    @Email(message = "Email không hợp lệ")
    private String email;
    
    @Column(name = "so_dien_thoai", length = 15)
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải có 10-11 chữ số")
    private String soDienThoai;
    
    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String diaChi;
    
    @Column(name = "gpa")
    @DecimalMin(value = "0.0", message = "GPA phải >= 0")
    @DecimalMax(value = "4.0", message = "GPA phải <= 4.0")
    private Double gpa;
    
    @Column(name = "trang_thai", length = 20)
    @Builder.Default
    private String trangThai = "DANG_HOC"; // DANG_HOC, TAM_NGUNG, DA_TOT_NGHIEP
    
    @Column(name = "ma_lop", length = 20, nullable = false)
    @NotBlank(message = "Mã lớp không được để trống")
    private String maLop;
    
    @Column(name = "ma_khoa", length = 20, nullable = false)
    @NotBlank(message = "Mã khoa không được để trống")
    private String maKhoa;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
