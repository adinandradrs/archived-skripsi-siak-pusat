package org.siak.face;

import java.rmi.Remote;
import java.util.List;
import org.siak.data.Agama;

public interface AgamaFace extends Remote {

	public boolean save(Agama o) throws Exception;
	public boolean delete(Agama o) throws Exception;
	public boolean update(Agama o) throws Exception;
	public List<Agama> listData() throws Exception;
	
}
