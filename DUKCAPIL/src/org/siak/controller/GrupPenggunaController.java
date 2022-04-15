package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Grup;
import org.siak.data.Gruppengguna;
import org.siak.data.GruppenggunaId;
import org.siak.data.Kecamatan;
import org.siak.data.Kelurahan;
import org.siak.data.Pengguna;
import org.siak.face.GrupFace;
import org.siak.face.GrupPenggunaFace;
import org.siak.face.KecamatanFace;
import org.siak.face.KelurahanFace;
import org.siak.face.PenggunaFace;
import org.siak.util.Configuration;

@WebServlet("/GrupPenggunaController")
public class GrupPenggunaController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private GrupPenggunaFace grupPenggunaFace;
	private GrupFace grupFace;
	private KecamatanFace kecamatanFace;
	private KelurahanFace kelurahanFace;
	private PenggunaFace penggunaFace;
	private JSONObject json;
	
    public GrupPenggunaController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry();
        try{
        	String command = request.getParameter("command");
        	if(command.equalsIgnoreCase("load")){
        		String type = request.getParameter("type");
        		if(type.equalsIgnoreCase("tree")){
        			this.loadTreeData(out);
        		}
        		else if(type.equalsIgnoreCase("grid")){
        			String select = request.getParameter("select");
        			int ID = Integer.parseInt(request.getParameter("id"));
        			if(select.equalsIgnoreCase("dukcapil")){
        				this.loadGridDataDUKCAPIL(out,ID);
        			}
        			else if(select.equalsIgnoreCase("kecamatan")){
        				int kecamatanID = Integer.parseInt(request.getParameter("kecamatanID"));
        				this.loadGridDataKecamatan(out, ID, kecamatanID);
        			}
        			else if(select.equalsIgnoreCase("kelurahan")){
        				int kelurahanID = Integer.parseInt(request.getParameter("kelurahanID"));
        				this.loadGridDataKelurahan(out, ID, kelurahanID);
        			}
        		}
        		else if(type.equalsIgnoreCase("combobox")){
        			this.loadComboBoxAvailableUser(out);
        		}
        	}
        	else if(command.equalsIgnoreCase("add")){
        		String column = request.getParameter("column");
        		String username = request.getParameter("username");
        		int grupID = Integer.parseInt(request.getParameter("grupID"));
        		if(column.equalsIgnoreCase("dukcapil")){
        			this.addDataRoleDUKCAPIL(out, username, grupID);
        		}
        		else if(column.equalsIgnoreCase("kecamatan")){
        			int kecamatanID = Integer.parseInt(request.getParameter("kecamatanID"));
        			this.addDataRoleKecamatan(out, username, grupID, kecamatanID);
        		}
        		else if(column.equalsIgnoreCase("kelurahan")){
        			int kelurahanID = Integer.parseInt(request.getParameter("kelurahanID"));
        			this.addDataRoleKelurahan(out, username, grupID, kelurahanID);
        		}
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		int grupPenggunaID = Integer.parseInt(request.getParameter("grupPenggunaID"));
        		String username = request.getParameter("username");
        		this.deleteDataRole(out, grupPenggunaID, username);
        	}
        }
        catch(Exception e){
        	
        }
        finally{
        	
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
			grupPenggunaFace = (GrupPenggunaFace) registry.lookup("grupPenggunaCore");
			grupFace = (GrupFace) registry.lookup("grupCore");
			kecamatanFace = (KecamatanFace) registry.lookup("kecamatanCore");
			kelurahanFace = (KelurahanFace) registry.lookup("kelurahanCore");
			penggunaFace = (PenggunaFace) registry.lookup("penggunaCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				grupPenggunaFace = (GrupPenggunaFace) registry.lookup("grupPenggunaCore");
				grupFace = (GrupFace) registry.lookup("grupCore");
				kecamatanFace = (KecamatanFace) registry.lookup("kecamatanCore");
				kelurahanFace = (KelurahanFace) registry.lookup("kelurahanCore");
				penggunaFace = (PenggunaFace) registry.lookup("penggunaCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadTreeData(PrintWriter out){
		List listTreeData = new LinkedList();
		String jsonString = "";
		JSONArray jsonArray = null;
		JSONObject jsonChild = null; 
		try{
			List<Grup> listParent = grupFace.listData();
			List<Kecamatan> listChild1 = kecamatanFace.listData();
			List<Kelurahan> listChild2 = kelurahanFace.listData();
			for (Grup g : listParent) {
				jsonArray = new JSONArray();
				json = new JSONObject();
				json.put("id", "g"+g.getGrupId());
				json.put("text", g.getNama());
				if(g.getNama().equalsIgnoreCase("kecamatan")){
					for(Kecamatan k : listChild1){
						jsonChild = new JSONObject();
						jsonChild.put("id", "c1"+k.getKecamatanId());
						jsonChild.put("parentID", "g"+g.getGrupId());
						jsonChild.put("text", k.getNama());
						jsonChild.put("iconCls", "kecamatan");
						jsonChild.put("leaf", true);
						jsonArray.add(jsonChild);
					}
				}
				else if(g.getNama().equalsIgnoreCase("kelurahan")){
					for(Kelurahan k : listChild2){
						jsonChild = new JSONObject();
						jsonChild.put("id", "c2"+k.getKelurahanId());
						jsonChild.put("text", k.getNama());
						jsonChild.put("iconCls", "kelurahan");
						jsonChild.put("leaf", true);
						jsonArray.add(jsonChild);
					}
				}
				
				if(g.getNama().equalsIgnoreCase("DUKCAPIL")){
					json.put("iconCls", "computer");
					json.put("leaf", true);
					json.put("expanded", false);
				}
				else{
					json.put("children", jsonArray);
					json.put("iconCls", "computer");
					json.put("leaf", false);
					json.put("expanded", false);
				}
				listTreeData.add(json);
				jsonString = JSONValue.toJSONString(listTreeData);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}

	private void loadGridDataKecamatan(PrintWriter out, int id, int kecamatanID){
		List listGridData = new LinkedList();
		String jsonString = "";
		try{
			List<Gruppengguna> list = grupPenggunaFace.listData();
			for(Gruppengguna gp : list){
				if(gp.getKecamatanId().equals(kecamatanID) && gp.getKelurahanId().equals(0) && gp.getGrup().getGrupId().equals(id) ){
					json = new JSONObject();
					json.put("grupPenggunaId", gp.getId().getGrupPenggunaId());
					json.put("username", gp.getPengguna().getUsername());
					listGridData.add(json);
					jsonString = JSONValue.toJSONString(listGridData);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
	private void loadGridDataDUKCAPIL(PrintWriter out, int id){
		List listGridData = new LinkedList();
		String jsonString = "";
		try{
			List<Gruppengguna> list = grupPenggunaFace.listData();
			for(Gruppengguna gp : list){
				if(gp.getKecamatanId().equals(0) && gp.getKelurahanId().equals(0) && gp.getGrup().getGrupId().equals(id)){
					json = new JSONObject();
					json.put("grupPenggunaId", gp.getId().getGrupPenggunaId());
					json.put("username", gp.getPengguna().getUsername());
					listGridData.add(json);
					jsonString = JSONValue.toJSONString(listGridData);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
	private void loadGridDataKelurahan(PrintWriter out, int id, int kecamatanID){
		List listGridData = new LinkedList();
		String jsonString = "";
		try{
			List<Gruppengguna> list = grupPenggunaFace.listData();
			for(Gruppengguna gp : list){
				if(gp.getKecamatanId().equals(0) && gp.getKelurahanId().equals(kecamatanID) && gp.getGrup().getGrupId().equals(id)){
					json = new JSONObject();
					json.put("grupPenggunaId", gp.getId().getGrupPenggunaId());
					json.put("username", gp.getPengguna().getUsername());
					listGridData.add(json);
					jsonString = JSONValue.toJSONString(listGridData);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
	private void loadComboBoxAvailableUser(PrintWriter out){
		List listAvailableUser = new LinkedList();
		String jsonString = "";
		try{
			List<Pengguna> listTab1 = penggunaFace.listData();
			List<Gruppengguna> listTab2 = grupPenggunaFace.listData();
			List<String> l1 = new ArrayList<String>();
			List<String> l2 = new ArrayList<String>();
			for (Pengguna pengguna : listTab1)
				l1.add(pengguna.getUsername());
			for (Gruppengguna grupPengguna : listTab2)
				l2.add(grupPengguna.getPengguna().getUsername());
			Collection<String> c = CollectionUtils.subtract(l1, l2);
			for (String string : c) {
				json = new JSONObject();
				json.put("username", string);
				listAvailableUser.add(json);
				jsonString = JSONValue.toJSONString(listAvailableUser);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void addDataRoleDUKCAPIL(PrintWriter out, String username, int grupID){
		json = new JSONObject();
		boolean success = false;
		try{
			Gruppengguna gp = new Gruppengguna();
			Pengguna p = new Pengguna();
			Grup g = new Grup();
			Kecamatan k1 = new Kecamatan();
			Kelurahan k2 = new Kelurahan(); 
			GruppenggunaId gpi = new GruppenggunaId();
			p.setUsername(username);
			g.setGrupId(grupID);
			k1.setKecamatanId(0);
			k2.setKelurahanId(0);
			gp.setGrup(g);
			gp.setKecamatanId(k1.getKecamatanId());
			gp.setKelurahanId(k2.getKelurahanId());
			gp.setPengguna(p);
			gpi.setUsername(username);
			gp.setId(gpi);
			success = grupPenggunaFace.save(gp);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void addDataRoleKecamatan(PrintWriter out, String username, int grupID, int kecamatanID){
		json = new JSONObject();
		boolean success = false;
		try{
			Gruppengguna gp = new Gruppengguna();
			Pengguna p = new Pengguna();
			Grup g = new Grup();
			Kecamatan k1 = new Kecamatan();
			Kelurahan k2 = new Kelurahan(); 
			GruppenggunaId gpi = new GruppenggunaId();
			p.setUsername(username);
			g.setGrupId(grupID);
			k1.setKecamatanId(kecamatanID);
			k2.setKelurahanId(0);
			gp.setGrup(g);
			gp.setKecamatanId(k1.getKecamatanId());
			gp.setKelurahanId(k2.getKelurahanId());
			gp.setPengguna(p);
			gpi.setUsername(username);
			gp.setId(gpi);
			success = grupPenggunaFace.save(gp);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void addDataRoleKelurahan(PrintWriter out, String username, int grupID, int kelurahanID){
		json = new JSONObject();
		boolean success = false;
		try{
			Gruppengguna gp = new Gruppengguna();
			Pengguna p = new Pengguna();
			Grup g = new Grup();
			Kecamatan k1 = new Kecamatan();
			Kelurahan k2 = new Kelurahan(); 
			GruppenggunaId gpi = new GruppenggunaId();
			p.setUsername(username);
			g.setGrupId(grupID);
			k1.setKecamatanId(0);
			k2.setKelurahanId(kelurahanID);
			gp.setGrup(g);
			gp.setKecamatanId(k1.getKecamatanId());
			gp.setKelurahanId(k2.getKelurahanId());
			gp.setPengguna(p);
			gpi.setUsername(username);
			gp.setId(gpi);
			success = grupPenggunaFace.save(gp);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteDataRole(PrintWriter out, int grupPenggunaID, String username){
		json = new JSONObject();
		boolean success = false;
		try{
			Gruppengguna gp = new Gruppengguna();
			GruppenggunaId gpi = new GruppenggunaId();
			gpi.setGrupPenggunaId(grupPenggunaID);
			gpi.setUsername(username);
			gp.setId(gpi);
			success = grupPenggunaFace.delete(gp);
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
