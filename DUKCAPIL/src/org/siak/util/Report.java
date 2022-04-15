package org.siak.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.siak.data.Aktakelahiran;
import org.siak.data.Aktakematian;
import org.siak.data.Aktanikahdetail;
import org.siak.dummy.AktaKelahiranDummy;
import org.siak.dummy.AktaKematianDummy;
import org.siak.dummy.AktaNikahDummy;

public class Report {
	
	public static void createAktaKelahiran(List<Aktakelahiran> list, String noAktaKelahiran,String nikAyah, String nikIbu, String path, String docPath, SimpleDateFormat formatter){
		try{
			AktaKelahiranDummy akd = new AktaKelahiranDummy();
			List<AktaKelahiranDummy> list2 = new ArrayList<AktaKelahiranDummy>();
			int i = 1;
			for (Aktakelahiran o : list)
				if(o.getBiodataByNikayah().getNik().equalsIgnoreCase(nikAyah))
					i++;
			for(Aktakelahiran o : list){
				if(o.getNoAktaKelahiran().equalsIgnoreCase(noAktaKelahiran)){
					akd.setNama(o.getNama());
					akd.setNoAktaKelahiran(noAktaKelahiran);
					akd.setTanggalLahir(formatter.format(o.getTanggalLahir()));
					akd.setTanggalBuat(formatter.format(o.getTanggalBuat()));
					akd.setTanggalCetak(formatter.format(new Date()));
					akd.setAnakKe(i+"");
					list2.add(akd);
				}
			}
			JasperReport report;
			JasperDesign design;
			JRDataSource datasource = new JRBeanCollectionDataSource(list2);
			design = JRXmlLoader.load(path);
	        report = JasperCompileManager.compileReport(design);
	        JasperPrint print = JasperFillManager.fillReport(report, new HashMap(),datasource);
	        JasperExportManager.exportReportToPdfFile(print,docPath+akd.getNoAktaKelahiran()+".pdf");
		}
		catch(Exception e){
			
		}
	}
	
	public static void createAktaNikah(List<Aktanikahdetail> list, String noAktaNikah, String path, String docPath, SimpleDateFormat formatter){
		try{
			AktaNikahDummy and = new AktaNikahDummy();
			List<AktaNikahDummy> list2 = new ArrayList<AktaNikahDummy>();
			for (Aktanikahdetail o : list) {
				if(o.getAktanikah().getNoAktaNikah().equals(noAktaNikah)){
					and.setNamaIstri(o.getBiodataByNikistri().getNama());
					and.setNamaSuami(o.getBiodataByNiksuami().getNama());
					and.setNoAktaNikah(o.getAktanikah().getNoAktaNikah());
					and.setTanggalBuat(formatter.format(o.getAktanikah().getTanggalBuat()));
					and.setTanggalCetak(formatter.format(new Date()));
					list2.add(and);
					break;
				}
			}
			JasperReport report;
			JasperDesign design;
			JRDataSource datasource = new JRBeanCollectionDataSource(list2);
			design = JRXmlLoader.load(path);
	        report = JasperCompileManager.compileReport(design);
	        JasperPrint print = JasperFillManager.fillReport(report, new HashMap(),datasource);
	        JasperExportManager.exportReportToPdfFile(print,docPath+and.getNoAktaNikah()+".pdf");
		}
		catch(Exception e){
			
		}
	}
	
	public static void createAktaKematian(List<Aktakematian> list, String noAktaKematian, String path, String docPath, SimpleDateFormat formatter){
		try{
			AktaKematianDummy akd = new AktaKematianDummy();
			List<AktaKematianDummy> list2 = new ArrayList<AktaKematianDummy>();
			for (Aktakematian o : list) {
				if(o.getNoAktaKematian().equals(noAktaKematian)){
					akd.setNoAktaKematian(noAktaKematian);
					akd.setNama(o.getBiodata().getNama());
					akd.setTanggalLahir(formatter.format(o.getBiodata().getTanggalLahir()));
					akd.setTanggalMeninggal(formatter.format(o.getTanggalMeninggal()));
					akd.setTempatLahir(o.getBiodata().getTempatLahir());
					akd.setTanggalCetak(formatter.format(new Date()));
					list2.add(akd);
					break;
				}
			}
			JasperReport report;
			JasperDesign design;
			JRDataSource datasource = new JRBeanCollectionDataSource(list2);
			design = JRXmlLoader.load(path);
	        report = JasperCompileManager.compileReport(design);
	        JasperPrint print = JasperFillManager.fillReport(report, new HashMap(),datasource);
	        JasperExportManager.exportReportToPdfFile(print,docPath+akd.getNoAktaKematian()+".pdf");
		}
		catch(Exception e){
			
		}
	}

}
