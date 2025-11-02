package com.example.presensi_app.services.presensi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.presensi_app.dto.ComboDto;
import com.example.presensi_app.dto.PageResponse;
import com.example.presensi_app.dto.presensi.AbsenIzinRequest;
import com.example.presensi_app.dto.presensi.PresensiPegawaiAdminDto;
import com.example.presensi_app.dto.presensi.PresensiPegawaiDto;
import com.example.presensi_app.enums.ProfileEnum;
import com.example.presensi_app.enums.StatusAbsensiEnum;
import com.example.presensi_app.jwt.JwtUtil;
import com.example.presensi_app.models.Presensi;
import com.example.presensi_app.models.User;
import com.example.presensi_app.repositorys.PresensiRepository;
import com.example.presensi_app.repositorys.UserRepository;

@Service
public class PresensiServiceImpl implements PresensiService {
    @Autowired
    private PresensiRepository presensiRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<ComboDto> getStatusAbsenCombo(Long tglAwal, Long tglAkhir) {
        if (tglAwal == null) {
            tglAwal = localDateToEpoch(LocalDate.now().withDayOfMonth(1));
        }
        if (tglAkhir == null) {
            tglAkhir = localDateToEpoch(LocalDate.now().plusDays(1));
        }
        List<StatusAbsensiEnum> results = presensiRepository.findDistinctStatusByTanggal(tglAwal, tglAkhir);
        if (results.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "Tidak ada status absensi dalam rentang tanggal tersebut.");
        }

        return results.stream()
                .map(s -> new ComboDto(s.getKode(), s.getNama()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> checkIn() {
        String email = jwtUtil.getEmailFromCurrentRequest();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED,
                        "Akun tidak ditemukan, silakan login ulang."));

        Long today = localDateToEpoch(LocalDate.now());

        Presensi existing = presensiRepository.findByIdUserAndTglAbsensi(user.getId(), today);
        if (existing != null) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "Anda sudah melakukan check-in hari ini!");
        }

        String jamMasuk = LocalDateTime.now().toLocalTime().toString();
        presensiRepository.save(Presensi.builder()
                .idUser(user.getId())
                .statusAbsensi(StatusAbsensiEnum.HADIR)
                .tglAbsensi(today)
                .jamMasuk(jamMasuk)
                .build());

        Map<String, String> response = new HashMap<>();
        response.put("jamMasuk", jamMasuk);
        return response;
    }

    @Override
    public Map<String, String> checkOut() {
        String email = jwtUtil.getEmailFromCurrentRequest();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED,
                        "Akun tidak ditemukan, silakan login ulang."));

        Long today = localDateToEpoch(LocalDate.now());

        Presensi presensi = presensiRepository.findByIdUserAndTglAbsensi(user.getId(), today);
        if (presensi == null) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "Anda belum melakukan check-in hari ini!");
        }

        if (presensi.getJamKeluar() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "Anda sudah melakukan check-out hari ini!");
        }

        String jamKeluar = LocalDateTime.now().toLocalTime().toString();
        presensi.setJamKeluar(jamKeluar);
        presensiRepository.save(presensi);

        Map<String, String> response = new HashMap<>();
        response.put("jamKeluar", jamKeluar);
        return response;
    }

    @Override
    public void absenIzin(AbsenIzinRequest request) {
        String email = jwtUtil.getEmailFromCurrentRequest();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED,
                        "Akun tidak ditemukan, silakan login ulang."));

        StatusAbsensiEnum status = StatusAbsensiEnum.fromKode(request.getKodeStatus());

        if (status == null || status == StatusAbsensiEnum.HADIR) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "Status absensi tidak valid untuk izin.");
        }

        Presensi existing = presensiRepository.findByIdUserAndTglAbsensi(user.getId(), request.getTglAbsensi());
        if (existing != null) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "Anda sudah mengajukan absensi pada tanggal ini!");
        }

        presensiRepository.save(Presensi.builder()
                .idUser(user.getId())
                .tglAbsensi(request.getTglAbsensi())
                .statusAbsensi(status)
                .build());
    }

    @Override
    public List<PresensiPegawaiDto> getDaftarPresensiPegawai(Long tglAwal, Long tglAkhir) {
        String email = jwtUtil.getEmailFromCurrentRequest();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED,
                        "Akun tidak ditemukan, silakan login ulang."));

        if (tglAwal == null) {
            tglAwal = localDateToEpoch(LocalDate.now().withDayOfMonth(1));
        }
        if (tglAkhir == null) {
            tglAkhir = localDateToEpoch(LocalDate.now().plusDays(1));
        }

        List<Presensi> dataList = presensiRepository.findPresensiByTanggalDanUser(user.getId(), tglAwal, tglAkhir);
        if (dataList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "Tidak ada data presensi dalam rentang tanggal tersebut.");
        }
        return dataList.stream()
                .map(data -> PresensiPegawaiDto.builder()
                        .tglAbsensi(data.getTglAbsensi())
                        .jamMasuk(data.getJamMasuk())
                        .jamKeluar(data.getJamKeluar())
                        .namaStatus(data.getStatusAbsensi().getNama())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<PresensiPegawaiAdminDto> getDaftarPresensiPegawaiAdmin(Long tglAwal, Long tglAkhir, int page,
            int size) {
        String email = jwtUtil.getEmailFromCurrentRequest();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                        "User belum login"));

        if (admin.getProfile() != ProfileEnum.ADMIN) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "Hanya Admin atau pegawai HRD yang bisa melakukan aksi ini");
        }

        if (tglAwal == null) {
            tglAwal = localDateToEpoch(LocalDate.now().withDayOfMonth(1));
        }
        if (tglAkhir == null) {
            tglAkhir = localDateToEpoch(LocalDate.now().plusDays(1));
        }

        List<Presensi> dataListPresensi = presensiRepository.findPresensiByTanggal(tglAwal, tglAkhir);

        long totalItems = dataListPresensi.size();

        int fromIndex = Math.min((page - 1) * size, dataListPresensi.size());
        int toIndex = Math.min(fromIndex + size, dataListPresensi.size());
        List<Presensi> pagedData = dataListPresensi.subList(fromIndex, toIndex);

        List<PresensiPegawaiAdminDto> dataPA = pagedData.stream()
                .map(data -> {
                    User user = userRepository.findById(data.getIdUser()).orElse(null);

                    return PresensiPegawaiAdminDto.builder()
                            .idUser(data.getIdUser())
                            .namaLengkap(user != null ? user.getNamaLengkap() : "-")
                            .tglAbsensi(data.getTglAbsensi())
                            .jamMasuk(data.getJamMasuk())
                            .jamKeluar(data.getJamKeluar())
                            .namaStatus(data.getStatusAbsensi() != null
                                    ? data.getStatusAbsensi().getNama()
                                    : "-")
                            .build();
                })
                .collect(Collectors.toList());

        return PageResponse.success(dataPA, page, size, totalItems);
    }

    private Long localDateToEpoch(LocalDate date) {
        return date.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
    }
}
