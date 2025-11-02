package com.example.presensi_app.dto.pegawai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboHrdDto {
    private String namaLengkap;
    private Integer kdJabatan;
    private String namaJabatan;
}