package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Hubungan;
import org.siak.face.HubunganFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class HubunganCore extends UnicastRemoteObject implements HubunganFace {

	private static final long serialVersionUID = 1L;

	public HubunganCore() throws Exception{
		
	}
	
	@Override
	public boolean save(Hubungan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Hubungan : " + o.getNama(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Hubungan : " + o.getNama(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Hubungan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Hubungan hubungan = (Hubungan) session.load(Hubungan.class, o.getHubunganId());
			session.delete(hubungan);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Hubungan : " + o.getHubunganId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Hubungan : " + o.getHubunganId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Hubungan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Hubungan hubungan = (Hubungan) session.load(Hubungan.class, o.getHubunganId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Hubungan : " + o.getHubunganId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Hubungan : " + o.getHubunganId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Hubungan> listData() throws Exception {
		List<Hubungan> list = new ArrayList<Hubungan>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Hubungan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Hubungan", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch daftar Hubungan", e.toString(), "err", true);
			return list;
		}
	}

}
