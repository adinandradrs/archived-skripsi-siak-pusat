package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Pekerjaan;
import org.siak.face.PekerjaanFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class PekerjaanCore extends UnicastRemoteObject implements PekerjaanFace {

	private static final long serialVersionUID = 1L;
	
	public PekerjaanCore() throws Exception{
		
	}

	@Override
	public boolean save(Pekerjaan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Pekerjaan : "+ o.getNama(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Pekerjaan : "+ o.getNama(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Pekerjaan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Pekerjaan pekerjaan = (Pekerjaan) session.load(Pekerjaan.class, o.getPekerjaanId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Pekerjaan : "+ o.getPekerjaanId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Pekerjaan : "+ o.getPekerjaanId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Pekerjaan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Pekerjaan pekerjaan = (Pekerjaan) session.load(Pekerjaan.class, o.getPekerjaanId());
			session.delete(pekerjaan);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Pekerjaan : "+ o.getPekerjaanId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Pekerjaan : "+ o.getPekerjaanId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Pekerjaan> listData() throws Exception {
		List<Pekerjaan> list = new ArrayList<Pekerjaan>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Pekerjaan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch data pekerjaaan", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch data pekerjaan", e.toString(), "err", true);
			return list;
		}
	}
	
	

}
