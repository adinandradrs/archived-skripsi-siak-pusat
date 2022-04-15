package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Kecamatan;

public interface KecamatanFace extends Remote {
	
	public boolean save(Kecamatan o) throws Exception;
	public boolean delete(Kecamatan o) throws Exception;
	public boolean update(Kecamatan o) throws Exception;
	public List<Kecamatan> listData() throws Exception;

}
