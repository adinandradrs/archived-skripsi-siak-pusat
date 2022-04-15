package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Gruppengguna;
import org.siak.face.GrupPenggunaFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class GrupPenggunaCore extends UnicastRemoteObject implements GrupPenggunaFace {

	private static final long serialVersionUID = 1L;

	public GrupPenggunaCore() throws Exception{
		
	}
	
	@Override
	public boolean save(Gruppengguna o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Grup Pengguna : " + o.getPengguna().getUsername(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Grup Pengguna : " + o.getPengguna().getUsername(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Gruppengguna o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Gruppengguna grupPengguna = (Gruppengguna) session.load(Gruppengguna.class, o.getId());
			session.delete(grupPengguna);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Grup Pengguna : " + o.getId().getGrupPenggunaId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Grup Pengguna : " + o.getId().getGrupPenggunaId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Gruppengguna o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Gruppengguna grupPengguna = (Gruppengguna) session.load(Gruppengguna.class, o.getId().getGrupPenggunaId());
			session.delete(grupPengguna);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Grup Pengguna : " + o.getId().getGrupPenggunaId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Grup Pengguna : " + o.getId().getGrupPenggunaId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Gruppengguna> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Gruppengguna> list = new ArrayList<Gruppengguna>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Gruppengguna");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Grup Pengguna", "", "log", true);
			return list;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			Logger.createLog("Fetch daftar Grup Pengguna", e.toString(), "err", true);
			return list;
		}
	}

}
