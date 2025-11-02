package com.example.presensi_app.services.pegawai;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.presensi_app.dto.ComboDto;
import com.example.presensi_app.dto.pegawai.ComboHrdDto;
import com.example.presensi_app.enums.JenisKelaminEnum;
import com.example.presensi_app.enums.PendidikanEnum;
import com.example.presensi_app.models.User;
import com.example.presensi_app.repositorys.DepartemenRepository;
import com.example.presensi_app.repositorys.JabatanRepository;
import com.example.presensi_app.repositorys.UnitKerjaRepository;
import com.example.presensi_app.repositorys.UserRepository;

@Service
public class PegawaiComboServiceImpl implements PegawaiComboService {
    @Autowired
    private DepartemenRepository departemenRepository;

    @Autowired
    private JabatanRepository jabatanRepository;

    @Autowired
    private UnitKerjaRepository unitKerjaRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ComboDto> getDepartemenCombo() {
        List<ComboDto> list = departemenRepository.findAll().stream()
                .map(d -> new ComboDto(d.getKdDepartemen(), d.getNamaDepartemen()))
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Tidak ada data departemen.");
        }

        return list;
    }

    @Override
    public List<ComboDto> getJabatanCombo() {
        List<ComboDto> list = jabatanRepository.findAll().stream()
                .map(j -> new ComboDto(j.getKdJabatan(), j.getNamaJabatan()))
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Tidak ada data jabatan.");
        }

        return list;
    }

    @Override
    public List<ComboDto> getUnitKerjaCombo() {
        List<ComboDto> list = unitKerjaRepository.findAll().stream()
                .map(u -> new ComboDto(u.getKdUnitKerja(), u.getNamaUnitKerja()))
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Tidak ada data unit kerja.");
        }

        return list;
    }

    @Override
    public List<ComboDto> getJenisKelaminCombo() {
        List<ComboDto> list = Arrays.stream(JenisKelaminEnum.values())
                .map(jk -> new ComboDto(jk.getKode(), jk.getNama()))
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Tidak ada data jenis kelamin.");
        }

        return list;
    }

    @Override
    public List<ComboDto> getPendidikanCombo() {
        List<ComboDto> list = Arrays.stream(PendidikanEnum.values())
                .map(p -> new ComboDto(p.getKode(), p.getNama()))
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Tidak ada data pendidikan.");
        }

        return list;
    }

    @Override
    public List<ComboHrdDto> getDepartemenHrdCombo() {
        List<User> hrUsers = userRepository.findAll().stream()
                .filter(u -> u.getDepartemen() != null)
                .filter(u -> "HRD".equalsIgnoreCase(u.getDepartemen().getNamaDepartemen()))
                .collect(Collectors.toList());

        if (hrUsers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "Tidak ada pegawai di departemen HRD.");
        }

        return hrUsers.stream()
                .map(u -> new ComboHrdDto(
                        u.getNamaLengkap(),
                        u.getJabatan() != null ? u.getJabatan().getKdJabatan() : null,
                        u.getJabatan() != null ? u.getJabatan().getNamaJabatan() : null))
                .collect(Collectors.toList());
    }
}
