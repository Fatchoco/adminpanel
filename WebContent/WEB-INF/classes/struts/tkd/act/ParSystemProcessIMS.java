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

public class ParSystemProcessIMS extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			
			try
			{
				// Get variable from Form
				FormParSystem myForm = (FormParSystem) form;
				String current_period = myForm.getCur_period();
				
				if(!current_period.equals(null))
				{
					String input_year = current_period.substring(0, 4);
					int input_month	  = Integer.parseInt(current_period.substring(4,6));
					
					current_period = current_period+"01";
					
					// Create DB Connection
					javax.sql.DataSource	dataSource;
					java.sql.Connection		myConnection;
					dataSource		= getDataSource(request,"IMS_STG");
					myConnection	= dataSource.getConnection();
					Statement stmt	= myConnection.createStatement();
					
					stmt.executeUpdate("UPDATE PAR_SYSTEM SET CURRENT_PERIOD = '"+ current_period +"'");
	
					stmt.close();
					myConnection.close();
					
					return new ActionForward(forward.getPath()+"?message=Current Period has been updated");
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