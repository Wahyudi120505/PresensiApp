package com.example.presensi_app.services.pegawai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.presensi_app.dto.InfoDto;
import com.example.presensi_app.dto.PageResponse;
import com.example.presensi_app.dto.pegawai.PegawaiRequest;
import com.example.presensi_app.enums.JenisKelaminEnum;
import com.example.presensi_app.enums.PendidikanEnum;
import com.example.presensi_app.enums.ProfileEnum;
import com.example.presensi_app.jwt.JwtUtil;
import com.example.presensi_app.models.Departemen;
import com.example.presensi_app.models.User;
import com.example.presensi_app.repositorys.DepartemenRepository;
import com.example.presensi_app.repositorys.JabatanRepository;
import com.example.presensi_app.repositorys.UnitKerjaRepository;
import com.example.presensi_app.repositorys.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PegawaiServiceImpl implements PegawaiService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartemenRepository departemenRepository;

    @Autowired
    private JabatanRepository jabatanRepository;

    @Autowired
    private UnitKerjaRepository unitKerjaRepository;

    @Transactional
    @Override
    public void createPegawai(PegawaiRequest request) {
        String email = jwtUtil.getEmailFromCurrentRequest();

        checkAdminOrHrd(email);

        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_IMPLEMENTED,
                            "Akun dengan email tersebut sudah terdaftar.");
                });

        if (!request.getPassword().equals(request.getPasswordC())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_IMPLEMENTED,
                    "Password baru dan konfirmasi password tidak sama. Mohon periksa kembali.");
        }

        JenisKelaminEnum jkEnum = JenisKelaminEnum.fromKode(request.getKdJenisKelamin());
        PendidikanEnum pEnum = PendidikanEnum.fromKode(request.getKdPendidikan());

        userRepository.save(User.builder()
                .profile(ProfileEnum.PEGAWAI)
                .email(request.getEmail())
                .namaLengkap(request.getNamaLengkap())
                .tempatLahir(request.getTempatLahir())
                .tanggalLahir(request.getTanggalLahir())
                .jenisKelamin(jkEnum)
                .pendidikan(pEnum)
                .departemen(departemenRepository.getReferenceById(request.getKdDepartemen()))
                .jabatan(jabatanRepository.getReferenceById(request.getKdJabatan()))
                .unitKerja(unitKerjaRepository.getReferenceById(request.getKdUnitKerja()))
                .nikUser(generateNik(departemenRepository.getReferenceById(request.getKdDepartemen())))
                .password(passwordEncoder.encode(request.getPasswordC()))
                .build());
    }

    @Transactional
    @Override
    public void editPegawai(PegawaiRequest request, String idUser) {
        String email = jwtUtil.getEmailFromCurrentRequest();

        checkAdminOrHrd(email);
        User user = userRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_IMPLEMENTED, "User tidak ditemukan"));

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (!request.getPassword().equals(request.getPasswordC())) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED,
                        "Password dan konfirmasi password tidak sama.");
            }
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user.setNamaLengkap(request.getNamaLengkap());
        user.setEmail(request.getEmail());
        user.setTempatLahir(request.getTempatLahir());
        user.setTanggalLahir(request.getTanggalLahir());
        user.setJenisKelamin(JenisKelaminEnum.fromKode(request.getKdJenisKelamin()));
        user.setPendidikan(PendidikanEnum.fromKode(request.getKdPendidikan()));

        if (request.getKdJabatan() != 0) {
            user.setJabatan(jabatanRepository.getReferenceById(request.getKdJabatan()));
        }
        if (request.getKdDepartemen() != 0) {
            user.setDepartemen(departemenRepository.getReferenceById(request.getKdDepartemen()));
        }
        if (request.getKdUnitKerja() != null) {
            user.setUnitKerja(unitKerjaRepository.getReferenceById(request.getKdUnitKerja()));
        }

        userRepository.save(user);
    }

    @Override
    public PageResponse<InfoDto> getAllPegawai(int page, int size) {
        String email = jwtUtil.getEmailFromCurrentRequest();

        checkAdminOrHrd(email);

        List<User> allUsers = userRepository.findAll().stream()
                .filter(u -> u.getProfile() != ProfileEnum.ADMIN)
                .collect(Collectors.toList());

        long totalItems = allUsers.size();

        int fromIndex = Math.min((page - 1) * size, allUsers.size());
        int toIndex = Math.min(fromIndex + size, allUsers.size());
        List<User> pagedUsers = allUsers.subList(fromIndex, toIndex);

        List<InfoDto> infoList = pagedUsers.stream()
                .map(this::toInfo)
                .collect(Collectors.toList());

        return PageResponse.success(infoList, page, size, totalItems);
    }

    @Override
    public void adminUploadImage(String idUser, MultipartFile fileImage, String fileName) throws IOException {
        String email = jwtUtil.getEmailFromCurrentRequest();

        checkAdminOrHrd(email);

        if (fileImage == null || fileImage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "File image wajib diisi");
        }

        fileName += "_" + fileImage.getOriginalFilename();

        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        fileImage.transferTo(path.toFile());

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "User tidak ditemukan"));
        user.setPhoto(fileName);
        userRepository.save(user);
    }

    @Override
    public void pegawaiUploadImage(MultipartFile file, String fileName) throws IOException {
        String email = jwtUtil.getEmailFromCurrentRequest();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED,
                        "Akun tidak ditemukan, silakan login ulang."));

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "File image wajib diisi");
        }

        fileName += "_" + file.getOriginalFilename();

        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        file.transferTo(path.toFile());

        user.setPhoto(fileName);
        userRepository.save(user);
    }

    private String generateNik(Departemen departemen) {
        String kodeDept = departemen.getNamaDepartemen().toLowerCase();
        long count = userRepository.countByDepartemen(departemen);
        long nomor = count + 1;
        String nomorStr = String.format("%03d", nomor);
        return kodeDept + nomorStr;
    }

    private void checkAdminOrHrd(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                        "User belum login"));

        boolean isAdmin = user.getProfile() == ProfileEnum.ADMIN;
        boolean isHrd = false;
        if (!isAdmin) {
            isHrd = user.getDepartemen() != null &&
                    "HRD".equalsIgnoreCase(user.getDepartemen().getNamaDepartemen());
        }

        if (!isAdmin && !isHrd) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "Hanya Admin atau pegawai HRD yang bisa melakukan aksi ini");
        }
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
                .kdDepartemen(user.getDepartemen() != null ? user.getDepartemen().getKdDepartemen() : null)
                .namaDepartemen(user.getDepartemen() != null ? user.getDepartemen().getNamaDepartemen() : null)
                .kdJabatan(user.getJabatan() != null ? user.getJabatan().getKdJabatan() : null)
                .namaJabatan(user.getJabatan() != null ? user.getJabatan().getNamaJabatan() : null)
                .kdUnitKerja(user.getUnitKerja() != null ? user.getUnitKerja().getKdUnitKerja() : null)
                .namaUnitKerja(user.getUnitKerja() != null ? user.getUnitKerja().getNamaUnitKerja() : null)
                .kdJenisKelamin(user.getJenisKelamin() != null ? user.getJenisKelamin().getKode() : null)
                .namaJenisKelamin(user.getJenisKelamin() != null ? user.getJenisKelamin().getNama() : null)
                .kdPendidikan(user.getPendidikan() != null ? user.getPendidikan().getKode() : null)
                .namaPendidikan(user.getPendidikan() != null ? user.getPendidikan().getNama() : null)
                .photo(user.getPhoto() != null ? "/uploads/" + user.getPhoto() : null)
                .build();
    }

}
