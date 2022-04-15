package org.siak.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.siak.data.Biodata;
import org.siak.face.BiodataFace;
import org.siak.util.Configuration;

@WebServlet("/ImageFrame")
public class ImageFrame extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ImageFrame() {
        super();
    }
    
    private String ipService1;
	private String ipService2;
	private int port;
	private BiodataFace biodataFace;
	
	private void bindRegistry(){
		String path = getServletContext().getRealPath("/Configuration/web.ini");
    	ipService1 = Configuration.file(path).get("Service", "ipService1");
    	ipService2 = Configuration.file(path).get("Service", "ipService2");
    	port = Integer.parseInt(Configuration.file(path).get("Service", "port"));
		try{
        	Registry registry = LocateRegistry.getRegistry(ipService1, port);
        	biodataFace = (BiodataFace) registry.lookup("biodataCore");
		}
		catch(Exception e){
			try{
	        	Registry registry = LocateRegistry.getRegistry(ipService2, port);
	        	biodataFace = (BiodataFace) registry.lookup("biodataCore");
			}
			catch(Exception ex){
				
			}
		}
	}
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("image/jpeg");
    	this.bindRegistry();
        try{
        	String param = request.getParameter("nik");
        	String nik = param;
        	byte[] i = null;
        	for (Biodata b : biodataFace.listData()) {
				if(b.getNik().equals(nik)){
					i = b.getFoto();
				}
			}
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(i);
            outStream.flush();
        }
        catch(Exception e){
        }
        finally{
        }
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}
	
	private void writeImage(OutputStream o){
		
	}

}
