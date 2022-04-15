package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Hubungandetail;
import org.siak.face.HubunganDetailFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class HubunganDetailCore extends UnicastRemoteObject implements HubunganDetailFace {

	private static final long serialVersionUID = 1L;

	public HubunganDetailCore() throws Exception {
		
	}
	
	@Override
	public boolean save(Hubungandetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Hubungan Detail : " + o.getNama(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Hubungan Detail : " + o.getNama(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Hubungandetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Hubungandetail hubunganDetail = (Hubungandetail) session.load(Hubungandetail.class, o.getHubunganDetailId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Hubungan Detail : " + o.getHubunganDetailId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Hubungan Detail : " + o.getHubunganDetailId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Hubungandetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Hubungandetail hubunganDetail = (Hubungandetail) session.load(Hubungandetail.class, o.getHubunganDetailId());
			session.delete(hubunganDetail);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Hubungan Detail : " + o.getHubunganDetailId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Hubungan Detail : " + o.getHubunganDetailId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Hubungandetail> listData() throws Exception {
		List<Hubungandetail> list = new ArrayList<Hubungandetail>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Hubungandetail");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Hubungan Detail", "", "log", true);
			return list;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			Logger.createLog("Fetch daftar Hubungan Detail", e.toString(), "err", true);
			return list;
		}
	}

}
