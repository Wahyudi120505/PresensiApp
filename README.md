# 📋 Dokumentasi Aplikasi Presensi Karyawan

**Disusun oleh:** Ahmad Wahyudi Tanjung  
**Versi:** 1.0  
**Tanggal:** 2024

## 📖 1. Latar Belakang dan Tujuan

Aplikasi Presensi Karyawan ini dikembangkan untuk mempermudah proses pencatatan kehadiran pegawai secara digital menggunakan Spring Boot sebagai backend service. Aplikasi ini memanfaatkan teknologi REST API dan autentikasi JWT agar sistem lebih efisien, aman, dan mudah diintegrasikan dengan aplikasi lain.

### 🎯 Tujuan Utama:
- Mencatat presensi pegawai (check-in dan check-out) secara otomatis
- Menyediakan fitur izin tidak hadir (sakit, izin, alfa)
- Menyediakan panel admin untuk melihat data presensi semua pegawai
- Memberikan autentikasi aman bagi setiap pengguna aplikasi

## 🛠 2. Alasan Pemilihan Teknologi

| Komponen | Teknologi | Alasan |
|----------|------------|---------|
| **Backend** | Spring Boot (Java 21) | Framework Java modern, ringan, dan cocok untuk aplikasi enterprise REST API |
| **Database** | PostgreSQL | Open-source, kuat, mendukung tipe data epoch & enum, serta stabil untuk data besar |
| **ORM** | Spring Data JPA (Hibernate) | Mempermudah mapping antara entity dan tabel database tanpa query manual |
| **Auth** | JWT (JSON Web Token) | Autentikasi stateless yang efisien untuk sistem RESTful |
| **API Documentation & Testing** | Swagger UI | Menyediakan dokumentasi interaktif langsung di browser tanpa Postman |
| **DB Client** | DBeaver | Aplikasi GUI untuk manajemen dan pengujian query PostgreSQL |

## 🧩 3. Struktur Fitur Utama

### 🔐 Modul Auth
- `POST /api/auth/init-data` — Inisialisasi admin & perusahaan pertama
- `POST /api/auth/login` — Login user dan mendapatkan JWT token
- `PUT /api/auth/ubah-password-sendiri` — Mengubah password akun sendiri

### 👥 Modul Pegawai
- CRUD data pegawai (tambah, ubah, hapus, lihat)
- Mengubah foto profil pegawai
- Combo data master (jabatan, departemen, unit kerja, pendidikan, jenis kelamin)

### 🕐 Modul Presensi
- `POST /presensi/in` → Check-in otomatis berdasarkan waktu server
- `POST /presensi/out` → Check-out otomatis
- `POST /presensi/abseni` → Ajukan izin (tidak hadir, sakit, dsb)
- `GET /presensi/combo/status-absen` → Ambil daftar status absensi (Hadir, Izin, Sakit, Alfa)
- `GET /presensi/daftar/pegawai` → Melihat riwayat presensi pegawai yang sedang login
- `GET /presensi/daftar/admin` → Melihat presensi semua pegawai (khusus admin)

## 🗃 4. Desain Database Utama

### Tabel Utama:
- `users` → Menyimpan akun pegawai dan admin
- `presensi` → Menyimpan log kehadiran harian pegawai
- `jabatan`, `departemen`, `unit_kerja` → Master data

**Catatan:** Semua tanggal disimpan dalam format epoch second, sesuai standar REST API.

## 💻 5. Instalasi dan Setup

### ⚙️ Prasyarat
- Java 21
- PostgreSQL
- DBeaver (opsional)
- Browser untuk akses Swagger UI

### 🚀 Langkah Instalasi

#### 1. Clone Project
```bash
git clone https://github.com/Wahyudi120505/PresensiApp.git
cd PresensiApp
```

#### 2. Setup Database
Buat database baru di PostgreSQL:
```sql
CREATE DATABASE presensi_db;
```

Lalu ubah konfigurasi di `application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/presensi_db
    username: postgres
    password: <password_kamu>
```

#### 3. Build dan Jalankan
```bash
mvn clean install
mvn spring-boot:run
```

#### 4. Akses Swagger UI
Setelah aplikasi berjalan, buka browser dan ketik:
```
http://localhost:8080/swagger-ui/index.html
```

Di Swagger UI Anda bisa langsung:
- Melihat semua endpoint API
- Mencoba request langsung tanpa Postman
- Melihat parameter, contoh input, dan response

#### 5. Inisialisasi Data Awal
Jalankan endpoint `POST /api/auth/init-data` di Swagger:

**Request:**
```json
{
  "namaAdmin": "Admin Utama",
  "perusahaan": "PT. Contoh Sejahtera"
}
```

**Response:**
```json
{
  "email": "admin@contoh.com",
  "password": "admin123",
  "profile": "ADMIN"
}
```

#### 6. Login
1. Gunakan endpoint `POST /api/auth/login` dengan kredensial yang didapat
2. Salin token JWT dari hasil response
3. Masukkan token ke tombol **Authorize** di Swagger (pojok kanan atas) agar semua endpoint bisa diakses

## ⏰ 6. Konversi Epoch

Semua tanggal disimpan sebagai epoch (detik sejak 1 Jan 1970 UTC).

**Contoh konversi di Java:**
```java
private Long localDateToEpoch(LocalDate date) {
    return date.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
}
```

## ⚠️ 7. Penanganan Error

| Kode | Jenis | Contoh Kasus |
|------|-------|--------------|
| 501 | Business Error | Sudah check-in hari ini, atau data izin tidak valid |
| 400 / 404 / 500 | System Error | Kesalahan query, entity tidak ditemukan, atau bug teknis |

**Format Response Error:**
```json
{
  "success": false,
  "message": "Data tidak ditemukan",
  "data": null
}
```

## 🔒 8. Informasi Tambahan

- **JWT Token** berlaku 24 jam sejak login
- **Password** disimpan dalam format BCrypt
- **Akses API** dikontrol menggunakan Spring Security
- **Response format** seragam:

```json
{
  "success": true,
  "message": "Berhasil mengambil data",
  "data": [...]
}
```

## ✅ 9. Kesimpulan

Aplikasi Presensi ini dirancang sebagai solusi digital untuk manajemen kehadiran pegawai dengan keamanan modern dan kemudahan penggunaan. Dengan Spring Boot + PostgreSQL + Swagger, sistem ini siap dikembangkan lebih lanjut menjadi aplikasi web atau mobile tanpa mengubah struktur API utama.

---
