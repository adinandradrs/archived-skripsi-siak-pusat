package org.siak.face;

import java.rmi.Remote;

import org.siak.data.Gruppengguna;
import org.siak.data.Pengguna;

public interface LoginFace extends Remote {
	
	public boolean loginKecamatan(Pengguna p) throws Exception;
	public boolean loginDUKCAPIL(Pengguna p) throws Exception;
	public boolean loginKelurahan(Pengguna p) throws Exception;
	public Gruppengguna dataPengguna(Pengguna p) throws Exception;
	
}
