package org.siak.face;

import java.rmi.Remote;
import java.util.List;
import org.siak.data.Cacat;

public interface CacatFace extends Remote {
	
	public boolean save(Cacat o) throws Exception;
	public boolean delete(Cacat o) throws Exception;
	public boolean update(Cacat o) throws Exception;
	public List<Cacat> listData() throws Exception;

}
