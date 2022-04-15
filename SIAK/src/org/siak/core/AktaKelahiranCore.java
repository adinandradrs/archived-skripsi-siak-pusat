package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Aktakelahiran;
import org.siak.face.AktaKelahiranFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class AktaKelahiranCore extends UnicastRemoteObject implements AktaKelahiranFace {

	private static final long serialVersionUID = 1L;

	public AktaKelahiranCore() throws Exception{
		
	}

	@Override
	public boolean save(Aktakelahiran o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Akta kelahiran : " + o.getNoAktaKelahiran(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Akta kelahiran : " + o.getNoAktaKelahiran(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Aktakelahiran o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Aktakelahiran aktaKelahiran = (Aktakelahiran) session.load(Aktakelahiran.class, o.getNoAktaKelahiran());
			session.delete(aktaKelahiran);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Akta Kelahiran : " + o.getNoAktaKelahiran(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Akta Kelahiran : " + o.getNoAktaKelahiran(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Aktakelahiran o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Aktakelahiran aktaKelahiran = (Aktakelahiran) session.load(Aktakelahiran.class, o.getNoAktaKelahiran());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Akta Kelahiran : " + o.getNoAktaKelahiran(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Akta Kelahiran : " + o.getNoAktaKelahiran(), "", "err", true);
			return false;
		}
	}

	@Override
	public List<Aktakelahiran> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Aktakelahiran> list = new ArrayList<Aktakelahiran>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Aktakelahiran as k inner join fetch k.biodataByNikayah inner join fetch k.biodataByNikibu inner join fetch k.permohonan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Akta Kelahiran", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch daftar Akta Kelahiran", e.toString(), "err", true);
			return list;
		}
	}

}
