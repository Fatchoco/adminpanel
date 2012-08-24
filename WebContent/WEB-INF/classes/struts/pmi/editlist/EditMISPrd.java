package struts.pmi.editlist;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;


public class EditMISPrd extends Action {

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
					String sqlUpdate="UPDATE PMI_MIS_PRODUCT_DIM SET MIS_PRODUCT_DESC=?, " +
							" PRODUCT_TAG_1=?, PRODUCT_TAG_2=?, PRODUCT_TAG_3=?, PRODUCT_TAG_4=?, PRODUCT_TAG_5=?, " +
							" REMARK=?, FLAG_NEW=? " +
							" where PARTITION_KEY||MIS_PRODUCT_CODE=?";
					//System.out.println(sqlUpdate);
					
					String sqlUpdate2="UPDATE PMI_MIS_PRODUCT_MAP SET MIS_PRODUCT_DESC=?, " +
							" PRODUCT_TAG_1=?, PRODUCT_TAG_2=?, PRODUCT_TAG_3=?, PRODUCT_TAG_4=?, PRODUCT_TAG_5=?, " +
							" REMARK=?, FLAG_NEW=? " +
							" where MIS_PRODUCT_CODE=?";
					
					PreparedStatement pstmt = myConnection.prepareStatement(sqlUpdate);
					PreparedStatement pstmt2 = myConnection.prepareStatement(sqlUpdate2);
					
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
							pstmt.setString(3,request.getParameter("lbl4_"+thisid));
							pstmt.setString(4,request.getParameter("lbl5_"+thisid));
							pstmt.setString(5,request.getParameter("lbl6_"+thisid));
							pstmt.setString(6,request.getParameter("lbl7_"+thisid));
							pstmt.setString(7,request.getParameter("lbl8_"+thisid));
							pstmt.setString(8,request.getParameter("lbl9_"+thisid));
							pstmt.setString(9,thisid);
							pstmt.executeUpdate();
							
							//thisid=request.getParameter("thispc_"+i);
							pstmt2.setString(1,request.getParameter("lbl2_"+thisid));
							//System.out.println("1-" + request.getParameter("lbl2_"+thisid));
							pstmt2.setString(2,request.getParameter("lbl3_"+thisid));
							//System.out.println("1-" + request.getParameter("lbl2_"+thisid));
							pstmt2.setString(3,request.getParameter("lbl4_"+thisid));
							//System.out.println("1-" + request.getParameter("lbl2_"+thisid));
							pstmt2.setString(4,request.getParameter("lbl5_"+thisid));
							//System.out.println("1-" + request.getParameter("lbl2_"+thisid));
							pstmt2.setString(5,request.getParameter("lbl6_"+thisid));
							//System.out.println("1-" + request.getParameter("lbl2_"+thisid));
							pstmt2.setString(6,request.getParameter("lbl7_"+thisid));
							//System.out.println("1-" + request.getParameter("lbl2_"+thisid));
							pstmt2.setString(7,request.getParameter("lbl8_"+thisid));
							//System.out.println("1-" + request.getParameter("lbl2_"+thisid));
							pstmt2.setString(8,request.getParameter("lbl9_"+thisid));
							//System.out.println("1-" + request.getParameter("lbl2_"+thisid));
							pstmt2.setString(9,request.getParameter("thispc_"+i));
							//System.out.println("thispc:" + request.getParameter("thispc_"+i));
							pstmt2.executeUpdate();
							
							
						}
						CallableStatement cstmt = myConnection.prepareCall("{call DBMS_MVIEW.REFRESH('MV_PMI_MIS_PRODUCT_DIM')}");
						try
						{
						cstmt.execute();
						}
						catch(SQLException se)
						{
							myConnection.rollback();
							cstmt.close();
							pstmt.close();
							pstmt2.close();
							myConnection.close();
							response.sendRedirect("ListMISPrd.scb?Page="+page+"&Search="+search+"&message=Refresh MV failed: "+se );
						}
						cstmt.close();	
					}
					catch(SQLException e)
					{
						myConnection.rollback();
						pstmt.close();
						pstmt2.close();
						myConnection.close();
						response.sendRedirect("ListMISPrd.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful "+thisid+" with error"+e);
					}
					myConnection.commit();
					pstmt.close();
					pstmt2.close();
					myConnection.close();
					response.sendRedirect("ListMISPrd.scb?Page="+page+"&Search="+search+"&message=Update Successful");
				}
				catch(Exception e)
				{
					System.out.println("ERROR : " + e);
					e.printStackTrace(System.out);
					response.sendRedirect("ListMISPrd.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful: "+e);
				}

				return null;
				//return new ActionForward(nextPage.getPath() + "?message=" + message); 
	}

}