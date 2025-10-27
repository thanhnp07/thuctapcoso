package com.qlsv.service;

import com.qlsv.entity.Khoa;
import com.qlsv.entity.Lop;
import com.qlsv.entity.SinhVien;
import com.qlsv.repository.KhoaRepository;
import com.qlsv.repository.LopRepository;
import com.qlsv.repository.SinhVienRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelExportService {
    
    private final SinhVienRepository sinhVienRepository;
    private final LopRepository lopRepository;
    private final KhoaRepository khoaRepository;
    
    public byte[] exportSinhVienToExcel(List<SinhVien> sinhVienList) throws IOException {
        log.info("Xuất {} sinh viên ra Excel", sinhVienList.size());
        
        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Danh sách sinh viên");
            
            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"STT", "Mã SV", "Họ tên", "Ngày sinh", "Giới tính", 
                              "Email", "SĐT", "Lớp", "Khoa", "GPA", "Trạng thái"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            int rowNum = 1;
            
            for (SinhVien sv : sinhVienList) {
                Row row = sheet.createRow(rowNum++);
                
                // Lookup related entities manually
                Lop lop = lopRepository.findById(sv.getMaLop()).orElse(null);
                Khoa khoa = khoaRepository.findById(sv.getMaKhoa()).orElse(null);
                
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(sv.getMaSV());
                row.createCell(2).setCellValue(sv.getHoTen());
                row.createCell(3).setCellValue(sv.getNgaySinh().format(dateFormatter));
                row.createCell(4).setCellValue(sv.getGioiTinh());
                row.createCell(5).setCellValue(sv.getEmail() != null ? sv.getEmail() : "");
                row.createCell(6).setCellValue(sv.getSoDienThoai() != null ? sv.getSoDienThoai() : "");
                row.createCell(7).setCellValue(lop != null ? lop.getTenLop() : "");
                row.createCell(8).setCellValue(khoa != null ? khoa.getTenKhoa() : "");
                row.createCell(9).setCellValue(sv.getGpa() != null ? sv.getGpa() : 0.0);
                row.createCell(10).setCellValue(sv.getTrangThai());
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            log.info("Xuất Excel thành công");
            return out.toByteArray();
        }
    }
    
    public byte[] exportAllSinhVienToExcel() throws IOException {
        List<SinhVien> allSinhVien = sinhVienRepository.findAll();
        return exportSinhVienToExcel(allSinhVien);
    }
    
    public byte[] exportSinhVienByKhoaToExcel(String maKhoa) throws IOException {
        List<SinhVien> sinhVienList = sinhVienRepository.findByMaKhoa(maKhoa);
        return exportSinhVienToExcel(sinhVienList);
    }
    
    public byte[] exportSinhVienByLopToExcel(String maLop) throws IOException {
        List<SinhVien> sinhVienList = sinhVienRepository.findByMaLop(maLop);
        return exportSinhVienToExcel(sinhVienList);
    }
}
