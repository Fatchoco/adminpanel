package struts.pmi.form;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.security.*;
import java.sql.*;

import struts.pmi.function.authLDAP;


public class doLoginAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;

		String plainText="";
	    String msgErr="";
	    String ldap_host="";
	    int ldap_port=0;
	    String ldap_dn="";
	    String ldap_uid="";
	    String strUname = request.getParameter("txtuname");
	    String strPwd = request.getParameter("txtpwd");
	    String strRole="";
	    String strFull_name="";
	    String strOU="";
	    int errCode=0;
	    String pkey="";
	    String pagesize="";
	    String admpwd="";
		
		Login2Form myForm = (Login2Form)form;
		
//		if (myForm.getTxtuname().equals("salah")) {
//			nextPage = mapping.findForward("err");
//		} else if (myForm.getTxtuname().equals("bener")) {
//			nextPage = mapping.findForward("ok");
//		};

		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		 Statement stmt = myConnection.createStatement();
		 ResultSet r = null;

		 //new code
		 try 
		 {
			 
			 r = stmt.executeQuery ("SELECT VARNAME, VALUE_ FROM PMI_APPVARIABLES WHERE " +
				"VARNAME NOT LIKE 'LDAP_%' ");
			 while (r.next()) {
				if(r.getString(1).equals("PARTITION_KEY"))
				{
					pkey = r.getString(2);
				}
				else if(r.getString(1).equals("PAGE_NUMBER"))
				{
					pagesize = r.getString(2);
				}
				else if(r.getString(1).equals("ADMIN_ACCESS"))
				{
					admpwd = r.getString(2);
				}
			 };

			 if (request.getParameter("txtuname").toUpperCase().equals("ADMIN")) {


				 if (!admpwd.equals("")) {

					 	/*MD5 logic*/
						plainText = request.getParameter("txtpwd");
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
						}
						/*end MD5 logic*/
						
						if (admpwd.equals(hexString.toString())) {
							nextPage = mapping.findForward("ok");
							//set ADMIN session
							request.getSession().setAttribute("username",strUname);
							request.getSession().setAttribute("fullName","Application Administrator");
							request.getSession().setAttribute("role","ADM");
							request.getSession().setAttribute("isAdmin","1");
							request.getSession().setAttribute("partition_key",pkey);
							request.getSession().setAttribute("page_number",pagesize);
						} else {
							msgErr = "incorrect password";
							nextPage = mapping.findForward("err");
						}
					 
				 } else {
					 msgErr = "Admin credential not found on app settings";
					 nextPage = mapping.findForward("err");
				 };
				 
			 } else {
				 //NOT ADMIN check user table
				 
				 r = stmt.executeQuery ("select role, full_name, ou from pmi_user where userid = '" + strUname.trim() + "'");
				 
				 if (r.next()) {
					 
					 //String[] arrRole = r.getString(1).split(",");
					 if (r.getString(1) != null) {
						 strRole = r.getString(1).trim().replace(",", "_jsepj_");
					 };
					 //System.out.println("selesai cek");
					 strFull_name = r.getString(2).trim();
					 strOU = r.getString(3).trim();

				 //check ldap_server setting
					 r = stmt.executeQuery ("select VARNAME, VALUE_ from pmi_appvariables where varname like 'LDAP_%'");

					 while(r.next())
					{
							//out.println(r.getString(1));
							if(r.getString(1).equals("LDAP_SERVER"))
							{
								ldap_host = r.getString(2);
							}
							else if(r.getString(1).equals("LDAP_PORT"))
							{
								ldap_port = Integer.parseInt(r.getString(2));
							}
							else if(r.getString(1).equals("LDAP_BASEDN"))
							{
								ldap_dn = r.getString(2);
							}	
							else if(r.getString(1).equals("LDAP_USER_IDENTIFIER"))
							{
								ldap_uid = r.getString(2);
							}	

					};
					
					//System.out.println("role=" + strRole);
					
					if (!strRole.equals("")){
						
					
					if (!ldap_host.equals("") && ldap_port!=0 && !ldap_dn.equals("") && !ldap_uid.equals("")) {
						
						authLDAP ldp = new authLDAP();
						errCode = ldp.authenticate(ldap_host, ldap_port, ldap_dn, ldap_uid, strUname, strPwd, strOU);
						if (errCode == 0) {
						//continue setting param
							nextPage = mapping.findForward("ok");
							//set user session
							request.getSession().setAttribute("username",strUname);
							request.getSession().setAttribute("fullName",strFull_name);
							request.getSession().setAttribute("role",strRole);
							request.getSession().setAttribute("isAdmin","0");
							request.getSession().setAttribute("partition_key",pkey);
							request.getSession().setAttribute("page_number",pagesize);
							
						} else {
							msgErr = "LDAP authentication error: " + ldp.errMessage(errCode);
							nextPage = mapping.findForward("err");
						};
						
					} else {
						msgErr = "incomplete LDAP settings";
						nextPage = mapping.findForward("err");
					}; //end if ldap setting check
				 
					} else {
						msgErr = "user dont have role setup";
						nextPage = mapping.findForward("err");
					}
						
				 } else {  //user not registered
					msgErr = "username not registered";
					nextPage = mapping.findForward("err");
				 }; //end if user check
			 };
			 
		 } catch (Exception e) {
			 //forward to error page - general unknown error
			 msgErr = "general error: " + e.getMessage();
			 nextPage = mapping.findForward("err");

		 } finally {
			if (r != null) r.close();
			if (stmt != null) stmt.close();
			if (myConnection != null && !myConnection.isClosed()) myConnection.close();
		 };
		

		if (msgErr.equals("")) {
			return new ActionForward(nextPage.getPath());
		} else {
			return new ActionForward(nextPage.getPath()+"?e="+msgErr);
		}
	}

}