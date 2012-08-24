package struts.pmi.form;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;
import java.util.*;

public class ReportingDateAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		
		//check login stat
		if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != ""))
		{
			ActionForward login=mapping.findForward("loginpage");
			return login;
			
		}	
		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		 ResultSet r;
		 Statement stmt=myConnection.createStatement();
		 
		//get pass
		ReportingDateForm myForm=(ReportingDateForm) form;
		String PRD=myForm.getPrd();
		String ORD=myForm.getOrd();
		String BRD=myForm.getBrd();
		String MR=myForm.getMr();

		if(!(PRD.equals(null))&&!(MR.equals(null))&&!(ORD.equals(null))&&!(BRD.equals(null)))
		{
			int month1;
			int year1;
			int lastDate;
			Calendar cal= Calendar.getInstance();
			month1=Integer.parseInt(PRD.substring(4,6));
			year1=Integer.parseInt(PRD.substring(0,4));
			cal.set(Calendar.MONTH, month1-1);
			cal.set(Calendar.YEAR, year1);
			cal.set(Calendar.DATE,1);									
			lastDate = cal.getActualMaximum(cal.DATE);
			PRD=PRD+lastDate+"0000";
			month1=Integer.parseInt(ORD.substring(4,6));
			year1=Integer.parseInt(ORD.substring(0,4));
			cal.set(Calendar.MONTH, month1-1);
			cal.set(Calendar.YEAR, year1);	
			cal.set(Calendar.DATE,1);								
			lastDate = cal.getActualMaximum(cal.DATE);
			ORD=ORD+lastDate+"0000";
			month1=Integer.parseInt(BRD.substring(4,6));
			year1=Integer.parseInt(BRD.substring(0,4));
			cal.set(Calendar.MONTH, month1-1);
			cal.set(Calendar.YEAR, year1);	
			cal.set(Calendar.DATE,1);								
			lastDate = cal.getActualMaximum(cal.DATE);
			BRD=BRD+lastDate+"0000";

			r = stmt.executeQuery ("select count(*) from pmi_system_parameter where param_id=1");
			r.next();
			
			try {
			
			
			if(r.getInt(1)==0)
			{
				String sql="insert into pmi_system_parameter(param_id,current_partition,is_manual,occ_current_partition,br_current_partition,create_user) values(?,?,?,?,?,?)";
				PreparedStatement pstmt =myConnection.prepareStatement(sql);	

				pstmt.setInt(1,1);
				pstmt.setString(2,PRD);
				pstmt.setInt(3,Integer.parseInt(MR));
				pstmt.setString(4,ORD);
				pstmt.setString(5,BRD);
				
				pstmt.setString(6,(String)request.getSession().getAttribute("username"));
				pstmt.executeUpdate();


			}
			else
			{
				String sql="update pmi_system_parameter set current_partition=?,is_manual=?,occ_current_partition=?,br_current_partition=?,create_user=? where param_id=1";
				PreparedStatement pstmt =myConnection.prepareStatement(sql);	

				pstmt.setString(1,PRD);
				pstmt.setInt(2,Integer.parseInt(MR));
				pstmt.setString(3,ORD);
				pstmt.setString(4,BRD);
				
				pstmt.setString(5,(String)request.getSession().getAttribute("username"));
				pstmt.executeUpdate();
				
			}
			
			} catch (SQLException se) {
				System.out.println(se);
			} finally {
				if (r != null) r.close();
				if (stmt != null) stmt.close();
				if (myConnection != null && !myConnection.isClosed()) myConnection.close();
			}
			

			return new ActionForward(nextPage.getPath()+"?message=Reporting Date Successfully Updated");
						
		}
		else
		{
			return new ActionForward(nextPage.getPath()+"?message=Please select the value");
		}	


	}

}