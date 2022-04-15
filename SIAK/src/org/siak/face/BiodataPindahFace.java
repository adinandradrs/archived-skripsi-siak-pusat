package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Biodatapindah;

public interface BiodataPindahFace extends Remote {
	
	public boolean save(Biodatapindah o) throws Exception;
	public boolean delete(Biodatapindah o) throws Exception;
	public boolean update(Biodatapindah o) throws Exception;
	public List<Biodatapindah> listData() throws Exception;
	

}
