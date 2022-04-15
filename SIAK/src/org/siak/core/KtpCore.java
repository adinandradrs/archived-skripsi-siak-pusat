package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Ktp;
import org.siak.face.KtpFace;
import org.siak.util.Counter;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class KtpCore extends UnicastRemoteObject implements KtpFace {

	private static final long serialVersionUID = 1L;

	public KtpCore() throws Exception{
		
	}
	
	@Override
	public boolean save(Ktp o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Counter c = new Counter();
			o.setExpired(c.getExpiredKTP(o.getBiodata().getTanggalLahir()));
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah KTP : " + o.getNik(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah KTP : " + o.getNik(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Ktp o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Ktp ktp = (Ktp) session.load(Ktp.class, o.getNik());
			session.delete(ktp);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus KTP : " + o.getNik(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus KTP : " + o.getNik(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Ktp o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Ktp ktp = (Ktp) session.load(Ktp.class, o.getNik());
			if(o.getJenisBuat() == 4){
				int newYear = Integer.parseInt(o.getExpired().substring(0,4)) + 5;
				String newExpired = newYear + o.getExpired().substring(4, o.getExpired().length());
				o.setExpired(newExpired);
			}
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah KTP : " + o.getNik(), "","log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah KTP : " + o.getNik(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Ktp> listData() throws Exception {
		List<Ktp> list = new ArrayList<Ktp>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Ktp as k1 inner join fetch k1.biodata as b inner join fetch b.agama inner join fetch b.pekerjaan inner join fetch b.hubungandetail inner join fetch b.kelurahan as k2 inner join fetch k2.kecamatan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch data KTP", "", "log", true);
			return list;
		}
		catch(Exception e){
			session.close();
			Logger.createLog("Fetch data KTP", e.toString(), "err", true);
			return list;
		}
	}

}
