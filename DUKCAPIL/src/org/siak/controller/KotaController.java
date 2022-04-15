package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Kota;
import org.siak.data.KotaId;
import org.siak.face.KotaFace;
import org.siak.util.Configuration;
import org.siak.util.Encryption;

@WebServlet("/KotaController")
public class KotaController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private JSONObject json;
	private KotaFace kotaFace;
	private Format formatter;
	
    public KotaController() {
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
        		String username = request.getParameter("username");
        		int aktif = 0;
        		String aktifBool = request.getParameter("aktif");
        		if(aktifBool.equalsIgnoreCase("true"))
        			aktif = 1;
        		else
        			aktif = 0;
        		this.addData(out, nama, username, aktif);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		int kotaID = Integer.parseInt(request.getParameter("kotaID"));
        		String nama = request.getParameter("nama");
        		String username = request.getParameter("username");
        		int aktif = 0;
        		String aktifBool = request.getParameter("aktif");
        		String waktuLogin = request.getParameter("waktuLogin");
        		String sandi = request.getParameter("sandi");
        		if(aktifBool.equalsIgnoreCase("true"))
        			aktif = 1;
        		else
        			aktif = 0;
        		this.editData(out, kotaID, nama, username, waktuLogin, sandi, aktif);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		int kotaID = Integer.parseInt(request.getParameter("kotaID"));
        		String username = request.getParameter("username");
        		this.deleteData(out, kotaID, username);
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
	
	private void bindRegistry(){
		String path = getServletContext().getRealPath("/Configuration/web.ini");
    	String ipService1 = Configuration.file(path).get("Service", "ipService1");
    	String ipService2 = Configuration.file(path).get("Service", "ipService2");
    	int port = Integer.parseInt(Configuration.file(path).get("Service", "port"));
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			kotaFace = (KotaFace) registry.lookup("kotaCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				kotaFace = (KotaFace) registry.lookup("kotaCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listKota = new LinkedList();
		String jsonString = "";
		formatter = new SimpleDateFormat("dd-MM-yyyy");
		try{
			List<Kota> list = kotaFace.listData();
			for (Kota k : list) {
				json = new JSONObject();
				json.put("kotaId", k.getKotaId());
				json.put("username", k.getUsername());
				json.put("nama", k.getNama());
				json.put("sandi", k.getSandi());
				json.put("waktuLogin",  formatter.format(k.getWaktuLogin()));
				if(k.getAktif() == 1)
					json.put("aktif", true);
				else
					json.put("aktif", false);
				listKota.add(json);
				jsonString = JSONValue.toJSONString(listKota);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}

	private void addData(PrintWriter out, String nama, String username, int aktif){
		json = new JSONObject();
		boolean success = false;
		try{
			Kota k = new Kota();
			KotaId ki = new KotaId();
			ki.setUsername(username);
			k.setAktif(aktif);
			k.setKotaId(1);
			k.setNama(nama);
			k.setUsername(username);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse("2000-01-01");
			k.setWaktuLogin(date);
			k.setSandi(Encryption.SHA1("admin"));
			success = kotaFace.save(k);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void editData(PrintWriter out, int kotaID, String nama, String username, String waktuLogin, String sandi, int aktif){
		json = new JSONObject();
		boolean success = false;
		try{
			Kota k = new Kota();
			KotaId ki = new KotaId();
			ki.setKotaId(kotaID);
			ki.setUsername(username);
			k.setAktif(aktif);
			k.setKotaId(kotaID);
			k.setNama(nama);
			k.setUsername(username);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date date = formatter.parse(waktuLogin);
			k.setWaktuLogin(date);
			k.setSandi(sandi);
			success = kotaFace.update(k);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dilakukan perubahan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, int kotaID, String username){
		json = new JSONObject();
		boolean success = false;
		try{
			Kota k = new Kota();
			KotaId ki = new KotaId();
			ki.setKotaId(kotaID);
			ki.setUsername(username);
			k.setKotaId(kotaID);
			success = kotaFace.delete(k);
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
