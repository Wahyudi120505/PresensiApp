package com.example.presensi_app.services.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.presensi_app.dto.InfoDto;
import com.example.presensi_app.dto.auth.InitDataRequest;
import com.example.presensi_app.dto.auth.InitDataResponse;
import com.example.presensi_app.dto.auth.LoginRequest;
import com.example.presensi_app.dto.auth.LoginResponse;
import com.example.presensi_app.dto.auth.UbahPasswordRequest;
import com.example.presensi_app.enums.ProfileEnum;
import com.example.presensi_app.jwt.JwtUtil;
import com.example.presensi_app.models.Perusahaan;
import com.example.presensi_app.models.User;
import com.example.presensi_app.repositorys.PerusahaanRepository;
import com.example.presensi_app.repositorys.UserRepository;

@Service
public class AuthServicesImpl implements AuthService {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PerusahaanRepository perusahaanRepository;

        @Override
        public InitDataResponse initData(InitDataRequest request) {
                

                if (request.getNamaAdmin() == null || request.getPerusahaan() == null) {
                        throw new ResponseStatusException(
                                        HttpStatus.NOT_IMPLEMENTED,
                                        "Data request tidak boleh kosong.");
                }

                String email = request.getNamaAdmin().toLowerCase().replace(" ", "") +
                                "@" + request.getPerusahaan().toLowerCase().replace(" ", "") + ".com";

                Optional<User> existingAdmin = userRepository.findByEmail(email);
                if (existingAdmin.isPresent()) {
                        throw new ResponseStatusException(
                                        HttpStatus.NOT_IMPLEMENTED,
                                        "Admin sudah dibuat sebelumnya untuk perusahaan ini.");
                }

                Perusahaan perusahaan = Perusahaan.builder()
                                .nama(request.getPerusahaan())
                                .build();
                perusahaanRepository.save(perusahaan);

                String passAdmin = "admin123";
                User newAdminUser = User.builder()
                                .profile(ProfileEnum.ADMIN)
                                .namaLengkap(request.getNamaAdmin())
                                .email(email)
                                .password(passwordEncoder.encode(passAdmin))
                                .perusahaan(perusahaan)
                                .build();

                userRepository.save(newAdminUser);

                return InitDataResponse.builder()
                                .email(email)
                                .password(passAdmin)
                                .profile(newAdminUser.getProfile().name())
                                .build();
        }

        @Override
        public LoginResponse<?> login(LoginRequest request) {
                if (request.getEmail() == null || request.getPassword() == null ||
                                request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
                        throw new ResponseStatusException(
                                        HttpStatus.NOT_IMPLEMENTED,
                                        "Email dan password wajib diisi.");
                }

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_IMPLEMENTED,
                                                "Akun dengan email tersebut tidak terdaftar."));

                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        throw new ResponseStatusException(
                                        HttpStatus.NOT_IMPLEMENTED,
                                        "Password yang kamu masukkan salah.");
                }

                InfoDto info = toInfo(user);
                String token = jwtUtil.generateToken(user.getEmail());

                return LoginResponse.builder()
                                .token(token)
                                .info(info)
                                .build();

        }

        private InfoDto toInfo(User user) {
                return InfoDto.builder()
                        .profile(user.getProfile().name())
                        .idUser(user.getId())
                        .namaLengkap(user.getNamaLengkap())
                        .tempatLahir(user.getTempatLahir())
                        .tanggalLahir(user.getTanggalLahir())
                        .email(user.getEmail())
                        .nikUser(user.getNikUser())
                        .kdDepartemen(user.getDepartemen() != null ? user.getDepartemen().getKdDepartemen()
                                        : null)
                        .namaDepartemen(user.getDepartemen() != null ? user.getDepartemen().getNamaDepartemen()
                                        : null)
                        .kdJabatan(user.getJabatan() != null ? user.getJabatan().getKdJabatan() : null)
                        .namaJabatan(user.getJabatan() != null ? user.getJabatan().getNamaJabatan() : null)
                        .kdUnitKerja(user.getUnitKerja() != null ? user.getUnitKerja().getKdUnitKerja() : null)
                        .namaUnitKerja(user.getUnitKerja() != null ? user.getUnitKerja().getNamaUnitKerja()
                                        : null)
                        .kdJenisKelamin(user.getJenisKelamin() != null ? user.getJenisKelamin().getKode()
                                        : null)
                        .namaJenisKelamin(user.getJenisKelamin() != null ? user.getJenisKelamin().getNama()
                                        : null)
                        .kdPendidikan(user.getPendidikan() != null ? user.getPendidikan().getKode() : null)
                        .namaPendidikan(user.getPendidikan() != null ? user.getPendidikan().getNama() : null)
                        .photo(user.getPhoto())
                        .build();
        }

        @Override
        public void ubahPassword(UbahPasswordRequest request) {
                String email = jwtUtil.getEmailFromCurrentRequest();

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_IMPLEMENTED,
                                                "Akun tidak ditemukan, silakan login ulang."));

                if (!passwordEncoder.matches(request.getPasswordAsli(), user.getPassword())) {
                        throw new ResponseStatusException(
                                        HttpStatus.NOT_IMPLEMENTED, "Password lama yang kamu masukkan tidak sesuai.");
                }

                if (!request.getPasswordBaru1().equals(request.getPasswordBaru2())) {
                        throw new ResponseStatusException(
                                        HttpStatus.NOT_IMPLEMENTED, "Password baru yang kamu masukkan tidak sama.");
                }

                user.setPassword(passwordEncoder.encode(request.getPasswordBaru1()));
                userRepository.save(user);
        }
}
