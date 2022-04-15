package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.siak.data.Pendidikan;
import org.siak.face.PendidikanFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class PendidikanCore extends UnicastRemoteObject implements PendidikanFace {

	private static final long serialVersionUID = 1L;

	public PendidikanCore() throws Exception {
		
	}

	@Override
	public boolean save(Pendidikan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah pendidikan : "+ o.getNama(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah pendidikan : " + o.getNama(), e.toString(),"err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Pendidikan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Pendidikan pendidikan = (Pendidikan) session.load(Pendidikan.class, o.getPendidikanId());
			session.delete(pendidikan);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus pendidikan : " + o.getPendidikanId(), "","log" ,true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus pendidikan : " + o.getPendidikanId(), e.toString(),"err" ,true);
			return false;
		}
	}

	@Override
	public boolean update(Pendidikan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Pendidikan pendidikan = (Pendidikan) session.load(Pendidikan.class, o.getPendidikanId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah pendidikan : " + o.getPendidikanId(), "","log" ,true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah pendidikan : " + o.getPendidikanId(), e.toString(),"err" ,true);
			return false;
		}
	}

	@Override
	public List<Pendidikan> listData() throws Exception {
		List<Pendidikan> list = new ArrayList<Pendidikan>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Pendidikan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch data pendidikan", "","log" ,true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch data pendidikan", e.toString(),"err" ,true);
			return list;
		}
	}

}
