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

public class DelHeadCount extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String query="";
		String queryCount="";
		int totalRowPerPage = 20;
		String search="";
		String sqlDelete="";
		String id="";
		int start=0;
		int end=0;
		int count=0;
		int tot_page=0;
		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		 Statement stmt = myConnection.createStatement();
		 ResultSet r;
			

			//check login stat
			if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != ""))
			{
				ActionForward login=mapping.findForward("loginpage");
				return login;
				
			}			

				try
				{
					sqlDelete="DELETE FROM PMI_HEAD_COUNT WHERE PARTITION_KEY LIKE ?";
					PreparedStatement ppStmtDelete=myConnection.prepareStatement(sqlDelete);
					 if((request.getParameter("pkey") != null && request.getParameter("pkey") != ""))
					{
							ppStmtDelete.setString(1,request.getParameter("pkey") + "%");
							ppStmtDelete.executeUpdate();
							ppStmtDelete.close();
							myConnection.close();
					}
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
		
				response.sendRedirect("ListHeadCount.scb?message=Delete Success");
				return null;
				//return new ActionForward(nextPage.getPath() + "?message=Delete Success" ); 
	}

}