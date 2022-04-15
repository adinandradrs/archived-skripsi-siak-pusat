package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Permohonan;
import org.siak.face.PermohonanFace;
import org.siak.util.HibernateUtil;

public class PermohonanCore extends UnicastRemoteObject implements PermohonanFace {

	private static final long serialVersionUID = 1L;

	public PermohonanCore() throws Exception {
		
	}

	@Override
	public boolean save(Permohonan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			return false;
		}
	}

	@Override
	public boolean delete(Permohonan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Permohonan permohonan = (Permohonan) session.load(Permohonan.class, o.getPermohonanId());
			session.delete(permohonan);
			session.getTransaction().commit();
			session.close();
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			return false;
		}
	}

	@Override
	public boolean update(Permohonan o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Permohonan permohonan = (Permohonan) session.load(Permohonan.class, o.getPermohonanId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			return false;
		}
	}

	@Override
	public List<Permohonan> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Permohonan> list = new ArrayList<Permohonan>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Permohonan");
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
