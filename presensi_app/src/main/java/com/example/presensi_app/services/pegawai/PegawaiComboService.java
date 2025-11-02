package com.example.presensi_app.services.pegawai;

import java.util.List;

import com.example.presensi_app.dto.ComboDto;
import com.example.presensi_app.dto.pegawai.ComboHrdDto;

public interface PegawaiComboService {
    List<ComboDto> getJabatanCombo();

    List<ComboDto> getDepartemenCombo();

    List<ComboDto> getUnitKerjaCombo();

    List<ComboDto> getPendidikanCombo();

    List<ComboDto> getJenisKelaminCombo();

    List<ComboHrdDto> getDepartemenHrdCombo();
    
}
