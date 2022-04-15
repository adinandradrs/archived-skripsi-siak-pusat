package org.siak.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.siak.data.Biodata;
import org.siak.data.Kartukeluarga;
import org.siak.data.Kartukeluargadetail;
import org.siak.data.Ktp;
import org.siak.dummy.KartuKeluargaAtasDummy;
import org.siak.dummy.KartuKeluargaBawahDummy;
import org.siak.dummy.KartuKeluargaMasterDummy;
import org.siak.dummy.KtpDummy;

public class Report {
	
	public static void createKartuKeluarga(List<Kartukeluargadetail> list, List<Biodata> lBiodata, List<Kartukeluarga> lKK, String noKK, String path, String docPath, SimpleDateFormat formatter){
		try{
			KartuKeluargaMasterDummy kkmd = new KartuKeluargaMasterDummy();
			List<KartuKeluargaAtasDummy> lAtas = new ArrayList<KartuKeluargaAtasDummy>();
			List<KartuKeluargaBawahDummy> lBawah = new ArrayList<KartuKeluargaBawahDummy>();
			kkmd.setNoKK(noKK);
			for (Kartukeluargadetail kk2 : list) {
				if(kk2.getKartukeluarga().getNoKk().equals(noKK)){
					KartuKeluargaAtasDummy kkad = new KartuKeluargaAtasDummy();
					KartuKeluargaBawahDummy kkbd = new KartuKeluargaBawahDummy();
					kkad.setAgama(kk2.getAgama().getNama());
					kkad.setJenisKelamin(kk2.getJenisKelamin());
					final String niks  = kk2.getNiks();
					Collection<Biodata> smallList = CollectionUtils.select(lBiodata, new Predicate() {
						@Override
						public boolean evaluate(Object o) {
							Biodata b = (Biodata) o;
							return b.getNik().equals(niks);
						}
					});
					kkad.setNamaLengkap(smallList.iterator().next().getNama());
					kkad.setNik(kk2.getNiks());
					kkad.setJenisPekerjaan(kk2.getPekerjaan().getNama());
					kkad.setPendidikan(kk2.getPendidikan().getNama());
					kkad.setTanggalLahir(formatter.format(kk2.getTanggalLahir()));
					kkad.setTempatLahir(kk2.getTempatLahir());
					
					kkbd.setStatusKawin(kk2.getHubungandetailByStatusKawin().getNama());
					kkbd.setStatusHubungan(kk2.getHubungandetailByStatusHubungan().getNama());
					kkbd.setKewarganegaraan("INDONESIA");
					kkbd.setNoPaspor("-");
					kkbd.setNoKitas("-");
					kkbd.setNamaAyah(kk2.getNamaAyah());
					kkbd.setNamaIbu(kk2.getNamaIbu());
					
					lAtas.add(kkad);
					lBawah.add(kkbd);
				}
			}
			
			kkmd.setAtas(lAtas);
			kkmd.setBawah(lBawah);
			
			for (Kartukeluarga kk : lKK) {
				if(kk.getNoKk().equals(noKK)){
					kkmd.setAlamat(kk.getBiodata().getAlamat());
					kkmd.setKecamatan(kk.getBiodata().getKelurahan().getKecamatan().getNama());
					kkmd.setKelurahan(kk.getBiodata().getKelurahan().getNama());
					kkmd.setNama(kk.getBiodata().getNama());
					kkmd.setRtrw(kk.getBiodata().getRt()+"/"+kk.getBiodata().getRw());
				}
			}
			
			JasperReport report;
			JasperDesign design;
			JRDataSource datasource = new JRBeanCollectionDataSource(Arrays.asList(kkmd));
			design = JRXmlLoader.load(path);
	        report = JasperCompileManager.compileReport(design);
	        JasperPrint print = JasperFillManager.fillReport(report, new HashMap(),datasource);
	        JasperExportManager.exportReportToPdfFile(print,docPath+noKK+".pdf");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void createKTP(List<Ktp> list, Ktp ktp, String path, String docPath, SimpleDateFormat formatter){
		try{
			List<KtpDummy> listDummy = new ArrayList<KtpDummy>();
			for (Ktp ktp2 : list) {
				if(ktp2.getNik().equalsIgnoreCase(ktp.getNik())){
					KtpDummy kd = new KtpDummy();
					kd.setNik(ktp2.getNik());
					kd.setNama(ktp2.getBiodata().getNama());
					kd.setTempatLahir(ktp2.getBiodata().getTempatLahir());
					kd.setTanggalLahir(formatter.format(ktp2.getBiodata().getTanggalLahir()));
					kd.setJenisKelamin(ktp2.getBiodata().getJenisKelamin());
					kd.setAlamat(ktp2.getBiodata().getAlamat());
					kd.setRt(ktp2.getBiodata().getRt());
					kd.setRw(ktp2.getBiodata().getRw());
					kd.setKelurahan(ktp2.getBiodata().getKelurahan().getNama());
					kd.setKecamatan(ktp2.getBiodata().getKelurahan().getKecamatan().getNama());
					kd.setAgama(ktp2.getBiodata().getAgama().getNama());
					kd.setPekerjaan(ktp2.getBiodata().getPekerjaan().getNama());
					kd.setExpired(ktp2.getExpired());
					kd.setFoto(new ImageIcon((byte[]) ktp2.getBiodata().getFoto()).getImage());
					kd.setStatus(ktp2.getBiodata().getHubungandetail().getNama());
					listDummy.add(kd);
				}
			}
			JasperReport report;
			JasperDesign design;
			JRDataSource datasource = new JRBeanCollectionDataSource(listDummy);
			design = JRXmlLoader.load(path);
	        report = JasperCompileManager.compileReport(design);
	        JasperPrint print = JasperFillManager.fillReport(report, new HashMap(),datasource);
	        JasperExportManager.exportReportToPdfFile(print,docPath+ktp.getNik()+"-KTP-"+ktp.getJenisBuat()+".pdf");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
