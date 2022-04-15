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
import org.siak.data.Agama;
import org.siak.face.AgamaFace;
import org.siak.util.Configuration;

@WebServlet("/AgamaController")
public class AgamaController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private AgamaFace agamaFace;
	private JSONObject json;

    public AgamaController() {
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
        		String nama = request.getParameter("nama");
        		int agamaID = Integer.parseInt(request.getParameter("agamaID"));
        		this.editData(out, nama, agamaID);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		int agamaID = Integer.parseInt(request.getParameter("agamaID"));
        		this.deleteData(out, agamaID);
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
        	agamaFace = (AgamaFace) registry.lookup("agamaCore");
        }
        catch(Exception e){
        	try{
            	registry = LocateRegistry.getRegistry(ipService2, port);
            	agamaFace = (AgamaFace) registry.lookup("agamaCore");
            }
            catch(Exception ex){
            	out.print(ex.getMessage());
            }
        }
	}
	
	private void loadData(PrintWriter out){
		List listAgama = new LinkedList();
		String jsonString = "";
		try{
			List<Agama> list = agamaFace.listData();
			for(Agama a : list){
				json = new JSONObject();
				json.put("agamaId", a.getAgamaId());
				json.put("nama", a.getNama());
				listAgama.add(json);
				jsonString = JSONValue.toJSONString(listAgama);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			json = new JSONObject();
			json.put("agamaId", 0);
			json.put("nama", "Maaf untuk sementara data tidak dapat ditampilkan");
			listAgama.add(json);
			jsonString = JSONValue.toJSONString(listAgama);
			out.print(jsonString);
		}
	}
	
	private void addData(PrintWriter out, String nama){
		json = new JSONObject();
		String jsonString = "";
		boolean success = false;
		try{
			Agama agama = new Agama();
			agama.setNama(nama);
			success = agamaFace.save(agama);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}

	private void editData(PrintWriter out, String nama, int agamaID){
		json = new JSONObject();
		String jsonString = "";
		boolean success = false;
		try{
			Agama agama = new Agama();
			agama.setAgamaId(agamaID);
			agama.setNama(nama);
			success = agamaFace.update(agama);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dilakukan perubahan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, int agamaID){
		json = new JSONObject();
		String jsonString = "";
		boolean success = false;
		try{
			Agama agama = new Agama();
			agama.setAgamaId(agamaID);
			success = agamaFace.delete(agama);
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
