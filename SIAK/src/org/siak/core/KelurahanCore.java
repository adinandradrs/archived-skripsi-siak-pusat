package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Kelurahan;
import org.siak.face.KelurahanFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class KelurahanCore extends UnicastRemoteObject implements KelurahanFace {

	private static final long serialVersionUID = 1L;

	public KelurahanCore() throws Exception{
		
	}
	
	@Override
	public boolean save(Kelurahan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Kelurahan : " + o.getNama(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Kelurahan : " + o.getNama(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Kelurahan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Kelurahan kelurahan = (Kelurahan) session.load(Kelurahan.class, o.getKelurahanId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Kelurahan : " + o.getKelurahanId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Kelurahan : " + o.getKelurahanId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Kelurahan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Kelurahan kelurahan = (Kelurahan) session.load(Kelurahan.class, o.getKelurahanId());
			session.delete(kelurahan);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Kelurahan : " + o.getKelurahanId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Kelurahan : " + o.getKelurahanId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Kelurahan> listData() throws Exception {
		List<Kelurahan> list = new ArrayList<Kelurahan>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Kelurahan as k inner join fetch k.kecamatan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch data Kelurahan", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch data Kelurahan", e.toString(), "err", true);
			return list;
		}
	}
	
}
