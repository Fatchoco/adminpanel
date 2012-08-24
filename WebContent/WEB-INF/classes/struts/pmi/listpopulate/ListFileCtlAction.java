package struts.pmi.listpopulate;
import struts.pmi.validation.*;
import struts.pmi.form.process.*;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;

import org.apache.struts.upload.FormFile;
import java.util.*;
//levin17lacustre 25092009
public class ListFileCtlAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		ActionForward main=mapping.findForward("UploadMenu");
		int is_record=0;
		String partition="";
		String role="";
		
		//check Admin
		if(request.getSession().getAttribute("role") != null && request.getSession().getAttribute("role")!="" )
		{
			role = (String)request.getSession().getAttribute("role");

		}

		
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
		 Statement stmt = myConnection.createStatement();
		 
		 //check parameter partition
		 if((request.getParameter("Part") != null && request.getParameter("Part") != ""))
		{
			partition=request.getParameter("Part");
			is_record=1;	
		}
		 else
		 {
			 
			 try
			 {
				 ResultSet r1;	
				 r1 = stmt.executeQuery ("select current_partition from pmi_system_parameter");
				 if(r1.next())
				 {
				 	 partition=(r1.getString(1).trim()).substring(0,6);
				 	 is_record=1;
				 }
			 }
			 catch(SQLException e)
			 {
				 System.out.println(e);
			 }
		 }
		 
		 
		 
		 String query = "select  file_id,nvl(file_name,'-')as file_name,uploaded_record,rejected_record,nvl(to_char(create_date,'fm dd-mm-yyyy'),'-') as create_date from pmi_file_ctl where partition_key like '%"+partition+"%'";		
		 try
		 {
			ResultSet r2;
			r2 = stmt.executeQuery(query);
			int flagKorea=0;
			int flagEFX=0;
			int flagHC=0;

			while (r2.next())
			{    
				if(r2.getString(1).equals("KOREA_STATIC")){flagKorea=1;}
				else if(r2.getString(1).equals("EFX")){flagEFX=1;}
				else if(r2.getString(1).equals("HEAD_COUNT")){flagHC=1;}

			}
			request.setAttribute("flagKorea",flagKorea );
			request.setAttribute("flagEFX",flagEFX );
			request.setAttribute("flagHC",flagHC );
			
			ResultSet r3;
			r3=stmt.executeQuery(query);
			Result result = ResultSupport.toResult(r3);
			request.setAttribute("listResult",result );
		 }
		 
		 catch(SQLException e)
		 {
			 System.out.println(e);
		 }
		//close connection
		try
		{
			stmt.close();
			myConnection.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		Calendar cal= Calendar.getInstance();
		int month=cal.get(Calendar.MONTH)+1;
		int year=cal.get(Calendar.YEAR);
	 request.setAttribute("partition",partition);
	 request.setAttribute("month",month);
	 request.setAttribute("year",year);		
	 request.setAttribute("is_record",is_record);	

		//redirect
	 	
		return nextPage;
	}

}