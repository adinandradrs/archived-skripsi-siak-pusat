package org.siak.face;

import java.rmi.Remote;

import org.siak.data.Gruppengguna;
import org.siak.data.Kota;
import org.siak.data.Pengguna;

public interface LoginFace extends Remote {
	
	public boolean loginDUKCAPIL(Pengguna p) throws Exception;
	public boolean loginKecamatan(Pengguna p) throws Exception;
	public boolean loginKelurahan(Pengguna p) throws Exception;
	public boolean loginKota(Kota k) throws Exception;
	public Gruppengguna dataPengguna(Pengguna p) throws Exception;
	public Kota dataKota(Kota k) throws Exception;
	
}
