package com.example.presensi_app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusAbsensiEnum {
    HADIR(1, "Hadir"),
    IZIN(2, "Izin"),
    SAKIT(3, "Sakit"),
    ALFA(4, "Alfa");

    private final int kode;
    private final String nama;

    public static StatusAbsensiEnum fromKode(int kode) {
        for (StatusAbsensiEnum jk : values()) {
            if (jk.getKode() == kode)
                return jk;
        }
        return null;
    }
}
