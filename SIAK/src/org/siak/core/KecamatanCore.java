package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Kecamatan;
import org.siak.face.KecamatanFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class KecamatanCore extends UnicastRemoteObject implements KecamatanFace {

	private static final long serialVersionUID = 1L;

	public KecamatanCore() throws Exception {
		
	}
	
	@Override
	public boolean save(Kecamatan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Kecamatan : " + o.getNama(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Kecamatan : " + o.getNama(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Kecamatan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Kecamatan kecamatan = (Kecamatan) session.load(Kecamatan.class, o.getKecamatanId());
			session.delete(kecamatan);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Kecamatan : " + o.getKecamatanId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Kecamatan : " + o.getKecamatanId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Kecamatan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Kecamatan kecamatan = (Kecamatan) session.load(Kecamatan.class, o.getKecamatanId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Kecamatan : " + o.getKecamatanId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Kecamatan : " + o.getKecamatanId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Kecamatan> listData() throws Exception {
		List<Kecamatan> list = new ArrayList<Kecamatan>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Kecamatan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch data Kecamatan", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch data Kecamatan", e.toString(), "err", true);
			return list;
		}
	}
	
	

}
