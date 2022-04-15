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
import org.siak.data.Aktakematian;
import org.siak.data.Biodata;
import org.siak.data.Kartukeluarga;
import org.siak.data.Kartukeluargadetail;
import org.siak.data.Permohonan;
import org.siak.data.Permohonandetail;
import org.siak.face.AktaKematianFace;
import org.siak.face.BiodataFace;
import org.siak.face.KartuKeluargaDetailFace;
import org.siak.face.PermohonanDetailFace;
import org.siak.util.Configuration;
import org.siak.util.Report;

@WebServlet("/AktaKematianController")
public class AktaKematianController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private AktaKematianFace aktaKematianFace;
	private PermohonanDetailFace permohonanDetailFace;
	private KartuKeluargaDetailFace kkDetailFace;
	private BiodataFace biodataFace;
	private JSONObject json;
	private SimpleDateFormat formatter;

    public AktaKematianController() {
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
        		String noAktaKematian = request.getParameter("noAktaKematian");
        		String nik = request.getParameter("nik");
        		String noKK = request.getParameter("noKK");
        		String tanggalMeninggal = request.getParameter("tanggalMeninggal");
        		String tempatMeninggal = request.getParameter("tempatMeninggal");
        		String keterangan = request.getParameter("keterangan");
        		this.addData(out, noAktaKematian, nik, noKK, tanggalMeninggal, tempatMeninggal, keterangan);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		String noAktaKematian = request.getParameter("noAktaKematian");
        		this.deleteData(out, noAktaKematian);
        	}
        	else if(command.equalsIgnoreCase("print")){
        		String noAktaKematian = request.getParameter("noAktaKematian");
        		this.printData(out, noAktaKematian);
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
			aktaKematianFace = (AktaKematianFace) registry.lookup("aktaKematianCore");
			permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
			biodataFace = (BiodataFace) registry.lookup("biodataCore");
			kkDetailFace = (KartuKeluargaDetailFace) registry.lookup("kkDetailCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				aktaKematianFace = (AktaKematianFace) registry.lookup("aktaKematianCore");
				permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
				biodataFace = (BiodataFace) registry.lookup("biodataCore");
				kkDetailFace = (KartuKeluargaDetailFace) registry.lookup("kkDetailCore");
			}
			catch(Exception ex){
				out.print(ex);
			}
		}
	}
	
	private void loadData(PrintWriter out){
		String jsonString = "";
		List listAktaNikah = new ArrayList();
		try{
			List<Aktakematian> list = aktaKematianFace.listData();
			List<Kartukeluargadetail> list2 = kkDetailFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (Aktakematian o : list) {
				json = new JSONObject();
				json.put("noAktaKematian", o.getNoAktaKematian());
				json.put("nik", o.getBiodata().getNik());
				json.put("nama", o.getBiodata().getNama());
				json.put("tanggalMeninggal", formatter.format(o.getTanggalMeninggal()));
				String namaAyah = "";
				String namaIbu = "";
				for (Kartukeluargadetail x : list2) {
					if(x.getNiks().equalsIgnoreCase(o.getBiodata().getNik())){
						namaAyah = x.getNamaAyah();
						namaIbu = x.getNamaIbu();
					}
				}
				json.put("namaAyah", namaAyah);
				json.put("namaIbu", namaIbu);
				json.put("tempatMeninggal", o.getTempatMeninggal());
				json.put("keterangan",o.getKeterangan());
				json.put("noKk",o.getKartukeluarga().getNoKk());
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
				listAktaNikah.add(json);
				jsonString = JSONValue.toJSONString(listAktaNikah);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void addData(PrintWriter out, String noAktaKematian, String nik, String noKK, String tanggalMeninggal, String tempatMeninggal,String keterangan){
		boolean totalSuccess = false, success1 = false, success2 = false;
		json = new JSONObject();
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Aktakematian ak = new Aktakematian();
			
			Biodata b = new Biodata();
			
			String nikAyah = "";
			String nikIbu = "";
			for (Kartukeluargadetail o : kkDetailFace.listData()) {
				if(o.getNiks().equalsIgnoreCase(nik)){
					nikAyah = o.getNikayah();
					nikIbu = o.getNikibu();
				}
			}
			if(nikAyah.equalsIgnoreCase("") && nikIbu.equalsIgnoreCase("")){
				json.put("success", totalSuccess);
			}
			else{
				b.setNik(nik);
				
				Kartukeluarga kk = new Kartukeluarga();
				kk.setNoKk(noKK);
				
				Permohonan p = new Permohonan();
				p.setPermohonanId(5);
				
				ak.setBiodata(b);
				ak.setNikayah(nikAyah);
				ak.setNikibu(nikIbu);
				ak.setJenisBuat(1);
				ak.setKartukeluarga(kk);
				ak.setKeterangan(keterangan);
				ak.setNoAktaKematian(noAktaKematian);
				ak.setPermohonan(p);
				ak.setTanggalMeninggal(formatter.parse(tanggalMeninggal));
				ak.setTempatMeninggal(tempatMeninggal);
				
				Permohonandetail pd = new Permohonandetail();
				pd.setBiodata(b);
				pd.setPermohonan(p);
				pd.setTanggalPengajuan(new Date());
				
				success1 = permohonanDetailFace.save(pd);
				if(success1 == true){
					success2 = aktaKematianFace.save(ak);
				}
				if(success1 == true && success2 == true){
					totalSuccess = biodataFace.update(updateInnerBiodata(b, nik));
				}
				json.put("success", totalSuccess);
			}
			
			
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", totalSuccess);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, String noAktaKematian){
		boolean success = false;
		json = new JSONObject();
		try{
			Aktakematian ak = new Aktakematian();
			ak.setNoAktaKematian(noAktaKematian);
			success = aktaKematianFace.delete(ak);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dihapus karena memiliki keterkaitan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private Biodata updateInnerBiodata(Biodata b, String nik){
		try{
			for (Biodata o : biodataFace.listData()) {
				if(o.getNik().equalsIgnoreCase(nik)){
					b.setNik(nik);
					b.setAgama(o.getAgama());
					b.setAktif(0);
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

	private void printData(PrintWriter out, String noAktaKematian){
		json = new JSONObject();
		String docPath ="" ;
		try{
			List<Aktakematian> list = aktaKematianFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			String path = getServletContext().getRealPath("/Document/AktaKematian.jrxml");
			docPath = Configuration.file(getServletContext().getRealPath("/Configuration/web.ini")).get("Document", "path");
			Report.createAktaKematian(list, noAktaKematian, path, docPath, formatter);
		}
		catch(Exception e){
			
		}
		json.put("success", true);
		json.put("explain", "Silakan cek pada drive " + docPath);
		out.print(json);
	}
}
