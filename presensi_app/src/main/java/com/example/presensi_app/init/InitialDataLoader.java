package com.example.presensi_app.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.presensi_app.models.Departemen;
import com.example.presensi_app.models.Jabatan;
import com.example.presensi_app.models.UnitKerja;
import com.example.presensi_app.repositorys.DepartemenRepository;
import com.example.presensi_app.repositorys.JabatanRepository;
import com.example.presensi_app.repositorys.UnitKerjaRepository;


@Component
public class InitialDataLoader implements ApplicationRunner {
    @Autowired
    private JabatanRepository jabatanRepository;
    @Autowired
    private DepartemenRepository departemenRepository;
    @Autowired
    private UnitKerjaRepository unitKerjaRepository;



    @Override
    public void run(ApplicationArguments args) {
        if(jabatanRepository.count() == 0){
            jabatanRepository.saveAll(List.of(
                Jabatan.builder().kdJabatan(1).namaJabatan("Manager").build(),
                Jabatan.builder().kdJabatan(2).namaJabatan("Staff").build()
            ));
        }

        if(departemenRepository.count() == 0){
            departemenRepository.saveAll(List.of(
                Departemen.builder().kdDepartemen(1).namaDepartemen("HRD").build(),
                Departemen.builder().kdDepartemen(2).namaDepartemen("IT").build()
            ));
        }

        if(unitKerjaRepository.count() == 0){
            unitKerjaRepository.saveAll(List.of(
                UnitKerja.builder().kdUnitKerja(1).namaUnitKerja("Jakarta").build(),
                UnitKerja.builder().kdUnitKerja(2).namaUnitKerja("Bandung").build()
            ));
        }
    }
}
