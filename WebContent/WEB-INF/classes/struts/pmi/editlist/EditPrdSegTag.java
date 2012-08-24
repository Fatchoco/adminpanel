package struts.pmi.editlist;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;

//levin17lacustre 25092009
public class EditPrdSegTag extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//ActionForward nextPage = null;
		//nextPage = mapping.findForward("forward");
		String search="";

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
					String sqlUpdate="UPDATE PMI_PRD_SEG_TAG_MAPPING SET PRODUCT_SEGMENT_TAG1=?,PRODUCT_SEGMENT_TAG2=?,CREATE_DATE=SYSDATE,CREATE_USER='PMI_UPLOAD_UPDATE' where PRODUCT_SEGMENT_ID =?";
					PreparedStatement pstmt =myConnection.prepareStatement(sqlUpdate);

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
							pstmt.setString(1,request.getParameter("lbl2_"+thisid));
							pstmt.setString(2,request.getParameter("lbl3_"+thisid));
							pstmt.setString(3,thisid);
							pstmt.executeUpdate();
						}
						
					}
					catch(SQLException e)
					{
						myConnection.rollback();
						pstmt.close();
						myConnection.close();
						response.sendRedirect("ListPrdSegTag.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful "+thisid+" with error"+e);
					}
					myConnection.commit();
					pstmt.close();
					myConnection.close();
					response.sendRedirect("ListPrdSegTag.scb?Page="+page+"&Search="+search+"&message=Update Successful");
				}
				catch(Exception e)
				{
					System.out.println("ERROR : " + e);
					e.printStackTrace(System.out);
					response.sendRedirect("ListPrdSegTag.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful: "+e);
				}

				return null;
				//return new ActionForward(nextPage.getPath() + "?message=" + message); 
	}

}