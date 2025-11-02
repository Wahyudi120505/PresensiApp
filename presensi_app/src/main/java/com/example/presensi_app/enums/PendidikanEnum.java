package com.example.presensi_app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PendidikanEnum {
    SD(1, "SD"),
    SMP(2, "SMP"),
    SMA(3, "SMA"),
    S1(4, "S1"),
    S2(5, "S2"),
    S3(6, "S3");

    private final int kode;
    private final String nama;

    public static PendidikanEnum fromKode(int kode) {
        for (PendidikanEnum jk : values()) {
            if (jk.getKode() == kode)
                return jk;
        }
        return null;
    }
}
