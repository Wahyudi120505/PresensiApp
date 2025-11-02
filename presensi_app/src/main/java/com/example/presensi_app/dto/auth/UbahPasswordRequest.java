package com.example.presensi_app.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UbahPasswordRequest {
    private String passwordAsli;
    private String passwordBaru1;
    private String passwordBaru2;
}
