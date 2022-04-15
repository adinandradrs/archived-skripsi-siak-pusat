package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Biodatacacat;

public interface BiodataCacatFace extends Remote {
	
	public boolean save(Biodatacacat o) throws Exception;
	public boolean delete(Biodatacacat o) throws Exception;
	public boolean update(Biodatacacat o) throws Exception;
	public List<Biodatacacat> listData() throws Exception;

}
