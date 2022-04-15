package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Aktanikahdetail;
import org.siak.face.AktaNikahDetailFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class AktaNikahDetailCore extends UnicastRemoteObject implements AktaNikahDetailFace {

	private static final long serialVersionUID = 1L;

	public AktaNikahDetailCore() throws Exception {
		
	}

	@Override
	public boolean save(Aktanikahdetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Akta Nikah Detail : " + o.getAktanikah().getNoAktaNikah(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Akta Nikah Detail : " + o.getAktanikah().getNoAktaNikah(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Aktanikahdetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Aktanikahdetail aktaNikahDetail = (Aktanikahdetail) session.load(Aktanikahdetail.class, o.getAktaNikahDetailId());
			session.delete(aktaNikahDetail);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Akta Nikah Detail : " + o.getAktaNikahDetailId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Akta Nikah Detail : " + o.getAktaNikahDetailId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Aktanikahdetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Aktanikahdetail aktaNikahDetail = (Aktanikahdetail) session.load(Aktanikahdetail.class, o.getAktaNikahDetailId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Akta Nikah Detail : " + o.getAktaNikahDetailId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Akta Nikah Detail : " + o.getAktaNikahDetailId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Aktanikahdetail> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Aktanikahdetail> list = new ArrayList<Aktanikahdetail>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Aktanikahdetail as k inner join fetch k.biodataByNikistri inner join fetch k.biodataByNiksuami inner join fetch k.aktanikah");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Akta Nikah Detail", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch daftar Akta Nikah Detail", e.toString(), "err", true);
			return list;
		}
	}

}
