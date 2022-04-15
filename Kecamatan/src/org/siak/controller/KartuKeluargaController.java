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
        		session = request.getSession(false);
        		Gruppengguna gp = (Gruppengguna) session.getAttribute("user");
        		this.loadData(out, gp.getKecamatanId());
        	}
        	else if(command.equalsIgnoreCase("add")){
        		session = request.getSession(true);
        		String nik = request.getParameter("nik");
        		int kelurahanID = Integer.parseInt(request.getParameter("kelurahanID"));
        		this.addData(out, session, nik ,kelurahanID);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		String nik = request.getParameter("nik");
        		String noKK = request.getParameter("noKK");
        		Date tanggalBuat = new Date();
        		int jenisBuat = Integer.parseInt(request.getParameter("jenisCetak"));
        		this.editData(out, noKK, nik, tanggalBuat, jenisBuat);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		String noKK = request.getParameter("noKK");
        		this.deleteData(out, noKK);
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
	
	private void loadData(PrintWriter out, int kecamatanID){
		List listKK = new ArrayList();
		String jsonString = "";
		try{
			List<Kartukeluarga> list = kkFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (Kartukeluarga kk : list) {
				if(kk.getBiodata().getKelurahan().getKecamatan().getKecamatanId().equals(kecamatanID)){
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
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e.getMessage());
		}
	}
	
	private void addData(PrintWriter out, HttpSession session, String nik, int kelurahanID){
		json = new JSONObject();
		try{
			Kartukeluarga kk = new Kartukeluarga();
			
			Biodata biodata = new Biodata();
			Permohonan permohonan = new Permohonan();
			Kelurahan kelurahan = new Kelurahan();
			kelurahan.setKelurahanId(kelurahanID);
			
			biodata.setNik(nik);
			biodata.setKelurahan(kelurahan);
			permohonan.setPermohonanId(2);
			kk.setBiodata(biodata);
			kk.setJenisBuat(1);
			kk.setPermohonan(permohonan);
			kk.setTanggalBuat(new Date());
			
			Permohonandetail pd = new Permohonandetail();
			pd.setBiodata(biodata);
			pd.setPermohonan(permohonan);
			pd.setTanggalPengajuan(new Date());
			
			String noKK = "Tidak dapat melakukan permohonan pembuatan Kartu Keluarga";
			if(permohonanDetailFace.save(pd)){
				noKK = kkFace.save(kk);
				session.setAttribute("noKK", noKK);
			}
			json.put("success", true);
			json.put("explain", noKK);
		}
		catch(Exception e){
			json.put("success", false);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void editData(PrintWriter out, String noKK, String nik, Date tanggalBuat, int jenisBuat){
		json = new JSONObject();
		boolean totalSuccess = false, success1 = false, success2 = false;
		try{
			Kartukeluarga kk = new Kartukeluarga();
			
			Biodata biodata = new Biodata();
			Permohonan permohonan = new Permohonan();
			
			biodata.setNik(nik);
			permohonan.setPermohonanId(2);
			kk.setNoKk(noKK);
			kk.setBiodata(biodata);
			kk.setJenisBuat(jenisBuat);
			kk.setPermohonan(permohonan);
			kk.setTanggalBuat(new Date());
			
			Permohonandetail pd = new Permohonandetail();
			pd.setBiodata(biodata);
			pd.setPermohonan(permohonan);
			pd.setTanggalPengajuan(new Date());
			
			success1 = permohonanDetailFace.save(pd);
			out.print(success1);
			
			if(success1 == true)
				success2 = kkFace.update(kk);
			if(success1==true && success2 ==true)
				totalSuccess = true;
			json.put("success", totalSuccess);
			json.put("explain", "Tidak dapat dilakukan pencetakan dan perubahan data");
		}
		catch(Exception e){
			json.put("success", totalSuccess);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, String noKK){
		json = new JSONObject();
		boolean success = false;
		try{
			Kartukeluarga kk = new Kartukeluarga();
			kk.setNoKk(noKK);
			success = kkFace.delete(kk);
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