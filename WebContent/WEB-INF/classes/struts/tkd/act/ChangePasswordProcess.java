package struts.tkd.act;


import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.jsp.jstl.sql.Result;
//import javax.servlet.jsp.jstl.sql.ResultSupport;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.omg.CORBA.Request;

import struts.tkd.actfrm.ActionFormValidated;
import struts.tkd.actfrm.FormAddEditUser;
import struts.tkd.function.userSessionCheck;

public class ChangePasswordProcess extends ActionFormValidated{
	public ActionForward doExecute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{

		ActionForward nextPage = null;

		javax.sql.DataSource	dataSource;
		java.sql.Connection		myConnection;
		Statement				stmt;
		dataSource		= getDataSource(request,"TKD_DIST_STG");
		ResultSet r	= null;
		String msgErr="";
		String sql ="";
		String query ="";
		myConnection	= dataSource.getConnection();
		FormAddEditUser myForm=(FormAddEditUser) form;
		String userName =request.getSession().getAttribute("USER_NAME").toString();
		stmt			= myConnection.createStatement();
		 
		PreparedStatement ppStmt = null;
		try
		{
			if (userName.toUpperCase().equals("ADMIN_OP"))
			{
				query = "SELECT VAR_NAME, VAR_VALUE FROM TKD_APP_VAR WHERE VAR_NAME = 'ADMIN_PWD'";
			}
			else
			{
				query = "SELECT USER_NAME,password FROM TKD_USER_LIST WHERE USER_NAME='"+ userName +"'";
			}
			r = stmt.executeQuery(query);
			if (!r.next())
			{
				msgErr ="Wrong User id";
			} else 
			{
				if (!r.getString(2).equals(userSessionCheck.EncryptMD5(myForm.getuserPassword())))
						{
								msgErr ="Wrong password";
						}
			}

			if (msgErr == "") 
			{
				if (userName.toUpperCase().equals("ADMIN_OP"))
				{
					sql = "UPDATE TKD_APP_VAR set VAR_VALUE=? WHERE VAR_NAME = 'ADMIN_PWD'";
					ppStmt = myConnection.prepareStatement(sql);
					ppStmt.setString(1,userSessionCheck.EncryptMD5(myForm.getNewUserPassword()));
				} else
				{
					sql = "UPDATE TKD_USER_LIST set PASSWORD= ? where USER_NAME=?";
					ppStmt = myConnection.prepareStatement(sql);
					ppStmt.setString(1,userSessionCheck.EncryptMD5(myForm.getNewUserPassword()));
					ppStmt.setString(2, userName);
				}
				
				ppStmt.executeUpdate();
			}

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
			if (stmt != null) stmt.close();
			if (myConnection != null && !myConnection.isClosed()) myConnection.close();
			myForm.setNewUserPassword("");
			myForm.setuserPassword("");
			

		};

		if (msgErr.equals("")) {
			nextPage = mapping.findForward("forward");
			return new ActionForward(nextPage.getPath()+"?messageDone= Password Changed");
		} else {
			nextPage = mapping.findForward("forward");
			return new ActionForward(nextPage.getPath()+"?message="+msgErr);
		}	

	}
}
