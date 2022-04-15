package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Aktanikahdetail;

public interface AktaNikahDetailFace extends Remote {
	
	public boolean save(Aktanikahdetail o) throws Exception;
	public boolean delete(Aktanikahdetail o) throws Exception;
	public boolean update(Aktanikahdetail o) throws Exception;
	public List<Aktanikahdetail> listData() throws Exception;

}
