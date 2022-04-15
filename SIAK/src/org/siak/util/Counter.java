package org.siak.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.siak.data.Biodata;
import org.siak.data.Kartukeluarga;

public class Counter {

	public String generateNoKK(List<Kartukeluarga> listKartuKeluarga, int kodeDaerahKecamatan){
		List<String> list = new ArrayList<String>();
		for (Kartukeluarga kartukeluarga: listKartuKeluarga)
			if(kartukeluarga.getBiodata().getKelurahan().getKecamatan().getKodeDaerah() == kodeDaerahKecamatan)
				list.add(kartukeluarga.getNoKk());
		
		String propinsi = Configuration.file("app.ini").get("Kode", "propinsi");
		String kota = Configuration.file("app.ini").get("Kode", "kota");
		int kecamatan = kodeDaerahKecamatan;
		String noKK = propinsi+kota+kecamatan;
		SimpleDateFormat format = new SimpleDateFormat("ddMMyy");
		String genDate = format.format(new Date());
		List<String> list2= new ArrayList<String>();
		for (String string : list) {
			if(string.substring(0,6).equalsIgnoreCase(noKK))
				list2.add(string);
		}
		list.clear();
		Comparator c =  Collections.reverseOrder();
		Collections.sort(list2,c);
		
		int size = list2.size();
		int counter = 0;
		String counterString = "";
		if(size == 0)
			counter = 1;
		else{
			String noKKLast = list2.get(0);
			int noKKIntegerLast = Integer.parseInt(noKKLast.substring(noKKLast.length()-4, noKKLast.length()));
			counter = noKKIntegerLast + (++counter);
		}
		counterString = "0000"+counter;
		String string = counterString.substring(counterString.length()-4, counterString.length());
		noKK += genDate + string;
		return noKK;
	}
	
	public String generateNIK(Biodata b, List<Biodata> listBiodata, int kodeDaerahKecamatan) {
		String propinsi = Configuration.file("app.ini").get("Kode", "propinsi");
		String kota = Configuration.file("app.ini").get("Kode", "kota");
		int kecamatan = kodeDaerahKecamatan;
		String nik = propinsi+kota+kecamatan;
		SimpleDateFormat format = new SimpleDateFormat("ddMMyy");
		String tanggalLahir = format.format(b.getTanggalLahir());
		int birthDate = 0;
		if(b.getJenisKelamin().equalsIgnoreCase("Perempuan")){
			birthDate = Integer.parseInt(tanggalLahir.substring(0, 2)) + 40;
			tanggalLahir = birthDate + tanggalLahir.substring(2);
		}
		
		List<String> list = new ArrayList<String>();
		for (Biodata o : listBiodata)
			list.add(o.getNik());
		
		List<String> list2 = new ArrayList<String>();
		if(b.getJenisKelamin().equalsIgnoreCase("Perempuan")){
			for (String string : list) {
				int criteria = Integer.parseInt(string.substring(6,8));
				if(criteria <= 31 )
					list2.add(string);	
			}
		}
		
		list.removeAll(list2);
		list2.clear();
		
		Comparator c =  Collections.reverseOrder();
		Collections.sort(list,c);
		
		int size = list.size();
		int counter = 0;
		String counterString = "";
		if(size == 0)
			counter = 1;
		else{
			String nikLast = list.get(0);
			int nikIntegerLast = Integer.parseInt(nikLast.substring(nikLast.length()-4, nikLast.length()));
			counter = nikIntegerLast + (++counter);
		}
		counterString = "0000"+counter;
		String string = counterString.substring(counterString.length()-4, counterString.length());
		nik += tanggalLahir+string;
		list.clear();
		
		return nik;
	}

	public String getExpiredKTP(Date tanggalLahir){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int lastYear = year + 5;
		String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(tanggalLahir);
		String expired = lastYear + "-" + tanggal.substring(tanggal.length()-4,tanggal.length());
		return expired;
	}
}
