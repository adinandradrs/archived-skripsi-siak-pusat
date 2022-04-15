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

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Agama;
import org.siak.data.Biodata;
import org.siak.data.Kecamatan;
import org.siak.data.Permohonan;
import org.siak.data.Permohonandetail;
import org.siak.face.AgamaFace;
import org.siak.face.BiodataFace;
import org.siak.face.KecamatanFace;
import org.siak.face.PermohonanDetailFace;
import org.siak.face.PermohonanFace;
import org.siak.util.Configuration;

@WebServlet("/DashboardController")
public class DashboardController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private AgamaFace agamaFace;
	private BiodataFace biodataFace;
	private KecamatanFace kecamatanFace;
	private PermohonanDetailFace permohonanDetailFace;
	private JSONObject json;
	
    public DashboardController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	String dash = request.getParameter("dash");
        	if(dash.equalsIgnoreCase("permohonan")){
        		this.loadDashPermohonan(out);
        	}
        	else if(dash.equalsIgnoreCase("agama")){
        		this.loadDashAgama(out);
        	}
        	else if(dash.equalsIgnoreCase("penduduk")){
        		this.loadDashPenduduk(out);
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
		int port = Integer.parseInt(Configuration.file(path).get("Service","port"));
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			agamaFace = (AgamaFace) registry.lookup("agamaCore");
			biodataFace = (BiodataFace) registry.lookup("biodataCore");
			permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
			kecamatanFace = (KecamatanFace) registry.lookup("kecamatanCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				agamaFace = (AgamaFace) registry.lookup("agamaCore");
				biodataFace = (BiodataFace) registry.lookup("biodataCore");
				permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
				kecamatanFace = (KecamatanFace) registry.lookup("kecamatanCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadDashAgama(PrintWriter out){
		List listAgama = new ArrayList();
		String jsonString = "";
		try{
			List<Agama> list1 = agamaFace.listData();
			List<Biodata> list2 = biodataFace.listData();
			for (Agama a : list1) {
				int i = 0;
				for (Biodata b : list2)
					if(b.getAgama().getAgamaId().equals(a.getAgamaId()))
						i++;
				if(i!=0){
					json = new JSONObject();
					json.put("nama", ""+a.getNama()+"");
					json.put("jumlah", i);
					listAgama.add(json);
					jsonString = JSONValue.toJSONString(listAgama);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDashPenduduk(PrintWriter out){
		List listPenduduk = new ArrayList();
		String jsonString = "";
		try{
			List<Kecamatan> list1 = kecamatanFace.listData();
			List<Biodata> list2 = biodataFace.listData();
			for (Kecamatan k : list1) {
				int i = 0;
				for (Biodata b : list2)
					if(b.getKelurahan().getKecamatan().getKecamatanId().equals(k.getKecamatanId()))
						i++;
				if(i!=0){
					json = new JSONObject();
					json.put("nama", ""+k.getNama()+"");
					json.put("jumlah", i);
					listPenduduk.add(json);
					jsonString = JSONValue.toJSONString(listPenduduk);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDashPermohonan(PrintWriter out){
		List listPermohonan = new ArrayList();
		String jsonString = "";
		try{
			json = new JSONObject();
			List<Permohonandetail> list = permohonanDetailFace.listData();
			int ktp = 0, kk=0, an = 0, akl = 0;
			for (Permohonandetail p : list) {
				if(p.getPermohonan().getPermohonanId().equals(1))
					ktp++;
				else if(p.getPermohonan().getPermohonanId().equals(2))
					kk++;
				else if(p.getPermohonan().getPermohonanId().equals(3))
					an++;
				else if(p.getPermohonan().getPermohonanId().equals(4))
					akl++;
			}
			json.put("ktp", ktp);
			json.put("kk", kk);
			json.put("an", an);
			json.put("akl", akl);
			listPermohonan.add(json);
			jsonString = JSONValue.toJSONString(listPermohonan);
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e);
		}
	}

}
