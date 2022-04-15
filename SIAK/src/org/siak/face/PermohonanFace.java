package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Permohonan;

public interface PermohonanFace extends Remote {
	
	public boolean save(Permohonan o) throws Exception;
	public boolean delete(Permohonan o) throws Exception;
	public boolean update(Permohonan o) throws Exception;
	public List<Permohonan> listData() throws Exception;

}
