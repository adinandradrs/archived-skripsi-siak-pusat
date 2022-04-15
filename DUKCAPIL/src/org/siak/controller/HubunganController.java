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
import org.siak.data.Hubungan;
import org.siak.data.Hubungandetail;
import org.siak.face.HubunganDetailFace;
import org.siak.face.HubunganFace;
import org.siak.util.Configuration;

@WebServlet("/HubunganController")
public class HubunganController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private HubunganFace hubunganFace;
	private HubunganDetailFace hubunganDetailFace;
	private JSONObject json;
	
    public HubunganController() {
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
        		else if(select.equalsIgnoreCase("hubungan")){
        			this.loadDataHubungan(out);
        		}
        		else if(select.equalsIgnoreCase("hubunganDetail")){
        			int hubunganID = Integer.parseInt(request.getParameter("hubunganID"));
        			this.loadDataHubunganDetail(out,hubunganID);
        		}
        	}
        	else if(command.equalsIgnoreCase("add")){
        		String scope = request.getParameter("scope");
        		String nama = request.getParameter("nama");
        		int hubunganID = 0;
        		try{
        			hubunganID = Integer.parseInt(request.getParameter("hubunganID"));
        		}
        		catch(Exception e){
        			
        		}
    			this.addData(out, scope, nama, hubunganID);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		String scope = request.getParameter("scope");
        		String nama = request.getParameter("nama");
        		int hubunganID = Integer.parseInt(request.getParameter("hubunganID"));
        		int hubunganDetailID = Integer.parseInt(request.getParameter("hubunganDetailID"));
        		this.editData(out,scope,nama,hubunganID,hubunganDetailID);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		String scope = request.getParameter("scope");
        		int ID = Integer.parseInt(request.getParameter("ID"));
        		this.deleteData(out, scope, ID);
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
        	hubunganFace = (HubunganFace) registry.lookup("hubunganCore");
        	hubunganDetailFace = (HubunganDetailFace) registry.lookup("hubunganDetailCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
	        	hubunganFace = (HubunganFace) registry.lookup("hubunganCore");
	        	hubunganDetailFace = (HubunganDetailFace) registry.lookup("hubunganDetailCore");
			}
			catch(Exception ex){
				out.print(ex.getMessage());
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listHubungan = new LinkedList();
		String jsonString = "";
		try{
			List<Hubungan> listParent = hubunganFace.listData();
			List<Hubungandetail> listChildren = hubunganDetailFace.listData();
			JSONObject jsonChild = null;
			JSONArray jsonArray = null;
			for(Hubungan p : listParent){
				jsonArray = new JSONArray();
				for(Hubungandetail c : listChildren){
					if(c.getHubungan().getHubunganId().equals(p.getHubunganId())){
						jsonChild = new JSONObject();
						jsonChild.put("id", "c"+c.getHubunganDetailId());
						jsonChild.put("text", c.getNama());
						jsonChild.put("iconCls", "file");
						jsonChild.put("leaf", true);
						jsonArray.add(jsonChild);
					}
				}
				json = new JSONObject();
				json.put("children", jsonArray);
				json.put("id", "p"+p.getHubunganId());
				json.put("text", p.getNama());
				json.put("iconCls", "folder");
				json.put("leaf", false);
				json.put("expanded", false);
				listHubungan.add(json);
				jsonString = JSONValue.toJSONString(listHubungan);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDataHubungan(PrintWriter out){
		List listHubungan = new LinkedList();
		String jsonString = "";
		try{
			List<Hubungan> list = hubunganFace.listData();
			for (Hubungan h : list) {
				json = new JSONObject();
				json.put("hubunganId", h.getHubunganId());
				json.put("nama", h.getNama());
				listHubungan.add(json);
				jsonString = JSONValue.toJSONString(listHubungan);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
	private void loadDataHubunganDetail(PrintWriter out, int hubunganID){
		List listHubungan = new LinkedList();
		String jsonString = "";
		try{
			List<Hubungandetail> list = hubunganDetailFace.listData();
			for (Hubungandetail h : list) {
				if(h.getHubungan().getHubunganId().equals(hubunganID)){
					json = new JSONObject();
					json.put("hubunganDetailId", h.getHubunganDetailId());
					json.put("nama", h.getNama());
					listHubungan.add(json);
					jsonString = JSONValue.toJSONString(listHubungan);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
	private void addData(PrintWriter out,String scope, String nama, int hubunganID){
		json = new JSONObject();
		boolean success = false;
		try{
			if(scope.equalsIgnoreCase("hubungan")){
				Hubungan h = new Hubungan();
				h.setNama(nama);
				success = hubunganFace.save(h);
			}
			else if(scope.equalsIgnoreCase("hubunganDetail")){
				Hubungandetail hd = new Hubungandetail();
				Hubungan h = new Hubungan();
				h.setHubunganId(hubunganID);
				hd.setNama(nama);
				hd.setHubungan(h);
				success = hubunganDetailFace.save(hd);
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
	
	private void editData(PrintWriter out, String scope, String nama, int hubunganID, int hubunganDetailID){
		json = new JSONObject();
		boolean success = false;
		try{
			if(scope.equalsIgnoreCase("hubungan")){
				Hubungan h = new Hubungan();
				h.setHubunganId(hubunganID);
				h.setNama(nama);
				success = hubunganFace.update(h);
			}
			else if(scope.equalsIgnoreCase("hubunganDetail")){
				Hubungandetail hd = new Hubungandetail();
				Hubungan h = new Hubungan();
				h.setHubunganId(hubunganID);
				hd.setHubungan(h);
				hd.setNama(nama);
				hd.setHubunganDetailId(hubunganDetailID);
				success = hubunganDetailFace.update(hd);
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
		boolean success = false;
		try{
			if(scope.equalsIgnoreCase("hubungan")){
				Hubungan h = new Hubungan();
				h.setHubunganId(ID);
				success = hubunganFace.delete(h);
			}
			else if(scope.equalsIgnoreCase("hubunganDetail")){
				Hubungandetail hd = new Hubungandetail();
				hd.setHubunganDetailId(ID);
				success = hubunganDetailFace.delete(hd);
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
