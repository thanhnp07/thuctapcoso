package com.qlsv.controller;

import com.qlsv.service.ExcelExportService;
import com.qlsv.service.PdfExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ExportController {
    
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;
    
    @GetMapping("/excel/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<byte[]> exportAllSinhVienToExcel() {
        try {
            log.info("Xuất toàn bộ sinh viên ra Excel");
            byte[] data = excelExportService.exportAllSinhVienToExcel();
            
            String filename = "DanhSachSinhVien_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Lỗi khi xuất Excel: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/excel/khoa/{maKhoa}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<byte[]> exportSinhVienByKhoaToExcel(@PathVariable String maKhoa) {
        try {
            log.info("Xuất sinh viên khoa {} ra Excel", maKhoa);
            byte[] data = excelExportService.exportSinhVienByKhoaToExcel(maKhoa);
            
            String filename = "DanhSachSinhVien_Khoa_" + maKhoa + "_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Lỗi khi xuất Excel: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/excel/lop/{maLop}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<byte[]> exportSinhVienByLopToExcel(@PathVariable String maLop) {
        try {
            log.info("Xuất sinh viên lớp {} ra Excel", maLop);
            byte[] data = excelExportService.exportSinhVienByLopToExcel(maLop);
            
            String filename = "DanhSachSinhVien_Lop_" + maLop + "_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Lỗi khi xuất Excel: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/pdf/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<byte[]> exportAllSinhVienToPdf() {
        try {
            log.info("Xuất toàn bộ sinh viên ra PDF");
            byte[] data = pdfExportService.exportAllSinhVienToPdf();
            
            String filename = "DanhSachSinhVien_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Lỗi khi xuất PDF: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/pdf/khoa/{maKhoa}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<byte[]> exportSinhVienByKhoaToPdf(@PathVariable String maKhoa) {
        try {
            log.info("Xuất sinh viên khoa {} ra PDF", maKhoa);
            byte[] data = pdfExportService.exportSinhVienByKhoaToPdf(maKhoa);
            
            String filename = "DanhSachSinhVien_Khoa_" + maKhoa + "_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Lỗi khi xuất PDF: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/pdf/lop/{maLop}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<byte[]> exportSinhVienByLopToPdf(@PathVariable String maLop) {
        try {
            log.info("Xuất sinh viên lớp {} ra PDF", maLop);
            byte[] data = pdfExportService.exportSinhVienByLopToPdf(maLop);
            
            String filename = "DanhSachSinhVien_Lop_" + maLop + "_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Lỗi khi xuất PDF: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
