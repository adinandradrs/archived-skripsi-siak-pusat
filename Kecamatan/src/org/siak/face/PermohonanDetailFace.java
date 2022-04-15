package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Permohonandetail;

public interface PermohonanDetailFace extends Remote {
	
	public boolean save(Permohonandetail o) throws Exception;
	public boolean update(Permohonandetail o) throws Exception;
	public boolean delete(Permohonandetail o) throws Exception;
	public List<Permohonandetail> listData() throws Exception;

}
