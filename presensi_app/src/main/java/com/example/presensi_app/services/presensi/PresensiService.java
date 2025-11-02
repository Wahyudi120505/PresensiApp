package com.example.presensi_app.services.presensi;

import java.util.List;
import java.util.Map;

import com.example.presensi_app.dto.ComboDto;
import com.example.presensi_app.dto.PageResponse;
import com.example.presensi_app.dto.presensi.AbsenIzinRequest;
import com.example.presensi_app.dto.presensi.PresensiPegawaiAdminDto;
import com.example.presensi_app.dto.presensi.PresensiPegawaiDto;

public interface PresensiService {
    List<ComboDto> getStatusAbsenCombo(Long tglAwal, Long tglAkhir);

    Map<String, String> checkIn();

    Map<String, String> checkOut();

    void absenIzin(AbsenIzinRequest request);

    List<PresensiPegawaiDto> getDaftarPresensiPegawai(Long tglAwal, Long tglAkhir);

    PageResponse<PresensiPegawaiAdminDto> getDaftarPresensiPegawaiAdmin(Long tglAwal, Long tglAkhir, int page,
            int size);
}
