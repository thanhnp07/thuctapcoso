package com.qlsv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopDTO {
    
    private Long id;
    
    private String maLop;
    
    @NotBlank(message = "Tên lớp không được để trống")
    @Size(max = 200, message = "Tên lớp không quá 200 ký tự")
    private String tenLop;
    
    private String khoaHoc;
    
    private Integer siSo;
    
    @NotNull(message = "Khoa không được để trống")
    private Long idKhoa;
    
    private String maKhoa;
    
    private String tenKhoa;
    
    private Long soLuongSinhVien;
}
