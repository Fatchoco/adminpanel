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

public class DelUserProcess extends ActionFormValidated{
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception{
						
		  ActionForward forward = null;
			
			forward = mapping.findForward("forward");
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			 dataSource		= getDataSource(request,"TKD_DIST_STG");
			 ResultSet r	= null;
			 String userName = request.getParameter("UserName");
			 String msgErr ="";
			String query ="";
			 String sql ="";
			 myConnection	= dataSource.getConnection();
			 sql = "DELETE FROM TKD_USER_LIST WHERE USER_NAME= ?"; 
			PreparedStatement ppStmt = myConnection.prepareStatement(sql);
			 try
			 {
				 
				 ppStmt.setString(1,userName);
				 
				 ppStmt.executeUpdate();
					
				 
			 }
			 catch(Exception e)
			 {
				 if (e.getMessage().contains("ORA-00001"))
				 {
					 msgErr ="User Name has been used, Please user another user name";
				 }				 else
				 {
					 msgErr = "User "+ userName +" failed to be deleted: " + e.getMessage();
				 }
				 
			 } finally {
					if (r != null) r.close();
					if (ppStmt != null) ppStmt.close();
					if (myConnection != null && !myConnection.isClosed()) myConnection.close();
				
				 };
			
			 if (msgErr.equals("")) {
				 		return new ActionForward(forward.getPath());
				} else {
					return new ActionForward(forward.getPath()+"?message="+msgErr);
				}	
			
	  }
}
