package com.example.presensi_app.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.presensi_app.models.Departemen;
import com.example.presensi_app.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByEmail(String email);
    long countByDepartemen(Departemen departemen);
}
