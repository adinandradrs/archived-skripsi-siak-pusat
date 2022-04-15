package org.siak.core;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.siak.data.Gruppengguna;
import org.siak.data.Kota;
import org.siak.data.Pengguna;
import org.siak.face.LoginFace;
import org.siak.util.HibernateUtil;
import org.siak.util.Logger;

public class LoginCore extends UnicastRemoteObject implements LoginFace {

	private static final long serialVersionUID = 1L;

	public LoginCore() throws Exception{
		
	}
	
	//inner function
	private boolean userCanLogin(Pengguna p){
		Session session = HibernateUtil.getSessionFactory().openSession();
		int counter = 0;
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Pengguna.class)
					.add(Restrictions.eq("username", p.getUsername()))
					.add(Restrictions.eq("sandi", p.getSandi()))
					.add(Restrictions.eq("aktif", 1));
			counter = criteria.list().size();
			session.getTransaction().commit();
			session.close();
			if(counter == 1)
				return true;
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			return false;
		}
	}

	private boolean grupCanLogin(Pengguna p){
		Session session = HibernateUtil.getSessionFactory().openSession();
		int aktif = 0;
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Gruppengguna.class)
					.add(Restrictions.eq("pengguna", p));
			List<Gruppengguna> l = criteria.list();
			for (Gruppengguna gp : l)
				aktif = gp.getGrup().getAktif();
			session.getTransaction().commit();
			session.close();
			if(aktif == 1)
				return true;
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			return false;
		}
	}
	
	private boolean kecamatanCanLogin(Pengguna p){
		Session session = HibernateUtil.getSessionFactory().openSession();
		int aktif = 0;
		int kecamatan = 9999;
		int kelurahan = 9999;
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Gruppengguna.class)
					.add(Restrictions.eq("pengguna", p));
			List<Gruppengguna> l = criteria.list();
			for (Gruppengguna gp : l){
				//aktif = gp.getKecamatan().getAktif();
				kecamatan = gp.getKecamatanId();
				kelurahan = gp.getKelurahanId();
			}
			session.getTransaction().commit();
			session.close();
			//aktif == 1 && 
			if(kecamatan != 0 && kelurahan == 0)
				return true;
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			return false;
		}
	}
	
	private boolean DUKCAPILCanLogin(Pengguna p){
		Session session = HibernateUtil.getSessionFactory().openSession();
		int kecamatan = 9999;
		int kelurahan = 9999;
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Gruppengguna.class)
					.add(Restrictions.eq("pengguna", p));
			List<Gruppengguna> l = criteria.list();
			for (Gruppengguna gp : l){
				kecamatan = gp.getKecamatanId();
				kelurahan = gp.getKelurahanId();
			}
			session.getTransaction().commit();
			session.close();
			if(kecamatan == 0 && kelurahan == 0)
				return true;
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			return false;
		}
	}
	
	private boolean kelurahanCanLogin(Pengguna p){
		Session session = HibernateUtil.getSessionFactory().openSession();
		int aktif = 0;
		int kecamatan = 9999;
		int kelurahan = 9999;
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Gruppengguna.class)
					.add(Restrictions.eq("pengguna", p));
			List<Gruppengguna> l = criteria.list();
			for (Gruppengguna gp : l){
				//aktif = gp.getKelurahan().getAktif();
				kecamatan = gp.getKecamatanId();
				kelurahan = gp.getKelurahanId();
			}
			session.getTransaction().commit();
			session.close();
			System.out.println("sini");
			//aktif == 1 && 
			if(kecamatan == 0 && kelurahan != 0)
				return true;
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			return false;
		}
	}
	
	private void updateWhenLogin(Pengguna o){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Pengguna p = (Pengguna) session.load(Pengguna.class, o.getUsername());
			p.setIplogin(o.getIplogin());
			p.setWaktuLogin(o.getWaktuLogin());
			session.update(p);
			session.getTransaction().commit();
			session.close();
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
		}
	}
	
	//interface function
	@Override
	public boolean loginKecamatan(Pengguna p) throws Exception {
		boolean userLogin = userCanLogin(p), grupLogin = grupCanLogin(p), kecamatanLogin = kecamatanCanLogin(p);
		if(userLogin == true && grupLogin == true && kecamatanLogin == true){
			this.updateWhenLogin(p);
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public boolean loginDUKCAPIL(Pengguna p) throws Exception {
		boolean userLogin = userCanLogin(p);
		boolean canLogin = false; 
		if(userLogin == true){
			this.updateWhenLogin(p);
			canLogin = DUKCAPILCanLogin(p);
		}
		return canLogin;
	}
	
	@Override
	public boolean loginKelurahan(Pengguna p) throws Exception {
		boolean userLogin = userCanLogin(p), grupLogin = grupCanLogin(p), kelurahanLogin = kelurahanCanLogin(p);
		if(userLogin == true && grupLogin == true && kelurahanLogin == true){
			this.updateWhenLogin(p);
			System.out.println("sini");
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public Gruppengguna dataPengguna(Pengguna p) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Gruppengguna o = null;
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Gruppengguna.class)
					.add(Restrictions.eq("pengguna", p));
			o = (Gruppengguna) criteria.list().get(0);
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Login : " + p.getUsername(), "", "log", true);
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
		}
		return o;
	}

	@Override
	public boolean loginKota(Kota k) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		boolean allow = false;
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Kota.class)
					.add(Restrictions.eq("username", k.getUsername()))
					.add(Restrictions.eq("sandi", k.getSandi()));
			int count = criteria.list().size();
			if(count == 1){
				k = (Kota)criteria.list().get(0);
				if(k.getAktif() == 1)
					allow = true;
				else
					allow = false;
			}
			else
				allow = false;
			session.getTransaction().commit();
			session.close();
			Logger.createLog("Login : " + k.getUsername(), "", "log", true);
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
		}
		return allow;
	}

	@Override
	public Kota dataKota(Kota k) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Kota.class)
					.add(Restrictions.eq("username", k.getUsername()))
					.add(Restrictions.eq("sandi", k.getSandi()));
			k = (Kota)criteria.list().get(0);
			session.getTransaction().commit();
			session.close();
		}
		catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			session.close();
		}
		return k;
	}

}
