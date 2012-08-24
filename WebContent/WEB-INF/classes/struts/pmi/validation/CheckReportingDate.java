package struts.pmi.validation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

//levin17lacustre 29092009

public class CheckReportingDate extends Action {


  public ActionForward execute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception  {
		String role="";
		String [] roles=new String[0];
		if(request.getSession().getAttribute("role") != null && request.getSession().getAttribute("role")!="" )
		{
			role = (String)request.getSession().getAttribute("role");
			roles=role.split("_jsepj_");
		}
		int flag=0;
		
//		for(int i=0;i<roles.length;i++)
//		{
//			if(roles[i].equals("PMI")){
//				flag=1;}
//							
//		}
		//check whether user is 'role' or admin	
		
		flag = 1;
		
		
		if(flag!=1&&!(role.equals("ADM")))
		{
			ActionForward nextPage = null;
			nextPage = mapping.findForward("UploadMenu");
			return nextPage;

		}
		
		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		 Statement stmt = myConnection.createStatement();
		 ResultSet r;
		 
		 r = stmt.executeQuery ("select current_partition,is_manual,occ_current_partition,br_current_partition from pmi_system_parameter where param_id=1");
		 String pmi_part="";
		 int is_man=0;
		 String occ_part="";
		 String br_part="";
		 int is_record=0;
		 if(r.next())
		 {
		 	 pmi_part=(r.getString(1).trim()).substring(0,6);
		 	 is_man=r.getInt(2);
		 	 occ_part=(r.getString(3).trim()).substring(0,6);
		 	 br_part=(r.getString(4).trim()).substring(0,6);
		 	 is_record=1;
		 }
		 try
		 {
			if (r != null) r.close();
			if (stmt != null) stmt.close();
			if (myConnection != null && !myConnection.isClosed()) myConnection.close();
		 }
		 catch(SQLException e)
		 {
			 System.out.println(e);
		 }
			Calendar cal= Calendar.getInstance();
			int month=cal.get(Calendar.MONTH)+1;
			int year=cal.get(Calendar.YEAR);

		 request.setAttribute("pmi_part",pmi_part);
		 request.setAttribute("occ_part",occ_part);
		 request.setAttribute("br_part",br_part);
		 request.setAttribute("is_man",is_man);
		 request.setAttribute("is_record",is_record);

		 request.setAttribute("month",month);
		 request.setAttribute("year",year);
		ActionForward forward = null;
		forward = mapping.findForward("forward");
		return forward;
  } // End execute()


} // End class
