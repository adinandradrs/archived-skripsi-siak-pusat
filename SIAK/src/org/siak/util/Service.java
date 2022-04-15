package org.siak.util;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.siak.core.AgamaCore;
import org.siak.core.AktaKelahiranCore;
import org.siak.core.AktaKematianCore;
import org.siak.core.AktaNikahCore;
import org.siak.core.AktaNikahDetailCore;
import org.siak.core.BiodataCacatCore;
import org.siak.core.BiodataCore;
import org.siak.core.BiodataPindahCore;
import org.siak.core.CacatCore;
import org.siak.core.GrupCore;
import org.siak.core.GrupPenggunaCore;
import org.siak.core.HubunganCore;
import org.siak.core.HubunganDetailCore;
import org.siak.core.KartuKeluargaCore;
import org.siak.core.KartuKeluargaDetailCore;
import org.siak.core.KecamatanCore;
import org.siak.core.KelurahanCore;
import org.siak.core.KotaCore;
import org.siak.core.KtpCore;
import org.siak.core.LogCore;
import org.siak.core.LoginCore;
import org.siak.core.PekerjaanCore;
import org.siak.core.PendidikanCore;
import org.siak.core.PenggunaCore;
import org.siak.core.PermohonanCore;
import org.siak.core.PermohonanDetailCore;

public class Service {
	
	public static void main(String [] args){
		try{
			Registry registry = LocateRegistry.createRegistry(51090);
			registry.rebind("loginCore", new LoginCore());
			registry.rebind("agamaCore", new AgamaCore());
			registry.rebind("kecamatanCore", new KecamatanCore());
			registry.rebind("kelurahanCore", new KelurahanCore());
			registry.rebind("hubunganCore", new HubunganCore());
			registry.rebind("hubunganDetailCore", new HubunganDetailCore());
			registry.rebind("pekerjaanCore", new PekerjaanCore());
			registry.rebind("pendidikanCore", new PendidikanCore());
			registry.rebind("cacatCore", new CacatCore());
			registry.rebind("penggunaCore", new PenggunaCore());
			registry.rebind("grupCore", new GrupCore());
			registry.rebind("grupPenggunaCore", new GrupPenggunaCore());
			registry.rebind("kotaCore", new KotaCore());
			
			registry.rebind("biodataCore", new BiodataCore());
			registry.rebind("biodataCacatCore", new BiodataCacatCore());
			registry.rebind("biodataPindahCore", new BiodataPindahCore());
			
			registry.rebind("ktpCore", new KtpCore());
			registry.rebind("kkCore", new KartuKeluargaCore());
			registry.rebind("kkDetailCore", new KartuKeluargaDetailCore());
			registry.rebind("aktaNikahCore", new AktaNikahCore());
			registry.rebind("aktaNikahDetailCore", new AktaNikahDetailCore());
			registry.rebind("aktaKelahiranCore", new AktaKelahiranCore());
			registry.rebind("aktaKematianCore", new AktaKematianCore());
			
			registry.rebind("permohonanCore", new PermohonanCore());
			registry.rebind("permohonanDetailCore", new PermohonanDetailCore());
			
			registry.rebind("logCore", new LogCore());
			Logger.createLog("Service is on a new session", "", "log", true);
		}
		catch(Exception e){
			Logger.createLog("Starting service is failed", e.toString(), "err", true);
		}
	}

}
