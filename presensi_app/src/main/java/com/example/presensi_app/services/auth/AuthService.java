package com.example.presensi_app.services.auth;

import com.example.presensi_app.dto.auth.InitDataRequest;
import com.example.presensi_app.dto.auth.InitDataResponse;
import com.example.presensi_app.dto.auth.LoginRequest;
import com.example.presensi_app.dto.auth.LoginResponse;
import com.example.presensi_app.dto.auth.UbahPasswordRequest;

public interface AuthService {
    InitDataResponse initData(InitDataRequest request);

    LoginResponse<?> login(LoginRequest request);
    
    void ubahPassword(UbahPasswordRequest request);
} 
