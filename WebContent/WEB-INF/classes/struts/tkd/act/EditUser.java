package struts.tkd.act;

import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import struts.tkd.actfrm.FormAddEditUser;
import struts.tkd.actfrm.ActionFormValidated;



public class EditUser extends ActionFormValidated{
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {
			ActionForward forward = null;
			
			forward = mapping.findForward("forward");



			Result result;
		
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			Statement				stmt;
			ResultSet				rset=null;
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			String msgErr="";
			
			FormAddEditUser myForm=new FormAddEditUser();
			try
			{				
				// Get Record Count
				String userName = request.getParameter("UserName");
				String query ="";
				query = "SELECT USER_NAME, FULL_NAME, IS_ADMIN, APP_ACCESS, FORM_ACCESS, PASSWORD FROM TKD_USER_LIST WHERE USER_NAME='"+ userName +"'";
				request.setAttribute("qdata", query);
				rset = stmt.executeQuery(query);
				if (rset.next())
				{
					myForm.setuserAdmin(rset.getString(3));
					myForm.setDBFormAccess(rset.getString(5));
					myForm.setDBAppAccess(rset.getString(4));
					myForm.setuserFullName(rset.getString(2));
					myForm.setuseridName(rset.getString(1));
					//myForm.setuserPassword(rset.getString(6));
				} else
				{
					msgErr ="User no longer exists";
				}
				
				result = ResultSupport.toResult(rset);
				request.setAttribute("FormAddEditUser",myForm );
				
			}
			catch(Exception e)
			{

				msgErr =e.getMessage();
			}
			finally
			{
				stmt.close();
				myConnection.close();
			}
			
			 if (msgErr.equals("")) {
					return new ActionForward(forward.getPath());
				} else {
	
					return new ActionForward(forward.getPath()+"?message="+msgErr);
				}	
			
	  }
}