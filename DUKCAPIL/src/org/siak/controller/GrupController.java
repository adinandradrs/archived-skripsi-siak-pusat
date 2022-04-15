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
import org.siak.data.Grup;
import org.siak.face.GrupFace;
import org.siak.util.Configuration;

@WebServlet("/GrupController")
public class GrupController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private JSONObject json;
	private GrupFace grupFace;
	
    public GrupController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry();
        try{
        	String command = request.getParameter("command");
        	if(command.equalsIgnoreCase("load")){
        		this.loadData(out);
        	}
        	else if(command.equalsIgnoreCase("add")){
        		String nama = request.getParameter("nama");
        		String aktifBool = request.getParameter("aktif");
        		int aktif = 0;
        		if(aktifBool.equalsIgnoreCase("true"))
        			aktif = 1;
        		else
        			aktif = 0;
        		this.addData(out, nama,aktif);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		int id = Integer.parseInt(request.getParameter("id"));
        		String nama = request.getParameter("nama");
        		String aktifBool = request.getParameter("aktif");
        		int aktif = 0;
        		if(aktifBool.equalsIgnoreCase("true"))
        			aktif = 1;
        		else
        			aktif = 0;
        		this.editData(out, id, nama, aktif);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		int id = Integer.parseInt(request.getParameter("id"));
        		this.deleteData(out,id);
        	}
        }
        catch(Exception e){
        	
        }
        finally{
        	out.close();
        }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	private void bindRegistry(){
		String path = getServletContext().getRealPath("/Configuration/web.ini");
    	String ipService1 = Configuration.file(path).get("Service", "ipService1");
    	String ipService2 = Configuration.file(path).get("Service", "ipService2");
    	int port = Integer.parseInt(Configuration.file(path).get("Service", "port"));
		try {
			registry = LocateRegistry.getRegistry(ipService1, port);
			grupFace = (GrupFace)registry.lookup("grupCore");
		} catch (Exception e) {
			try {
				registry = LocateRegistry.getRegistry(ipService2, port);
				grupFace = (GrupFace)registry.lookup("grupCore");
			} catch (Exception ex) {
				
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listGrup = new LinkedList();
		String jsonString = "";
		try{
			List<Grup> list = grupFace.listData();
			for(Grup g : list){
				json = new JSONObject();
				json.put("grupId", g.getGrupId());
				json.put("nama", g.getNama());
				if(g.getAktif() == 1)
					json.put("aktif", true);
				else 
					json.put("aktif", false);
				listGrup.add(json);
				jsonString = JSONValue.toJSONString(listGrup);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
	private void addData(PrintWriter out, String nama, int aktif){
		json = new JSONObject();
		boolean success = false;
		try{
			Grup g = new Grup();
			g.setNama(nama);
			g.setAktif(aktif);
			success = grupFace.save(g);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void editData(PrintWriter out, int id, String nama, int aktif){
		json = new JSONObject();
		boolean success = false;
		try{
			Grup g = new Grup();
			g.setNama(nama);
			g.setGrupId(id);
			g.setAktif(aktif);
			success = grupFace.update(g);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dilakukan perubahan");
		}
		catch(Exception e){
			json.put("success",success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, int id){
		json = new JSONObject();
		boolean success = false;
		try{
			Grup g = new Grup();
			g.setGrupId(id);
			success = grupFace.delete(g);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dihapus karena memiliki keterkaitan");
		}
		catch(Exception e){
			json.put("success",success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}

}
