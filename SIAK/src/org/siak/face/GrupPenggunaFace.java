package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Gruppengguna;

public interface GrupPenggunaFace extends Remote {
	
	public boolean save(Gruppengguna o) throws Exception;
	public boolean delete(Gruppengguna o) throws Exception;
	public boolean update(Gruppengguna o) throws Exception;
	public List<Gruppengguna> listData() throws Exception;

}
