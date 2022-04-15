package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Agama;
import org.siak.data.Aktanikah;
import org.siak.data.Aktanikahdetail;
import org.siak.data.Biodata;
import org.siak.data.Gruppengguna;
import org.siak.data.Hubungandetail;
import org.siak.data.Kartukeluargadetail;
import org.siak.data.Kelurahan;
import org.siak.data.Pekerjaan;
import org.siak.data.Pendidikan;
import org.siak.face.AktaNikahDetailFace;
import org.siak.face.AktaNikahFace;
import org.siak.face.BiodataFace;
import org.siak.face.KartuKeluargaDetailFace;
import org.siak.util.Configuration;

@WebServlet("/PendudukController")
public class PendudukController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	
	private BiodataFace biodataFace;
	private KartuKeluargaDetailFace kkDetailFace;
	private AktaNikahDetailFace aktaNikahDetailFace;
	
	private JSONObject json;
	private SimpleDateFormat formatter;
	
    public PendudukController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	HttpSession session = request.getSession(false);
			Gruppengguna gp = (Gruppengguna) session.getAttribute("user");
			int kecamatanID = gp.getKecamatanId();
        	String command = request.getParameter("command");
        	if(command.equalsIgnoreCase("load")){
    			this.loadData(out, kecamatanID);
    		}
        	else if(command.equalsIgnoreCase("search")){
        		String nik = request.getParameter("nik");
        		this.loadDataDetail(out, kecamatanID, nik);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		String nik = request.getParameter("nik");
        		this.deleteData(out, nik);
        	}
        	else if(command.equalsIgnoreCase("edit")){
        		String nik = request.getParameter("nik");
        		String nama = request.getParameter("nama");
        		int agama = Integer.parseInt(request.getParameter("agama"));
        		String tanggalLahir = request.getParameter("tanggalLahir");
        		String tempatLahir = request.getParameter("tempatLahir");
        		String golonganDarah = request.getParameter("golonganDarah");
        		int statusKawin = Integer.parseInt(request.getParameter("statusKawin"));
        		int pendidikan = Integer.parseInt(request.getParameter("pendidikan"));
        		int pekerjaan = Integer.parseInt(request.getParameter("pekerjaan"));
        		String aktaKelahiran = request.getParameter("aktaKelahiran");
        		String kartuKeluarga = request.getParameter("kartuKeluarga");
        		String telepon = request.getParameter("telepon");
        		String alamat = request.getParameter("alamat");
        		int rt = Integer.parseInt(request.getParameter("rt"));
        		int rw = Integer.parseInt(request.getParameter("rw"));
        		int kelurahanID = Integer.parseInt(request.getParameter("kelurahanID"));
        		String jenisKelamin = request.getParameter("jenisKelamin");
        		
        		Biodata b = new Biodata();
        		Agama a = new Agama();
        		Pendidikan p1 = new Pendidikan();
        		Pekerjaan p2 = new Pekerjaan();
        		Hubungandetail hd = new Hubungandetail();
        		Kelurahan k = new Kelurahan();
        		
        		a.setAgamaId(agama);
        		p1.setPendidikanId(pendidikan);
        		p2.setPekerjaanId(pekerjaan);
        		hd.setHubunganDetailId(statusKawin);
        		k.setKelurahanId(kelurahanID);
        		
        		b.setNik(nik);
        		b.setAgama(a);
        		b.setAktif(1);
        		b.setAlamat(alamat);
        		b.setGolonganDarah(golonganDarah);
        		b.setHubungandetail(hd);
        		b.setJenisKelamin(jenisKelamin);
        		b.setKelurahan(k);
        		b.setNama(nama);
        		b.setRt(rt);
        		b.setRw(rw);
        		formatter = new SimpleDateFormat("yyyy-MM-dd");
        		b.setTanggalLahir(formatter.parse(tanggalLahir));
        		b.setTelepon(telepon);
        		b.setTempatLahir(tempatLahir);
        		b.setNoKk(kartuKeluarga);
        		b.setNoAktaKelahiran(aktaKelahiran);
        		b.setPekerjaan(p2);
        		b.setPendidikan(p1);
        		this.updateData(out, b);
        	}
        }
        catch(Exception e){
        	out.print(e);
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
    		kkDetailFace = (KartuKeluargaDetailFace) registry.lookup("kkDetailCore");
    		aktaNikahDetailFace = (AktaNikahDetailFace) registry.lookup("aktaNikahDetailCore");
    	}
    	catch(Exception e){
    		try{
        		registry = LocateRegistry.getRegistry(ipService2, port);
        		biodataFace = (BiodataFace) registry.lookup("biodataCore");
        		kkDetailFace = (KartuKeluargaDetailFace) registry.lookup("kkDetailCore");
        		aktaNikahDetailFace = (AktaNikahDetailFace) registry.lookup("aktaNikahDetailCore");
    		}
    		catch(Exception ex){
    			out.print(ex.getMessage());
    		}
    	}
	}
	
	private void loadData(PrintWriter out, int kecamatanID){
		List listBiodata = new LinkedList();
		String jsonString = "";
		try{
			List<Biodata> list = biodataFace.listData();
			List<Kartukeluargadetail> list2 = kkDetailFace.listData();
			List<Aktanikahdetail> list3 = aktaNikahDetailFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (Biodata b : list) {
				if(b.getKelurahan().getKecamatan().getKecamatanId().equals(kecamatanID) && b.getAktif() == 1){
					json = new JSONObject();
					json.put("nik", b.getNik());
					json.put("nama", b.getNama());
					json.put("agama", b.getAgama().getNama());
					json.put("pekerjaan", b.getPekerjaan().getNama());
					json.put("pendidikan", b.getPendidikan().getNama());
					json.put("jenisKelamin", b.getJenisKelamin());
					json.put("golonganDarah", b.getGolonganDarah());
					json.put("tempatLahir", b.getTempatLahir());
					json.put("kelurahanId", b.getKelurahan().getKelurahanId());
					json.put("foto", "<img src='../ImageFrame?nik="+b.getNik()+"' style='border:1px solid black;' width=65px height=65px></img>");
					json.put("tanggalLahir",  formatter.format(b.getTanggalLahir()));
					
					String noKK = "-";
					for (Kartukeluargadetail kkd : list2) {
						if(kkd.getNiks().equals(b.getNik())){
							noKK = kkd.getKartukeluarga().getNoKk();
							break;
						}
					}
					json.put("kartuKeluarga", noKK);
					json.put("noKK", b.getNoKk());
					json.put("telepon", b.getTelepon());
					json.put("alamat", b.getAlamat());
					json.put("rt", b.getRt());
					json.put("rw", b.getRw());
					
					String noAktaNikah = "-";
					for(Aktanikahdetail and : list3){
						if(and.getBiodataByNikistri().getNik().equals(b.getNik()) || and.getBiodataByNiksuami().getNik().equals(b.getNik())){
							noAktaNikah = and.getAktanikah().getNoAktaNikah();
							break;
						}
					}
					json.put("aktaNikah", noAktaNikah);
					
					//FOR : Kartu Keluarga 
					json.put("kelurahanId", b.getKelurahan().getKelurahanId());
					json.put("pekerjaanId", b.getPekerjaan().getPekerjaanId());
					json.put("pendidikanId", b.getPendidikan().getPendidikanId());
					json.put("agamaId", b.getAgama().getAgamaId());
					json.put("aktaKelahiran", b.getNoAktaKelahiran());
					json.put("statusKawin", b.getHubungandetail().getHubunganDetailId());
					
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
	
	private void loadDataDetail(PrintWriter out, int kecamatanID, String nik){
		try{
			List<Biodata> list = biodataFace.listData();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			int counter = 0;
			for (Biodata b : list) {
				if(b.getKelurahan().getKecamatan().getKecamatanId().equals(kecamatanID) && b.getAktif() == 1 && b.getNik().equalsIgnoreCase(nik)){
					json = new JSONObject();
					json.put("success", true);
					json.put("nik", b.getNik());
					json.put("nama", b.getNama());
					json.put("agama", b.getAgama().getNama());
					json.put("pekerjaan", b.getPekerjaan().getNama());
					json.put("pendidikan", b.getPendidikan().getNama());
					json.put("jenisKelamin", b.getJenisKelamin());
					json.put("golonganDarah", b.getGolonganDarah());
					json.put("tempatLahir", b.getTempatLahir());
					json.put("kelurahanId", b.getKelurahan().getKelurahanId());
					json.put("foto", "<img src='../ImageFrame?nik="+b.getNik()+"' style='border:1px solid black;' width=65px height=65px></img>");
					json.put("tanggalLahir",  formatter.format(b.getTanggalLahir()));
					counter++;
					
				}
			}
			if(counter>0){
				out.print(json);
			}
			else{
				json = new JSONObject();
				json.put("success", false);
				json.put("nama", "Tidak ditemukan");
				out.print(json);
			}
		}
		catch(Exception e){
			out.print(e);
		}
	}

	private void updateData(PrintWriter out, Biodata b){
		boolean success = false;
		json = new JSONObject();
		try{
			List<Biodata> list = biodataFace.listData();
			for (Biodata b_ : list) {
				if(b_.getNik().equalsIgnoreCase(b.getNik())){
					b.setFoto(b_.getFoto());
					break;
				}
			}
			success = biodataFace.update(b);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dilakukan perubahan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, String nik){
		boolean success = false;
		json = new JSONObject();
		try{
			Biodata b = new Biodata();
			b.setNik(nik);
			success = biodataFace.delete(b);
			json.put("success", success);
			json.put("explain", "Data tidak dapat dihapus karena terdapat keterkaitan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
}
