package struts.pmi.editlist;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditExRate extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//ActionForward nextPage = null;
		//nextPage = mapping.findForward("forward");
		String search="";
		boolean isValid = true;
		String msgValid = "";
	

		Calendar calCurr = GregorianCalendar.getInstance();
		String strCompositeDate = "";
		
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
					String sqlUpdate="UPDATE PMI_EXCH_RATE_BOOK_COMPANY SET COUNTRY_ID = ?, COMPANY_CODE = ?, EFFECTIVE_PERIOD = ? ," +
									" CREATE_DATE=SYSDATE,CREATE_USER='PMI_UPLOAD_UPDATE' " +
									" where id = ?";
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
							
							pstmt.setString(1,request.getParameter("dd_"+thisid));
							
							pstmt.setString(2,request.getParameter("lbl2_"+thisid));

							//System.out.println("dua-"+request.getParameter("lbl3_"+thisid));
							if (request.getParameter("lbl3_"+thisid).equals("") || 
									request.getParameter("lbl4_"+thisid).equals("") ) 
							{
								//System.out.println("test1-"+request.getParameter("lbl3_"+thisid));
								isValid = false;
								msgValid = "Country code: " + request.getParameter("dd_"+thisid) + " effective date must be filled";
								break;
							};
							
							//System.out.println("satu"+request.getParameter("lbl2_"+thisid));
							if (Integer.parseInt(request.getParameter("lbl3_"+thisid)) <= 1900 || Integer.parseInt(request.getParameter("lbl3_"+thisid)) >= 2200 ) {
								isValid = false;
								msgValid = "Country code: " + request.getParameter("dd_"+thisid) + " has invalid effective date - year";
								break;
							} else if (Integer.parseInt(request.getParameter("lbl4_"+thisid)) <= 0 || Integer.parseInt(request.getParameter("lbl4_"+thisid)) > 12 ) {
								isValid = false;
								msgValid = "Country code: " + request.getParameter("dd_"+thisid) + " has invalid effective date - month";
								break;
							};
							
							if (request.getParameter("lbl4_"+thisid).length() == 1) {
								strCompositeDate = request.getParameter("lbl3_"+thisid) + "0" + request.getParameter("lbl4_"+thisid);
							} else if (request.getParameter("lbl4_"+thisid).length() == 2) {
								strCompositeDate = request.getParameter("lbl3_"+thisid) + request.getParameter("lbl4_"+thisid);
							};
							calCurr.set(Integer.parseInt(strCompositeDate.substring(0,4)), Integer.parseInt(strCompositeDate.substring(4))-1, 1); // Months are 0 to 11
							pstmt.setString(3,strCompositeDate.trim()+ calCurr.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) +"0000");
							
							pstmt.setString(4,thisid);
							
							if (isValid) {
								pstmt.executeUpdate();
								isValid = true ;
							};
						}
						
					}
					catch(SQLException e)
					{
						myConnection.rollback();
						pstmt.close();
						myConnection.close();
						response.sendRedirect("ListExRate.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful "+thisid+" with error"+e);
					}
					
					if (isValid) {
						myConnection.commit();
						pstmt.close();
						myConnection.close();
						response.sendRedirect("ListExRate.scb?Page="+page+"&Search="+search+"&message=Update Successful");
					} else {
						myConnection.rollback();
						pstmt.close();
						myConnection.close();
						response.sendRedirect("EditExRate.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful with validation error: "+ msgValid );
					}
					
				}
				catch(Exception e)
				{
					System.out.println("ERROR : " + e);
					e.printStackTrace(System.out);
					response.sendRedirect("ListExRate.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful: "+e);
				}

				return null;
				//return new ActionForward(nextPage.getPath() + "?message=" + message); 
	}

}