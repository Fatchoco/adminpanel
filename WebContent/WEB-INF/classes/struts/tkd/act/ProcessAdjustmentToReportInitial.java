package struts.tkd.act;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.actfrm.ActionFormValidated;

public class ProcessAdjustmentToReportInitial extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception{
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			
			String eq = request.getParameter("eq")==null?"":request.getParameter("eq");
			
			// Variables
			String current_period;
			Calendar cal;
			DateFormat formatter;
			Date date;
			
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
				formatter = new SimpleDateFormat("yyyyMMdd");
				date = (Date)formatter.parse(current_period+"01");
				cal = Calendar.getInstance();
				cal.setTime(date);
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
			//cal = Calendar.getInstance();
			int i_year  = cal.get(Calendar.YEAR);
			int i_month	= cal.get(Calendar.MONTH)+1; //Java Calendar Month starts from 0... @_@
			
			// Set Variable for next page
			request.setAttribute("current_period", current_period);
			request.setAttribute("i_year", i_year);
			request.setAttribute("i_month", i_month);
			
				//Get Background Process Error Message
				if(eq.equalsIgnoreCase("1"))
				{
					String bgermsg  = "";
					dataSource		= getDataSource(request,"TKD_DIST");
					myConnection	= dataSource.getConnection();
					stmt			= myConnection.createStatement();
					rset = stmt.executeQuery("SELECT * FROM RUN_PROCESS_STATUS");
					rset.next();
					bgermsg = rset.getString("ERROR_MSG");
					rset.close();
					stmt.close();
					myConnection.close();
					
					request.setAttribute("errormsg", "error");
					
					return new ActionForward(forward.getPath()+"?message="+bgermsg);
				}
			
			return forward;
	  }
}
