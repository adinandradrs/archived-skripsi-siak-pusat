package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Cacat;
import org.siak.face.CacatFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class CacatCore extends UnicastRemoteObject implements CacatFace {
	
	private static final long serialVersionUID = 1L;

	public CacatCore() throws Exception {
		
	}

	@Override
	public boolean save(Cacat o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Cacat : " + o.getNama(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Cacat : " + o.getNama(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Cacat o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Cacat cacat = (Cacat) session.load(Cacat.class, o.getCacatId());
			session.delete(cacat);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Cacat : " + o.getCacatId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Cacat : " + o.getCacatId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Cacat o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Cacat cacat = (Cacat) session.load(Cacat.class, o.getCacatId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Cacat : " + o.getCacatId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Cacat : " + o.getCacatId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Cacat> listData() throws Exception {
		List<Cacat> list = new ArrayList<Cacat>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Cacat");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Cacat", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch daftar Cacat", e.toString(), "err", true);
			return list;
		}
	}

}
