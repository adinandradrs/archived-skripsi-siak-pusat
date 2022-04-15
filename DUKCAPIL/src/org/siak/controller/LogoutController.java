package org.siak.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutController")
public class LogoutController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	HttpSession session = null;
	
    public LogoutController() {
        super();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try{
        	session = request.getSession(false);
        	session.removeAttribute("user");
        	session.removeAttribute("hasLogin");
        	out.print("<center><b>Terima kasih telah menggunakan SIAK OS 1.0 RC</b></center>");
        	out.print("<meta http-equiv='refresh' content='2;URL=index.jsp'>");
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

}
