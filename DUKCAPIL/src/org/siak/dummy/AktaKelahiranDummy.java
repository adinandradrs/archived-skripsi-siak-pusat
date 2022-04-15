package org.siak.dummy;

public class AktaKelahiranDummy {
	
	private String noAktaKelahiran;
	private String nama;
	private String tanggalLahir;
	private String tanggalBuat;
	private String tanggalCetak;
	private String anakKe;
	
	public String getAnakKe(){
		return this.anakKe;
	}
	
	public void setAnakKe(String anakKe){
		this.anakKe = anakKe;
	}
	
	public String getTanggalCetak() {
		return tanggalCetak;
	}
	public void setTanggalCetak(String tanggalCetak) {
		this.tanggalCetak = tanggalCetak;
	}
	public String getNoAktaKelahiran() {
		return noAktaKelahiran;
	}
	public void setNoAktaKelahiran(String noAktaKelahiran) {
		this.noAktaKelahiran = noAktaKelahiran;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getTanggalLahir() {
		return tanggalLahir;
	}
	public void setTanggalLahir(String tanggalLahir) {
		this.tanggalLahir = tanggalLahir;
	}
	public String getTanggalBuat() {
		return tanggalBuat;
	}
	public void setTanggalBuat(String tanggalBuat) {
		this.tanggalBuat = tanggalBuat;
	}
}
