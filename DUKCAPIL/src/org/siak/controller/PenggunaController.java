package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
import org.siak.data.Pengguna;
import org.siak.face.PenggunaFace;
import org.siak.util.Configuration;
import org.siak.util.Encryption;

import java.text.Format;
import java.text.SimpleDateFormat;

@WebServlet("/PenggunaController")
public class PenggunaController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private PenggunaFace penggunaFace;
	private JSONObject json;
	private Format formatter;
	
    public PenggunaController() {
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
        		String username = request.getParameter("username");
        		String nama = request.getParameter("nama");
        		String aktifBool = request.getParameter("aktif");
        		int aktif = 0;
        		if(aktifBool.equalsIgnoreCase("true"))
        			aktif = 1;
        		else 
        			aktif = 0;
        		this.addData(out,username, nama, aktif);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		String username = request.getParameter("username");
        		String nama = request.getParameter("nama");
        		String sandi = request.getParameter("sandi");
        		String waktuLogin = request.getParameter("waktuLogin");
        		String ipLogin = request.getParameter("ipLogin");
        		String aktifBool = request.getParameter("aktif");
        		int aktif = 0;
        		if(aktifBool.equalsIgnoreCase("true"))
        			aktif = 1;
        		else 
        			aktif = 0;
        		this.editData(out, username, nama, sandi, waktuLogin, ipLogin, aktif);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		String username = request.getParameter("username");
        		this.deleteData(out, username);
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
			penggunaFace = (PenggunaFace) registry.lookup("penggunaCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				penggunaFace = (PenggunaFace) registry.lookup("penggunaCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listPengguna = new LinkedList();
		String jsonString = "";
		formatter = new SimpleDateFormat("dd-MM-yyyy");
		try{
			List<Pengguna> list = penggunaFace.listData();
			for (Pengguna p : list) {
				json = new JSONObject();
				json.put("username", p.getUsername());
				json.put("nama", p.getNama());
				json.put("ipLogin", p.getIplogin());
				json.put("sandi", p.getSandi());
				json.put("waktuLogin",  formatter.format(p.getWaktuLogin()));
				if(p.getAktif() == 1)
					json.put("aktif", true);
				else
					json.put("aktif", false);
				listPengguna.add(json);
				jsonString = JSONValue.toJSONString(listPengguna);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void addData(PrintWriter out, String username, String nama, int aktif){
		json = new JSONObject();
		boolean success = false;
		try{
			Pengguna p = new Pengguna();
			p.setUsername(username);
			p.setNama(nama);
			p.setSandi(Encryption.SHA1("admin"));
			p.setAktif(aktif);
			p.setIplogin("Belum login");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse("2000-01-01");
			p.setWaktuLogin(date);
			success = penggunaFace.save(p);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void editData(PrintWriter out, String username, String nama, String sandi, String waktuLogin, String ipLogin, int aktif){
		json = new JSONObject();
		boolean success = false;
		try{
			Pengguna p = new Pengguna();
			p.setUsername(username);
			p.setAktif(aktif);
			p.setSandi(sandi);
			p.setNama(nama);
			p.setIplogin(ipLogin);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date date = formatter.parse(waktuLogin);
			p.setWaktuLogin(date);
			success = penggunaFace.update(p);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dilakukan perubahan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, String username){
		json = new JSONObject();
		boolean success = false;
		try{
			Pengguna p = new Pengguna();
			p.setUsername(username);
			success = penggunaFace.delete(p);
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
