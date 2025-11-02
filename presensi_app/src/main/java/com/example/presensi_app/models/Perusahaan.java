package com.example.presensi_app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Perusahaan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "perusahaan_id", nullable = false)
    private String id;
    
    private String nama;
}
