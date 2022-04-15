package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Biodata;

public interface BiodataFace extends Remote {

	public String save(Biodata o) throws Exception;
	public boolean delete(Biodata o) throws Exception;
	public boolean update(Biodata o) throws Exception;
	public List<Biodata> listData() throws Exception;
	
}
