package struts.tkd.act;

//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.jsp.jstl.sql.Result;
//import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.security.*;
import java.sql.*;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.function.userSessionCheck;

public class LoginProcess extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ActionForward nextPage = null;

		String uname = request.getParameter("txtuname");
		String passw = request.getParameter("txtpwd");
		String app = request.getParameter("menu");
		String APP_ACCESS ="";
		String FORM_ACCESS ="";
		String IS_ADMIN="";
		String FULL_NAME ="";
		javax.sql.DataSource	dataSource;
		java.sql.Connection		myConnection;
		dataSource		= getDataSource(request,"TKD_DIST_STG");
		myConnection	= dataSource.getConnection();
		Statement stmt = myConnection.createStatement();
		ResultSet r	= null;
		String admpwd="";
		String msgErr="";
		String loginName= "";

		try
		{
			r = stmt.executeQuery ("SELECT VAR_NAME, VAR_VALUE FROM TKD_APP_VAR WHERE " +
			"VAR_NAME = 'ADMIN_PWD'");
			while (r.next()) {
				admpwd = r.getString(2);
			};

			if (request.getParameter("txtuname").toUpperCase().equals("ADMIN_OP")) {

				if (!admpwd.equals("")) {

					/*MD5 logic*/
					/*plainText = request.getParameter("txtpwd");
						MessageDigest md = MessageDigest.getInstance("MD5");
						md.reset();
						md.update(plainText.getBytes());

						byte[] digest = md.digest();
						StringBuffer hexString = new StringBuffer();

						for (int i = 0; i < digest.length; i++) 
						{
							plainText = Integer.toHexString(0xFF & digest[i]);
							if (plainText.length() < 2) 
							{
								plainText = '0' + plainText;
							}
							hexString.append(plainText);
						}*/
					/*end MD5 logic*/

					if  (admpwd.equals(userSessionCheck.EncryptMD5(passw.toString())) && app.equals("1"))
					{
						nextPage = mapping.findForward("ok");
						IS_ADMIN ="1";
						FORM_ACCESS= "ALL";
						APP_ACCESS="ALL";	
						loginName =uname.toString();
						FULL_NAME ="Administrator";
						//set ADMIN session
						request.getSession().setAttribute("LOGIN_APP","DIST");

					} 
					else if  (admpwd.equals(userSessionCheck.EncryptMD5(passw.toString())) && app.equals("2"))
					{
						nextPage = mapping.findForward("ok2");
						IS_ADMIN ="1";
						FORM_ACCESS= "ALL";
						APP_ACCESS="ALL";	
						loginName =uname.toString();
						FULL_NAME ="Administrator";
						//set ADMIN session
						request.getSession().setAttribute("LOGIN_APP","IMS");

					} else {
						msgErr = "incorrect password";
						nextPage = mapping.findForward("err");
					}

				} else {
					msgErr = "Admin credential not found on app settings";
					nextPage = mapping.findForward("err");
				};

			}else {
				r = stmt.executeQuery("SELECT  USER_NAME,APP_ACCESS,FORM_ACCESS,PASSWORD,IS_ADMIN,FULL_NAME FROM TKD_USER_LIST WHERE user_name = '"+ uname +"'");
				//r.next();

				if (r.next()) {
					if  (r.getString(4).toString().equals(userSessionCheck.EncryptMD5(passw)))
					{
						loginName =uname.toString();
						IS_ADMIN =r.getString(5);
						FORM_ACCESS= r.getString(3);
						FULL_NAME= r.getString(6);
						APP_ACCESS=r.getString(2);
					}
					else
					{
						msgErr = "incorrect password";
					}

				}else
				{
					msgErr = "Wrong username";
				}

				if (!loginName.equals("") && app.equals("1")) {
						nextPage = mapping.findForward("ok");
					}
				else if (!loginName.equals("") && app.equals("2")) {
					nextPage = mapping.findForward("ok2");
				}
				else
				{
					nextPage = mapping.findForward("err");
				}

			}

		}
		catch(Exception e)
		{
			msgErr = "general error: " + e.getMessage();
			nextPage = mapping.findForward("err");
		} finally {
			if (r != null) r.close();
			if (stmt != null) stmt.close();
			if (myConnection != null && !myConnection.isClosed()) myConnection.close();

		};
		if (msgErr.equals("")) {
			int APP_ACCESS_DIST = APP_ACCESS.indexOf("DIST");
			int APP_ACCESS_IMS = APP_ACCESS.indexOf("IMS");
			
			int FORM_ACCESS_PRDMST = FORM_ACCESS.indexOf("PRDMST");
			int FORM_ACCESS_DISTMST = FORM_ACCESS.indexOf("DISTMST");
			int FORM_ACCESS_CUSTMST = FORM_ACCESS.indexOf("CUSTMST");
			int FORM_ACCESS_GEOMST = FORM_ACCESS.indexOf("GEOMST");
			int FORM_ACCESS_DISTPRICEMST = FORM_ACCESS.indexOf("DISTPRICEMST");
			int FORM_ACCESS_PARMMST = FORM_ACCESS.indexOf("PARMMST");

			int FORM_ACCESS_DISTFILE = FORM_ACCESS.indexOf("DISTFILE");
			int FORM_ACCESS_DISTADJFILE = FORM_ACCESS.indexOf("DISTADJFILE");
			int FORM_ACCESS_COLMAP = FORM_ACCESS.indexOf("COLMAP");
			int FORM_ACCESS_INVTFILE = FORM_ACCESS.indexOf("INVTFILE");

			int FORM_ACCESS_PRDMAP = FORM_ACCESS.indexOf("PRDMAP");
			int FORM_ACCESS_CUSTMAP = FORM_ACCESS.indexOf("CUSTMAP");
			int FORM_ACCESS_MRMAP = FORM_ACCESS.indexOf("MRMAP");
			int FORM_ACCESS_HPTARGET = FORM_ACCESS.indexOf("HPTARGET");
			int FORM_ACCESS_EFTARGET = FORM_ACCESS.indexOf("EFTARGET");

			int FORM_ACCESS_UNMPPRD = FORM_ACCESS.indexOf("UNMPPRD");
			int FORM_ACCESS_UNMPCUST = FORM_ACCESS.indexOf("UNMPCUST");
			int FORM_ACCESS_DISTFILEEXCP = FORM_ACCESS.indexOf("DISTFILEEXCP");
			int FORM_ACCESS_DISTADJFILEEXCP = FORM_ACCESS.indexOf("DISTADJFILEEXCP");

			int FORM_ACCESS_DISTRUN = FORM_ACCESS.indexOf("DISTRUN");
			int FORM_ACCESS_DISTADJRUN = FORM_ACCESS.indexOf("DISTADJRUN");
			
			int FORM_ACCESS_IUPDAT = FORM_ACCESS.indexOf("IUPDAT");
			int FORM_ACCESS_IUPMAP = FORM_ACCESS.indexOf("IUPMAP");
			
			int FORM_ACCESS_ISETPER = FORM_ACCESS.indexOf("ISETPER");
			int FORM_ACCESS_ISETDIR = FORM_ACCESS.indexOf("ISETDIR");

			request.getSession().setAttribute("USER_NAME",loginName.toString());
			request.getSession().setAttribute("FULL_NAME",FULL_NAME);
			request.getSession().setAttribute("APP_ACCESS",APP_ACCESS.toString());
			request.getSession().setAttribute("FORM_ACCESS",FORM_ACCESS.toString());
			request.getSession().setAttribute("IS_ADMIN",IS_ADMIN.toString());
			
			request.getSession().setAttribute("APP_ACCESS_DIST",APP_ACCESS_DIST);
			request.getSession().setAttribute("APP_ACCESS_IMS",APP_ACCESS_IMS);
			
			request.getSession().setAttribute("FORM_ACCESS_PRDMST",FORM_ACCESS_PRDMST);
			request.getSession().setAttribute("FORM_ACCESS_DISTMST",FORM_ACCESS_DISTMST);
			request.getSession().setAttribute("FORM_ACCESS_CUSTMST",FORM_ACCESS_CUSTMST);
			request.getSession().setAttribute("FORM_ACCESS_GEOMST",FORM_ACCESS_GEOMST);
			request.getSession().setAttribute("FORM_ACCESS_DISTPRICEMST",FORM_ACCESS_DISTPRICEMST);
			request.getSession().setAttribute("FORM_ACCESS_PARMMST",FORM_ACCESS_PARMMST);

			request.getSession().setAttribute("FORM_ACCESS_DISTFILE",FORM_ACCESS_DISTFILE);
			request.getSession().setAttribute("FORM_ACCESS_DISTADJFILE",FORM_ACCESS_DISTADJFILE);
			request.getSession().setAttribute("FORM_ACCESS_COLMAP",FORM_ACCESS_COLMAP);
			request.getSession().setAttribute("FORM_ACCESS_INVTFILE",FORM_ACCESS_INVTFILE);

			request.getSession().setAttribute("FORM_ACCESS_PRDMAP",FORM_ACCESS_PRDMAP);
			request.getSession().setAttribute("FORM_ACCESS_CUSTMAP",FORM_ACCESS_CUSTMAP);
			request.getSession().setAttribute("FORM_ACCESS_MRMAP",FORM_ACCESS_MRMAP);
			request.getSession().setAttribute("FORM_ACCESS_HPTARGET",FORM_ACCESS_HPTARGET);
			request.getSession().setAttribute("FORM_ACCESS_EFTARGET",FORM_ACCESS_EFTARGET);

			request.getSession().setAttribute("FORM_ACCESS_UNMPPRD",FORM_ACCESS_UNMPPRD);
			request.getSession().setAttribute("FORM_ACCESS_UNMPCUST",FORM_ACCESS_UNMPCUST);
			request.getSession().setAttribute("FORM_ACCESS_DISTFILEEXCP",FORM_ACCESS_DISTFILEEXCP);
			request.getSession().setAttribute("FORM_ACCESS_DISTADJFILEEXCP",FORM_ACCESS_DISTADJFILEEXCP);

			request.getSession().setAttribute("FORM_ACCESS_DISTRUN",FORM_ACCESS_DISTRUN);
			request.getSession().setAttribute("FORM_ACCESS_DISTADJRUN",FORM_ACCESS_DISTADJRUN);
			
			request.getSession().setAttribute("FORM_ACCESS_IUPDAT",FORM_ACCESS_IUPDAT);
			request.getSession().setAttribute("FORM_ACCESS_IUPMAP",FORM_ACCESS_IUPMAP);
			
			request.getSession().setAttribute("FORM_ACCESS_ISETPER",FORM_ACCESS_ISETPER);
			request.getSession().setAttribute("FORM_ACCESS_ISETDIR",FORM_ACCESS_ISETDIR);

			return new ActionForward(nextPage.getPath());
		} else {
			return new ActionForward(nextPage.getPath()+"?e="+msgErr);
		}	

	}
}
