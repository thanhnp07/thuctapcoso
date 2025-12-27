package com.qlsv.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.qlsv.entity.Khoa;
import com.qlsv.entity.Lop;
import com.qlsv.entity.SinhVien;
import com.qlsv.repository.KhoaRepository;
import com.qlsv.repository.LopRepository;
import com.qlsv.repository.SinhVienRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfExportService {
    
    private final SinhVienRepository sinhVienRepository;
    private final LopRepository lopRepository;
    private final KhoaRepository khoaRepository;
    
    public byte[] exportSinhVienToPdf(List<SinhVien> sinhVienList) throws DocumentException, IOException {
        log.info("Xuất {} sinh viên ra PDF", sinhVienList.size());
        
        Document document = new Document(PageSize.A4.rotate()); // Landscape
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            
            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("DANH SACH SINH VIEN", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Table
            PdfPTable table = new PdfPTable(11); // 11 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            float[] columnWidths = {1f, 2f, 3f, 2f, 1.5f, 3f, 2f, 2f, 2.5f, 1.5f, 2f};
            table.setWidths(columnWidths);
            
            // Header
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            String[] headers = {"STT", "Ma SV", "Ho ten", "Ngay sinh", "Gioi tinh", 
                              "Email", "SDT", "Lop", "Khoa", "GPA", "Trang thai"};
            
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setPadding(5);
                table.addCell(cell);
            }
            
            // Data
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 9);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            int stt = 1;
            
            for (SinhVien sv : sinhVienList) {
                Lop lop = sv.getLop();
                Khoa khoa = sv.getKhoa();
                
                table.addCell(new Phrase(String.valueOf(stt++), dataFont));
                table.addCell(new Phrase(sv.getMaSV(), dataFont));
                table.addCell(new Phrase(sv.getHoTen(), dataFont));
                table.addCell(new Phrase(sv.getNgaySinh().format(dateFormatter), dataFont));
                table.addCell(new Phrase(sv.getGioiTinh(), dataFont));
                table.addCell(new Phrase(sv.getEmail() != null ? sv.getEmail() : "", dataFont));
                table.addCell(new Phrase(sv.getSoDienThoai() != null ? sv.getSoDienThoai() : "", dataFont));
                table.addCell(new Phrase(lop != null ? lop.getTenLop() : "", dataFont));
                table.addCell(new Phrase(khoa != null ? khoa.getTenKhoa() : "", dataFont));
                table.addCell(new Phrase(sv.getGpa() != null ? String.format("%.2f", sv.getGpa()) : "", dataFont));
                table.addCell(new Phrase(sv.getTrangThai(), dataFont));
            }
            
            document.add(table);
            
            // Footer
            Paragraph footer = new Paragraph("Tong so sinh vien: " + sinhVienList.size(), 
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(10);
            document.add(footer);
            
            log.info("Xuất PDF thành công");
            
        } finally {
            document.close();
        }
        
        return out.toByteArray();
    }
    
    public byte[] exportAllSinhVienToPdf() throws DocumentException, IOException {
        List<SinhVien> allSinhVien = sinhVienRepository.findAll();
        return exportSinhVienToPdf(allSinhVien);
    }
    
    public byte[] exportSinhVienByKhoaToPdf(Long idKhoa) throws DocumentException, IOException {
        List<SinhVien> sinhVienList = sinhVienRepository.findByIdKhoa(idKhoa);
        return exportSinhVienToPdf(sinhVienList);
    }
    
    public byte[] exportSinhVienByLopToPdf(Long idLop) throws DocumentException, IOException {
        List<SinhVien> sinhVienList = sinhVienRepository.findByIdLop(idLop);
        return exportSinhVienToPdf(sinhVienList);
    }
}
