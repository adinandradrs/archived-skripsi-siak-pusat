package org.siak.dummy;

import java.util.List;

public class KartuKeluargaMasterDummy {
	
	private String noKK;
	private String nama;
	private String alamat;
	private String rtrw;
	private String kelurahan;
	private String kecamatan;
	
	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getRtrw() {
		return rtrw;
	}

	public void setRtrw(String rtrw) {
		this.rtrw = rtrw;
	}

	public String getKelurahan() {
		return kelurahan;
	}

	public void setKelurahan(String kelurahan) {
		this.kelurahan = kelurahan;
	}

	public String getKecamatan() {
		return kecamatan;
	}

	public void setKecamatan(String kecamatan) {
		this.kecamatan = kecamatan;
	}

	public List<KartuKeluargaBawahDummy> getBawah() {
		return bawah;
	}

	private List<KartuKeluargaAtasDummy> atas;
	private List<KartuKeluargaBawahDummy> bawah;

	public List<KartuKeluargaBawahDummy> getbawah() {
		return bawah;
	}

	public void setBawah(List<KartuKeluargaBawahDummy> bawah) {
		this.bawah = bawah;
	}
	
	public List<KartuKeluargaAtasDummy> getAtas() {
		return atas;
	}

	public void setAtas(List<KartuKeluargaAtasDummy> atas) {
		this.atas = atas;
	}

	public String getNoKK() {
		return noKK;
	}

	public void setNoKK(String noKK) {
		this.noKK = noKK;
	}

}
