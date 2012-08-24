package struts.tkd.act;

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

import struts.tkd.actfrm.FormParSystem;
import struts.tkd.actfrm.ActionFormValidated;

public class DeleteDistributorFiles extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			
			try
			{
				// Get variable from Form
				FormParSystem myForm = (FormParSystem) form;
				String current_period = myForm.getCur_period();
				String par_search2 = request.getParameter("searchq2");
				
				if(!current_period.equals(null))
				{
					// Create DB Connection
					javax.sql.DataSource	dataSource;
					java.sql.Connection		myConnection;
					dataSource		= getDataSource(request,"TKD_DIST_STG");
					myConnection	= dataSource.getConnection();
					Statement stmt	= myConnection.createStatement();
					
					stmt.executeUpdate("DELETE DATA_STG0 WHERE PERIOD = '"+ current_period +"' AND FLAG_ADJUSTMENT LIKE '%"+par_search2+"%'");
					stmt.executeUpdate("DELETE DATA_STG1 WHERE PERIOD = '"+ current_period +"' AND FLAG_ADJUSTMENT LIKE '%"+par_search2+"%'");
					stmt.executeUpdate("DELETE CTL_DATA_STG1 WHERE PERIOD = '"+ current_period +"' AND FLAG_ADJUSTMENT LIKE '%"+par_search2+"%'");
	
					stmt.close();
					myConnection.close();
					
					if(par_search2.equalsIgnoreCase("Y"))
						return new ActionForward(forward.getPath()+"?message=Data Adjustment for period "+current_period+" has been deleted."+"&Period="+current_period+"&Search2="+par_search2);
					else if(par_search2.equalsIgnoreCase("N"))
						return new ActionForward(forward.getPath()+"?message=Data Sales for period "+current_period+" has been deleted."+"&Period="+current_period+"&Search2="+par_search2);
					else
						return new ActionForward(forward.getPath()+"?message=Data for period "+current_period+" has been deleted."+"&Period="+current_period+"&Search2="+par_search2);
				}
				else
				{
					return new ActionForward(forward.getPath()+"?message=Please select Period value");
				}
			}
			catch(Exception e)
			{
				return new ActionForward(forward.getPath()+"?message="+e.getMessage());
			}
	  }
}