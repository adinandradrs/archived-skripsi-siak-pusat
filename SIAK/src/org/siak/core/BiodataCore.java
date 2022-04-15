package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.siak.data.Biodata;
import org.siak.data.Kecamatan;
import org.siak.data.Kelurahan;
import org.siak.face.BiodataFace;
import org.siak.util.Counter;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class BiodataCore extends UnicastRemoteObject implements BiodataFace {
	
	private static final long serialVersionUID = 1L;

	public BiodataCore() throws Exception{
			}
	
	//inner function 	
	private int getKodeDaerahKecamatan(int kelurahanID){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Kelurahan k = null;
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Kelurahan.class).
					add(Restrictions.eq("kelurahanId", kelurahanID));
			k = (Kelurahan)criteria.list().get(0);
			session.getTransaction().commit();
			session.close();
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
		}
		
		session = HibernateUtil.getSessionFactory().openSession();
		Kecamatan k2 = null;
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Kecamatan.class).
					add(Restrictions.eq("kecamatanId", k.getKecamatan().getKecamatanId()));
			k2 = (Kecamatan)criteria.list().get(0);
			session.getTransaction().commit();
			session.close();
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
		}
		return k2.getKodeDaerah();
	}
	
	private String getNik(Biodata o){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Biodata> list = new ArrayList<Biodata>();
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Biodata.class)
					.add(Restrictions.eq("kelurahan", o.getKelurahan()));
			list = criteria.list();
			session.getTransaction().commit();
			session.close();
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
		}
		Counter c = new Counter();
		String nik = c.generateNIK(o, list,getKodeDaerahKecamatan(o.getKelurahan().getKelurahanId()));
		return nik;
	}
	
	@Override
	public String save(Biodata o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			o.setNik(this.getNik(o));
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Biodata : " + o.getNik(), "", "log", true);
			return o.getNik();
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Biodata : " + o.getNik(), e.toString(), "err", true);
			return "Generate otomatis NIK gagal";
		}
	}

	@Override
	public boolean delete(Biodata o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Biodata biodata = (Biodata) session.load(Biodata.class, o.getNik());
			session.delete(biodata);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Biodata : " + o.getNik(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Biodata : " + o.getNik(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Biodata o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Biodata biodata = (Biodata) session.load(Biodata.class, o.getNik());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Biodata : " + o.getNik(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Biodata : " + o.getNik(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Biodata> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Biodata> list = new ArrayList<Biodata>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Biodata as b inner join fetch b.hubungandetail inner join fetch b.pekerjaan inner join fetch b.pendidikan inner join fetch b.kelurahan as k inner join fetch k.kecamatan inner join fetch b.agama"); 
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Biodata", "", "log", true);
			return list;
		}
		catch(Exception e){
			Logger.createLog("Fetch daftar Biodata", e.toString(), "err", true);
			session.close();
			return list;
		}
	}

}
