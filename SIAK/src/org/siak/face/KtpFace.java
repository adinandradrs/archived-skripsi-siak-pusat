package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Ktp;

public interface KtpFace extends Remote {
	
	public boolean save(Ktp o) throws Exception;
	public boolean delete(Ktp o) throws Exception;
	public boolean update(Ktp o) throws Exception;
	public List<Ktp> listData() throws Exception;

}
