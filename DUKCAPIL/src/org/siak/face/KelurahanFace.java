package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Kelurahan;

public interface KelurahanFace extends Remote {
	
	public boolean save(Kelurahan o) throws Exception;
	public boolean update(Kelurahan o) throws Exception;
	public boolean delete(Kelurahan o) throws Exception;
	public List<Kelurahan> listData() throws Exception;

}
