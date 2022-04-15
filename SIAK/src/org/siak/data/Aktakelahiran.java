package org.siak.data;
// Generated Jul 28, 2012 10:43:28 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Aktakelahiran generated by hbm2java
 */
public class Aktakelahiran  implements java.io.Serializable {


     private String noAktaKelahiran;
     private Biodata biodataByNikibu;
     private Biodata biodataByNikayah;
     private Permohonan permohonan;
     private String nama;
     private Date tanggalLahir;
     private String tempatLahir;
     private String jenisKelamin;
     private Date tanggalBuat;
     private String noKk;
     private String jenisKelahiran;
     private String bantuanKelahiran;
     private int jenisBuat;
     private Set biodatas = new HashSet(0);

    public Aktakelahiran() {
    }

	
    public Aktakelahiran(String noAktaKelahiran, Permohonan permohonan, String nama, Date tanggalLahir, String tempatLahir, String jenisKelamin, Date tanggalBuat, String jenisKelahiran, String bantuanKelahiran, int jenisBuat) {
        this.noAktaKelahiran = noAktaKelahiran;
        this.permohonan = permohonan;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.tempatLahir = tempatLahir;
        this.jenisKelamin = jenisKelamin;
        this.tanggalBuat = tanggalBuat;
        this.jenisKelahiran = jenisKelahiran;
        this.bantuanKelahiran = bantuanKelahiran;
        this.jenisBuat = jenisBuat;
    }
    public Aktakelahiran(String noAktaKelahiran, Biodata biodataByNikibu, Biodata biodataByNikayah, Permohonan permohonan, String nama, Date tanggalLahir, String tempatLahir, String jenisKelamin, Date tanggalBuat, String noKk, String jenisKelahiran, String bantuanKelahiran, int jenisBuat, Set biodatas) {
       this.noAktaKelahiran = noAktaKelahiran;
       this.biodataByNikibu = biodataByNikibu;
       this.biodataByNikayah = biodataByNikayah;
       this.permohonan = permohonan;
       this.nama = nama;
       this.tanggalLahir = tanggalLahir;
       this.tempatLahir = tempatLahir;
       this.jenisKelamin = jenisKelamin;
       this.tanggalBuat = tanggalBuat;
       this.noKk = noKk;
       this.jenisKelahiran = jenisKelahiran;
       this.bantuanKelahiran = bantuanKelahiran;
       this.jenisBuat = jenisBuat;
       this.biodatas = biodatas;
    }
   
    public String getNoAktaKelahiran() {
        return this.noAktaKelahiran;
    }
    
    public void setNoAktaKelahiran(String noAktaKelahiran) {
        this.noAktaKelahiran = noAktaKelahiran;
    }
    public Biodata getBiodataByNikibu() {
        return this.biodataByNikibu;
    }
    
    public void setBiodataByNikibu(Biodata biodataByNikibu) {
        this.biodataByNikibu = biodataByNikibu;
    }
    public Biodata getBiodataByNikayah() {
        return this.biodataByNikayah;
    }
    
    public void setBiodataByNikayah(Biodata biodataByNikayah) {
        this.biodataByNikayah = biodataByNikayah;
    }
    public Permohonan getPermohonan() {
        return this.permohonan;
    }
    
    public void setPermohonan(Permohonan permohonan) {
        this.permohonan = permohonan;
    }
    public String getNama() {
        return this.nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
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
    public String getJenisKelamin() {
        return this.jenisKelamin;
    }
    
    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
    public Date getTanggalBuat() {
        return this.tanggalBuat;
    }
    
    public void setTanggalBuat(Date tanggalBuat) {
        this.tanggalBuat = tanggalBuat;
    }
    public String getNoKk() {
        return this.noKk;
    }
    
    public void setNoKk(String noKk) {
        this.noKk = noKk;
    }
    public String getJenisKelahiran() {
        return this.jenisKelahiran;
    }
    
    public void setJenisKelahiran(String jenisKelahiran) {
        this.jenisKelahiran = jenisKelahiran;
    }
    public String getBantuanKelahiran() {
        return this.bantuanKelahiran;
    }
    
    public void setBantuanKelahiran(String bantuanKelahiran) {
        this.bantuanKelahiran = bantuanKelahiran;
    }
    public int getJenisBuat() {
        return this.jenisBuat;
    }
    
    public void setJenisBuat(int jenisBuat) {
        this.jenisBuat = jenisBuat;
    }
    public Set getBiodatas() {
        return this.biodatas;
    }
    
    public void setBiodatas(Set biodatas) {
        this.biodatas = biodatas;
    }




}


