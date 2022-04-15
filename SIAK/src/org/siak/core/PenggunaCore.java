package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Pengguna;
import org.siak.face.PenggunaFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class PenggunaCore extends UnicastRemoteObject implements PenggunaFace {

	private static final long serialVersionUID = 1L;

	public PenggunaCore() throws Exception{
	
	}

	@Override
	public boolean save(Pengguna o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Pengguna : " + o.getUsername(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Pengguna : " + o.getUsername(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Pengguna o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Pengguna pengguna = (Pengguna) session.load(Pengguna.class, o.getUsername());
			session.delete(pengguna);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Pengguna : " + o.getUsername(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Pengguna : " + o.getUsername(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Pengguna o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Pengguna pengguna = (Pengguna) session.load(Pengguna.class, o.getUsername());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Pengguna : " + o.getUsername(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			e.printStackTrace();
			Logger.createLog("Ubah Pengguna : " + o.getUsername(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Pengguna> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Pengguna> list = new ArrayList<Pengguna>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Pengguna");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			return list;
		}
		catch(Exception e){
			session.close();
			return list;
		}
	}
	
}
