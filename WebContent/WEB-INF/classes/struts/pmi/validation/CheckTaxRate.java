package struts.pmi.validation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class CheckTaxRate extends Action {


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
			if(roles[i].equals("PMI")){
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
		 
		 r = stmt.executeQuery ("select tax_rate from pmi_calc_parameter where rownum = 1");
		 BigDecimal stax_rate = new BigDecimal(0.0);
		 if(r.next())
		 {
			 stax_rate=r.getBigDecimal(1);
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

		 request.setAttribute("tax_rate",stax_rate);

		 ActionForward forward = null;
		forward = mapping.findForward("forward");
		return forward;
  } // End execute()


} // End class
