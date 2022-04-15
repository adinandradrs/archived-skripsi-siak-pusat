package org.siak.face;

import java.rmi.Remote;
import java.util.List;

import org.siak.data.Hubungandetail;

public interface HubunganDetailFace extends Remote {
	
	public boolean save(Hubungandetail o) throws Exception;
	public boolean update(Hubungandetail o) throws Exception;
	public boolean delete(Hubungandetail o) throws Exception;
	public List<Hubungandetail> listData() throws Exception;

}
