package org.siak.data;
// Generated Jul 28, 2012 10:43:28 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Kecamatan generated by hbm2java
 */
public class Kecamatan  implements java.io.Serializable {


     private Integer kecamatanId;
     private String nama;
     private int aktif;
     private int kodeDaerah;
     private Set kelurahans = new HashSet(0);

    public Kecamatan() {
    }

	
    public Kecamatan(String nama, int aktif, int kodeDaerah) {
        this.nama = nama;
        this.aktif = aktif;
        this.kodeDaerah = kodeDaerah;
    }
    public Kecamatan(String nama, int aktif, int kodeDaerah, Set kelurahans) {
       this.nama = nama;
       this.aktif = aktif;
       this.kodeDaerah = kodeDaerah;
       this.kelurahans = kelurahans;
    }
   
    public Integer getKecamatanId() {
        return this.kecamatanId;
    }
    
    public void setKecamatanId(Integer kecamatanId) {
        this.kecamatanId = kecamatanId;
    }
    public String getNama() {
        return this.nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    public int getAktif() {
        return this.aktif;
    }
    
    public void setAktif(int aktif) {
        this.aktif = aktif;
    }
    public int getKodeDaerah() {
        return this.kodeDaerah;
    }
    
    public void setKodeDaerah(int kodeDaerah) {
        this.kodeDaerah = kodeDaerah;
    }
    public Set getKelurahans() {
        return this.kelurahans;
    }
    
    public void setKelurahans(Set kelurahans) {
        this.kelurahans = kelurahans;
    }




}


