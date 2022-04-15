package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Kartukeluarga;

public interface KartuKeluargaFace extends Remote {
	
	public String save(Kartukeluarga o) throws Exception;
	public boolean update(Kartukeluarga o) throws Exception;
	public boolean delete(Kartukeluarga o) throws Exception;
	public List<Kartukeluarga> listData() throws Exception;

}
