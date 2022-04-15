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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Kecamatan;
import org.siak.data.Kelurahan;
import org.siak.face.KecamatanFace;
import org.siak.face.KelurahanFace;
import org.siak.util.Configuration;

@WebServlet("/WilayahController")
public class WilayahController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private KecamatanFace kecamatanFace;
	private KelurahanFace kelurahanFace;
	private JSONObject json;

    public WilayahController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	String command = request.getParameter("command");
        	if(command.equalsIgnoreCase("load")){
        		String select = request.getParameter("select");
        		if(select == null){
        			this.loadData(out);
        		}
        		else if(select.equalsIgnoreCase("kecamatan")){
        			this.loadDataKecamatan(out);
        		}
        		else if(select.equalsIgnoreCase("kelurahan")){
        			int kecamatanID = Integer.parseInt(request.getParameter("kecamatanID"));
        			this.loadDataKelurahan(out, kecamatanID);
        		}
        	}
        	else if(command.equalsIgnoreCase("add")){
        		String scope = request.getParameter("scope");
        		String nama = request.getParameter("nama");
        		int kodeDaerah = Integer.parseInt(request.getParameter("kodeDaerah"));
        		String aktifBool = request.getParameter("aktif");
        		int aktif = 0;
        		if(aktifBool.equalsIgnoreCase("true"))
        			aktif = 1;
        		else
        			aktif = 0;
        		Integer kecamatanID = 0;
        		try{
        			kecamatanID = Integer.parseInt(request.getParameter("kecamatanID"));
        		}
        		catch(Exception e){
        			
        		}
        		this.addData(out, scope, nama, kodeDaerah, aktif, kecamatanID);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		String scope = request.getParameter("scope");
        		String nama = request.getParameter("nama");
        		
        		int kodeDaerah = Integer.parseInt(request.getParameter("kodeDaerah"));
        		String aktifBool = request.getParameter("aktif");
        		int aktif = 0;
        		if(aktifBool.equalsIgnoreCase("true"))
        			aktif = 1;
        		else
        			aktif = 0;
        		int kecamatanID = 0, kelurahanID = 0;
        		try{
        			kecamatanID = Integer.parseInt(request.getParameter("kecamatanID"));
        			kelurahanID = Integer.parseInt(request.getParameter("kelurahanID"));
        		}
        		catch(Exception e){
        			
        		}
        		this.editData(out, scope, kecamatanID, nama, kodeDaerah, aktif, kelurahanID);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		String scope = request.getParameter("scope");
        		int ID = 0;
        		try{
        			ID = Integer.parseInt(request.getParameter("ID"));
        		}
        		catch(Exception e){
        			
        		}
        		this.deleteData(out, scope, ID);
        	}
        }
        catch(Exception e){
        	out.print(e);
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
        	kecamatanFace = (KecamatanFace) registry.lookup("kecamatanCore");
        	kelurahanFace = (KelurahanFace) registry.lookup("kelurahanCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
	        	kecamatanFace = (KecamatanFace) registry.lookup("kecamatanCore");
	        	kelurahanFace = (KelurahanFace) registry.lookup("kelurahanCore");
			}
			catch(Exception ex){
				out.print(ex.getMessage());
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listWilayah = new LinkedList();
		String jsonString = "";
		try{
			List<Kecamatan> listParent = kecamatanFace.listData();
			List<Kelurahan> listChildren = kelurahanFace.listData();
			JSONObject jsonChild = null;
			JSONArray jsonArray = null;
			for(Kecamatan k1 : listParent){
				jsonArray = new JSONArray();
				for(Kelurahan k2 : listChildren){
					if(k2.getKecamatan().getKecamatanId().equals(k1.getKecamatanId())){
						jsonChild = new JSONObject();
						jsonChild.put("id", "c"+k2.getKelurahanId());
						jsonChild.put("text", k2.getNama());
						jsonChild.put("iconCls", "kelurahan");
						jsonChild.put("leaf", true);
						jsonArray.add(jsonChild);
					}
				}
				json = new JSONObject();
				json.put("children", jsonArray);
				json.put("id", "p"+k1.getKecamatanId());
				json.put("text", k1.getNama());
				json.put("iconCls", "kecamatan");
				json.put("leaf", false);
				json.put("expanded", false);
				listWilayah.add(json);
				jsonString = JSONValue.toJSONString(listWilayah);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
	private void loadDataKecamatan(PrintWriter out){
		List listKecamatan = new LinkedList();
		String jsonString = "";
		try{
			List<Kecamatan> list = kecamatanFace.listData();
			for(Kecamatan k : list){
				json = new JSONObject();
				json.put("kecamatanId", k.getKecamatanId());
				json.put("nama", k.getNama());
				if(k.getAktif() == 1){
					json.put("aktif", true);
				}
				else if (k.getAktif() == 0){
					json.put("aktif", false);
				}
				json.put("kodeDaerah", k.getKodeDaerah());
				listKecamatan.add(json);
				jsonString = JSONValue.toJSONString(listKecamatan);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());	
		}
	}

	private void loadDataKelurahan(PrintWriter out, int kecamatanID){
		List listKelurahan = new LinkedList();
		String jsonString = "";
		try{
			List<Kelurahan> list = kelurahanFace.listData();
			for(Kelurahan k : list){
				if(k.getKecamatan().getKecamatanId() == kecamatanID){
					json = new JSONObject();
					json.put("kelurahanId", k.getKelurahanId());
					json.put("nama", k.getNama());
					if(k.getAktif() == 1){
						json.put("aktif", true);
					}
					else if (k.getAktif() == 0){
						json.put("aktif", false);
					}
					json.put("kodeDaerah", k.getKodeDaerah());
					listKelurahan.add(json);
					jsonString = JSONValue.toJSONString(listKelurahan);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e);
		}
	}
	
	private void addData(PrintWriter out, String scope, String nama, int kodeDaerah, int aktif, int kecamatanID){
		json = new JSONObject();
		String jsonString = "";
		boolean success = false;
		try{
			if(scope.equalsIgnoreCase("kecamatan")){
				Kecamatan kecamatan = new Kecamatan();
				kecamatan.setAktif(aktif);
				kecamatan.setNama(nama);
				kecamatan.setKodeDaerah(kodeDaerah);
				success = kecamatanFace.save(kecamatan);
			}
			else if(scope.equalsIgnoreCase("kelurahan")){
				Kelurahan kelurahan = new Kelurahan();
				Kecamatan kecamatan = new Kecamatan();
				kecamatan.setKecamatanId(kecamatanID);
				kelurahan.setNama(nama);
				kelurahan.setKecamatan(kecamatan);
				kelurahan.setAktif(aktif);
				kelurahan.setKodeDaerah(kodeDaerah);
				success = kelurahanFace.save(kelurahan);
			}
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void editData(PrintWriter out, String scope, int kecamatanID, String nama, int kodeDaerah, int aktif, int kelurahanID){
		json = new JSONObject();
		String jsonString = "";
		boolean success = false;
		try{
			if(scope.equalsIgnoreCase("kecamatan")){
				Kecamatan kecamatan = new Kecamatan();
				kecamatan.setKecamatanId(kecamatanID);
				kecamatan.setAktif(aktif);
				kecamatan.setNama(nama);
				kecamatan.setKodeDaerah(kodeDaerah);
				success = kecamatanFace.update(kecamatan);
			}
			else if(scope.equalsIgnoreCase("kelurahan")){
				Kelurahan kelurahan = new Kelurahan();
				Kecamatan kecamatan = new Kecamatan();
				kecamatan.setKecamatanId(kecamatanID);
				kelurahan.setKelurahanId(kelurahanID);
				kelurahan.setNama(nama);
				kelurahan.setKecamatan(kecamatan);
				kelurahan.setAktif(aktif);
				kelurahan.setKodeDaerah(kodeDaerah);
				success = kelurahanFace.update(kelurahan);
			}
			json.put("success", success);
			json.put("explain", "Data tidak dapat dilakukan perubahan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, String scope, int ID){
		json = new JSONObject();
		String jsonString = "";
		boolean success = false;
		try{
			if(scope.equalsIgnoreCase("kecamatan")){
				Kecamatan kecamatan = new Kecamatan();
				kecamatan.setKecamatanId(ID);
				success = kecamatanFace.delete(kecamatan);
			}
			else if(scope.equalsIgnoreCase("kelurahan")){
				Kelurahan kelurahan = new Kelurahan();
				kelurahan.setKelurahanId(ID);
				success = kelurahanFace.delete(kelurahan);
			}
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
