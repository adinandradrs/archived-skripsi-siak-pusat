package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Cacat;
import org.siak.face.CacatFace;
import org.siak.util.Configuration;

@WebServlet("/CacatController")
public class CacatController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private CacatFace cacatFace;
	private JSONObject json;
	
    public CacatController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	String command = request.getParameter("command");
        	if(command.equalsIgnoreCase("load")){
        		this.loadData(out);
        	}
        	else if(command.equalsIgnoreCase("add")){
        		String nama = request.getParameter("nama");
        		this.addData(out, nama);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		int cacatID = Integer.parseInt(request.getParameter("cacatID"));
        		String nama = request.getParameter("nama");
        		this.editData(out, cacatID, nama);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		int cacatID = Integer.parseInt(request.getParameter("cacatID"));
        		this.deleteData(out, cacatID);
        	}
        }
        catch(Exception e){
        	
        }
        finally{
        	out.close();
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
			cacatFace = (CacatFace) registry.lookup("cacatCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				cacatFace = (CacatFace) registry.lookup("cacatCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listCacat = new LinkedList();
		String jsonString = "";
		try{
			List<Cacat> list = cacatFace.listData();
			for(Cacat c : list){
				json = new JSONObject();
				json.put("cacatId", c.getCacatId());
				json.put("nama", c.getNama());
				listCacat.add(json);
				jsonString = JSONValue.toJSONString(listCacat);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void addData(PrintWriter out, String nama){
		json = new JSONObject();
		boolean success = false;
		try {
			Cacat c = new Cacat();
			c.setNama(nama);
			success = cacatFace.save(c);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		} catch (Exception e) {
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void editData(PrintWriter out, int cacatID, String nama){
		json = new JSONObject();
		boolean success = false;
		try {
			Cacat c = new Cacat();
			c.setCacatId(cacatID);
			c.setNama(nama);
			success = cacatFace.update(c);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dilakukan perubahan");
		} catch (Exception e) {
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, int cacatID){
		json = new JSONObject();
		boolean success = false;
		try {
			Cacat c = new Cacat();
			c.setCacatId(cacatID);
			success = cacatFace.delete(c);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dihapus karena memiliki keterkaitan");
		} catch (Exception e) {
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}

}
