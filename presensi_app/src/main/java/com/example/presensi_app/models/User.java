package com.example.presensi_app.models;


import com.example.presensi_app.enums.JenisKelaminEnum;
import com.example.presensi_app.enums.PendidikanEnum;
import com.example.presensi_app.enums.ProfileEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    private ProfileEnum profile;

    private String namaLengkap;
    private String tempatLahir;
    private Long tanggalLahir;
    private String email;
    private String password;
    private String nikUser;

    @ManyToOne
    @JoinColumn(name = "kd_jabatan")
    private Jabatan jabatan;

    @ManyToOne
    @JoinColumn(name = "kd_departemen")
    private Departemen departemen;

    @ManyToOne
    @JoinColumn(name = "kd_unit_kerja")
    private UnitKerja unitKerja;

    @Enumerated(EnumType.STRING)
    private JenisKelaminEnum jenisKelamin;

    @Enumerated(EnumType.STRING)
    private PendidikanEnum pendidikan;

    private String photo;

    @ManyToOne
    @JoinColumn(name = "perusahaan_id")
    private Perusahaan perusahaan;
}
