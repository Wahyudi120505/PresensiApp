package com.example.presensi_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.presensi_app.dto.GenericResponse;
import com.example.presensi_app.dto.auth.InitDataRequest;
import com.example.presensi_app.dto.auth.LoginRequest;
import com.example.presensi_app.dto.auth.UbahPasswordRequest;
import com.example.presensi_app.services.auth.AuthService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/init-data")
    public ResponseEntity<?> initData(@RequestBody InitDataRequest request) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(authService.initData(request), "Berhasil"));
        } catch (ResponseStatusException e) {
            log.info(" gagal menginit data: {}", e.getMessage());
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            log.error("Error saat init data: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(authService.login(request), "Login berhasil"));
        } catch (ResponseStatusException e) {
            log.info("Login gagal: {}", e.getMessage());
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            log.error("Error saat ini: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/ubah-password-sendiri")
    public ResponseEntity<?> ubahPasswordSendiri(
            @RequestBody UbahPasswordRequest request) {

        try {
            authService.ubahPassword(request);
            return ResponseEntity.ok(GenericResponse.success(null, "Password berhasil diubah"));
        } catch (ResponseStatusException e) {
            log.info("Ubah password gagal: {}", e.getMessage());
            return ResponseEntity.status(501).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            log.error("Error saat ubah password: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

}
