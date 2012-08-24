package struts.pmi.editlist;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;

public class EditUsers extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//ActionForward nextPage = null;
		//nextPage = mapping.findForward("forward");
		String search="";
		String roles="";

		int page=1;
		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();

			//check login stat
			if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != ""))
			{
				ActionForward login=mapping.findForward("loginpage");
				return login;
				
			}			

				try
				{

					 if((request.getParameter("cmbPage") != null && request.getParameter("cmbPage") != ""))
					{
						page=Integer.parseInt(request.getParameter("cmbPage"));
						
					}
					myConnection.setAutoCommit(false);
					String sqlUpdate="UPDATE PMI_USER SET ROLE=?,DATE_UPDATED=SYSDATE,USER_UPDATED='PMI_UPLOAD_UPDATE' " +
							" where USERID = ?";
					PreparedStatement pstmt = myConnection.prepareStatement(sqlUpdate);

					 if((request.getParameter("searchq") != null && request.getParameter("searchq") != ""))
					{
						search=request.getParameter("searchq").toUpperCase().replaceAll("'","''");	
						
					}
					int tot_rec=Integer.parseInt(request.getParameter("total_record"));
					String thisid="";

					try
					{
						for(int i=1;i<=tot_rec;i++)
						{
							thisid=request.getParameter("thisid_"+i);

							//logic to join role string
							
							if (request.getParameter("cb_pmi_"+thisid) != null ) {
								roles = roles + "PMI,";
							};
							if (request.getParameter("cb_occ_"+thisid) != null) {
								roles = roles + "OCC,";
							}; 
							if (request.getParameter("cb_tb_"+thisid) != null) {
								roles = roles + "TB-MI,";
							}; 
							if (request.getParameter("cb_br_"+thisid) != null) {
								roles = roles + "BR,";
							};
							
							//System.out.println("ROLE: "+ roles);
							pstmt.setString(1,roles);

							pstmt.setString(2,thisid);
							
							pstmt.executeUpdate();
							roles = "";
						}
						
					}
					catch(SQLException e)
					{
						myConnection.rollback();
						pstmt.close();
						myConnection.close();
						response.sendRedirect("ListUsers.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful "+thisid+" with error"+e);
					}
					myConnection.commit();
					pstmt.close();
					myConnection.close();
					response.sendRedirect("ListUsers.scb?Page="+page+"&Search="+search+"&message=User modified successfully");
				}
				catch(Exception e)
				{
					System.out.println("ERROR : " + e);
					e.printStackTrace(System.out);
					response.sendRedirect("ListUsers.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful: "+e);
				}

				return null;
				//return new ActionForward(nextPage.getPath() + "?message=" + message); 
	}

}