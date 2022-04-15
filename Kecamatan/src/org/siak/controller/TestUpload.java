package org.siak.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

@WebServlet("/TestUpload")
public class TestUpload extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(TestUpload.class);

	private String getFileName(Part part) {
		String partHeader = part.getHeader("content-disposition");
		logger.info("Part Header = " + partHeader);
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;

	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			for (Part part : request.getParts()) {
				logger.info(part.getName());
				InputStream is = request.getPart(part.getName())
						.getInputStream();
				int i = is.available();
				byte[] b = new byte[i];
				is.read(b);
				logger.info("Length : " + b.length);
				String fileName = getFileName(part);
				logger.info("File name : " + fileName);
				FileOutputStream os = new FileOutputStream("C:/"
						+ fileName);
				os.write(b);
				is.close();
			}
		} catch (Exception e) {
			out.print(e);
		} finally {
			out.close();
		}
	}

	public TestUpload() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}

}
