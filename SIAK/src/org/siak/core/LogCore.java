package org.siak.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.siak.face.LogFace;

public class LogCore extends UnicastRemoteObject implements LogFace  {

	private static final long serialVersionUID = 1L;

	public LogCore() throws Exception{
		
	}

	@Override
	public List<String> listData() throws Exception {
		List<String> list = new ArrayList<String>();
		File path = new File("log");
		File files[];
	    files = path.listFiles();
	    Arrays.sort(files);
	    for (int i = 0, n = files.length; i < n; i++)
	      list.add(files[i].toString());
		return list;
	}

	@Override
	public String logContent(String file) throws Exception {
		String content = "";
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String currentLine;
			while ((currentLine = br.readLine()) != null)
				content += currentLine+"<br/>";
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return content;
	}
	
}
