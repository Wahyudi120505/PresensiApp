package com.example.presensi_app.dto.presensi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresensiPegawaiAdminDto {
    private String idUser;  
    private String namaLengkap;  
    private Long tglAbsensi;  
    private String jamMasuk;
    private String jamKeluar;
    private String namaStatus;
}
