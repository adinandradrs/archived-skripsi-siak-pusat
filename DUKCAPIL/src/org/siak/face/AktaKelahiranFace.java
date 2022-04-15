package org.siak.face;

import java.rmi.Remote;
import java.util.List;
import org.siak.data.Aktakelahiran;

public interface AktaKelahiranFace extends Remote {
	
	public boolean save(Aktakelahiran o) throws Exception;
	public boolean delete(Aktakelahiran o) throws Exception;
	public boolean update(Aktakelahiran o) throws Exception;
	public List<Aktakelahiran> listData() throws Exception;

}
