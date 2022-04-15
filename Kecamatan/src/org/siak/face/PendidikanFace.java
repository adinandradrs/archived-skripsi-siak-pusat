package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Pendidikan;

public interface PendidikanFace extends Remote {
	
	public boolean save(Pendidikan o) throws Exception;
	public boolean delete(Pendidikan o) throws Exception;
	public boolean update(Pendidikan o) throws Exception;
	public List<Pendidikan> listData() throws Exception;

}
