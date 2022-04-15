package org.siak.util;

import java.io.*;
import java.util.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	private static Date date = null;
    private static Format formatter;

    public static void createLog(String content, String opt, String type, boolean includeTime) 
    {
    	try
        {
            formatter = new SimpleDateFormat("dd-MM-yyyy");
            date = new Date();
            String title = "log/"+formatter.format(date)+Calendar.getInstance().getTime().getDate()+"."+type;
            String logContent = "";
            String time = getTimeMilis();
            if(includeTime == true)
            	logContent = time + " | ";
            if(opt.equals(""))
            	logContent += content;
            else
            	logContent += opt  +" | "+ content;
            logContent += "\n";
            File log = new File(title);
            FileWriter logWriter = new FileWriter(log,true);
            logWriter.write(logContent);
            logWriter.flush();
            logWriter.close();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public static String getTimeMilis(){
        date = new Date();
        formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        return formatter.format(date);
    }

}
