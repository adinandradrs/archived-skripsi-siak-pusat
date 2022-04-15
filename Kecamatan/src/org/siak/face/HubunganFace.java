package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Hubungan;

public interface HubunganFace extends Remote {
	
	public boolean save(Hubungan o) throws Exception;
	public boolean delete(Hubungan o) throws Exception;
	public boolean update(Hubungan o) throws Exception;
	public List<Hubungan> listData() throws Exception;

}
