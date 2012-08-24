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


public class EditPmiSwitch extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//ActionForward nextPage = null;
		//nextPage = mapping.findForward("forward");
		String search="";

		String opMode = request.getParameter("m");
		String opModeStr = "";
		String strCompositeDate = "";
		boolean isValid = true;
		String msgValid = "";
		
		Calendar calCurr = GregorianCalendar.getInstance();
		
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

					if (opMode.equals("p")){
						opModeStr = "PMI";
					}else if (opMode.equals("t")){
						opModeStr = "TBMI";
					}else if (opMode.equals("b")){
						opModeStr = "BR";
					};
					
					String sqlUpdate="UPDATE PMI_COUNTRY_SWITCH SET SOURCE_CODE=?, EFFECTIVE_PERIOD=?, CREATE_DATE=SYSDATE, CREATE_USER='PMI_UPLOAD_UPDATE' " +
									" where TARGET_CODE = ? and COUNTRY_ID=?";
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
							//System.out.println("awal1-"+request.getParameter("lbl3_"+thisid));
							pstmt.setString(1,request.getParameter("srccode_"+thisid));
							
							//System.out.println("srccode-" + request.getParameter("srccode_"+thisid));
							if (request.getParameter("srccode_"+thisid).equals("BNCPR")) {

								//System.out.println("dua-"+request.getParameter("lbl3_"+thisid));
								if (request.getParameter("lbl3_"+thisid).equals("") || 
										request.getParameter("lbl2_"+thisid).equals("") ) 
								{
									//System.out.println("test1-"+request.getParameter("lbl3_"+thisid));
									isValid = false;
									msgValid = "Country code: " + thisid + " effective date must be filled";
									break;
								};
								
								//System.out.println("satu"+request.getParameter("lbl2_"+thisid));
								if (Integer.parseInt(request.getParameter("lbl2_"+thisid)) <= 1900 || Integer.parseInt(request.getParameter("lbl2_"+thisid)) >= 2200 ) {
									isValid = false;
									msgValid = "Country code: " + thisid + " has invalid effective date - year";
									break;
								} else if (Integer.parseInt(request.getParameter("lbl3_"+thisid)) <= 0 || Integer.parseInt(request.getParameter("lbl3_"+thisid)) > 12 ) {
									isValid = false;
									msgValid = "Country code: " + thisid + " has invalid effective date - month";
									break;
								};
								
								if (request.getParameter("lbl3_"+thisid).length() == 1) {
									strCompositeDate = request.getParameter("lbl2_"+thisid) + "0" + request.getParameter("lbl3_"+thisid);
								} else if (request.getParameter("lbl3_"+thisid).length() == 2) {
									strCompositeDate = request.getParameter("lbl2_"+thisid) + request.getParameter("lbl3_"+thisid);
								};
								
								calCurr.set(Integer.parseInt(strCompositeDate.substring(0,4)), Integer.parseInt(strCompositeDate.substring(4))-1, 1); // Months are 0 to 11
								pstmt.setString(2,strCompositeDate.trim()+ calCurr.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) +"0000");
							} else {
								pstmt.setString(2,"-");
							};
							pstmt.setString(3,opModeStr);
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
						response.sendRedirect("ListPmiSwitch.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful "+thisid+" with error"+e+"&m="+opMode);
					}
					if (isValid) {
						myConnection.commit();
						pstmt.close();
						myConnection.close();
						response.sendRedirect("ListPmiSwitch.scb?Page="+page+"&Search="+search+"&message=Update Successful"+"&m="+opMode);
					} else {
						myConnection.rollback();
						pstmt.close();
						myConnection.close();
						response.sendRedirect("EditPmiSwitch.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful with validation error: "+ msgValid +"&m="+opMode);
					}
				}
				catch(Exception e)
				{
					System.out.println("ERROR : " + e);
					e.printStackTrace(System.out);
					response.sendRedirect("ListPmiSwitch.scb?Page="+page+"&Search="+search+"&message=Update Unsuccessful: "+e+"&m="+opMode);
				}

				return null;
				//return new ActionForward(nextPage.getPath() + "?message=" + message); 
	}

}