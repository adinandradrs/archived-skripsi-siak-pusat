package org.siak.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.siak.data.Agama;
import org.siak.data.Aktakelahiran;
import org.siak.data.Biodata;
import org.siak.data.Biodatacacat;
import org.siak.data.Cacat;
import org.siak.data.Gruppengguna;
import org.siak.data.Hubungandetail;
import org.siak.data.Kelurahan;
import org.siak.data.Pekerjaan;
import org.siak.data.Pendidikan;
import org.siak.face.AgamaFace;
import org.siak.face.BiodataCacatFace;
import org.siak.face.BiodataFace;
import org.siak.face.CacatFace;
import org.siak.face.HubunganDetailFace;
import org.siak.face.KelurahanFace;
import org.siak.face.PekerjaanFace;
import org.siak.face.PendidikanFace;
import org.siak.util.Configuration;

@WebServlet("/BiodataController")
public class BiodataController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private HttpSession session;
	private CacatFace cacatFace;
	private AgamaFace agamaFace;
	private HubunganDetailFace hubunganDetailFace;
	private PendidikanFace pendidikanFace;
	private PekerjaanFace pekerjaanFace;
	private KelurahanFace kelurahanFace;
	private BiodataFace biodataFace;
	private BiodataCacatFace biodataCacatFace;
	private JSONObject json;
	
    public BiodataController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry();
        try{
        	String command = request.getParameter("command");
        	if(command.equalsIgnoreCase("upload")){
        		Part part = request.getPart("photo");
                String temp = part.getHeader("content-disposition").split(";")[2].trim();
                String filename = temp.substring("filename=\"".length(), temp.length() - 1);
                ServletContext servletContext = getServletContext();
        		String contextPath = servletContext.getRealPath(File.separator);
                InputStream inputStream = part.getInputStream();
                String path = contextPath+"\\Upload\\" + filename;
                FileOutputStream stream = new FileOutputStream(path);
                int i = -1;
                while ((i = inputStream.read()) != -1)
                    stream.write(i);
                stream.close();
                inputStream.close();
                
                File file = new File(path);
                byte[] bFile = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(bFile);
       	     	fis.close();
       	     	
       	     	session = request.getSession(true);
       	     	session.setAttribute("photo", bFile);
                
                JSONObject json = new JSONObject();
                json.put("success",true);
                out.print(json);
        	}
        	else if(command.equalsIgnoreCase("load")){
        		String select = request.getParameter("select");
        		if(select.equalsIgnoreCase("cacat")){
        			this.loadDataCacat(out);
        		}
        		else if(select.equalsIgnoreCase("agama")){
        			this.loadDataAgama(out);
        		}
        		else if(select.equalsIgnoreCase("statuskawin")){
        			this.loadDataStatusKawin(out);
        		}
        		else if(select.equalsIgnoreCase("statusHubungan")){
        			this.loadDataStatusHubungan(out);
        		}
        		else if(select.equalsIgnoreCase("pendidikan")){
        			this.loadDataPendidikan(out);
        		}
        		else if(select.equalsIgnoreCase("pekerjaan")){
        			this.loadDataPekerjaan(out);
        		}
        		else if(select.equalsIgnoreCase("kelurahan")){
        			HttpSession session = request.getSession(false);
        			Gruppengguna gp = (Gruppengguna) session.getAttribute("user");
        			int kecamatanID = gp.getKecamatanId();
        			this.loadDataKelurahan(out, kecamatanID);
        		}
        	}
        	else if (command.equalsIgnoreCase("add")){
        		String type = request.getParameter("type");
        		if(type.equalsIgnoreCase("biodata")){
	        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        		int kelurahanID = Integer.parseInt(request.getParameter("kelurahanComboBox"));
	        		String nama = request.getParameter("namaTextField");
	    			Date tanggalLahir = formatter.parse(request.getParameter("tanggalLahirDateField"));
	    			String tempatLahir = request.getParameter("tempatLahirTextField");
	    			String jenisKelamin = request.getParameter("kelaminComboBox");
	    			String golonganDarah = request.getParameter("golonganDarahComboBox");
	    			int agamaID = Integer.parseInt(request.getParameter("agamaComboBox"));
	    			int statusKawin = Integer.parseInt(request.getParameter("statusKawinComboBox"));
	    			int pendidikanID = Integer.parseInt(request.getParameter("pendidikanComboBox"));
	    			int pekerjaanID = Integer.parseInt(request.getParameter("pekerjaanComboBox"));
	    			int rt = Integer.parseInt(request.getParameter("rtTextField"));
	    			int rw = Integer.parseInt(request.getParameter("rwTextField"));
	    			String noAktaLahir = request.getParameter("aktaLahirTextField");
	    			String noKK = request.getParameter("kkTextField");
	    			String telepon = request.getParameter("teleponTextField");
	    			String alamat = request.getParameter("alamatTextArea");
	    			byte[] foto = (byte[])session.getAttribute("photo");
	    			session = request.getSession(true);
	    			this.addData(out, kelurahanID, nama, tanggalLahir, tempatLahir, jenisKelamin, golonganDarah, agamaID, statusKawin, 
	    					pendidikanID, pekerjaanID, noAktaLahir, noKK, telepon, alamat, foto, rt, rw);
        		}
        		else if(type.equalsIgnoreCase("cacat")){
        			String data = request.getParameter("data");
        			String[] split = data.split("\\,");
        			session = request.getSession(true);
        			this.addBiodataCacatData(out,split);
        		}
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
	
	private String ipService1;
	private String ipService2;
	private int port;
	private void bindRegistry(){
		String path = getServletContext().getRealPath("/Configuration/web.ini");
    	ipService1 = Configuration.file(path).get("Service", "ipService1");
    	ipService2 = Configuration.file(path).get("Service", "ipService2");
    	port = Integer.parseInt(Configuration.file(path).get("Service", "port"));
	}
	
	private void loadDataKelurahan(PrintWriter out, int kecamatanID){
		List listKelurahan = new LinkedList();
		String jsonString = "";
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			kelurahanFace = (KelurahanFace) registry.lookup("kelurahanCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				kelurahanFace = (KelurahanFace) registry.lookup("kelurahanCore");
			}
			catch(Exception ex){
				
			}
		}
		try{
			List<Kelurahan> list = kelurahanFace.listData();
			for (Kelurahan k : list) {
				if(k.getKecamatan().getKecamatanId().equals(kecamatanID)){
					json = new JSONObject();
					json.put("kelurahanId", k.getKelurahanId());
					json.put("nama", k.getNama());
					listKelurahan.add(json);
					jsonString = JSONValue.toJSONString(listKelurahan);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDataCacat(PrintWriter out){
		List listCacat = new LinkedList();
		String jsonString = "";
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			cacatFace = (CacatFace) registry.lookup("cacatCore");
		}
		catch(Exception e){
			try {
				registry = LocateRegistry.getRegistry(ipService2, port);
				cacatFace = (CacatFace) registry.lookup("cacatCore");
			}
			catch(Exception ex){
				
			}
		}
		try{
			List<Cacat> list = cacatFace.listData();
			for(Cacat c : list){
				json = new JSONObject();
				json.put("cacatId", c.getCacatId());
				json.put("nama", c.getNama());
				listCacat.add(json);
				jsonString = JSONValue.toJSONString(listCacat);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDataAgama(PrintWriter out){
		List listAgama = new LinkedList();
		String jsonString = "";
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			agamaFace = (AgamaFace) registry.lookup("agamaCore");
		}
		catch(Exception e){
			try {
				registry = LocateRegistry.getRegistry(ipService2, port);
				agamaFace = (AgamaFace) registry.lookup("agamaCore");
			}
			catch(Exception ex){
				
			}
		}
		try{
			List<Agama> list = agamaFace.listData();
			for(Agama a : list){
				json = new JSONObject();
				json.put("agamaId", a.getAgamaId());
				json.put("nama", a.getNama());
				listAgama.add(json);
				jsonString = JSONValue.toJSONString(listAgama);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDataStatusKawin(PrintWriter out){
		List listStatusKawin = new LinkedList();
		String jsonString = "";
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			hubunganDetailFace = (HubunganDetailFace) registry.lookup("hubunganDetailCore");
		}
		catch(Exception e){
			try {
				registry = LocateRegistry.getRegistry(ipService2, port);
				hubunganDetailFace = (HubunganDetailFace) registry.lookup("hubunganDetailCore");
			}
			catch(Exception ex){
				
			}
		}
		try{
			List<Hubungandetail> list = hubunganDetailFace.listData();
			for (Hubungandetail o : list) {
				if(o.getHubungan().getHubunganId() == 1){
					json = new JSONObject();
					json.put("hubunganDetailId", o.getHubunganDetailId());
					json.put("nama", o.getNama());
					listStatusKawin.add(json);
					jsonString = JSONValue.toJSONString(listStatusKawin);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDataStatusHubungan(PrintWriter out){
		List listStatusKawin = new LinkedList();
		String jsonString = "";
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			hubunganDetailFace = (HubunganDetailFace) registry.lookup("hubunganDetailCore");
		}
		catch(Exception e){
			try {
				registry = LocateRegistry.getRegistry(ipService2, port);
				hubunganDetailFace = (HubunganDetailFace) registry.lookup("hubunganDetailCore");
			}
			catch(Exception ex){
				
			}
		}
		try{
			List<Hubungandetail> list = hubunganDetailFace.listData();
			for (Hubungandetail o : list) {
				if(o.getHubungan().getHubunganId() == 2){
					json = new JSONObject();
					json.put("hubunganDetailId", o.getHubunganDetailId());
					json.put("nama", o.getNama());
					listStatusKawin.add(json);
					jsonString = JSONValue.toJSONString(listStatusKawin);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDataPendidikan(PrintWriter out){
		List listPendidikan = new LinkedList();
		String jsonString = "";
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			pendidikanFace = (PendidikanFace) registry.lookup("pendidikanCore");
		}
		catch(Exception e){
			try {
				registry = LocateRegistry.getRegistry(ipService2, port);
				pendidikanFace = (PendidikanFace) registry.lookup("pendidikanCore");
			}
			catch(Exception ex){
				
			}
		}
		try{
			List<Pendidikan> list = pendidikanFace.listData();
			for (Pendidikan o : list) {
				json = new JSONObject();
				json.put("pendidikanId", o.getPendidikanId());
				json.put("nama", o.getNama());
				listPendidikan.add(json);
				jsonString = JSONValue.toJSONString(listPendidikan);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void loadDataPekerjaan(PrintWriter out){
		List listPekerjaan = new LinkedList();
		String jsonString = "";
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			pekerjaanFace = (PekerjaanFace) registry.lookup("pekerjaanCore");
		}
		catch(Exception e){
			try {
				registry = LocateRegistry.getRegistry(ipService2, port);
				pekerjaanFace = (PekerjaanFace) registry.lookup("pekerjaanCore");
			}
			catch(Exception ex){
				
			}
		}
		try{
			List<Pekerjaan> list = pekerjaanFace.listData();
			for (Pekerjaan o : list) {
				json = new JSONObject();
				json.put("pekerjaanId", o.getPekerjaanId());
				json.put("nama", o.getNama());
				listPekerjaan.add(json);
				jsonString = JSONValue.toJSONString(listPekerjaan);
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void addData(PrintWriter out, int kelurahanID, String nama, Date tanggalLahir, String tempatLahir, 
			String jenisKelamin, String golonganDarah, int agamaID, int statusKawin, int pendidikanID, 
			int pekerjaanID, String noAktaLahir, String noKK,String telepon, String alamat, byte[] foto, int rt, int rw){
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			biodataFace = (BiodataFace) registry.lookup("biodataCore");
		}
		catch(Exception e){
			out.print(e.getMessage());
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				biodataFace = (BiodataFace) registry.lookup("biodataCore");
			}
			catch(Exception ex){
				out.print(ex.getMessage());
			}
		}
		json = new JSONObject();
		
		Biodata b = new Biodata();
		Agama a = new Agama();
		Hubungandetail hd = new Hubungandetail();
		Pekerjaan p = new Pekerjaan();
		Pendidikan p2 = new Pendidikan();
		Kelurahan k = new Kelurahan();
		
		a.setAgamaId(agamaID);
		hd.setHubunganDetailId(statusKawin);
		p.setPekerjaanId(pekerjaanID);
		p2.setPendidikanId(pendidikanID);
		k.setKelurahanId(kelurahanID);
		
		b.setAktif(1);
		b.setAlamat(alamat);
		b.setGolonganDarah(golonganDarah);
		b.setHubungandetail(hd);
		b.setNama(nama);
		b.setPekerjaan(p);
		b.setPendidikan(p2);
		b.setAgama(a);
		b.setTanggalLahir(tanggalLahir);
		b.setTelepon(telepon);
		b.setTempatLahir(tempatLahir);
		b.setKelurahan(k);
		b.setFoto(foto);
		b.setJenisKelamin(jenisKelamin);
		b.setRt(rt);
		b.setRw(rw);
		b.setNoAktaKelahiran(noAktaLahir);
		b.setNoKk(noKK);
		
		String nik = "";
		try{
			nik = biodataFace.save(b);
			session.setAttribute("nik", nik);
			json.put("success", true);
			json.put("explain", nik);
		}
		catch(Exception e){
			json.put("success", false);
			json.put("explain", nik);
		}
		out.print(json);
	}
	
	private void addBiodataCacatData(PrintWriter out,String[] split){
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			biodataCacatFace = (BiodataCacatFace) registry.lookup("biodataCacatCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				biodataCacatFace = (BiodataCacatFace) registry.lookup("biodataCacatCore");
			}
			catch(Exception ex){
				
			}
		}
		boolean success = false;
		json = new JSONObject();
		Biodatacacat bc = new Biodatacacat();
		Biodata b = new Biodata();
		String nik = (String) session.getAttribute("nik");
		b.setNik(nik);
		bc.setBiodata(b);
		try{
			for (String string : split) {
				Cacat c = new Cacat();
				c.setCacatId(Integer.parseInt(string));
				bc.setCacat(c);
				bc.setAktif(1);
				success = biodataCacatFace.save(bc);
			}
			json.put("success", success);
			json.put("explain","Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain",e.getMessage());
		}
		out.print(json);
	}
}
