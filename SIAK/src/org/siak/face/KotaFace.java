package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Kota;

public interface KotaFace extends Remote {
	
	public boolean save(Kota k) throws Exception;
	public boolean update(Kota k) throws Exception;
	public boolean delete(Kota k) throws Exception;
	public List<Kota> listData() throws Exception;

}
