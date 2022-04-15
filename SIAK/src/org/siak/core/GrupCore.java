package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Grup;
import org.siak.face.GrupFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class GrupCore extends UnicastRemoteObject implements GrupFace {
	
	private static final long serialVersionUID = 1L;

	public GrupCore() throws Exception {
		
	}

	@Override
	public boolean save(Grup o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Grup : " + o.getNama(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Grup : " + o.getNama(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Grup o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Grup grup = (Grup) session.load(Grup.class, o.getGrupId());
			session.delete(grup);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Grup : " + o.getGrupId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Grup : " + o.getGrupId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Grup o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Grup grup = (Grup) session.load(Grup.class, o.getGrupId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Grup : " + o.getGrupId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Grup : " + o.getGrupId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Grup> listData() throws Exception {
		List<Grup> list = new ArrayList<Grup>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Grup");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Grup", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch daftar Grup", e.toString(), "err", true);
			return list;
		}
	}

}
