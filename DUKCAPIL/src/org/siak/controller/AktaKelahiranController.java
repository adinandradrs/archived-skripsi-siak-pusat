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
import org.siak.data.Aktakelahiran;
import org.siak.data.Aktanikahdetail;
import org.siak.data.Biodata;
import org.siak.data.Permohonan;
import org.siak.data.Permohonandetail;
import org.siak.face.AktaKelahiranFace;
import org.siak.face.AktaNikahDetailFace;
import org.siak.face.PermohonanDetailFace;
import org.siak.util.Configuration;
import org.siak.util.Report;

@WebServlet("/AktaKelahiranController")
public class AktaKelahiranController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private AktaKelahiranFace aktaKelahiranFace;
	private PermohonanDetailFace permohonanDetailFace;
	private AktaNikahDetailFace aktaNikahDetailFace;
	private JSONObject json;
	private SimpleDateFormat formatter;
	
    public AktaKelahiranController() {
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
            	String noAktaKelahiran = request.getParameter("noAktaKelahiran");
    	    	String nama = request.getParameter("nama");
    	    	String tanggalLahir = request.getParameter("tanggalLahir");
    	    	String tempatLahir = request.getParameter("tempatLahir");
    	    	String noAktaNikah = request.getParameter("noAktaNikah");
    	    	String jenisKelamin = request.getParameter("jenisKelamin");
    	    	String noKK = request.getParameter("noKK");
    	    	String jenisKelahiran = request.getParameter("jenisKelahiran");
    	    	String bantuanKelahiran = request.getParameter("bantuanKelahiran");
    	    	this.addData(out, noAktaKelahiran, nama, tanggalLahir, tempatLahir, noAktaNikah, jenisKelamin, noKK, jenisKelahiran, bantuanKelahiran);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		String noAktaKelahiran = request.getParameter("noAktaKelahiran");
        		this.deleteData(out, noAktaKelahiran);
        	}
        	else if(command.equalsIgnoreCase("print")){
        		String noAktaKelahiran = request.getParameter("noAktaKelahiran");
        		String nikAyah = request.getParameter("nikAyah");
        		String nikIbu = request.getParameter("nikIbu");
        		this.printData(out, noAktaKelahiran, nikAyah, nikIbu);
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
			aktaKelahiranFace = (AktaKelahiranFace) registry.lookup("aktaKelahiranCore");
			permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
			aktaNikahDetailFace = (AktaNikahDetailFace) registry.lookup("aktaNikahDetailCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				aktaKelahiranFace = (AktaKelahiranFace) registry.lookup("aktaKelahiranCore");
				permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
				aktaNikahDetailFace = (AktaNikahDetailFace) registry.lookup("aktaNikahDetailCore");
			}
			catch(Exception ex){
				out.print(ex);
			}
		}
	}
	
	private void loadData(PrintWriter out){
		List listAktaKelahiran = new ArrayList(); 
		String jsonString = "";
		try{
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			List<Aktakelahiran> list = aktaKelahiranFace.listData();
			for (Aktakelahiran o : list) {
				json = new JSONObject();
				json.put("noAktaKelahiran", o.getNoAktaKelahiran());
				json.put("nama", o.getNama());
				json.put("tanggalLahir", formatter.format(o.getTanggalLahir()));
				json.put("tempatLahir", o.getTempatLahir());
				json.put("nikAyah", o.getBiodataByNikayah().getNik());
				json.put("nikIbu", o.getBiodataByNikibu().getNik());
				json.put("jenisKelamin", o.getJenisKelamin());
				json.put("tanggalBuat", formatter.format(o.getTanggalBuat()));
				json.put("noKK", o.getNoKk());
				json.put("jenisKelahiran", o.getJenisKelahiran());
				json.put("bantuanKelahiran", o.getBantuanKelahiran());
				json.put("namaAyah", o.getBiodataByNikayah().getNama());
				json.put("namaIbu", o.getBiodataByNikibu().getNama());
				json.put("golDarahAyah", o.getBiodataByNikayah().getGolonganDarah());
				json.put("golDarahIbu", o.getBiodataByNikibu().getGolonganDarah());
				switch (o.getJenisBuat()) {
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
					json.put("jenisBuat", "Tidak ketahui");
					break;
				}
				listAktaKelahiran.add(json);
				jsonString = JSONValue.toJSONString(listAktaKelahiran);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void addData(PrintWriter out,String noAktaKelahiran, String nama, String tanggalLahir, String tempatLahir, 
			String noAktaNikah, String jenisKelamin, String noKK, String jenisKelahiran, String bantuanKelahiran){
		boolean totalSuccess = false, success1 = false, success2 = false;
		json = new JSONObject();
		try{
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			Aktakelahiran ak = new Aktakelahiran();
			Biodata b1 = new Biodata();
			
			String nikAyah = "";
			String nikIbu = "";
			for (Aktanikahdetail o : aktaNikahDetailFace.listData()) {
				if(o.getAktanikah().getNoAktaNikah().equals(noAktaNikah)){
					nikAyah = o.getBiodataByNiksuami().getNik();
					nikIbu = o.getBiodataByNikistri().getNik();
				}
			}
			
			b1.setNik(nikAyah);
			Biodata b2 = new Biodata();
			b2.setNik(nikIbu);
			Permohonan p = new Permohonan();
			p.setPermohonanId(4);
			
			ak.setBantuanKelahiran(bantuanKelahiran);
			ak.setBiodataByNikayah(b1);
			ak.setBiodataByNikibu(b2);
			ak.setJenisBuat(1);
			ak.setJenisKelahiran(jenisKelahiran);
			ak.setJenisKelamin(jenisKelamin);
			ak.setNama(nama);
			ak.setNoAktaKelahiran(noAktaKelahiran);
			ak.setNoKk(noKK);
			ak.setPermohonan(p);
			ak.setTanggalBuat(new Date());
			ak.setTanggalLahir(formatter.parse(tanggalLahir));
			ak.setTempatLahir(tempatLahir);
					
			Permohonandetail pd = new Permohonandetail();
			pd.setBiodata(b1);
			pd.setPermohonan(p);
			pd.setTanggalPengajuan(new Date());
		
			success1 = permohonanDetailFace.save(pd);
			if(success1 == true)
				success2 = aktaKelahiranFace.save(ak);
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
	
	private void deleteData(PrintWriter out, String noAktaKelahiran){
		boolean success = false;
		json = new JSONObject();
		try{
			Aktakelahiran ak = new Aktakelahiran();
			ak.setNoAktaKelahiran(noAktaKelahiran);
			
			success = aktaKelahiranFace.delete(ak);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dihapus karena memiliki keterkaitan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void printData(PrintWriter out, String noAktaKelahiran, String nikAyah, String nikIbu){
		json = new JSONObject();
		String docPath ="" ;
		try{
			List<Aktakelahiran> list = aktaKelahiranFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			String path = getServletContext().getRealPath("/Document/AktaKelahiran.jrxml");
			docPath = Configuration.file(getServletContext().getRealPath("/Configuration/web.ini")).get("Document", "path");
			Report.createAktaKelahiran(list, noAktaKelahiran, nikAyah, nikIbu, path, docPath, formatter);
		}
		catch(Exception e){
			
		}
		json.put("success", true);
		json.put("explain", "Silakan cek pada drive " + docPath);
		out.print(json);
	}

}
