package org.siak.data;
// Generated Jul 28, 2012 10:43:28 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Permohonan generated by hbm2java
 */
public class Permohonan  implements java.io.Serializable {


     private Integer permohonanId;
     private String jenisPermohonan;
     private Set kartukeluargas = new HashSet(0);
     private Set aktakematians = new HashSet(0);
     private Set permohonandetails = new HashSet(0);
     private Set aktakelahirans = new HashSet(0);
     private Set ktps = new HashSet(0);
     private Set aktanikahs = new HashSet(0);

    public Permohonan() {
    }

	
    public Permohonan(String jenisPermohonan) {
        this.jenisPermohonan = jenisPermohonan;
    }
    public Permohonan(String jenisPermohonan, Set kartukeluargas, Set aktakematians, Set permohonandetails, Set aktakelahirans, Set ktps, Set aktanikahs) {
       this.jenisPermohonan = jenisPermohonan;
       this.kartukeluargas = kartukeluargas;
       this.aktakematians = aktakematians;
       this.permohonandetails = permohonandetails;
       this.aktakelahirans = aktakelahirans;
       this.ktps = ktps;
       this.aktanikahs = aktanikahs;
    }
   
    public Integer getPermohonanId() {
        return this.permohonanId;
    }
    
    public void setPermohonanId(Integer permohonanId) {
        this.permohonanId = permohonanId;
    }
    public String getJenisPermohonan() {
        return this.jenisPermohonan;
    }
    
    public void setJenisPermohonan(String jenisPermohonan) {
        this.jenisPermohonan = jenisPermohonan;
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
    public Set getPermohonandetails() {
        return this.permohonandetails;
    }
    
    public void setPermohonandetails(Set permohonandetails) {
        this.permohonandetails = permohonandetails;
    }
    public Set getAktakelahirans() {
        return this.aktakelahirans;
    }
    
    public void setAktakelahirans(Set aktakelahirans) {
        this.aktakelahirans = aktakelahirans;
    }
    public Set getKtps() {
        return this.ktps;
    }
    
    public void setKtps(Set ktps) {
        this.ktps = ktps;
    }
    public Set getAktanikahs() {
        return this.aktanikahs;
    }
    
    public void setAktanikahs(Set aktanikahs) {
        this.aktanikahs = aktanikahs;
    }




}


