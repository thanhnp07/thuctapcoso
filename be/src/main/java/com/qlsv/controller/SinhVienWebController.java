package com.qlsv.controller;

import com.qlsv.dto.SinhVienDTO;
import com.qlsv.entity.BaoCao;
import com.qlsv.entity.SinhVien;
import com.qlsv.exception.ResourceNotFoundException;
import com.qlsv.repository.BaoCaoRepository;
import com.qlsv.repository.SinhVienRepository;
import com.qlsv.service.ExcelExportService;
import com.qlsv.service.KhoaService;
import com.qlsv.service.LopService;
import com.qlsv.service.PdfExportService;
import com.qlsv.service.SinhVienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sinhvien")
@RequiredArgsConstructor
@Slf4j
public class SinhVienWebController {
    
    private final SinhVienService sinhVienService;
    private final KhoaService khoaService;
    private final LopService lopService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;
    private final SinhVienRepository sinhVienRepository;
    private final BaoCaoRepository baoCaoRepository;
    
    @GetMapping
    public String listSinhVien(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long idKhoa,
            @RequestParam(required = false) Long idLop,
            @RequestParam(required = false) String gioiTinh,
            Model model) {
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("maSV").ascending());
            Page<SinhVienDTO> sinhVienPage = sinhVienService.searchSinhVien(keyword, idKhoa, idLop, gioiTinh, pageable);
            
            model.addAttribute("sinhVienPage", sinhVienPage);
            model.addAttribute("keyword", keyword);
            model.addAttribute("idKhoa", idKhoa);
            model.addAttribute("idLop", idLop);
            model.addAttribute("gioiTinh", gioiTinh);
            
            // Load danh sách khoa và lớp cho filter
            model.addAttribute("danhSachKhoa", khoaService.getAllKhoa());
            model.addAttribute("danhSachLop", lopService.getAllLop());
            
            return "sinhvien/list";
        } catch (Exception e) {
            log.error("Lỗi khi tải danh sách sinh viên", e);
            model.addAttribute("error", "Không thể tải danh sách sinh viên");
            return "error";
        }
    }
    
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("sinhVien", new SinhVienDTO());
        model.addAttribute("danhSachKhoa", khoaService.getAllKhoa());
        model.addAttribute("danhSachLop", lopService.getAllLop());
        return "sinhvien/form";
    }
    
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable("id") String maSV, Model model, RedirectAttributes redirectAttributes) {
        try {
            SinhVienDTO sinhVien = sinhVienService.getSinhVienById(maSV);
            model.addAttribute("sinhVien", sinhVien);
            model.addAttribute("danhSachKhoa", khoaService.getAllKhoa());
            model.addAttribute("danhSachLop", lopService.getAllLop());
            return "sinhvien/form";
        } catch (Exception e) {
            log.error("Không tìm thấy sinh viên: " + maSV, e);
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sinh viên");
            return "redirect:/sinhvien";
        }
    }
    
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveSinhVien(@Valid @ModelAttribute SinhVienDTO sinhVienDTO, RedirectAttributes redirectAttributes) {
        try {
            // Check nếu đang edit (maSV đã tồn tại trong DB)
            if (sinhVienDTO.getMaSV() != null && !sinhVienDTO.getMaSV().isEmpty()) {
                try {
                    // Thử get - nếu tồn tại thì update
                    sinhVienService.getSinhVienById(sinhVienDTO.getMaSV());
                    sinhVienService.updateSinhVien(sinhVienDTO.getMaSV(), sinhVienDTO);
                    redirectAttributes.addFlashAttribute("success", "Cập nhật sinh viên thành công");
                } catch (ResourceNotFoundException e) {
                    // Mã SV chưa tồn tại - tạo mới
                    sinhVienService.createSinhVien(sinhVienDTO);
                    redirectAttributes.addFlashAttribute("success", "Thêm sinh viên thành công");
                }
            } else {
                // Create mới
                sinhVienService.createSinhVien(sinhVienDTO);
                redirectAttributes.addFlashAttribute("success", "Thêm sinh viên thành công");
            }
            return "redirect:/sinhvien";
        } catch (Exception e) {
            log.error("Lỗi khi lưu sinh viên", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/sinhvien/add";
        }
    }
    
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSinhVien(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            sinhVienService.deleteSinhVien(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sinh viên thành công");
        } catch (Exception e) {
            log.error("Lỗi khi xóa sinh viên", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/sinhvien";
    }
    
    @GetMapping("/view/{id}")
    public String viewSinhVien(@PathVariable("id") String maSV, Model model) {
        try {
            SinhVienDTO sinhVien = sinhVienService.getSinhVienById(maSV);
            model.addAttribute("sinhVien", sinhVien);
            return "sinhvien/view";
        } catch (Exception e) {
            log.error("Không tìm thấy sinh viên: " + maSV, e);
            return "redirect:/sinhvien?error=notfound";
        }
    }
    
    @GetMapping("/export/excel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long idKhoa,
            @RequestParam(required = false) Long idLop,
            @RequestParam(required = false) String gioiTinh,
            Authentication authentication) {
        try {
            List<SinhVien> sinhVienList;
            String moTa;
            String tieuChiLoc = "";
            
            // Kiểm tra có filter không
            if (keyword != null || idKhoa != null || idLop != null || gioiTinh != null) {
                // Export theo filter - dùng query với JOIN FETCH
                sinhVienList = sinhVienRepository.searchByKeywordWithRelationships(keyword, idLop, idKhoa, gioiTinh);
                
                // Tạo mô tả và tiêu chí lọc
                StringBuilder criteria = new StringBuilder();
                if (keyword != null) criteria.append("Từ khóa: ").append(keyword).append("; ");
                if (idKhoa != null) criteria.append("Khoa ID: ").append(idKhoa).append("; ");
                if (idLop != null) criteria.append("Lớp ID: ").append(idLop).append("; ");
                if (gioiTinh != null) criteria.append("Giới tính: ").append(gioiTinh).append("; ");
                
                tieuChiLoc = criteria.toString();
                moTa = "Xuất danh sách sinh viên theo bộ lọc (" + sinhVienList.size() + " sinh viên) - " + tieuChiLoc;
            } else {
                // Export toàn bộ
                sinhVienList = sinhVienRepository.findAllWithRelationships();
                moTa = "Xuất toàn bộ danh sách sinh viên (" + sinhVienList.size() + " sinh viên)";
            }
            
            byte[] excelData = excelExportService.exportSinhVienToExcel(sinhVienList);
            
            String filename = "DanhSachSinhVien_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            
            // Lưu báo cáo vào database
            BaoCao baoCao = BaoCao.builder()
                .loai("DANH_SACH_SINH_VIEN")
                .dinhDang("EXCEL")
                .tenFile(filename)
                .moTa(moTa)
                .tieuChiLoc(tieuChiLoc.isEmpty() ? null : tieuChiLoc)
                .maSV(authentication != null ? authentication.getName() : "system")
                .build();
            baoCaoRepository.save(baoCao);
            
            log.info("Đã lưu báo cáo Excel: {}", filename);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelData);
        } catch (Exception e) {
            log.error("Lỗi khi xuất Excel", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/export/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long idKhoa,
            @RequestParam(required = false) Long idLop,
            @RequestParam(required = false) String gioiTinh,
            Authentication authentication) {
        try {
            List<SinhVien> sinhVienList;
            String moTa;
            String tieuChiLoc = "";
            
            // Kiểm tra có filter không
            if (keyword != null || idKhoa != null || idLop != null || gioiTinh != null) {
                // Export theo filter - dùng query với JOIN FETCH
                sinhVienList = sinhVienRepository.searchByKeywordWithRelationships(keyword, idLop, idKhoa, gioiTinh);
                
                // Tạo mô tả và tiêu chí lọc
                StringBuilder criteria = new StringBuilder();
                if (keyword != null) criteria.append("Từ khóa: ").append(keyword).append("; ");
                if (idKhoa != null) criteria.append("Khoa ID: ").append(idKhoa).append("; ");
                if (idLop != null) criteria.append("Lớp ID: ").append(idLop).append("; ");
                if (gioiTinh != null) criteria.append("Giới tính: ").append(gioiTinh).append("; ");
                
                tieuChiLoc = criteria.toString();
                moTa = "Xuất danh sách sinh viên theo bộ lọc (" + sinhVienList.size() + " sinh viên) - " + tieuChiLoc;
            } else {
                // Export toàn bộ
                sinhVienList = sinhVienRepository.findAllWithRelationships();
                moTa = "Xuất toàn bộ danh sách sinh viên (" + sinhVienList.size() + " sinh viên)";
            }
            
            byte[] pdfData = pdfExportService.exportSinhVienToPdf(sinhVienList);
            
            String filename = "DanhSachSinhVien_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
            // Lưu báo cáo vào database
            BaoCao baoCao = BaoCao.builder()
                .loai("DANH_SACH_SINH_VIEN")
                .dinhDang("PDF")
                .tenFile(filename)
                .moTa(moTa)
                .tieuChiLoc(tieuChiLoc.isEmpty() ? null : tieuChiLoc)
                .maSV(authentication != null ? authentication.getName() : "system")
                .build();
            baoCaoRepository.save(baoCao);
            
            log.info("Đã lưu báo cáo PDF: {}", filename);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
        } catch (Exception e) {
            log.error("Lỗi khi xuất PDF", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
