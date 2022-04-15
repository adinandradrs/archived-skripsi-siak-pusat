package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.face.LogFace;
import org.siak.util.Configuration;

@WebServlet("/LogController")
public class LogController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private LogFace logFace;
	private JSONObject json;
	
    public LogController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	String command = request.getParameter("command");
        	if(command.equalsIgnoreCase("load")){
        		String select = request.getParameter("select");
        		if(select.equalsIgnoreCase("file")){
        			this.loadDataFile(out);
        		}
        		else if(select.equalsIgnoreCase("log")){
        			String file = request.getParameter("file");
        			this.loadDataLog(out, file);
        		}
        	}
        }
        catch(Exception e){
        	
        }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}

	private void bindRegistry(PrintWriter out){
		String path = getServletContext().getRealPath("/Configuration/web.ini");
		String ipService1 = Configuration.file(path).get("Service", "ipService1");
		String ipService2 = Configuration.file(path).get("Service", "ipService2");
		int port = Integer.parseInt(Configuration.file(path).get("Service", "port"));
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			logFace = (LogFace) registry.lookup("logCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				logFace = (LogFace) registry.lookup("logCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadDataFile(PrintWriter out){
		List listLog = new ArrayList();
		String jsonString = "";
		try{
			List<String> list = logFace.listData();
			for (String s : list) {
				json = new JSONObject();
				json.put("file", s);
				listLog.add(json);
				jsonString = JSONValue.toJSONString(listLog);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDataLog(PrintWriter out, String file){
		json = new JSONObject();
		try{
			String logContent = logFace.logContent(file);
			json.put("success", true);
			json.put("log", logContent);
			out.print(json);
		}
		catch(Exception e){
			
		}
	}
	
}
