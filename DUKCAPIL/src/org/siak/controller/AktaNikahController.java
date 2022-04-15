package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Aktanikah;
import org.siak.data.Aktanikahdetail;
import org.siak.data.Permohonan;
import org.siak.face.AktaNikahDetailFace;
import org.siak.face.AktaNikahFace;
import org.siak.util.Configuration;
import org.siak.util.Report;

@WebServlet("/AktaNikahController")
public class AktaNikahController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private JSONObject json;
	private AktaNikahFace aktaNikahFace;
	private SimpleDateFormat formatter;
	private AktaNikahDetailFace aktaNikahDetailFace;
	
    public AktaNikahController() {
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
        		String noAktaNikah = request.getParameter("noAktaNikah");
        		this.addData(out, noAktaNikah);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		String noAktaNikah = request.getParameter("noAktaNikah");
        		this.deleteData(out, noAktaNikah);
        	}
        	else if(command.equalsIgnoreCase("print")){
        		String noAktaNikah = request.getParameter("noAktaNikah");
        		this.printData(out, noAktaNikah);
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
			aktaNikahFace = (AktaNikahFace) registry.lookup("aktaNikahCore");
			aktaNikahDetailFace = (AktaNikahDetailFace) registry.lookup("aktaNikahDetailCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				aktaNikahFace = (AktaNikahFace) registry.lookup("aktaNikahCore");			
				aktaNikahDetailFace = (AktaNikahDetailFace) registry.lookup("aktaNikahDetailCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listAktaNikah = new ArrayList();
		String jsonString = "";
		try{
			List<Aktanikah> list = aktaNikahFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (Aktanikah o : list) {
				json = new JSONObject();
				json.put("noAktaNikah", o.getNoAktaNikah());
				switch (o.getJenisBuat()) {
				case 1:
					json.put("jenisBuat", "Baru");
					break;
				case 2:
					json.put("jenisBuat", "Ubah data");
					break;
				case 3:
					json.put("jenisBuat", "Hilang");
					break;
				default:
					json.put("jenisBuat", "Tidak diketahui");
					break;
				}
				json.put("tanggalBuat", formatter.format(o.getTanggalBuat()));
				listAktaNikah.add(json);
				jsonString = JSONValue.toJSONString(listAktaNikah);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void addData(PrintWriter out, String noAktaNikah){
		boolean success = false;
		json = new JSONObject();
		try{
			Aktanikah an = new Aktanikah();
			Permohonan p = new Permohonan();
			p.setPermohonanId(3);
			an.setNoAktaNikah(noAktaNikah);
			an.setJenisBuat(1);
			an.setPermohonan(p);
			an.setTanggalBuat(new Date());
			success = aktaNikahFace.save(an);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, String noAktaNikah){
		boolean success = false;
		json = new JSONObject();
		try{
			Aktanikah an = new Aktanikah();
			an.setNoAktaNikah(noAktaNikah);
			success = aktaNikahFace.delete(an);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dihapus karena memiliki keterkaitan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void printData(PrintWriter out, String noAktaNikah){
		json = new JSONObject();
		String docPath ="" ;
		try{
			List<Aktanikahdetail> list = aktaNikahDetailFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			String path = getServletContext().getRealPath("/Document/AktaNikah.jrxml");
			docPath = Configuration.file(getServletContext().getRealPath("/Configuration/web.ini")).get("Document", "path");
			Report.createAktaNikah(list, noAktaNikah, path, docPath, formatter);
		}
		catch(Exception e){
			
		}
		json.put("success", true);
		json.put("explain", "Silakan cek pada drive " + docPath);
		out.print(json);
	}

}
