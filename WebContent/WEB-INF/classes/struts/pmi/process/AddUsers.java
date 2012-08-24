package struts.pmi.process;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;

public class AddUsers extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String sql="";
		String id="";
		String message = "";
		String fulln = "";
		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		 Statement stmt = myConnection.createStatement();
		 ResultSet r;
			

			//check login stat
			if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != "" && !request.getSession().getAttribute("isAdmin").equals("0")))
			{
				ActionForward login=mapping.findForward("loginpage");
				return login;
				
			}			

				try
				{
					if (!request.getParameter("uid").equals("") && !request.getParameter("full").equals("") && !request.getParameter("ou").equals("")) 
					{
						fulln = request.getParameter("full").replaceAll("_jcommaj_", ",");
						
						sql = "INSERT INTO PMI_USER (USERID, FULL_NAME, OU, DATE_CREATED, USER_CREATED) " +
						  " VALUES (?,?,?, SYSDATE, 'PMI_UPLOAD_INSERT') ";
						PreparedStatement ppStmt = myConnection.prepareStatement(sql);
						ppStmt.setString(1,request.getParameter("uid"));
						ppStmt.setString(2,fulln);
						ppStmt.setString(3,request.getParameter("ou"));
						ppStmt.executeUpdate();
						message = "New user added successfully";
					}
				}
				catch(Exception e)
				{
					System.out.println(e);
					message = "ERR: Add user failed - " + e.getMessage();
				}
		response.sendRedirect("ListUsers.scb?message="+message);
		return null;
				//return new ActionForward(nextPage.getPath() + "?message=Delete Success" ); 
	}

}