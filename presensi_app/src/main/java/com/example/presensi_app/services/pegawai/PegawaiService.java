package com.example.presensi_app.services.pegawai;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.presensi_app.dto.InfoDto;
import com.example.presensi_app.dto.PageResponse;
import com.example.presensi_app.dto.pegawai.PegawaiRequest;

public interface PegawaiService {
    void createPegawai(PegawaiRequest request);

    void editPegawai(PegawaiRequest request, String idUser);

    PageResponse<InfoDto> getAllPegawai(int page, int size);

    void adminUploadImage(String id, MultipartFile file, String fileName) throws IOException;

    void pegawaiUploadImage(MultipartFile file, String fileName) throws IOException;
}
