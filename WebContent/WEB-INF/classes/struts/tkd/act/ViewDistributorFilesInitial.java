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

public class ViewDistributorFilesInitial extends ActionFormValidated {
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
				String par_search = request.getParameter("Search");
				String par_search2 = request.getParameter("Search2");
				
				// Create DB Connection
				javax.sql.DataSource	dataSource;
				java.sql.Connection		myConnection;
				Statement				stmt;
				ResultSet				rset;
				dataSource		= getDataSource(request,"TKD_DIST_STG");
				myConnection	= dataSource.getConnection();
				stmt			= myConnection.createStatement();

				//Get Current Period
				String current_period;
				if(request.getParameter("Period")==null || request.getParameter("Period").equals(""))
				{
					rset = stmt.executeQuery("SELECT MAX(CURRENT_PERIOD) CURRENT_PERIOD FROM PAR_SYSTEM");
					if(rset.next())
					{
						current_period = rset.getString("CURRENT_PERIOD");
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
				if (par_search != null && !par_search.equals(""))
				{
					par_search = par_search.trim().toUpperCase();
					qcount = "SELECT COUNT(*) rcount FROM(SELECT NVL(b.DISTRIBUTOR_CODE,'-') DISTRIBUTOR_CODE, a.FILE_NAME, a.REJECTED_COUNT RREJECT, a.ACCEPTED_COUNT RACCEPT, a.ACCEPTED_QTY RQTY FROM CTL_DATA_STG1 a LEFT OUTER JOIN MAP_FILE_COLUMN b ON a.FILE_NAME = b.FILE_NAME WHERE PERIOD = '"+current_period+"' AND b.DISTRIBUTOR_CODE LIKE '%"+par_search+"%' AND FLAG_ADJUSTMENT LIKE '%"+par_search2+"%' ORDER BY 2,3)";
				}
				else
				{
					qcount = "SELECT COUNT(*) rcount FROM(SELECT NVL(b.DISTRIBUTOR_CODE,'-') DISTRIBUTOR_CODE, a.FILE_NAME, a.REJECTED_COUNT RREJECT, a.ACCEPTED_COUNT RACCEPT, a.ACCEPTED_QTY RQTY FROM CTL_DATA_STG1 a LEFT OUTER JOIN MAP_FILE_COLUMN b ON a.FILE_NAME = b.FILE_NAME WHERE PERIOD = '"+current_period+"' AND FLAG_ADJUSTMENT LIKE '%"+par_search2+"%' ORDER BY 2,3)";
				}
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
				
				if (par_search != null && !par_search.equals(""))
				{
					par_search = par_search.trim().toUpperCase();
					qdata = "SELECT * FROM(SELECT ROWNUM MYROWNUM, DISTRIBUTOR_CODE, FILE_NAME, RACCEPT, RREJECT, RQTY, FLAG_ADJUSTMENT FROM(SELECT NVL(b.DISTRIBUTOR_CODE,'-') DISTRIBUTOR_CODE, a.FILE_NAME, a.REJECTED_COUNT RREJECT, a.ACCEPTED_COUNT RACCEPT, a.ACCEPTED_QTY RQTY, FLAG_ADJUSTMENT FROM CTL_DATA_STG1 a LEFT OUTER JOIN MAP_FILE_COLUMN b ON a.FILE_NAME = b.FILE_NAME WHERE PERIOD = '"+current_period+"' AND b.DISTRIBUTOR_CODE LIKE '%"+par_search+"%' AND FLAG_ADJUSTMENT LIKE '%"+par_search2+"%' ORDER BY 2,3)) WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
				}
				else
				{
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, DISTRIBUTOR_CODE, FILE_NAME, RACCEPT, RREJECT, RQTY, FLAG_ADJUSTMENT FROM(SELECT NVL(b.DISTRIBUTOR_CODE,'-') DISTRIBUTOR_CODE, a.FILE_NAME, a.REJECTED_COUNT RREJECT, a.ACCEPTED_COUNT RACCEPT, a.ACCEPTED_QTY RQTY, FLAG_ADJUSTMENT FROM CTL_DATA_STG1 a LEFT OUTER JOIN MAP_FILE_COLUMN b ON a.FILE_NAME = b.FILE_NAME WHERE PERIOD = '"+current_period+"' AND FLAG_ADJUSTMENT LIKE '%"+par_search2+"%' ORDER BY 2,3)) WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
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
				
				return forward;
			}
			catch(Exception e)
			{
				return new ActionForward(forward.getPath()+"?message="+e.getMessage().trim());
			}
	  }
}
