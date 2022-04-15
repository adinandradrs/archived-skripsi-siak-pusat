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
import org.siak.data.Pekerjaan;
import org.siak.face.PekerjaanFace;
import org.siak.util.Configuration;

@WebServlet("/PekerjaanController")
public class PekerjaanController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private PekerjaanFace pekerjaanFace;
	private JSONObject json;
	
    public PekerjaanController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        bindRegistry(out);
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
        		int pekerjaanID = Integer.parseInt(request.getParameter("pekerjaanID"));
        		this.editData(out, nama, pekerjaanID);
        	}
			else if(command.equalsIgnoreCase("delete")){
				int pekerjaanID = Integer.parseInt(request.getParameter("pekerjaanID"));
				this.deleteData(out, pekerjaanID);
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
			pekerjaanFace = (PekerjaanFace) registry.lookup("pekerjaanCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				pekerjaanFace = (PekerjaanFace) registry.lookup("pekerjaanCore");
			}
			catch(Exception ex){
				out.print(ex.getMessage());
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listPekerjaan = new LinkedList();
		String jsonString = "";
		try{
			List<Pekerjaan> list = pekerjaanFace.listData();
			for(Pekerjaan p : list){
				json = new JSONObject();
				json.put("pekerjaanId", p.getPekerjaanId());
				json.put("nama", p.getNama());
				listPekerjaan.add(json);
				jsonString = JSONValue.toJSONString(listPekerjaan);
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
			Pekerjaan p = new Pekerjaan();
			p.setNama(nama);
			success = pekerjaanFace.save(p);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void editData(PrintWriter out, String  nama, int pekerjaanID){
		boolean success = false;
		json = new JSONObject();
		try{
			Pekerjaan p = new Pekerjaan();
			p.setPekerjaanId(pekerjaanID);
			p.setNama(nama);
			success = pekerjaanFace.update(p);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dilakukan perubahan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, int pekerjaanID){
		boolean success = false;
		json = new JSONObject();
		try{
			Pekerjaan p = new Pekerjaan();
			p.setPekerjaanId(pekerjaanID);
			success = pekerjaanFace.delete(p);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dihapus karena terdapat keterkaitan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}

}
