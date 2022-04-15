package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Pengguna;

public interface PenggunaFace extends Remote {
	
	public boolean save(Pengguna o) throws Exception;
	public boolean delete(Pengguna o) throws Exception;
	public boolean update(Pengguna o) throws Exception;
	public List<Pengguna> listData() throws Exception;

}
