package com.example.presensi_app.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.presensi_app.models.Perusahaan;

@Repository
public interface PerusahaanRepository extends JpaRepository<Perusahaan, String>{
    
}
