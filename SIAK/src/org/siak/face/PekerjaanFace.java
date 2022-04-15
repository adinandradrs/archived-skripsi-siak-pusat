package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Pekerjaan;

public interface PekerjaanFace extends Remote {
	
	public boolean save(Pekerjaan o) throws Exception;
	public boolean update(Pekerjaan o) throws Exception;
	public boolean delete(Pekerjaan o) throws Exception;
	public List<Pekerjaan> listData() throws Exception;

}
