package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.siak.data.Biodatapindah;
import org.siak.face.BiodataPindahFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class BiodataPindahCore extends UnicastRemoteObject implements BiodataPindahFace {

	private static final long serialVersionUID = 1L;

	public BiodataPindahCore() throws Exception{
		
	}
	
	@Override
	public boolean save(Biodatapindah o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			session.close();
			
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Query query = session.createQuery("update Biodata set aktif = 0" +
    						" where nik = :nik");
			query.setParameter("nik", o.getNik());
			int result = query.executeUpdate();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Tambah Biodata Pindah : " + o.getNik(), "", "log", true);
			if(result == 1)
				return true;
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Tambah Biodata Pindah : " + o.getNik(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean delete(Biodatapindah o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Biodatapindah biodataPindah = (Biodatapindah) session.load(Biodatapindah.class, o.getNik());
			session.delete(biodataPindah);
			session.getTransaction().commit();
			session.close();
			
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Query query = session.createQuery("update Biodata set aktif = 1" +
    						" where nik = :nik");
			query.setParameter("nik", o.getNik());
			int result = query.executeUpdate();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Hapus Biodata Pindah : " + o.getNik(), "", "log", true);
			if(result == 1)
				return true;
			else
				return false;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Hapus Biodata Pindah : " + o.getNik(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public boolean update(Biodatapindah o) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Biodatapindah biodataPindah = (Biodatapindah) session.load(Biodatapindah.class, o.getNik());
			session.update(o);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Ubah Biodata Pindah : " + o.getNik(), "", "log", true);
			return true;
		}
		catch(Exception e){
			session.getTransaction().rollback();
			session.close();
			Logger.createLog("Ubah Biodata Pindah : " + o.getNik(), e.toString(), "err", true);
			return false;
		}
	}

	@Override
	public List<Biodatapindah> listData() throws Exception {
		List<Biodatapindah> list = new ArrayList<Biodatapindah>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Biodatapindah as bp inner join fetch bp.biodata as b inner join fetch b.agama inner join fetch b.pendidikan inner join fetch b.pekerjaan inner join fetch b.kelurahan as k inner join fetch k.kecamatan inner join fetch bp.kota");
			list = query.list();
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Fetch daftar Biodata Pindah", "", "log", true);
			return list;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			Logger.createLog("Fetch daftar Biodata Pindah", e.toString(), "err", true);
			return list;
		}
	}

}
