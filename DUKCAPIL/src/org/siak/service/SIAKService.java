package org.siak.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.siak.dummy.BiodataKelurahanDummy;
import org.siak.dummy.BiodataPindahDummy;
import org.siak.dummy.KelurahanDummy;
import org.siak.dummy.KotaDummy;
import org.siak.util.ServiceRail;

@WebService()
public class SIAKService {

	@WebMethod(operationName="authKota")
	public KotaDummy authKota(
			@WebParam(name="username") String username, 
			@WebParam(name="sandi") String sandi){
		return new ServiceRail().dataKota(username, sandi);
	}
	
	@WebMethod(operationName="infoKota")
	public List<BiodataPindahDummy> infoKota(
			@WebParam(name="kotaID") int kotaID){
		return new ServiceRail().listInfoBiodataPindah(kotaID);
	}
	
	@WebMethod(operationName="authKelurahan")
	public KelurahanDummy authKelurahan(
			@WebParam(name="username") String username,
			@WebParam(name="sandi") String sandi,
			@WebParam(name="waktuString") String waktuString,
			@WebParam(name="ipAddress") String ipAddress){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date waktuLogin = null;
		try {
			waktuLogin = formatter.parse(waktuString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new ServiceRail().dataKelurahan(username, sandi, waktuLogin, ipAddress);
	}
	
	@WebMethod(operationName="infoKTPKelurahan")
	public List<BiodataKelurahanDummy> infoKTPKelurahan(
			@WebParam(name="kelurahanID") int kelurahanID){
		return new ServiceRail().listInfoKTPKelurahan(kelurahanID);
	}
	
	@WebMethod(operationName="infoBiodataKelurahan")
	public List<BiodataKelurahanDummy> infoBiodataKelurahan(
			@WebParam(name="kelurahanID") int kelurahanID){
		return new ServiceRail().listInfoBiodataKelurahan(kelurahanID);
	}
	
}
