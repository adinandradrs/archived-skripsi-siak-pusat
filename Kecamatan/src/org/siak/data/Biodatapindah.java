package org.siak.data;
// Generated Jul 28, 2012 10:43:28 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Biodatapindah generated by hbm2java
 */
public class Biodatapindah  implements java.io.Serializable {


     private String nik;
     private Kota kota;
     private Biodata biodata;
     private Date tanggalPengajuan;

    public Biodatapindah() {
    }

    public Biodatapindah(String nik, Kota kota, Biodata biodata, Date tanggalPengajuan) {
       this.nik = nik;
       this.kota = kota;
       this.biodata = biodata;
       this.tanggalPengajuan = tanggalPengajuan;
    }
   
    public String getNik() {
        return this.nik;
    }
    
    public void setNik(String nik) {
        this.nik = nik;
    }
    public Kota getKota() {
        return this.kota;
    }
    
    public void setKota(Kota kota) {
        this.kota = kota;
    }
    public Biodata getBiodata() {
        return this.biodata;
    }
    
    public void setBiodata(Biodata biodata) {
        this.biodata = biodata;
    }
    public Date getTanggalPengajuan() {
        return this.tanggalPengajuan;
    }
    
    public void setTanggalPengajuan(Date tanggalPengajuan) {
        this.tanggalPengajuan = tanggalPengajuan;
    }




}


