package com.example.presensi_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.presensi_app.dto.GenericResponse;
import com.example.presensi_app.dto.InfoDto;
import com.example.presensi_app.dto.PageResponse;
import com.example.presensi_app.dto.pegawai.PegawaiRequest;
import com.example.presensi_app.services.pegawai.PegawaiService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pegawai")
@Slf4j
public class PegawaiController {
    @Autowired
    private PegawaiService pegawaiService;

    @PostMapping("/admin-tambah-pegawai")
    public ResponseEntity<?> createPegawai(@RequestBody PegawaiRequest request) {
        try {
            pegawaiService.createPegawai(request);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Berhasil Menambah Pegawai"));
        } catch (ResponseStatusException e) {
            log.info(" gagal: {}", e.getMessage());
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            log.error("Error saat ini: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/admin-ubah-pegawai")
    public ResponseEntity<?> editPegawai(@RequestParam("idUser") String idUser, @RequestBody PegawaiRequest request) {
        try {
            pegawaiService.editPegawai(request, idUser);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Berhasil Mengubah Data Pegawai"));
        } catch (ResponseStatusException e) {
            log.info(" gagal: {}", e.getMessage());
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            log.error("Error saat ini: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/daftar")
    public ResponseEntity<?> getAllPegawai(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            PageResponse<InfoDto> response = pegawaiService.getAllPegawai(page, size);
            return ResponseEntity.ok(GenericResponse.success(response, "Berhasil mengambil semua pegawai."));
        } catch (Exception e) {
            log.error("Gagal mengambil data pegawai: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping(value = "/admin-ubah-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ubahPhoto(
            @RequestParam("idUser") String idUser,
            @RequestParam("namaFile") String namaFile,
            @RequestPart("files") MultipartFile fileImage) {

        try {
            pegawaiService.adminUploadImage(idUser, fileImage, namaFile);
            return ResponseEntity.ok(GenericResponse.success(null, "Photo berhasil diubah"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            log.error("Gagal mengambil data pegawai: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping(value = "/ubah-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ubahPhoto(
            @RequestParam("namaFile") String namaFile,
            @RequestPart("files") MultipartFile fileImage) {

        try {
            pegawaiService.pegawaiUploadImage(fileImage, namaFile);
            return ResponseEntity.ok(GenericResponse.success(null, "Photo berhasil diubah"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

}
