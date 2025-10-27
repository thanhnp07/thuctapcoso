package com.qlsv.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienDTO {
    
    private String maSV;
    
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không quá 100 ký tự")
    private String hoTen;
    
    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate ngaySinh;
    
    @Pattern(regexp = "Nam|Nữ|Khác", message = "Giới tính phải là: Nam, Nữ, hoặc Khác")
    private String gioiTinh;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải có 10-11 chữ số")
    private String soDienThoai;
    
    private String diaChi;
    
    @DecimalMin(value = "0.0", message = "GPA phải >= 0")
    @DecimalMax(value = "4.0", message = "GPA phải <= 4.0")
    private Double gpa;
    
    private String trangThai;
    
    @NotBlank(message = "Mã lớp không được để trống")
    private String maLop;
    
    private String tenLop;
    
    @NotBlank(message = "Mã khoa không được để trống")
    private String maKhoa;
    
    private String tenKhoa;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
