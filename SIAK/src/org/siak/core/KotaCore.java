package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Kota;
import org.siak.face.KotaFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class KotaCore extends UnicastRemoteObject implements KotaFace {

	private static final long serialVersionUID = 1L;

	public KotaCore() throws Exception{
		
	}
	
	@Override
	public boolean save(Kota k) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(k);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Kota : " + k.getNama(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Kota : " + k.getNama(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Kota k) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Kota kota = (Kota) session.load(Kota.class, k.getKotaId());
			session.update(k);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Kota : " + k.getKotaId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Kota : " + k.getKotaId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Kota k) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Kota kota = (Kota) session.load(Kota.class, k.getKotaId());
			session.delete(kota);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Kota : " + k.getKotaId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Kota : " + k.getKotaId(), "", "err", true);
			return false;
		}
	}

	@Override
	public List<Kota> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Kota> list = new ArrayList<Kota>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Kota");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch data kota", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch data kota", e.toString(), "err", true);
			return list;
		}
	}
	
	

}
