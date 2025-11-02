package com.example.presensi_app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.presensi_app.dto.ComboDto;
import com.example.presensi_app.dto.GenericResponse;
import com.example.presensi_app.dto.PageResponse;
import com.example.presensi_app.dto.presensi.AbsenIzinRequest;
import com.example.presensi_app.dto.presensi.PresensiPegawaiAdminDto;
import com.example.presensi_app.services.presensi.PresensiService;

@RestController
@RequestMapping("/api/presensi")
public class PresensiController {

    @Autowired
    private PresensiService presensiService;

    @GetMapping("/combo/status-absen")
    public ResponseEntity<?> getStatusAbsenCombo(
            @RequestParam(required = false) Long tglAwal,
            @RequestParam(required = false) Long tglAkhir) {
        try {
            List<ComboDto> data = presensiService.getStatusAbsenCombo(tglAwal, tglAkhir);
            return ResponseEntity.ok(GenericResponse.success(data, "Berhasil mengambil data status absen"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/in")
    public ResponseEntity<?> checkIn() {
        try {
            Map<String, String> data = presensiService.checkIn();
            return ResponseEntity.ok(GenericResponse.success(data, "Check-in berhasil"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/out")
    public ResponseEntity<?> checkOut() {
        try {
            Map<String, String> data = presensiService.checkOut();
            return ResponseEntity.ok(GenericResponse.success(data, "Check-out berhasil"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/abseni")
    public ResponseEntity<?> absenIzin(@RequestBody AbsenIzinRequest request) {
        try {
            presensiService.absenIzin(request);
            return ResponseEntity.ok(GenericResponse.success(null, "Absensi izin berhasil disimpan!"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/daftar/pegawai")
    public ResponseEntity<?> getDaftarPresensiPegawai(
            @RequestParam(required = false) Long tglAwal,
            @RequestParam(required = false) Long tglAkhir) {
        try {
            return ResponseEntity
                    .ok(GenericResponse.success(presensiService.getDaftarPresensiPegawai(tglAwal, tglAkhir),
                            "Berhasil mengambil daftar presensi pegawai"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/daftar/admin")
    public ResponseEntity<?> getAllPegawai(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long tglAwal,
            @RequestParam(required = false) Long tglAkhir) {

        try {
            PageResponse<PresensiPegawaiAdminDto> response = presensiService.getDaftarPresensiPegawaiAdmin(tglAwal, tglAkhir, page, size);
            return ResponseEntity.ok(GenericResponse.success(response, "Berhasil mengambil semua pegawai."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

}
