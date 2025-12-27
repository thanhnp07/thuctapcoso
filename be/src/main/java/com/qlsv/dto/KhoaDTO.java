package com.qlsv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhoaDTO {
    
    private Long id;
    
    private String maKhoa;
    
    @NotBlank(message = "Tên khoa không được để trống")
    @Size(max = 200, message = "Tên khoa không quá 200 ký tự")
    private String tenKhoa;
    
    private String moTa;
    
    private Long soLuongLop;
    
    private Long soLuongSinhVien;
}
