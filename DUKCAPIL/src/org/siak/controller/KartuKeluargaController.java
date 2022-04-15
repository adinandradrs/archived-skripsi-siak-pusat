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
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Biodata;
import org.siak.data.Gruppengguna;
import org.siak.data.Kartukeluarga;
import org.siak.data.Kelurahan;
import org.siak.data.Permohonan;
import org.siak.data.Permohonandetail;
import org.siak.face.KartuKeluargaFace;
import org.siak.face.PermohonanDetailFace;
import org.siak.util.Configuration;

@WebServlet("/KartuKeluargaController")
public class KartuKeluargaController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Registry registry;
	private JSONObject json;
	private KartuKeluargaFace kkFace;
	private PermohonanDetailFace permohonanDetailFace;
	private SimpleDateFormat formatter;
	private HttpSession session;
	
    public KartuKeluargaController() {
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
			kkFace = (KartuKeluargaFace) registry.lookup("kkCore");
			permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
		}
		catch(Exception e){
			try {
				registry = LocateRegistry.getRegistry(ipService2, port);
				kkFace = (KartuKeluargaFace) registry.lookup("kkCore");
				permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
			} catch (Exception ex) {
				out.print(ex);
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listKK = new ArrayList();
		String jsonString = "";
		try{
			List<Kartukeluarga> list = kkFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (Kartukeluarga kk : list) {		
				json = new JSONObject();
				json.put("noKk", kk.getNoKk());
				json.put("nik", kk.getBiodata().getNik());
				json.put("nama", kk.getBiodata().getNama());
				switch (kk.getJenisBuat()) {
				case 1:
					json.put("jenisBuat", "Baru");
					break;
				case 2:
					json.put("jenisBuat", "Ubah Data");
					break;
				case 3:
					json.put("jenisBuat", "Hilang");
					break;
				default:
					json.put("jenisBuat", "Tidak Diketahui");
					break;
				}
				json.put("tanggalBuat", formatter.format(kk.getTanggalBuat()));
				listKK.add(json);
				jsonString = JSONValue.toJSONString(listKK);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
}