package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Grup;

public interface GrupFace extends Remote {
	
	public boolean save(Grup o) throws Exception;
	public boolean delete(Grup o) throws Exception;
	public boolean update(Grup o) throws Exception;
	public List<Grup> listData() throws Exception;

}
