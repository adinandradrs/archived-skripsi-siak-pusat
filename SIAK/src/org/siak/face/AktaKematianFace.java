package org.siak.face;

import java.rmi.Remote;
import java.util.List;
import org.siak.data.Aktakematian;

public interface AktaKematianFace extends Remote {
	
	public boolean save(Aktakematian o) throws Exception;
	public boolean delete(Aktakematian o) throws Exception;
	public boolean update(Aktakematian o) throws Exception;
	public List<Aktakematian> listData() throws Exception;

}
