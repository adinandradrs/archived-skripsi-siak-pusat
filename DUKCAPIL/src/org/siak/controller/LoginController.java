package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.siak.data.Pengguna;
import org.siak.face.LoginFace;
import org.siak.util.Configuration;
import org.siak.util.Encryption;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Registry registry;
	private JSONObject json;
	private LoginFace loginFace;
	private Format formatter;
	private HttpSession session;
	
    public LoginController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.bindRegistry(out);
        try{
        	session = request.getSession(true);
        	String username = request.getParameter("username");
        	String sandiClean = request.getParameter("sandi");
        	String sandi = Encryption.SHA1(sandiClean);
        	this.initializeUserKecamatan(out, username, sandi);
        }
        catch(Exception e){
        	
        }
        finally{
        	out.close();
        }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}
	
	private void bindRegistry(PrintWriter out){
		String path = getServletContext().getRealPath("/Configuration/web.ini");
    	String ipService1 = Configuration.file(path).get("Service", "ipService1");
    	String ipService2 = Configuration.file(path).get("Service", "ipService2");
    	int port = Integer.parseInt(Configuration.file(path).get("Service", "port"));
		try{
			registry = LocateRegistry.getRegistry(ipService1,port);
			loginFace = (LoginFace) registry.lookup("loginCore");
		}
		catch(Exception e){
			try{
				registry = LocateRegistry.getRegistry(ipService2,port);
				loginFace = (LoginFace) registry.lookup("loginCore");
			}
			catch(Exception ex){
				out.print(ex.getMessage());
			}
		}
	}

	private void initializeUserKecamatan(PrintWriter out, String username, String sandi){
		json = new JSONObject();
		boolean success = false;
		try{
			Pengguna p = new Pengguna();
			p.setUsername(username);
			p.setSandi(sandi);
			InetAddress localIP = InetAddress.getLocalHost();
			p.setIplogin(localIP.getHostAddress());
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String waktuString = formatter.format(date);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date waktuLogin = formatter.parse(waktuString);
			p.setWaktuLogin(waktuLogin);
			success = loginFace.loginDUKCAPIL(p);
			session.setAttribute("user", loginFace.dataPengguna(p));
			session.setAttribute("hasLogin", true);
			json.put("success", success);
			json.put("explain", "Maaf Anda tidak dapat login, silakan menghubungi pusat");
		}
		catch(Exception e){
			json.put("success", success);
			json.put("explain", e.getMessage());
		}
		out.print(json);
	}

}
