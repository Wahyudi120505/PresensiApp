package com.example.presensi_app.models;

import jakarta.persistence.Entity;
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
public class UnitKerja {
    @Id
    private Integer kdUnitKerja;
    private String namaUnitKerja;
}
