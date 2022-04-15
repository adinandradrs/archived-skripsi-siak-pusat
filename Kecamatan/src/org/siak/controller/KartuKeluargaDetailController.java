package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Agama;
import org.siak.data.Biodata;
import org.siak.data.Hubungandetail;
import org.siak.data.Kartukeluarga;
import org.siak.data.Kartukeluargadetail;
import org.siak.data.Pekerjaan;
import org.siak.data.Pendidikan;
import org.siak.face.BiodataFace;
import org.siak.face.KartuKeluargaDetailFace;
import org.siak.face.KartuKeluargaFace;
import org.siak.util.Configuration;
import org.siak.util.Report;

@WebServlet("/KartuKeluargaDetailController")
public class KartuKeluargaDetailController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Registry registry;
	private JSONObject json;
	private KartuKeluargaDetailFace kkDetailFace;
	private KartuKeluargaFace kkFace;
	private BiodataFace biodataFace;
	private HttpSession session;
	private SimpleDateFormat formatter;
	
    public KartuKeluargaDetailController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	String command = request.getParameter("command");
        	if(command.equalsIgnoreCase("add")){
        		String niks = request.getParameter("nikTextField");
        		String nikAyah = request.getParameter("nikAyahTextField");
        		String nikIbu = request.getParameter("nikIbuTextField");
        		String namaAyah = request.getParameter("namaAyahTextField");
        		String namaIbu = request.getParameter("namaIbuTextField");
        		String jenisKelamin = request.getParameter("jenisKelamin");//
        		int agamaID = Integer.parseInt(request.getParameter("agamaID"));//
        		int pendidikanID = Integer.parseInt(request.getParameter("pendidikanID"));//
        		int pekerjaanID = Integer.parseInt(request.getParameter("pekerjaanID"));//
        		int statusKawin = Integer.parseInt(request.getParameter("statusKawin"));//
        		int statusHubungan = Integer.parseInt(request.getParameter("statusHubunganComboBox"));
        		String tempatLahir = request.getParameter("tempatLahir");//
        		String tanggalLahirString = request.getParameter("tanggalLahir");
        		formatter = new SimpleDateFormat("dd-MM-yyyy");
        		Date tanggalLahir = formatter.parse(tanggalLahirString);
        		
        		
        		String noKK = "";
        		String by = request.getParameter("by");
        		if(by.equalsIgnoreCase("session")){
					session = request.getSession(false);
					noKK = (String) session.getAttribute("noKK");
        		}
        		else if(by.equalsIgnoreCase("qstring")){
        			noKK = request.getParameter("noKK");
        		}
        		this.addData(out, niks, nikAyah, nikIbu, namaAyah, namaIbu, jenisKelamin, agamaID, pendidikanID, pekerjaanID, statusKawin, statusHubungan, noKK, tempatLahir, tanggalLahir);
        	}
        	else if(command.equalsIgnoreCase("load")){
        		String by = request.getParameter("by");
        		String noKK = "";
        		if(by.equalsIgnoreCase("session")){
        			session = request.getSession(false);
            		noKK = (String) session.getAttribute("noKK");	
        		}
        		else if(by.equalsIgnoreCase("qString")){
        			noKK = request.getParameter("noKK");
        		}
        		String type = request.getParameter("type");
        		if(type.equalsIgnoreCase("atas")){
        			this.loadDataAtas(out, noKK);
        		}
        		else if(type.equalsIgnoreCase("bawah")){
        			this.loadDataBawah(out, noKK);
        		}
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		int kkDetailID = Integer.parseInt(request.getParameter("kkDetailID"));
        		this.deleteData(out, kkDetailID);
        	}
        	else if(command.equalsIgnoreCase("print")){
        		String type = request.getParameter("type");
        		String noKK = "";
        		if(type.equalsIgnoreCase("new")){
        			session = request.getSession(false);
            		noKK = (String) session.getAttribute("noKK");	
        			this.printKartuKeluarga(out, noKK);
        		}
        		else{
        			noKK = request.getParameter("noKK");
        			this.printKartuKeluarga(out, noKK);
        		}
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
			kkDetailFace = (KartuKeluargaDetailFace) registry.lookup("kkDetailCore");
			biodataFace = (BiodataFace) registry.lookup("biodataCore");
			kkFace = (KartuKeluargaFace) registry.lookup("kkCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				kkDetailFace = (KartuKeluargaDetailFace) registry.lookup("kkDetailCore");
				biodataFace = (BiodataFace) registry.lookup("biodataCore");
				kkFace = (KartuKeluargaFace) registry.lookup("kkCore");
			}
			catch(Exception ex){
				out.print(ex);
			}
		}
	}
	
	private void addData(PrintWriter out, String niks, String nikAyah, String nikIbu, String namaAyah, String namaIbu, String jenisKelamin, 
			int agamaID, int pendidikanID, int pekerjaanID, int statusKawin, int statusHubungan, String noKK,
			String tempatLahir, Date tanggalLahir){
		boolean success = false;
		json = new JSONObject();
		try{
			Kartukeluargadetail kkd = new Kartukeluargadetail();
			Agama agama = new Agama();
			Pekerjaan pekerjaan = new Pekerjaan();
			Pendidikan pendidikan = new Pendidikan();
			Kartukeluarga kartukeluarga = new Kartukeluarga();
			Hubungandetail hubungandetailByStatusHubungan = new Hubungandetail();
			Hubungandetail hubungandetailByStatusKawin = new Hubungandetail();
			
			Biodata b = new Biodata();
			b.setNik(niks);
			biodataFace.update(this.updateInnerBiodata(b, niks, noKK));
			
			agama.setAgamaId(agamaID);
			pendidikan.setPendidikanId(pendidikanID);
			pekerjaan.setPekerjaanId(pekerjaanID);
			kartukeluarga.setNoKk(noKK);
			hubungandetailByStatusHubungan.setHubunganDetailId(statusHubungan);
			hubungandetailByStatusKawin.setHubunganDetailId(statusKawin);
			
			kkd.setAgama(agama);
			kkd.setHubungandetailByStatusHubungan(hubungandetailByStatusHubungan);
			kkd.setHubungandetailByStatusKawin(hubungandetailByStatusKawin);
			kkd.setJenisKelamin(jenisKelamin);
			kkd.setKartukeluarga(kartukeluarga);
			kkd.setNamaAyah(namaAyah);
			kkd.setNamaIbu(namaIbu);
			kkd.setNikayah(nikAyah);
			kkd.setNikibu(nikIbu);
			kkd.setNiks(niks);
			kkd.setPekerjaan(pekerjaan);
			kkd.setPendidikan(pendidikan);
			kkd.setTanggalLahir(tanggalLahir);
			kkd.setTempatLahir(tempatLahir);		
			success = kkDetailFace.save(kkd);
			json.put("success", success);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void loadDataAtas(PrintWriter out, String noKK){
		List listKKDetail = new ArrayList();
		String jsonString = "";
		try{
			List<Kartukeluargadetail> list = kkDetailFace.listData();
			List<Biodata> lBiodata = biodataFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (Kartukeluargadetail o : list) {
				if(o.getKartukeluarga().getNoKk().equals(noKK)){
					final String niks = o.getNiks();
					Collection<Biodata> namaList = CollectionUtils.select(lBiodata, new Predicate() {
						@Override
						public boolean evaluate(Object o) {
							Biodata b = (Biodata) o;
							return b.getNik().equals(niks);
						}
					});
					
					json = new JSONObject();
					json.put("kkDetailId", o.getKartuKeluargaDetailId());
					json.put("nama", namaList.iterator().next().getNama());
					json.put("niks", o.getNiks());
					json.put("jenisKelamin", o.getJenisKelamin());
					json.put("tempatLahir", o.getTempatLahir());
					json.put("tanggalLahir", formatter.format(o.getTanggalLahir()));
					json.put("agama", o.getAgama().getNama());
					json.put("pendidikan", o.getPendidikan().getNama());
					json.put("pekerjaan", o.getPekerjaan().getNama());
					listKKDetail.add(json);
					jsonString = JSONValue.toJSONString(listKKDetail);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e);
		}
	}
	
	private void loadDataBawah(PrintWriter out, String noKK){
		List listKKDetail = new ArrayList();
		String jsonString = "";
		try{
			List<Kartukeluargadetail> list = kkDetailFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (Kartukeluargadetail o : list) {
				if(o.getKartukeluarga().getNoKk().equals(noKK)){
					json = new JSONObject();
					json.put("statusKawin", o.getHubungandetailByStatusKawin().getNama());
					json.put("statusHubungan", o.getHubungandetailByStatusHubungan().getNama());
					json.put("kewarganegaraan", "INDONESIA");
					json.put("noPaspor", "-");
					json.put("noKitas", "-");
					json.put("namaAyah", o.getNamaAyah());
					json.put("namaIbu", o.getNamaIbu());
					listKKDetail.add(json);
					jsonString = JSONValue.toJSONString(listKKDetail);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			out.print(e);
		}
	}

	private void deleteData(PrintWriter out, int kkDetailID){
		boolean success = false;
		json = new JSONObject();
		try{
			Kartukeluargadetail kkd = new Kartukeluargadetail();
			kkd.setKartuKeluargaDetailId(kkDetailID);
			success = kkDetailFace.delete(kkd);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dihapus karena memiliki keterkaitan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void printKartuKeluarga(PrintWriter out, String noKK){
		json = new JSONObject();
		try{
			List<Kartukeluargadetail> list = kkDetailFace.listData();
			List<Biodata> lBiodata = biodataFace.listData();
			List<Kartukeluarga> lKK = kkFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			String docPath = Configuration.file(getServletContext().getRealPath("/Configuration/web.ini")).get("Document", "path");
			String path = getServletContext().getRealPath("/Document/MKartuKeluarga.jrxml");
			Report.createKartuKeluarga(list, lBiodata,lKK, noKK, path, docPath, formatter);
			json.put("success", true);
			json.put("explain","");
		}
		catch(Exception e){
			json.put("success", true);
			json.put("explain",e);
		}
		out.print(json);
	}
	
	private Biodata updateInnerBiodata(Biodata b, String nik, String noKK){
		try{
			for (Biodata o : biodataFace.listData()) {
				if(o.getNik().equalsIgnoreCase(nik)){
					b.setNik(nik);
					b.setAgama(o.getAgama());
					b.setAktif(o.getAktif());
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
					b.setNoKk(noKK);
					b.setTempatLahir(o.getTempatLahir());
				}
			}
		}
		catch(Exception e){
			
		}
		return b;
	}
	
}
