package com.example.presensi_app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JenisKelaminEnum {
    LAKI_LAKI(1, "Laki-laki"),
    PEREMPUAN(2, "Perempuan");

    private final int kode;
    private final String nama;

    public static JenisKelaminEnum fromKode(int kode) {
        for (JenisKelaminEnum jk : values()) {
            if (jk.getKode() == kode)
                return jk;
        }
        return null;
    }

}
