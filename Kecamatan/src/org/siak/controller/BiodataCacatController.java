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
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Biodata;
import org.siak.data.Biodatacacat;
import org.siak.data.Cacat;
import org.siak.data.Gruppengguna;
import org.siak.face.BiodataCacatFace;
import org.siak.util.Configuration;

@WebServlet("/BiodataCacatController")
public class BiodataCacatController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private BiodataCacatFace biodataCacatFace;
	private JSONObject json;
	private HttpSession session;
	
    public BiodataCacatController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	String command = request.getParameter("command");
        	session = request.getSession(false);
        	Gruppengguna gp = (Gruppengguna) session.getAttribute("user");
        	int kecamatanID = gp.getKecamatanId();
        	if(command.equalsIgnoreCase("load")){
        		this.loadData(out,kecamatanID);
        	}
        	else if(command.equalsIgnoreCase("add")){
        		String nik = request.getParameter("nik");
        		int cacatID = Integer.parseInt(request.getParameter("cacatID"));
        		this.addData(out, nik, cacatID);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		int biodataCacatID = Integer.parseInt(request.getParameter("biodataCacatID"));
        		String nik = request.getParameter("nik");
        		int cacatID = Integer.parseInt(request.getParameter("cacatID"));
        		String aktifBool = request.getParameter("aktif");
        		int aktif = 0;
        		if(aktifBool.equalsIgnoreCase("true"))
        			aktif = 1;
        		else
        			aktif = 0;
        		this.editData(out, biodataCacatID, nik, cacatID, aktif);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		int biodataCacatID = Integer.parseInt(request.getParameter("biodataCacatID"));
        		this.deleteData(out, biodataCacatID);
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
    		biodataCacatFace = (BiodataCacatFace) registry.lookup("biodataCacatCore");
    	}
    	catch(Exception e){
    		try{
    			registry = LocateRegistry.getRegistry(ipService2, port);
        		biodataCacatFace = (BiodataCacatFace) registry.lookup("biodataCacatCore");
    		}
    		catch(Exception ex){
    			out.print(ex.getMessage());
    		}
    	}
	}
	
	private void loadData(PrintWriter out, int kecamatanID){
		List listBiodataCacat = new ArrayList();
		String jsonString = "";
		try{
			List<Biodatacacat> list = biodataCacatFace.listData();
			for(Biodatacacat bc : list){
				if(bc.getBiodata().getKelurahan().getKecamatan().getKecamatanId().equals(kecamatanID)){
					json = new JSONObject();
					json.put("biodataCacatId", bc.getBiodataCacatId());
					json.put("nik", bc.getBiodata().getNik());
					json.put("cacatNama", bc.getCacat().getNama());
					json.put("cacatId", bc.getCacat().getCacatId());
					if(bc.getAktif() == 1)
						json.put("aktif", true);
					else
						json.put("aktif", false);
					listBiodataCacat.add(json);
					jsonString = JSONValue.toJSONString(listBiodataCacat);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
	private void addData(PrintWriter out, String nik, int cacatID){
		boolean success = false;
		json = new JSONObject();
		try{
			Biodatacacat bc = new Biodatacacat();
			Biodata b = new Biodata();
			Cacat c = new Cacat();
			b.setNik(nik);
			c.setCacatId(cacatID);
			bc.setBiodata(b);
			bc.setAktif(1);
			bc.setCacat(c);
			success = biodataCacatFace.save(bc);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void editData(PrintWriter out, int biodataCacatID, String nik, int cacatID, int aktif){
		boolean success = false;
		json = new JSONObject();
		try{
			Biodatacacat bc = new Biodatacacat();
			Biodata b = new Biodata();
			Cacat c = new Cacat();
			bc.setBiodataCacatId(biodataCacatID);
			b.setNik(nik);
			c.setCacatId(cacatID);
			bc.setBiodata(b);
			bc.setAktif(aktif);
			bc.setCacat(c);
			success = biodataCacatFace.update(bc);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dilakukan perubahan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, int biodataCacatID){
		boolean success = false;
		json = new JSONObject();
		try{
			Biodatacacat bc = new Biodatacacat();
			bc.setBiodataCacatId(biodataCacatID);
			success = biodataCacatFace.delete(bc);
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
