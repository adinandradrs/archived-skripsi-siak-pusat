package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Kartukeluargadetail;
import org.siak.face.KartuKeluargaDetailFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class KartuKeluargaDetailCore extends UnicastRemoteObject implements KartuKeluargaDetailFace {
	
	private static final long serialVersionUID = 1L;

	public KartuKeluargaDetailCore() throws Exception{
		
	}

	@Override
	public boolean save(Kartukeluargadetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Kartu Keluarga Detail : " + o.getKartukeluarga().getNoKk(), "", "log", true);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Kartu Keluarga Detail : " + o.getKartukeluarga().getNoKk(), "", "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Kartukeluargadetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Kartukeluargadetail kartuKeluargaDetail = (Kartukeluargadetail) session.load(Kartukeluargadetail.class, o.getKartuKeluargaDetailId());
			session.delete(kartuKeluargaDetail);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Kartu Keluarga Detail : " + o.getKartuKeluargaDetailId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Kartu Keluarga Detail : " + o.getKartuKeluargaDetailId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Kartukeluargadetail o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Kartukeluargadetail kartuKeluargaDetail = (Kartukeluargadetail) session.load(Kartukeluargadetail.class, o.getKartuKeluargaDetailId());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Kartu Keluarga Detail : " + o.getKartuKeluargaDetailId(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Kartu Keluarga Detail : " + o.getKartuKeluargaDetailId(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Kartukeluargadetail> listData() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Kartukeluargadetail> list = new ArrayList<Kartukeluargadetail>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Kartukeluargadetail as k inner join fetch k.pekerjaan inner join fetch k.hubungandetailByStatusKawin inner join fetch k.pendidikan inner join fetch k.hubungandetailByStatusHubungan inner join fetch k.agama");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch data Kartu Keluarga Detail", "", "log", true);
			return list;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			Logger.createLog("Fetch data Kartu Keluarga Detail", e.toString(), "err", true);
			return list;
		}
	}

}
