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

public class DelMISPrd extends Action {

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
		String sqlDelete2="";
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
					sqlDelete="DELETE FROM PMI_MIS_PRODUCT_DIM WHERE PARTITION_KEY||MIS_PRODUCT_CODE = ? ";
					sqlDelete2="DELETE FROM PMI_MIS_PRODUCT_MAP WHERE MIS_PRODUCT_CODE = ? ";
					PreparedStatement ppStmtDelete=myConnection.prepareStatement(sqlDelete);
					PreparedStatement ppStmtDelete2=myConnection.prepareStatement(sqlDelete2);
					
					 if((request.getParameter("DeleteList") != null && request.getParameter("DeleteList") != ""))
					{
						 String[] deleteList=request.getParameter("DeleteList").split("_jSepj_");
							for(int i=0;i<deleteList.length;i++)
							{
								if("".equals(deleteList[i]))
									break;
						
								id=deleteList[i];

								//Delete USERID
								ppStmtDelete.setString(1,id);
								ppStmtDelete.executeUpdate();

								System.out.println(id.substring(12,id.length()));
								ppStmtDelete2.setString(1,id.substring(12,id.length()));
								ppStmtDelete2.executeUpdate();
								
								//ppStmtDelete2.close();
							}
						try
						{
							ppStmtDelete.close();
							ppStmtDelete2.close();
							myConnection.close();
						}
						catch(SQLException e)
						{
							System.out.println(e);
						}
					}
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
		response.sendRedirect("ListMISPrd.scb?message=Delete Success");
		return null;
				//return new ActionForward(nextPage.getPath() + "?message=Delete Success" ); 
	}

}