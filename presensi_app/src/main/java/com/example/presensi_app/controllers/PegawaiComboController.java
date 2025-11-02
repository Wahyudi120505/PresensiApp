package com.example.presensi_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.presensi_app.dto.ComboDto;
import com.example.presensi_app.dto.GenericResponse;
import com.example.presensi_app.dto.pegawai.ComboHrdDto;
import com.example.presensi_app.services.pegawai.PegawaiComboService;

import java.util.List;

@RestController
@RequestMapping("/api/pegawai/combo")
public class PegawaiComboController {

    @Autowired
    private PegawaiComboService comboService;

    @GetMapping("/jabatan")
    public ResponseEntity<?> getJabatanCombo() {
        try {
            List<ComboDto> data = comboService.getJabatanCombo();
            return ResponseEntity.ok(GenericResponse.success(data, "Berhasil mengambil data jabatan"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/departemen")
    public ResponseEntity<?> getDepartemenCombo() {
        try {
            List<ComboDto> data = comboService.getDepartemenCombo();
            return ResponseEntity.ok(GenericResponse.success(data, "Berhasil mengambil data departemen"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/unit-kerja")
    public ResponseEntity<?> getUnitKerjaCombo() {
        try {
            List<ComboDto> data = comboService.getUnitKerjaCombo();
            return ResponseEntity.ok(GenericResponse.success(data, "Berhasil mengambil data unit kerja"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/pendidikan")
    public ResponseEntity<?> getPendidikanCombo() {
        try {
            List<ComboDto> data = comboService.getPendidikanCombo();
            return ResponseEntity.ok(GenericResponse.success(data, "Berhasil mengambil data pendidikan"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/jenis-kelamin")
    public ResponseEntity<?> getJenisKelaminCombo() {
        try {
            List<ComboDto> data = comboService.getJenisKelaminCombo();
            return ResponseEntity.ok(GenericResponse.success(data, "Berhasil mengambil data jenis kelamin"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/departemen-hrd")
    public ResponseEntity<?> getDepartemenHrdCombo() {
        try {
            List<ComboHrdDto> data = comboService.getDepartemenHrdCombo();
            return ResponseEntity.ok(GenericResponse.success(data, "Berhasil mengambil data pegawai HRD"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }
}
