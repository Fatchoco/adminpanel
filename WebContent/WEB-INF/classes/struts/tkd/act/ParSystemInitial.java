package struts.tkd.act;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.actfrm.ActionFormValidated;

public class ParSystemInitial extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception{
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			
			// Variables
			String current_period;
			Calendar cal;
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			Statement stmt	= myConnection.createStatement();
			ResultSet rset;
			
			rset = stmt.executeQuery("SELECT MAX(CURRENT_PERIOD) CURRENT_PERIOD FROM PAR_SYSTEM");
			
			// Get CURRENT_PERIOD
			if(rset.next())
			{
				current_period = rset.getString("CURRENT_PERIOD");
			}
			else
			{
				cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				
				current_period = sdf.format(cal.getTime());
			}
			
			// Close DB Connection
			rset.close();
			stmt.close();
			myConnection.close();
			
			// Get Current Year to populate dropdown
			cal = Calendar.getInstance();
			int i_year  = cal.get(Calendar.YEAR);
			
			// Set Variable for next page
			request.setAttribute("current_period", current_period);
			request.setAttribute("i_year", i_year);
			
			return forward;
	  }
}
