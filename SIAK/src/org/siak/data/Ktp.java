package org.siak.data;
// Generated Jul 28, 2012 10:43:28 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Ktp generated by hbm2java
 */
public class Ktp  implements java.io.Serializable {


     private String nik;
     private Permohonan permohonan;
     private Biodata biodata;
     private Date tanggalBuat;
     private int jenisBuat;
     private String expired;

    public Ktp() {
    }

    public Ktp(String nik, Permohonan permohonan, Biodata biodata, Date tanggalBuat, int jenisBuat, String expired) {
       this.nik = nik;
       this.permohonan = permohonan;
       this.biodata = biodata;
       this.tanggalBuat = tanggalBuat;
       this.jenisBuat = jenisBuat;
       this.expired = expired;
    }
   
    public String getNik() {
        return this.nik;
    }
    
    public void setNik(String nik) {
        this.nik = nik;
    }
    public Permohonan getPermohonan() {
        return this.permohonan;
    }
    
    public void setPermohonan(Permohonan permohonan) {
        this.permohonan = permohonan;
    }
    public Biodata getBiodata() {
        return this.biodata;
    }
    
    public void setBiodata(Biodata biodata) {
        this.biodata = biodata;
    }
    public Date getTanggalBuat() {
        return this.tanggalBuat;
    }
    
    public void setTanggalBuat(Date tanggalBuat) {
        this.tanggalBuat = tanggalBuat;
    }
    public int getJenisBuat() {
        return this.jenisBuat;
    }
    
    public void setJenisBuat(int jenisBuat) {
        this.jenisBuat = jenisBuat;
    }
    public String getExpired() {
        return this.expired;
    }
    
    public void setExpired(String expired) {
        this.expired = expired;
    }




}


