package com.example.presensi_app.dto.presensi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbsenIzinRequest {
    private Long tglAbsensi;
    private Integer kodeStatus;
}
