package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.siak.data.Kartukeluargadetail;
import org.siak.face.AktaNikahFace;
import org.siak.face.KartuKeluargaDetailFace;
import org.siak.util.Configuration;

@WebServlet("/PendudukDetailController")
public class PendudukDetailController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private JSONObject json;
	private Registry registry;
	private KartuKeluargaDetailFace kkDetailFace;
	private AktaNikahFace aktaNikahFace;
	
    public PendudukDetailController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	String nik = request.getParameter("nik");
        	this.loadData(out, nik);
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
			kkDetailFace = (KartuKeluargaDetailFace) registry.lookup("kkDetailCore");
			aktaNikahFace = (AktaNikahFace) registry.lookup("aktaNikahCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				kkDetailFace = (KartuKeluargaDetailFace) registry.lookup("kkDetailCore");
				aktaNikahFace = (AktaNikahFace) registry.lookup("aktaNikahCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadData(PrintWriter out, String nik){
		try{
			List<Kartukeluargadetail> kkd = kkDetailFace.listData();
			for (Kartukeluargadetail kd : kkd) {
				
			}
		}
		catch(Exception e){
			
		}
	}

}
