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

public class ParSystemProcess extends ActionFormValidated {
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
					String input_fiscalyear;
					
					// Create DB Connection
					javax.sql.DataSource	dataSource;
					java.sql.Connection		myConnection;
					dataSource		= getDataSource(request,"TKD_DIST");
					myConnection	= dataSource.getConnection();
					Statement stmt	= myConnection.createStatement();
					
					ResultSet rset	= stmt.executeQuery("SELECT DISTINCT FISCAL_YEAR FROM DIM_TIME WHERE DATES = TO_DATE('"+current_period+"'||'01','YYYYMMDD')");
					rset.next();
					input_fiscalyear = rset.getString(1);
					rset.close();
					stmt.close();
					myConnection.close();
					
					// Create DB Connection
					dataSource		= getDataSource(request,"TKD_DIST_STG");
					myConnection	= dataSource.getConnection();
					stmt	= myConnection.createStatement();
					
					stmt.executeUpdate("UPDATE PAR_SYSTEM SET CURRENT_PERIOD = '"+ current_period +"', MONTH = "+input_month+", YEAR = '"+input_year+"', FISCAL_YEAR = '"+input_fiscalyear+"'");
	
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