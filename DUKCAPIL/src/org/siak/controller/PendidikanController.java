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
import org.siak.data.Pendidikan;
import org.siak.face.PendidikanFace;
import org.siak.util.Configuration;

@WebServlet("/PendidikanController")
public class PendidikanController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private PendidikanFace pendidikanFace;
	private JSONObject json;
	
    public PendidikanController() {
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
        		this.addData(out,nama);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		String nama = request.getParameter("nama");
        		int pendidikanID = Integer.parseInt(request.getParameter("pendidikanID"));
        		this.editData(out, nama, pendidikanID);
        	}
			else if(command.equalsIgnoreCase("delete")){
				int pendidikanID = Integer.parseInt(request.getParameter("pendidikanID"));
				this.deleteData(out, pendidikanID);
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
			pendidikanFace = (PendidikanFace)registry.lookup("pendidikanCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				pendidikanFace = (PendidikanFace)registry.lookup("pendidikanCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listPendidikan = new LinkedList();
		String jsonString = "";
		try{
			List<Pendidikan> list = pendidikanFace.listData();
			for(Pendidikan p : list){
				json = new JSONObject();
				json.put("pendidikanId", p.getPendidikanId());
				json.put("nama", p.getNama());
				listPendidikan.add(json);
				jsonString = JSONValue.toJSONString(listPendidikan);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void addData(PrintWriter out, String nama){
		boolean success = false;
		json = new JSONObject();
		try{
			Pendidikan p = new Pendidikan();
			p.setNama(nama);
			success = pendidikanFace.save(p);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}

	private void editData(PrintWriter out, String nama, int pendidikanID){
		boolean success = false;
		json = new JSONObject();
		try{
			Pendidikan p = new Pendidikan();
			p.setNama(nama);
			p.setPendidikanId(pendidikanID);
			success = pendidikanFace.update(p);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dilakukan perubahan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, int pendidikanID){
		boolean success = false;
		json = new JSONObject();
		try{
			Pendidikan p = new Pendidikan();
			p.setPendidikanId(pendidikanID);
			success = pendidikanFace.delete(p);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dihapus karena memiliki keterkaitan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}

}
