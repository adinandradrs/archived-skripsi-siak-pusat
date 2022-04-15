package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Aktanikah;
import org.siak.face.AktaNikahFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class AktaNikahCore extends UnicastRemoteObject implements AktaNikahFace {

	private static final long serialVersionUID = 1L;

	public AktaNikahCore() throws Exception{
		
	}

	@Override
	public boolean save(Aktanikah o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Akta Nikah : " + o.getNoAktaNikah(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Akta Nikah : " + o.getNoAktaNikah(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Aktanikah o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Aktanikah aktaNikah = (Aktanikah) session.load(Aktanikah.class, o.getNoAktaNikah());
			session.delete(aktaNikah);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Akta Nikah : " + o.getNoAktaNikah(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Akta Nikah : " + o.getNoAktaNikah(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Aktanikah o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Aktanikah aktaNikah = (Aktanikah) session.load(Aktanikah.class, o.getNoAktaNikah());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Akta Nikah : " + o.getNoAktaNikah(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Akta Nikah : " + o.getNoAktaNikah(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Aktanikah> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Aktanikah> list = new ArrayList<Aktanikah>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Aktanikah as k inner join fetch k.permohonan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Akta Nikah", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch daftar Akta Nikah", e.toString(), "err", true);
			return list;
		}
	}

}
