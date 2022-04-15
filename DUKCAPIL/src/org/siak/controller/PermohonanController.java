package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Gruppengguna;
import org.siak.data.Permohonan;
import org.siak.data.Permohonandetail;
import org.siak.face.PermohonanDetailFace;
import org.siak.face.PermohonanFace;
import org.siak.util.Configuration;

@WebServlet("/PermohonanController")
public class PermohonanController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private PermohonanFace permohonanFace;
	private PermohonanDetailFace permohonanDetailFace;
	private JSONObject json;
	
    public PermohonanController() {
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
        	else if(command.equalsIgnoreCase("delete")){
        		int permohonanDetailID = Integer.parseInt(request.getParameter("permohonanDetailID"));
        		this.deleteData(out, permohonanDetailID);
        	}
        }
        catch(Exception e){
        	e.printStackTrace();
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
			permohonanFace = (PermohonanFace) registry.lookup("permohonanCore");
			permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				permohonanFace = (PermohonanFace) registry.lookup("permohonanCore");
				permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listPermohonan = new ArrayList();
		String jsonString = "";
		try{
			List<Permohonan> listParent = permohonanFace.listData();
			List<Permohonandetail> listChildren = permohonanDetailFace.listData();
			JSONObject jsonChild = null;
			JSONArray jsonArray = null;
			for (Permohonan p1 : listParent) {
				json = new JSONObject();
				jsonArray = new JSONArray();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				for(Permohonandetail p2 : listChildren){
					if(p2.getPermohonan().getPermohonanId().equals(p1.getPermohonanId())){
						jsonChild = new JSONObject();
						jsonChild.put("permohonanID", p2.getPermohonanDetailId());
						jsonChild.put("nik", p2.getBiodata().getNik());
						jsonChild.put("jenisPermohonan", p2.getBiodata().getNama());
						jsonChild.put("tanggalPengajuan", formatter.format(p2.getTanggalPengajuan()));
						jsonChild.put("kecamatan", p2.getBiodata().getKelurahan().getKecamatan().getNama());
						jsonChild.put("kelurahan", p2.getBiodata().getKelurahan().getNama());
						jsonChild.put("iconCls", "file");
						jsonChild.put("leaf", true);
						jsonArray.add(jsonChild);
					}
				}
				json.put("children", jsonArray);
				json.put("permohonanID", "x"+p1.getPermohonanId());
				json.put("jenisPermohonan", p1.getJenisPermohonan());
				json.put("tanggalPengajuan", "-");
				json.put("kecamatan", "-");
				json.put("nik", "-");
				json.put("kelurahan", "-");
				json.put("expanded", true);
				json.put("leaf", false);
				json.put("iconCls", "folder");
				listPermohonan.add(json);
				jsonString = JSONValue.toJSONString(listPermohonan);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void deleteData(PrintWriter out, int permohonanDetailID){
		boolean success = false;
		json = new JSONObject();
		try{
			Permohonandetail pd = new Permohonandetail();
			pd.setPermohonanDetailId(permohonanDetailID);
			success = permohonanDetailFace.delete(pd);
			json.put("success", success);
			json.put("explain", "Permohonan tidak dapat dihapus");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}

}
