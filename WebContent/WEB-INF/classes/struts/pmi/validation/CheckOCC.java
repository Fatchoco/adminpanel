package struts.pmi.validation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import java.sql.*;
import java.util.*;
//levin17lacustre 29092009

public class CheckOCC extends Action {


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
		
		for(int i=0;i<roles.length;i++)
		{
			if(roles[i].equals("OCC")){
				flag=1;}
							
		}
		//check whether user is 'role' or admin	

		
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
		 r = stmt.executeQuery ("select occ_current_partition from pmi_system_parameter");
		 String partition="";
		 if(r.next())
		 {
		 	 partition=(r.getString(1).trim()).substring(0,6);
			
		 }
		 try
		 {
			 r.close();
			 stmt.close();
			 myConnection.close();
		 }
		 catch(SQLException e)
		 {
			 System.out.println(e);
		 }
			Calendar cal= Calendar.getInstance();
			int month=cal.get(Calendar.MONTH)+1;
			int year=cal.get(Calendar.YEAR);
		 request.setAttribute("partition",partition);
		 request.setAttribute("month",partition.substring(4,6));
		 request.setAttribute("year",partition.substring(0,4));
		 
	
		ActionForward forward = null;
		forward = mapping.findForward("forward");
		return forward;
  } // End execute()


} // End class
