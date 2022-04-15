package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Permohonandetail;
import org.siak.face.PermohonanDetailFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class PermohonanDetailCore extends UnicastRemoteObject implements PermohonanDetailFace {

	private static final long serialVersionUID = 1L;

	public PermohonanDetailCore() throws Exception{
		
	}
	
	@Override
	public boolean save(Permohonandetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Save Permohonan : " + o.getBiodata().getNik(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Save Permohonan : " + o.getBiodata().getNik(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Permohonandetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Permohonandetail permohonanDetail = (Permohonandetail) session.load(Permohonandetail.class, o.getPermohonanDetailId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			return true;
		}
		catch(Exception e){
			//e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			return false;
		}
	}

	@Override
	public boolean delete(Permohonandetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Permohonandetail permohonanDetail = (Permohonandetail) session.load(Permohonandetail.class, o.getPermohonanDetailId());
			session.delete(permohonanDetail);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Permohonan : " + o.getPermohonanDetailId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Permohonan : " + o.getPermohonanDetailId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Permohonandetail> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Permohonandetail> list = new ArrayList<Permohonandetail>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Permohonandetail as p inner join fetch p.biodata as b inner join fetch b.kelurahan as k inner join fetch k.kecamatan inner join fetch p.permohonan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar permohonan", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch daftar permohonan", e.toString(), "err", true);
			return list;
		}
	}

}
