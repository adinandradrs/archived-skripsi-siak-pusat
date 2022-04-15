package org.siak.data;
// Generated Jul 29, 2012 12:49:47 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Kartukeluargadetail generated by hbm2java
 */
public class Kartukeluargadetail  implements java.io.Serializable {


     private Integer kartuKeluargaDetailId;
     private Kartukeluarga kartukeluarga;
     private Pekerjaan pekerjaan;
     private Agama agama;
     private Hubungandetail hubungandetailByStatusKawin;
     private Hubungandetail hubungandetailByStatusHubungan;
     private Pendidikan pendidikan;
     private String niks;
     private String jenisKelamin;
     private Date tanggalLahir;
     private String tempatLahir;
     private String nikayah;
     private String nikibu;
     private String namaAyah;
     private String namaIbu;

    public Kartukeluargadetail() {
    }

	
    public Kartukeluargadetail(Pekerjaan pekerjaan, Agama agama, Hubungandetail hubungandetailByStatusKawin, Hubungandetail hubungandetailByStatusHubungan, Pendidikan pendidikan, String niks, String jenisKelamin, Date tanggalLahir, String tempatLahir, String nikayah, String nikibu) {
        this.pekerjaan = pekerjaan;
        this.agama = agama;
        this.hubungandetailByStatusKawin = hubungandetailByStatusKawin;
        this.hubungandetailByStatusHubungan = hubungandetailByStatusHubungan;
        this.pendidikan = pendidikan;
        this.niks = niks;
        this.jenisKelamin = jenisKelamin;
        this.tanggalLahir = tanggalLahir;
        this.tempatLahir = tempatLahir;
        this.nikayah = nikayah;
        this.nikibu = nikibu;
    }
    public Kartukeluargadetail(Kartukeluarga kartukeluarga, Pekerjaan pekerjaan, Agama agama, Hubungandetail hubungandetailByStatusKawin, Hubungandetail hubungandetailByStatusHubungan, Pendidikan pendidikan, String niks, String jenisKelamin, Date tanggalLahir, String tempatLahir, String nikayah, String nikibu, String namaAyah, String namaIbu) {
       this.kartukeluarga = kartukeluarga;
       this.pekerjaan = pekerjaan;
       this.agama = agama;
       this.hubungandetailByStatusKawin = hubungandetailByStatusKawin;
       this.hubungandetailByStatusHubungan = hubungandetailByStatusHubungan;
       this.pendidikan = pendidikan;
       this.niks = niks;
       this.jenisKelamin = jenisKelamin;
       this.tanggalLahir = tanggalLahir;
       this.tempatLahir = tempatLahir;
       this.nikayah = nikayah;
       this.nikibu = nikibu;
       this.namaAyah = namaAyah;
       this.namaIbu = namaIbu;
    }
   
    public Integer getKartuKeluargaDetailId() {
        return this.kartuKeluargaDetailId;
    }
    
    public void setKartuKeluargaDetailId(Integer kartuKeluargaDetailId) {
        this.kartuKeluargaDetailId = kartuKeluargaDetailId;
    }
    public Kartukeluarga getKartukeluarga() {
        return this.kartukeluarga;
    }
    
    public void setKartukeluarga(Kartukeluarga kartukeluarga) {
        this.kartukeluarga = kartukeluarga;
    }
    public Pekerjaan getPekerjaan() {
        return this.pekerjaan;
    }
    
    public void setPekerjaan(Pekerjaan pekerjaan) {
        this.pekerjaan = pekerjaan;
    }
    public Agama getAgama() {
        return this.agama;
    }
    
    public void setAgama(Agama agama) {
        this.agama = agama;
    }
    public Hubungandetail getHubungandetailByStatusKawin() {
        return this.hubungandetailByStatusKawin;
    }
    
    public void setHubungandetailByStatusKawin(Hubungandetail hubungandetailByStatusKawin) {
        this.hubungandetailByStatusKawin = hubungandetailByStatusKawin;
    }
    public Hubungandetail getHubungandetailByStatusHubungan() {
        return this.hubungandetailByStatusHubungan;
    }
    
    public void setHubungandetailByStatusHubungan(Hubungandetail hubungandetailByStatusHubungan) {
        this.hubungandetailByStatusHubungan = hubungandetailByStatusHubungan;
    }
    public Pendidikan getPendidikan() {
        return this.pendidikan;
    }
    
    public void setPendidikan(Pendidikan pendidikan) {
        this.pendidikan = pendidikan;
    }
    public String getNiks() {
        return this.niks;
    }
    
    public void setNiks(String niks) {
        this.niks = niks;
    }
    public String getJenisKelamin() {
        return this.jenisKelamin;
    }
    
    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
    public Date getTanggalLahir() {
        return this.tanggalLahir;
    }
    
    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
    public String getTempatLahir() {
        return this.tempatLahir;
    }
    
    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }
    public String getNikayah() {
        return this.nikayah;
    }
    
    public void setNikayah(String nikayah) {
        this.nikayah = nikayah;
    }
    public String getNikibu() {
        return this.nikibu;
    }
    
    public void setNikibu(String nikibu) {
        this.nikibu = nikibu;
    }
    public String getNamaAyah() {
        return this.namaAyah;
    }
    
    public void setNamaAyah(String namaAyah) {
        this.namaAyah = namaAyah;
    }
    public String getNamaIbu() {
        return this.namaIbu;
    }
    
    public void setNamaIbu(String namaIbu) {
        this.namaIbu = namaIbu;
    }




}


