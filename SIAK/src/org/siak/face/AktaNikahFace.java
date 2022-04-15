package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Aktanikah;

public interface AktaNikahFace extends Remote {
	
	public boolean save(Aktanikah o) throws Exception;
	public boolean delete(Aktanikah o) throws Exception;
	public boolean update(Aktanikah o) throws Exception;
	public List<Aktanikah> listData() throws Exception;

}
