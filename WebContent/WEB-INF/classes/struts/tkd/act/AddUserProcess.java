package struts.tkd.act;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.jsp.jstl.sql.Result;
//import javax.servlet.jsp.jstl.sql.ResultSupport;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.actfrm.ActionFormValidated;
import struts.tkd.actfrm.FormAddEditUser;
import struts.tkd.function.userSessionCheck;

public class AddUserProcess extends ActionFormValidated{
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception{
						
			ActionForward nextPage = null;
			
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			 dataSource		= getDataSource(request,"TKD_DIST_STG");
			 ResultSet r	= null;
			 String admpwd="";
			 String msgErr="";
			 String loginName= "";
			 String sql ="";
			 myConnection	= dataSource.getConnection();
			 FormAddEditUser myForm=(FormAddEditUser) form;
			 sql = "INSERT INTO TKD_USER_LIST (USER_NAME, FULL_NAME,PASSWORD,APP_ACCESS,FORM_ACCESS,IS_ADMIN) " +
			  " VALUES (?,?,?,?,?,?) ";
			PreparedStatement ppStmt = myConnection.prepareStatement(sql);
			 try
			 {
				 
				 ppStmt.setString(1,myForm.getuseridName());
				 ppStmt.setString(2,myForm.getuserFullName());
				 ppStmt.setString(3,userSessionCheck.EncryptMD5(myForm.getuserPassword()));
				 ppStmt.setString(4,myForm.getAppAccessString());
				 ppStmt.setString(5,myForm.getFormAccessString());
				 ppStmt.setString(6,myForm.getuserAdmin());
				 ppStmt.executeUpdate();
					
				 
			 }
			 catch(Exception e)
			 {
				 if (e.getMessage().contains("ORA-00001"))
				 {
					 msgErr ="User Name has been used, Please user another user name";
				 }				 else
				 {
					 msgErr = "general error: " + e.getMessage();
				 }
				 nextPage = mapping.findForward("err");
				 
			 } finally {
					if (r != null) r.close();
					if (ppStmt != null) ppStmt.close();
					if (myConnection != null && !myConnection.isClosed()) myConnection.close();
				
				 };
			
			 if (msgErr.equals("")) {
				 	nextPage = mapping.findForward("ok");
					return new ActionForward(nextPage.getPath());
				} else {
					nextPage = mapping.findForward("err");
					request.setAttribute("FormAddEditUserData",myForm );	
					return new ActionForward(nextPage.getPath()+"?message="+msgErr);
				}	
			
	  }
}
