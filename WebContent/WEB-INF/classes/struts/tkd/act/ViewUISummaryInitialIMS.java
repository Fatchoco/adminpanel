package struts.tkd.act;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.actfrm.ActionFormValidated;

public class ViewUISummaryInitialIMS extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			
			try
			{
				// Variables
				int rcount = 0;
				int pg_row_per_page	= 20;
				int pg_current		= 1;
				int pg_rownum_start;
				int pg_rownum_end;
				int pg_total_page;
				Calendar cal;
				
				Result result;
				String qdata;
				String qcount;
				String par_search2 = request.getParameter("Search2");
				
				// Create DB Connection
				javax.sql.DataSource	dataSource;
				java.sql.Connection		myConnection;
				Statement				stmt;
				ResultSet				rset;
				ResultSet				rset2;
				dataSource		= getDataSource(request,"IMS_STG");
				myConnection	= dataSource.getConnection();
				stmt			= myConnection.createStatement();
				
				Result cn_result;
				rset2 = stmt.executeQuery("SELECT * FROM COUNTRY");
				cn_result = ResultSupport.toResult(rset2);
				rset2.close();

				//Get Current Period
				String current_period;
				String current_periodo;
				if(request.getParameter("Period")==null || request.getParameter("Period").equals(""))
				{
					rset = stmt.executeQuery("SELECT MAX(CURRENT_PERIOD) CURRENT_PERIOD FROM PAR_SYSTEM");
					if(rset.next())
					{
						current_periodo = rset.getString("CURRENT_PERIOD");
						current_period = current_periodo.substring(0,6);
					}
					else
					{
						rset.close();
						stmt.close();
						myConnection.close();
						throw new Exception("No CURRENT_PERIOD is found on table PAR_SYSTEM");
					}
					rset.close();
				}
				else
				{
					current_period = request.getParameter("Period");
				}
				
				// Get Current Year to populate dropdown
				cal = Calendar.getInstance();
				int i_year  = cal.get(Calendar.YEAR);
				
				// Get Record Count
					qcount = "SELECT COUNT(*) rcount FROM(SELECT CREATED_DATE, CREATED_USER, COUNTRY, FILENAME, PERIOD, ACTION, COUNT(*) COUNT FROM IMS_SUMMARY WHERE PERIOD = '"+current_period+"' GROUP BY CREATED_DATE, CREATED_USER, COUNTRY, FILENAME, PERIOD, ACTION)";
				rset = stmt.executeQuery(qcount);
				rset.next();
				rcount = rset.getInt("rcount");
				rset.close();
				
				// Get TotalPage
				if(rcount != 0 && rcount % pg_row_per_page == 0)
				{
					pg_total_page = (int)Math.floor(rcount/pg_row_per_page);
				}
				else
				{
					pg_total_page = (int)Math.floor(rcount/pg_row_per_page)+1;
				}
				
				// Get All Record Result
				if (request.getParameter("Page") != null && !request.getParameter("Page").equals(""))
				{
					try
					{
						pg_current = Integer.parseInt(request.getParameter("Page"));
					}
					catch(Exception e)
					{
						pg_current = 1;
					}
				}
				
				pg_rownum_start = pg_row_per_page * (pg_current - 1);
				pg_rownum_end = pg_row_per_page * (pg_current);
				if (par_search2 != null)
				{
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, z.* FROM (SELECT * FROM IMS_SUMMARY a WHERE PERIOD = '"+current_period+"' AND COUNTRY='"+par_search2+"' ORDER BY CREATED_DATE DESC, PERIOD DESC, ACTION) z) WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
				}
				else
				{
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, z.* FROM (SELECT * FROM IMS_SUMMARY a WHERE PERIOD = '"+current_period+"' AND COUNTRY='CN' ORDER BY CREATED_DATE DESC, PERIOD DESC, ACTION) z) WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
				}
				request.setAttribute("qdata", qdata);
				rset = stmt.executeQuery(qdata);
				result = ResultSupport.toResult(rset);
				
				rset.close();
				stmt.close();
				myConnection.close();
				
				// Set Attribute for Page
				request.setAttribute("totalpage", pg_total_page);
				request.setAttribute("rcount", rcount);
				request.setAttribute("result", result);
				request.setAttribute("current_period", current_period);
				request.setAttribute("i_year", i_year);
				request.setAttribute("cn_result", cn_result);
				request.setAttribute("par_search2", par_search2);
				
				return forward;
			}
			catch(Exception e)
			{
				return new ActionForward(forward.getPath()+"?message="+e.getMessage().trim());
			}
	  }
}
