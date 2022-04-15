package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Kartukeluargadetail;

public interface KartuKeluargaDetailFace extends Remote {
	
	public boolean save(Kartukeluargadetail o) throws Exception;
	public boolean delete(Kartukeluargadetail o) throws Exception;
	public boolean update(Kartukeluargadetail o) throws Exception;
	public List<Kartukeluargadetail> listData() throws Exception;

}
