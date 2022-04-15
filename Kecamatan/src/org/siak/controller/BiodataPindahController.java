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
import org.siak.data.Biodatapindah;
import org.siak.data.Gruppengguna;
import org.siak.data.Kota;
import org.siak.data.KotaId;
import org.siak.face.BiodataFace;
import org.siak.face.BiodataPindahFace;
import org.siak.face.KotaFace;
import org.siak.util.Configuration;

@WebServlet("/BiodataPindahController")
public class BiodataPindahController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private JSONObject json;
	private BiodataPindahFace biodataPindahFace;
	private BiodataFace biodataFace;
	private KotaFace kotaFace;
	
    public BiodataPindahController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	String command = request.getParameter("command");
        	HttpSession session = request.getSession(false);
    		Gruppengguna gp = (Gruppengguna) session.getAttribute("user");
        	if(command.equalsIgnoreCase("load")){
        		String select = request.getParameter("select");
        		if(select == null){
	        		int kecamatanID = gp.getKecamatanId();
	        		this.loadData(out, kecamatanID);
        		}
        		else if(select.equalsIgnoreCase("kota")){
        			this.loadDataKota(out);
        		}
        	}
        	else if(command.equalsIgnoreCase("add")){
        		String nik = request.getParameter("nik");
        		int kotaID = Integer.parseInt(request.getParameter("kotaID"));
        		this.addData(out, nik, kotaID);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		String nik = request.getParameter("nik");
        		this.deleteData(out, nik);
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
	
	String path;
	String ipService1;
	String ipService2;
	int port;
	
	private void bindRegistry(PrintWriter out){
		String path = getServletContext().getRealPath("/Configuration/web.ini");
    	ipService1 = Configuration.file(path).get("Service", "ipService1");
    	ipService2 = Configuration.file(path).get("Service", "ipService2");
    	port = Integer.parseInt(Configuration.file(path).get("Service", "port"));
	}
	
	private void loadData(PrintWriter out, int kecamatanID){
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			biodataPindahFace = (BiodataPindahFace) registry.lookup("biodataPindahCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				biodataPindahFace = (BiodataPindahFace) registry.lookup("biodataPindahCore");
			}
			catch(Exception ex){
				
			}
		}
		
		List listBiodataPindah = new ArrayList();
		String jsonString = "";
		try{
			List<Biodatapindah> list = biodataPindahFace.listData();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			for (Biodatapindah bp : list) {
				if(bp.getBiodata().getKelurahan().getKecamatan().getKecamatanId().equals(kecamatanID)){
					json = new JSONObject();
					json.put("nik", bp.getBiodata().getNik());
					json.put("namaPenduduk", bp.getBiodata().getNama());
					json.put("kotaId", bp.getKota().getKotaId());
					json.put("namaKota", bp.getKota().getNama());
					json.put("tanggalPengajuan", formatter.format(bp.getTanggalPengajuan()));
					listBiodataPindah.add(json);
					jsonString = JSONValue.toJSONString(listBiodataPindah);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDataKota(PrintWriter out){
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			kotaFace = (KotaFace) registry.lookup("kotaCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2,port);
				kotaFace = (KotaFace) registry.lookup("kotaCore");
			}
			catch(Exception ex){
				out.print(ex.getMessage());
			}
		}
		
		List listKota = new ArrayList();
		String jsonString = "";
		try{
			List<Kota> list = kotaFace.listData();
			for (Kota k : list) {
				json = new JSONObject();
				json.put("kotaId", k.getKotaId());
				json.put("nama", k.getNama());
				listKota.add(json);
				jsonString = JSONValue.toJSONString(listKota);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
	private void addData(PrintWriter out, String nik, int kotaID){
		boolean totalSuccess = false, success1 = false, success2 = false;
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			biodataPindahFace = (BiodataPindahFace) registry.lookup("biodataPindahCore");
			biodataFace = (BiodataFace) registry.lookup("biodataCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				biodataPindahFace = (BiodataPindahFace) registry.lookup("biodataPindahCore");
				biodataFace = (BiodataFace) registry.lookup("biodataCore");
			}
			catch(Exception ex){
				out.print(ex.getMessage());
			}
		}
		
		json = new JSONObject();
		try{
			Biodatapindah bp = new Biodatapindah();
			Kota k = new Kota();
			KotaId kid = new KotaId();
			kid.setKotaId(kotaID);
			k.setKotaId(kotaID);
			Biodata b = new Biodata();
			b.setNik(nik);
			bp.setKota(k);
			bp.setNik(nik);
			bp.setBiodata(b);
			bp.setTanggalPengajuan(new java.util.Date());
			success1 = biodataPindahFace.save(bp);
			if(success1 == true)
				success2 = biodataFace.update(updateInnerBiodata(b, nik, 0));
			if(success1 == true && success2 == true)
				totalSuccess = true;
			json.put("success", totalSuccess);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", totalSuccess);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}

	private void deleteData(PrintWriter out, String nik){
		boolean totalSuccess = false, success1 = false, success2 = false;
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			biodataPindahFace = (BiodataPindahFace) registry.lookup("biodataPindahCore");
			biodataFace = (BiodataFace) registry.lookup("biodataCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				biodataPindahFace = (BiodataPindahFace) registry.lookup("biodataPindahCore");
				biodataFace = (BiodataFace) registry.lookup("biodataCore");
			}
			catch(Exception ex){
				out.print(ex.getMessage());
			}
		}
		
		json = new JSONObject();
		try{
			Biodatapindah bp = new Biodatapindah();
			Biodata b = new Biodata();
			b.setNik(nik);
			bp.setNik(nik);
			bp.setBiodata(b);
			success1 = biodataPindahFace.delete(bp);
			if(success1 == true)
				success2 = biodataFace.update(updateInnerBiodata(b, nik, 1));
			if(success1 == true && success2 == true)
				totalSuccess = true;
			json.put("success", totalSuccess);
			json.put("explain", "Data tidak dapat dihapus karena terdapat keterkaitan");
		}
		catch(Exception e){
			json.put("success", totalSuccess);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private Biodata updateInnerBiodata(Biodata b, String nik, int aktif){
		try{
			for (Biodata o : biodataFace.listData()) {
				if(o.getNik().equalsIgnoreCase(nik)){
					b.setNik(nik);
					b.setAgama(o.getAgama());
					b.setAktif(aktif);
					b.setAlamat(o.getAlamat());
					b.setFoto(o.getFoto());
					b.setGolonganDarah(o.getGolonganDarah());
					b.setHubungandetail(o.getHubungandetail());
					b.setJenisKelamin(o.getJenisKelamin());
					b.setKelurahan(o.getKelurahan());
					b.setNama(o.getNama());
					b.setPekerjaan(o.getPekerjaan());
					b.setPendidikan(o.getPendidikan());
					b.setRt(o.getRt());
					b.setRw(o.getRw());
					b.setTanggalLahir(o.getTanggalLahir());
					b.setTelepon(o.getTelepon());
					b.setTempatLahir(o.getTempatLahir());
				}
			}
		}
		catch(Exception e){
			
		}
		return b;
	}
	
}