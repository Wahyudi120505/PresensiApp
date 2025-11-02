package com.example.presensi_app.repositorys;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.presensi_app.enums.StatusAbsensiEnum;
import com.example.presensi_app.models.Presensi;

@Repository
public interface PresensiRepository extends JpaRepository<Presensi, String> {
    @Query("SELECT DISTINCT p.statusAbsensi FROM Presensi p WHERE p.tglAbsensi BETWEEN :tglAwal AND :tglAkhir")
    List<StatusAbsensiEnum> findDistinctStatusByTanggal(Long tglAwal, Long tglAkhir);

    @Query("SELECT p FROM Presensi p WHERE p.idUser = :idUser AND p.tglAbsensi BETWEEN :tglAwal AND :tglAkhir")
    List<Presensi> findPresensiByTanggalDanUser(String idUser, Long tglAwal, Long tglAkhir);

    @Query("SELECT p FROM Presensi p WHERE p.tglAbsensi BETWEEN :tglAwal AND :tglAkhir")
    List<Presensi> findPresensiByTanggal( Long tglAwal, Long tglAkhir);

    Optional<Presensi> findByIdUser(String userId);

    Presensi findByIdUserAndTglAbsensi(String idUser, Long tglAbsensi);

}
