package org.siak.util;

import java.io.File;

import org.ini4j.Wini;

public class Configuration {
	
	public static Wini file(String path){
		Wini fileINI = null;
		try{
			fileINI = new Wini(new File(path));
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return fileINI;
	}

}
