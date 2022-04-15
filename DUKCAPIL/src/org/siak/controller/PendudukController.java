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

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Biodata;
import org.siak.data.Kartukeluargadetail;
import org.siak.face.BiodataFace;
import org.siak.face.KartuKeluargaDetailFace;
import org.siak.util.Configuration;

@WebServlet("/PendudukController")
public class PendudukController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private Registry registry;
    private JSONObject json;
    private BiodataFace biodataFace;
    private KartuKeluargaDetailFace kkdetailFace;
    private SimpleDateFormat formatter;
	
    public PendudukController() {
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
			biodataFace = (BiodataFace) registry.lookup("biodataCore");
			kkdetailFace = (KartuKeluargaDetailFace) registry.lookup("kkDetailCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				biodataFace = (BiodataFace) registry.lookup("biodataCore");
				kkdetailFace = (KartuKeluargaDetailFace) registry.lookup("kkDetailCore");
			}
			catch(Exception ex){
				out.print(ex);
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listPenduduk = new ArrayList();
		String jsonString = "";
		try{
			List<Biodata> list = biodataFace.listData();
			List<Kartukeluargadetail> list2 = kkdetailFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (Biodata b : list) {
				if(b.getAktif() == 1){
					json = new JSONObject();
					json.put("nik", b.getNik());
					json.put("nama", b.getNama());
					json.put("agama", b.getAgama().getNama());
					json.put("pekerjaan", b.getPekerjaan().getNama());
					json.put("pendidikan", b.getPendidikan().getNama());
					json.put("jenisKelamin", b.getJenisKelamin());
					json.put("golonganDarah", b.getGolonganDarah());
					json.put("aktaKelahiran", b.getNoAktaKelahiran());
					json.put("tempatLahir", b.getTempatLahir());
					json.put("kelurahanId", b.getKelurahan().getKelurahanId());
					json.put("foto", "<img src='../ImageFrame?nik="+b.getNik()+"' style='border:1px solid black;' width=65px height=65px></img>");
					json.put("tanggalLahir",  formatter.format(b.getTanggalLahir()));
					json.put("kelurahan", b.getKelurahan().getNama());
					json.put("kecamatan", b.getKelurahan().getKecamatan().getNama());
					
					String noKK = b.getNoKk();
					
					for (Kartukeluargadetail o : list2){
						if(o.getNiks().equalsIgnoreCase(b.getNik())){
							noKK = o.getKartukeluarga().getNoKk();
							break;
						}
					}
					json.put("kartuKeluarga", noKK);
					
					listPenduduk.add(json);
					jsonString = JSONValue.toJSONString(listPenduduk);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
}
