package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.siak.data.Kartukeluarga;
import org.siak.data.Kecamatan;
import org.siak.data.Kelurahan;
import org.siak.face.KartuKeluargaFace;
import org.siak.util.Counter;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class KartuKeluargaCore extends UnicastRemoteObject implements
		KartuKeluargaFace {

	private static final long serialVersionUID = 1L;

	public KartuKeluargaCore() throws Exception {

	}

	// inner function
	private int getKodeDaerahKecamatan(int kelurahanID) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Kelurahan k = null;
		try {
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Kelurahan.class).add(
					Restrictions.eq("kelurahanId", kelurahanID));
			k = (Kelurahan) criteria.list().get(0);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
		}

		session = HibernateUtil.getSessionFactory().openSession();
		Kecamatan k2 = null;
		try {
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Kecamatan.class).add(
					Restrictions.eq("kecamatanId", k.getKecamatan()
							.getKecamatanId()));
			k2 = (Kecamatan) criteria.list().get(0);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
		}
		return k2.getKodeDaerah();
	}
	
	private String getNoKK(Kartukeluarga o){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Kartukeluarga> list = new ArrayList<Kartukeluarga>();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Kartukeluarga as kk inner join fetch kk.biodata as b inner join fetch b.kelurahan as k inner join fetch k.kecamatan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
		}
		int kodeKecamatan = getKodeDaerahKecamatan(o.getBiodata().getKelurahan().getKelurahanId());
		Counter c = new Counter();
		String noKK = c.generateNoKK(list, kodeKecamatan);
		return noKK;
	}

	@Override
	public String save(Kartukeluarga o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			o.setNoKk(this.getNoKK(o));
			session.save(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Kartu Keluraga : " + o.getNoKk(), "", "log", true);
			return o.getNoKk();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Kartu Keluraga : " + o.getNoKk(), e.toString(), "err", true);
			return "Generate otomatis No KK gagal";
		}
		
	}

	@Override
	public boolean update(Kartukeluarga o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Kartukeluarga kartuKeluarga = (Kartukeluarga) session.load(
					Kartukeluarga.class, o.getNoKk());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Kartu Keluraga : " + o.getNoKk(), "", "log", true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Kartu Keluraga : " + o.getNoKk(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Kartukeluarga o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Kartukeluarga kartuKeluarga = (Kartukeluarga) session.load(
					Kartukeluarga.class, o.getNoKk());
			session.delete(kartuKeluarga);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Kartu Keluraga : " + o.getNoKk(), "", "log", true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Kartu Keluraga : " + o.getNoKk(), "", "err", true);
			return false;
		}
	}

	@Override
	public List<Kartukeluarga> listData() throws Exception {
		List<Kartukeluarga> list = new ArrayList<Kartukeluarga>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Kartukeluarga as kk inner join fetch kk.biodata as b inner join fetch b.kelurahan as k inner join fetch k.kecamatan");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Kartu Keluarga", "", "log", true);
			return list;
		} catch (Exception e) {
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Fetch daftar Kartu Keluarga", e.toString(), "err", true);
			return list;
		}
	}

}
