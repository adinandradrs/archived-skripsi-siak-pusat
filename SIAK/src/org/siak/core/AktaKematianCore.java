package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.siak.data.Aktakematian;
import org.siak.face.AktaKematianFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class AktaKematianCore extends UnicastRemoteObject implements AktaKematianFace {

	private static final long serialVersionUID = 1L;

	public AktaKematianCore() throws Exception{
		
	}

	@Override
	public boolean save(Aktakematian o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Akta Kematian : "+ o.getNoAktaKematian(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Akta Kematian : "+ o.getNoAktaKematian(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Aktakematian o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Aktakematian aktaKematian = (Aktakematian) session.load(Aktakematian.class, o.getNoAktaKematian());
			session.delete(aktaKematian);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Akta Kematian : " + o.getNoAktaKematian(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Akta Kematian : " + o.getNoAktaKematian(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Aktakematian o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Aktakematian aktaKematian = (Aktakematian) session.load(Aktakematian.class, o.getNoAktaKematian());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Akta Kematian : " + o.getNoAktaKematian(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Akta Kematian : " + o.getNoAktaKematian(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Aktakematian> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Aktakematian> list = new ArrayList<Aktakematian>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Aktakematian as k inner join fetch k.kartukeluarga inner join fetch k.biodata inner join fetch k.permohonan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Akta Kematian", "", "log", true);
			return list;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			Logger.createLog("Fetch daftar Akta Kematian", e.toString(), "err", true);
			return list;
		}
	}
	
	

}
