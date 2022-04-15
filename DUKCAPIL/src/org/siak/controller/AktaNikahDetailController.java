package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
import org.siak.data.Aktanikah;
import org.siak.data.Aktanikahdetail;
import org.siak.data.Biodata;
import org.siak.data.Hubungandetail;
import org.siak.data.Permohonan;
import org.siak.data.Permohonandetail;
import org.siak.face.AktaNikahDetailFace;
import org.siak.face.BiodataFace;
import org.siak.face.PermohonanDetailFace;
import org.siak.util.Configuration;

@WebServlet("/AktaNikahDetailController")
public class AktaNikahDetailController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private Registry registry;
    private JSONObject json;
    private AktaNikahDetailFace aktaNikahDetailFace;
    private BiodataFace biodataFace;
    private PermohonanDetailFace permohonanDetailFace;
	
    public AktaNikahDetailController() {
        super();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	String command = request.getParameter("command");
        	if(command.equalsIgnoreCase("load")){
        		String noAktaNikah = request.getParameter("noAktaNikah");
        		this.loadData(out, noAktaNikah);
        	}
        	else if(command.equalsIgnoreCase("add")){
        		String noAktaNikah = request.getParameter("noAktaNikah");
        		String nikSuami = request.getParameter("nikSuami");
        		String nikIstri = request.getParameter("nikIstri");
        		this.addData(out, noAktaNikah, nikSuami, nikIstri);
        	}
        	else if(command.equalsIgnoreCase("delete")){
        		int aktaNikahDetailID = Integer.parseInt(request.getParameter("aktaNikahDetailID"));
        		this.deleteData(out, aktaNikahDetailID);
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
			aktaNikahDetailFace = (AktaNikahDetailFace) registry.lookup("aktaNikahDetailCore");
			permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
			biodataFace = (BiodataFace) registry.lookup("biodataCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				aktaNikahDetailFace = (AktaNikahDetailFace) registry.lookup("aktaNikahDetailCore");
				permohonanDetailFace = (PermohonanDetailFace) registry.lookup("permohonanDetailCore");
				biodataFace = (BiodataFace) registry.lookup("biodataCore");
			}
			catch(Exception ex){
				
			}
		}
	}
	
	private void loadData(PrintWriter out, String noAktaNikah){
		String jsonString = "";
		List listAktaNikahDetail = new ArrayList();
		try{
			List<Aktanikahdetail> list = aktaNikahDetailFace.listData();
			for (Aktanikahdetail o : list) {
				if(o.getAktanikah().getNoAktaNikah().equalsIgnoreCase(noAktaNikah)){
					json = new JSONObject();
					json.put("nikSuami", o.getBiodataByNiksuami().getNik());
					json.put("nikIstri", o.getBiodataByNikistri().getNik());
					json.put("namaSuami", o.getBiodataByNiksuami().getNama());
					json.put("namaIstri", o.getBiodataByNikistri().getNama());
					json.put("aktaNikahDetailId", o.getAktaNikahDetailId());
					listAktaNikahDetail.add(json);
					jsonString = JSONValue.toJSONString(listAktaNikahDetail);
				}
			}
			out.print(jsonString);
		}
		catch(Exception e){
			
		}
	}
	
	private void addData(PrintWriter out, String noAktaNikah, String nikSuami, String nikIstri){
		boolean totalSuccess = false, success1 = false, success2 = false;
		json = new JSONObject();
		try{
			Aktanikahdetail and = new Aktanikahdetail();
			Aktanikah an = new Aktanikah();
			Biodata b1 = new Biodata();
			Biodata b2 = new Biodata();
			an.setNoAktaNikah(noAktaNikah);
			b1.setNik(nikSuami);
			b2.setNik(nikIstri);
			
			and.setAktanikah(an);
			and.setBiodataByNiksuami(b1);
			and.setBiodataByNikistri(b2);
			success1 = aktaNikahDetailFace.save(and);
			
			Permohonan permohonan = new Permohonan();
			permohonan.setPermohonanId(3);
			
			Permohonandetail permohonanDetail = new Permohonandetail();
			if(success1 == true){
				permohonanDetail.setTanggalPengajuan(new Date());
				permohonanDetail.setPermohonan(permohonan);
				permohonanDetail.setBiodata(b1);
				success2 = permohonanDetailFace.save(permohonanDetail);
				permohonanDetail.setBiodata(b2);
				success2 = permohonanDetailFace.save(permohonanDetail);
			}
			if(success1 == true && success2==true){
				totalSuccess = true;
				biodataFace.update(this.updateInnerBiodata(b1, nikSuami));
				biodataFace.update(this.updateInnerBiodata(b2, nikIstri));
			}
			json.put("success", totalSuccess);
			json.put("explain", "Data tidak dapat ditambahkan");
		}
		catch(Exception e){
			json.put("success", totalSuccess);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}
	
	private void deleteData(PrintWriter out, int aktaNikahDetailID){
		boolean success = false;
		json = new JSONObject();
		try{
			Aktanikahdetail and = new Aktanikahdetail();
			and.setAktaNikahDetailId(aktaNikahDetailID);
			success = aktaNikahDetailFace.delete(and);
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
					b.setAktif(o.getAktif());
					b.setAlamat(o.getAlamat());
					b.setFoto(o.getFoto());
					b.setGolonganDarah(o.getGolonganDarah());
					Hubungandetail hd = new Hubungandetail();
					hd.setHubunganDetailId(2);
					b.setHubungandetail(hd);
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
