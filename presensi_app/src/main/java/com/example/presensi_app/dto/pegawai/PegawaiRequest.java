package com.example.presensi_app.dto.pegawai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PegawaiRequest {
    private String namaLengkap;
    private String email;
    private String tempatLahir;
    private Long tanggalLahir;
    private int kdJenisKelamin;
    private int kdPendidikan;
    private int kdJabatan;
    private int kdDepartemen;
    private Integer kdUnitKerja;
    private String password;
    private String passwordC;
}
