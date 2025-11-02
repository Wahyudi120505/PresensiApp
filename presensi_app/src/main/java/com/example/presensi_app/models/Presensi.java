package com.example.presensi_app.models;

import com.example.presensi_app.enums.StatusAbsensiEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Presensi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "presensi_id", nullable = false)
    private String id;
    
    private String idUser;
    private Long tglAbsensi; 
    private String jamMasuk;
    private String jamKeluar;

    @Enumerated(EnumType.STRING)
    private StatusAbsensiEnum statusAbsensi;
}
