package org.siak.util;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.siak.data.Biodata;
import org.siak.data.Biodatapindah;
import org.siak.data.Gruppengguna;
import org.siak.data.Kota;
import org.siak.data.Ktp;
import org.siak.data.Pengguna;
import org.siak.dummy.BiodataKelurahanDummy;
import org.siak.dummy.BiodataPindahDummy;
import org.siak.dummy.KelurahanDummy;
import org.siak.dummy.KotaDummy;
import org.siak.face.BiodataFace;
import org.siak.face.BiodataPindahFace;
import org.siak.face.KtpFace;
import org.siak.face.LoginFace;

public class ServiceRail {
	
	private BiodataPindahFace biodataPindahFace;
	private LoginFace loginFace;
	private KtpFace ktpFace;
	private BiodataFace biodataFace;
	
	private Registry registry;
	private String ipService1;
	private String ipService2;
	private int port;
	private int kota = 0;
	private int kelurahan = 0;
	
	private void bindRegistry(){
		String path = "D:/web.ini";
		this.ipService1 = Configuration.file(path).get("Service", "ipService1");
		this.ipService2 = Configuration.file(path).get("Service", "ipService2");
		this.port = Integer.parseInt(Configuration.file(path).get("Service", "port"));
		this.kota = Integer.parseInt(Configuration.file(path).get("Portal", "kota"));
		this.kelurahan = Integer.parseInt(Configuration.file(path).get("Portal", "kelurahan"));
		try{
			registry = LocateRegistry.getRegistry(ipService1, port);
			loginFace = (LoginFace) registry.lookup("loginCore");
			biodataPindahFace = (BiodataPindahFace) registry.lookup("biodataPindahCore");
			ktpFace = (KtpFace) registry.lookup("ktpCore");
			biodataFace = (BiodataFace) registry.lookup("biodataCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2, port);
				loginFace = (LoginFace) registry.lookup("loginCore");
				biodataPindahFace = (BiodataPindahFace) registry.lookup("biodataPindahCore");
				ktpFace = (KtpFace) registry.lookup("ktpCore");
				biodataFace = (BiodataFace) registry.lookup("biodataCore");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	private boolean allowLoginKota(String username, String sandiRef){
		bindRegistry();
		boolean allow = false;
		try{
			Kota k = new Kota();
			String sandi = Encryption.SHA1(sandiRef);
			k.setUsername(username);
			k.setSandi(sandi);
			allow = loginFace.loginKota(k);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return allow;
	}

	public KotaDummy dataKota(String username, String sandiRef){
		bindRegistry();
		KotaDummy kd = null;
		if(kota == 1){
			if(allowLoginKota(username, sandiRef)){
				try{
					Kota k = new Kota();
					k.setUsername(username);
					String sandi = Encryption.SHA1(sandiRef);
					k.setSandi(sandi);
					k = loginFace.dataKota(k);
					kd = new KotaDummy();
					kd.setKotaID(k.getKotaId());
					kd.setNamaKota(k.getNama());
					kd.setUsername(k.getUsername());
					System.out.print(kd.getKotaID());
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			return kd;
		}
		return null;
	}
	
	public List<BiodataPindahDummy> listInfoBiodataPindah(int kotaID){
		bindRegistry();
		if(kota == 1){
			List<BiodataPindahDummy> listInfo = new ArrayList<BiodataPindahDummy>();
			try{
				List<Biodatapindah> list = biodataPindahFace.listData();
				BiodataPindahDummy obj = null;
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				for (Biodatapindah x : list) {
					if(x.getKota().getKotaId().equals(kotaID)){
						obj = new BiodataPindahDummy();
						obj.setAgama(x.getBiodata().getAgama().getNama());
						obj.setNama(x.getBiodata().getNama());
						obj.setAlamat(x.getBiodata().getAlamat());
						obj.setNik(x.getNik());
						obj.setPendidikan(x.getBiodata().getPendidikan().getNama());
						obj.setPekerjaan(x.getBiodata().getPekerjaan().getNama());
						obj.setNoTelepon(x.getBiodata().getTelepon());
						obj.setFoto(x.getBiodata().getFoto());
						obj.setTanggalLahir(formatter.format(x.getBiodata().getTanggalLahir()));
						obj.setJenisKelamin(x.getBiodata().getJenisKelamin());
						obj.setTanggalPengajuan(formatter.format(x.getTanggalPengajuan()));
						listInfo.add(obj);
					}
				}
			}
			catch(Exception e){
				
			}
			return listInfo;
		}
		else{
			return null;
		}
	}
	
	public KelurahanDummy dataKelurahan(String username, String sandi, Date waktuLogin, String ipAddress){
		this.bindRegistry();
		KelurahanDummy kd = null;
		try{
			Pengguna p = new Pengguna();
			p.setUsername(username);
			p.setSandi(Encryption.SHA1(sandi));
			p.setWaktuLogin(waktuLogin);
			p.setIplogin(ipAddress);
			boolean success = loginFace.loginKelurahan(p);
			if(success){
				Gruppengguna gp = loginFace.dataPengguna(p);
				kd = new KelurahanDummy();
				kd.setKelurahanID(gp.getKelurahanId());
				kd.setUsername(gp.getPengguna().getUsername());
			}
		}
		catch(Exception e){
			
		}
		return kd;
	}
	
	public List<BiodataKelurahanDummy> listInfoKTPKelurahan(int kelurahanID){
		bindRegistry();
		if(kelurahan == 1){
			List<BiodataKelurahanDummy> listInfo = new ArrayList<BiodataKelurahanDummy>();
			try{
				List<Ktp> list = ktpFace.listData();
				BiodataKelurahanDummy obj = null;
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				for (Ktp x : list) {
					if(x.getBiodata().getKelurahan().getKelurahanId().equals(kelurahanID)){
						obj = new BiodataKelurahanDummy();
						obj.setAgama(x.getBiodata().getAgama().getNama());
						obj.setNama(x.getBiodata().getNama());
						obj.setAlamat(x.getBiodata().getAlamat());
						obj.setNik(x.getNik());
						obj.setPekerjaan(x.getBiodata().getPekerjaan().getNama());
						obj.setNoTelepon(x.getBiodata().getTelepon());
						obj.setFoto(x.getBiodata().getFoto());
						obj.setTanggalLahir(formatter.format(x.getBiodata().getTanggalLahir()));
						obj.setJenisKelamin(x.getBiodata().getJenisKelamin());
						obj.setExpiredKTP(x.getExpired());
						listInfo.add(obj);
					}
				}
			}
			catch(Exception e){
				
			}
			return listInfo;
		}
		else{
			return null;
		}
	}
	
	public List<BiodataKelurahanDummy> listInfoBiodataKelurahan(int kelurahanID){
		bindRegistry();
		if(kelurahan == 1){
			List<BiodataKelurahanDummy> listInfo = new ArrayList<BiodataKelurahanDummy>();
			try{
				List<Biodata> list = biodataFace.listData();
				BiodataKelurahanDummy obj = null;
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				for (Biodata x : list) {
					if(x.getKelurahan().getKelurahanId().equals(kelurahanID)){
						obj = new BiodataKelurahanDummy();
						obj.setAgama(x.getAgama().getNama());
						obj.setNama(x.getNama());
						obj.setAlamat(x.getAlamat());
						obj.setNik(x.getNik());
						obj.setPekerjaan(x.getPekerjaan().getNama());
						obj.setPendidikan(x.getPendidikan().getNama());
						obj.setNoTelepon(x.getTelepon());
						obj.setFoto(x.getFoto());
						obj.setTanggalLahir(formatter.format(x.getTanggalLahir()));
						obj.setJenisKelamin(x.getJenisKelamin());
						listInfo.add(obj);
					}
				}
			}
			catch(Exception e){
				
			}
			return listInfo;
		}
		else{
			return null;
		}
	}
}
