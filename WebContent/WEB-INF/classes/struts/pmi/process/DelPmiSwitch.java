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

public class DelPmiSwitch extends Action {

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
		String opMode = request.getParameter("m");
		String opModeStr ="";
		
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
					sqlDelete="DELETE FROM PMI_COUNTRY_SWITCH WHERE TARGET_CODE = ? and COUNTRY_ID=? ";
					PreparedStatement ppStmtDelete=myConnection.prepareStatement(sqlDelete);

					if (opMode.equals("p")){
						opModeStr = "PMI";
					}else if (opMode.equals("t")){
						opModeStr = "TBMI";
					}else if (opMode.equals("b")){
						opModeStr = "BR";
					};

					if((request.getParameter("DeleteList") != null && request.getParameter("DeleteList") != ""))
					{
						 String[] deleteList=request.getParameter("DeleteList").split("_jSepj_");
							for(int i=0;i<deleteList.length;i++)
							{
								if("".equals(deleteList[i]))
									break;
						
								id=deleteList[i];

								//Delete USERID
								ppStmtDelete.setString(1,opModeStr);
								ppStmtDelete.setString(2,id);
								ppStmtDelete.executeUpdate();
								//ppStmtDelete2.close();
							}
						try
						{
							ppStmtDelete.close();
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
		response.sendRedirect("ListPmiSwitch.scb?message=Delete Success&m="+request.getParameter("m"));
		return null;
				//return new ActionForward(nextPage.getPath() + "?message=Delete Success" ); 
	}

}