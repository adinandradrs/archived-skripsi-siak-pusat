package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Biodatacacat;
import org.siak.face.BiodataCacatFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class BiodataCacatCore extends UnicastRemoteObject implements BiodataCacatFace {

	private static final long serialVersionUID = 1L;

	public BiodataCacatCore() throws Exception{
		
	}
	
	@Override
	public boolean save(Biodatacacat o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Biodata Cacat : " + o.getBiodata().getNik(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Biodata Cacat : " + o.getBiodata().getNik(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Biodatacacat o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Biodatacacat biodataCacat = (Biodatacacat) session.load(Biodatacacat.class, o.getBiodataCacatId());
			session.delete(biodataCacat);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Biodata Cacat : " + o.getBiodataCacatId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Biodata Cacat : " + o.getBiodataCacatId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Biodatacacat o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Biodatacacat biodataCacat = (Biodatacacat) session.load(Biodatacacat.class, o.getBiodataCacatId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Biodata Cacat : " + o.getBiodataCacatId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Biodata Cacat : " + o.getBiodataCacatId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Biodatacacat> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Biodatacacat> list = new ArrayList<Biodatacacat>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Biodatacacat as bc inner join fetch bc.cacat inner join fetch bc.biodata as b inner join fetch b.kelurahan as k inner join fetch k.kecamatan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Biodata Cacat", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch daftar Biodata Cacat", e.toString(), "err", true);
			return list;	
		}
		
	}
	
	

}
