package org.siak.data;
// Generated Aug 1, 2012 3:18:02 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Biodata generated by hbm2java
 */
public class Biodata  implements java.io.Serializable {


     private String nik;
     private Pekerjaan pekerjaan;
     private Agama agama;
     private Hubungandetail hubungandetail;
     private Kelurahan kelurahan;
     private Pendidikan pendidikan;
     private String nama;
     private Date tanggalLahir;
     private String tempatLahir;
     private String golonganDarah;
     private String noAktaKelahiran;
     private String noKk;
     private String telepon;
     private String alamat;
     private byte[] foto;
     private int aktif;
     private String jenisKelamin;
     private Integer rt;
     private Integer rw;
     private Set ktps = new HashSet(0);
     private Set aktakelahiransForNikayah = new HashSet(0);
     private Set biodatapindahs = new HashSet(0);
     private Set aktanikahdetailsForNiksuami = new HashSet(0);
     private Set permohonandetails = new HashSet(0);
     private Set biodatacacats = new HashSet(0);
     private Set kartukeluargas = new HashSet(0);
     private Set aktakematians = new HashSet(0);
     private Set aktanikahdetailsForNikistri = new HashSet(0);
     private Set aktakelahiransForNikibu = new HashSet(0);

    public Biodata() {
    }

	
    public Biodata(String nik, Pekerjaan pekerjaan, Agama agama, Hubungandetail hubungandetail, Kelurahan kelurahan, Pendidikan pendidikan, String nama, Date tanggalLahir, String tempatLahir, String golonganDarah, String telepon, String alamat, byte[] foto, int aktif, String jenisKelamin) {
        this.nik = nik;
        this.pekerjaan = pekerjaan;
        this.agama = agama;
        this.hubungandetail = hubungandetail;
        this.kelurahan = kelurahan;
        this.pendidikan = pendidikan;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.tempatLahir = tempatLahir;
        this.golonganDarah = golonganDarah;
        this.telepon = telepon;
        this.alamat = alamat;
        this.foto = foto;
        this.aktif = aktif;
        this.jenisKelamin = jenisKelamin;
    }
    public Biodata(String nik, Pekerjaan pekerjaan, Agama agama, Hubungandetail hubungandetail, Kelurahan kelurahan, Pendidikan pendidikan, String nama, Date tanggalLahir, String tempatLahir, String golonganDarah, String noAktaKelahiran, String noKk, String telepon, String alamat, byte[] foto, int aktif, String jenisKelamin, Integer rt, Integer rw, Set ktps, Set aktakelahiransForNikayah, Set biodatapindahs, Set aktanikahdetailsForNiksuami, Set permohonandetails, Set biodatacacats, Set kartukeluargas, Set aktakematians, Set aktanikahdetailsForNikistri, Set aktakelahiransForNikibu) {
       this.nik = nik;
       this.pekerjaan = pekerjaan;
       this.agama = agama;
       this.hubungandetail = hubungandetail;
       this.kelurahan = kelurahan;
       this.pendidikan = pendidikan;
       this.nama = nama;
       this.tanggalLahir = tanggalLahir;
       this.tempatLahir = tempatLahir;
       this.golonganDarah = golonganDarah;
       this.noAktaKelahiran = noAktaKelahiran;
       this.noKk = noKk;
       this.telepon = telepon;
       this.alamat = alamat;
       this.foto = foto;
       this.aktif = aktif;
       this.jenisKelamin = jenisKelamin;
       this.rt = rt;
       this.rw = rw;
       this.ktps = ktps;
       this.aktakelahiransForNikayah = aktakelahiransForNikayah;
       this.biodatapindahs = biodatapindahs;
       this.aktanikahdetailsForNiksuami = aktanikahdetailsForNiksuami;
       this.permohonandetails = permohonandetails;
       this.biodatacacats = biodatacacats;
       this.kartukeluargas = kartukeluargas;
       this.aktakematians = aktakematians;
       this.aktanikahdetailsForNikistri = aktanikahdetailsForNikistri;
       this.aktakelahiransForNikibu = aktakelahiransForNikibu;
    }
   
    public String getNik() {
        return this.nik;
    }
    
    public void setNik(String nik) {
        this.nik = nik;
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
    public Hubungandetail getHubungandetail() {
        return this.hubungandetail;
    }
    
    public void setHubungandetail(Hubungandetail hubungandetail) {
        this.hubungandetail = hubungandetail;
    }
    public Kelurahan getKelurahan() {
        return this.kelurahan;
    }
    
    public void setKelurahan(Kelurahan kelurahan) {
        this.kelurahan = kelurahan;
    }
    public Pendidikan getPendidikan() {
        return this.pendidikan;
    }
    
    public void setPendidikan(Pendidikan pendidikan) {
        this.pendidikan = pendidikan;
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
    public String getGolonganDarah() {
        return this.golonganDarah;
    }
    
    public void setGolonganDarah(String golonganDarah) {
        this.golonganDarah = golonganDarah;
    }
    public String getNoAktaKelahiran() {
        return this.noAktaKelahiran;
    }
    
    public void setNoAktaKelahiran(String noAktaKelahiran) {
        this.noAktaKelahiran = noAktaKelahiran;
    }
    public String getNoKk() {
        return this.noKk;
    }
    
    public void setNoKk(String noKk) {
        this.noKk = noKk;
    }
    public String getTelepon() {
        return this.telepon;
    }
    
    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }
    public String getAlamat() {
        return this.alamat;
    }
    
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    public byte[] getFoto() {
        return this.foto;
    }
    
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    public int getAktif() {
        return this.aktif;
    }
    
    public void setAktif(int aktif) {
        this.aktif = aktif;
    }
    public String getJenisKelamin() {
        return this.jenisKelamin;
    }
    
    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
    public Integer getRt() {
        return this.rt;
    }
    
    public void setRt(Integer rt) {
        this.rt = rt;
    }
    public Integer getRw() {
        return this.rw;
    }
    
    public void setRw(Integer rw) {
        this.rw = rw;
    }
    public Set getKtps() {
        return this.ktps;
    }
    
    public void setKtps(Set ktps) {
        this.ktps = ktps;
    }
    public Set getAktakelahiransForNikayah() {
        return this.aktakelahiransForNikayah;
    }
    
    public void setAktakelahiransForNikayah(Set aktakelahiransForNikayah) {
        this.aktakelahiransForNikayah = aktakelahiransForNikayah;
    }
    public Set getBiodatapindahs() {
        return this.biodatapindahs;
    }
    
    public void setBiodatapindahs(Set biodatapindahs) {
        this.biodatapindahs = biodatapindahs;
    }
    public Set getAktanikahdetailsForNiksuami() {
        return this.aktanikahdetailsForNiksuami;
    }
    
    public void setAktanikahdetailsForNiksuami(Set aktanikahdetailsForNiksuami) {
        this.aktanikahdetailsForNiksuami = aktanikahdetailsForNiksuami;
    }
    public Set getPermohonandetails() {
        return this.permohonandetails;
    }
    
    public void setPermohonandetails(Set permohonandetails) {
        this.permohonandetails = permohonandetails;
    }
    public Set getBiodatacacats() {
        return this.biodatacacats;
    }
    
    public void setBiodatacacats(Set biodatacacats) {
        this.biodatacacats = biodatacacats;
    }
    public Set getKartukeluargas() {
        return this.kartukeluargas;
    }
    
    public void setKartukeluargas(Set kartukeluargas) {
        this.kartukeluargas = kartukeluargas;
    }
    public Set getAktakematians() {
        return this.aktakematians;
    }
    
    public void setAktakematians(Set aktakematians) {
        this.aktakematians = aktakematians;
    }
    public Set getAktanikahdetailsForNikistri() {
        return this.aktanikahdetailsForNikistri;
    }
    
    public void setAktanikahdetailsForNikistri(Set aktanikahdetailsForNikistri) {
        this.aktanikahdetailsForNikistri = aktanikahdetailsForNikistri;
    }
    public Set getAktakelahiransForNikibu() {
        return this.aktakelahiransForNikibu;
    }
    
    public void setAktakelahiransForNikibu(Set aktakelahiransForNikibu) {
        this.aktakelahiransForNikibu = aktakelahiransForNikibu;
    }




}


