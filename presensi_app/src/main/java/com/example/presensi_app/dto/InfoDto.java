package com.example.presensi_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoDto {
    private String profile;
    private String idUser;
    private String namaLengkap;
    private String tempatLahir;
    private Long tanggalLahir;
    private String email;
    private String nikUser;
    private Integer kdJabatan;
    private String namaJabatan;
    private Integer kdDepartemen;
    private String namaDepartemen;
    private Integer kdUnitKerja;
    private String namaUnitKerja;
    private Integer kdJenisKelamin;
    private String namaJenisKelamin;
    private Integer kdPendidikan;
    private String namaPendidikan;
    private String photo;

}
