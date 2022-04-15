package org.siak.face;

import java.rmi.Remote;
import java.util.List;

public interface LogFace extends Remote {

	public List<String> listData() throws Exception;		
	public String logContent(String file) throws Exception;
	
}
