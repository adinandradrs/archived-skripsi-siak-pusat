package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.siak.data.Agama;
import org.siak.face.AgamaFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class AgamaCore extends UnicastRemoteObject implements AgamaFace {
	
	private static final long serialVersionUID = 1L;

	public AgamaCore() throws Exception{
		
	}

	@Override
	public boolean save(Agama o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah agama : " + o.getNama(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah agama : " + o.getNama(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Agama o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Agama agama = (Agama) session.load(Agama.class, o.getAgamaId());
			session.delete(agama);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus agama : " + o.getAgamaId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus agama : " + o.getAgamaId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Agama o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Agama agama = (Agama) session.load(Agama.class, o.getAgamaId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah agama : " + o.getAgamaId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah agama : " + o.getAgamaId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Agama> listData() throws Exception {
		List<Agama> list = new ArrayList<Agama>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Agama");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar agama", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch daftar agama : ", e.toString(), "err", true);
			return list;
		}
	}
	
}
