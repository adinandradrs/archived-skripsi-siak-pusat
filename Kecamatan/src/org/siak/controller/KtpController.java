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
import org.siak.data.Ktp;
import org.siak.data.Permohonan;
import org.siak.data.Permohonandetail;
import org.siak.face.KtpFace;
import org.siak.face.PermohonanDetailFace;
import org.siak.face.PermohonanFace;
import org.siak.util.Configuration;
import org.siak.util.Report;

@WebServlet("/KtpController")
public class KtpController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private KtpFace ktpFace;
	private PermohonanFace permohonanFace;
	private PermohonanDetailFace permohonanDetailFace;
	private Registry registry;
	private JSONObject json;
	private SimpleDateFormat formatter;
	
    public KtpController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	String command = request.getParameter("command");
        	if(command.equalsIgnoreCase("load")){
        		HttpSession session = request.getSession(false);
    			Gruppengguna gp = (Gruppengguna) session.getAttribute("user");
        		this.loadData(out, gp.getKecamatanId());
        	}
        	else if(command.equalsIgnoreCase("add")){
        		String nik = request.getParameter("nik");
        		String tanggalLahir = request.getParameter("tanggalLahir");
        		this.addData(out, nik, tanggalLahir);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		String nik = request.getParameter("nik");
        		String expired = request.getParameter("expired");
        		int jenisCetak = Integer.parseInt(request.getParameter("jenisCetak"));
        		this.editData(out, nik, expired, jenisCetak);
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
			ktpFace = (KtpFace) registry.lookup("ktpCore");
			permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				ktpFace = (KtpFace) registry.lookup("ktpCore");
				permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
			}
			catch(Exception ex){
				out.print(ex.getMessage());
			}
			out.print(e.getMessage());
		}
	}
	
	private void loadData(PrintWriter out, int kecamatanID){
		List listBiodata = new ArrayList();
		String jsonString = "";
		try{
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			List<Ktp> list = ktpFace.listData();
			for (Ktp k : list) {
				if(k.getBiodata().getKelurahan().getKecamatan().getKecamatanId().equals(kecamatanID)){
					json = new JSONObject();
					json.put("nik", k.getNik());
					json.put("nama", k.getBiodata().getNama());
					json.put("tempatLahir", k.getBiodata().getTempatLahir());
					json.put("tanggalLahir", formatter.format(k.getBiodata().getTanggalLahir()));
					json.put("jenisKelamin", k.getBiodata().getJenisKelamin());
					json.put("alamat", k.getBiodata().getAlamat());
					json.put("kelurahan", k.getBiodata().getKelurahan().getNama());
					json.put("kecamatan", k.getBiodata().getKelurahan().getKecamatan().getNama());
					json.put("agama", k.getBiodata().getAgama().getNama());
					json.put("pekerjaan", k.getBiodata().getPekerjaan().getNama());
					json.put("expired", k.getExpired());
					json.put("rt", k.getBiodata().getRt());
					json.put("rw", k.getBiodata().getRw());
					json.put("foto", "<img src='../ImageFrame?nik="+k.getNik()+"' style='border:1px solid black;' width=65px height=65px></img>");
					switch (k.getJenisBuat()) {
						case 1:
							json.put("status", "Baru");
							break;
						case 2:
							json.put("status", "Ubah Data");
							break;
						case 3:
							json.put("status", "Hilang");
							break;
						case 4:
							json.put("status", "Perpanjangan");
							break;
						default:
							json.put("status", "Lainnya");
							break;
					}
					//FOR : Kartu Keluarga 
					json.put("kelurahanId", k.getBiodata().getKelurahan().getKelurahanId());
					
					listBiodata.add(json);
					jsonString = JSONValue.toJSONString(listBiodata);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e);
		}
	}
	
	private void addData(PrintWriter out, String nik, String tanggalLahir){
		json = new JSONObject();
		boolean success1 = false;
		boolean success2 = false;
		boolean totalSuccess = false;
		formatter = new SimpleDateFormat("dd-MM-yyyy");
		try{
			
			Biodata biodata = new Biodata();
			Permohonan permohonan = new Permohonan();
			biodata.setNik(nik);
			biodata.setTanggalLahir(formatter.parse(tanggalLahir));
			permohonan.setPermohonanId(1);
			
			Ktp ktp = new Ktp();
			ktp.setNik(nik);
			ktp.setBiodata(biodata);
			ktp.setJenisBuat(1);
			ktp.setPermohonan(permohonan);
			ktp.setTanggalBuat(new Date());
			ktp.setExpired("");
			success1 = ktpFace.save(ktp);
			
			if(success1 == true){
				Permohonandetail permohonanDetail = new Permohonandetail();
				permohonanDetail.setBiodata(biodata);
				permohonanDetail.setPermohonan(permohonan);
				permohonanDetail.setTanggalPengajuan(ktp.getTanggalBuat());
				success2 = permohonanDetailFace.save(permohonanDetail);
			}
			
			if(success1 == true && success2 == true){
				totalSuccess = true;
				List<Ktp> list = ktpFace.listData();
				String docPath = Configuration.file(getServletContext().getRealPath("/Configuration/web.ini")).get("Document", "path");
				String path = getServletContext().getRealPath("/Document/KTP.jrxml");
				Report.createKTP(list, ktp, path, docPath, formatter);
			}
			
			json.put("success", totalSuccess);
			json.put("explain", "Permohonan dan Pencetakan KTP tidak dapat dilakukan");
		}
		catch(Exception e){
			json.put("success", totalSuccess);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void editData(PrintWriter out, String nik, String expired, int jenis){
		json = new JSONObject();
		boolean totalSuccess = false;
		boolean success1 = false;
		boolean success2 = false;
		try{
			Ktp ktp = new Ktp();
			Biodata biodata = new Biodata();
			Permohonan permohonan = new Permohonan();
			Permohonandetail permohonanDetail = new Permohonandetail();
			
			biodata.setNik(nik);
			permohonan.setPermohonanId(1);
			permohonanDetail.setBiodata(biodata);
			permohonanDetail.setTanggalPengajuan(new Date());
			permohonanDetail.setPermohonan(permohonan);
			ktp.setBiodata(biodata);
			ktp.setExpired(expired);
			ktp.setJenisBuat(jenis);
			ktp.setPermohonan(permohonan);
			ktp.setNik(nik);
			ktp.setTanggalBuat(new Date());
			success1 = ktpFace.update(ktp);

			if(success1 == true)
				success2 = permohonanDetailFace.save(permohonanDetail);
			
			if(success1 == true && success2 == true){
				totalSuccess = true;
				List<Ktp> list = ktpFace.listData();
				String docPath = Configuration.file(getServletContext().getRealPath("/Configuration/web.ini")).get("Document", "path");
				String path = getServletContext().getRealPath("/Document/KTP.jrxml");
				Report.createKTP(list, ktp, path, docPath, formatter);
			}
			json.put("success", totalSuccess);
			json.put("explain", "Permohonan dan Pencetakan KTP tidak dapat dilakukan");
		}
		catch(Exception e){
			json.put("success", totalSuccess);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}

}

