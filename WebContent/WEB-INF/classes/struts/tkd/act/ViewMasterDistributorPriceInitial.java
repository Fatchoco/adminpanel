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

public class ViewMasterDistributorPriceInitial extends ActionFormValidated {
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
				int i_year;
				
				Result result;
				String qdata;
				String qcount;
				String qperiod;
				String current_period;
				String par_search1 = request.getParameter("Search1");
				String par_search2 = request.getParameter("Search2");
				String period_param = request.getParameter("Period");
				String active_period;
				
				// Create DB Connection
				javax.sql.DataSource	dataSource;
				java.sql.Connection		myConnection;
				Statement				stmt;
				ResultSet				rset;
				dataSource		= getDataSource(request,"TKD_DIST_STG");
				myConnection	= dataSource.getConnection();
				stmt			= myConnection.createStatement();
				
				qperiod = "SELECT CURRENT_PERIOD, YEAR FROM PAR_SYSTEM";
				rset = stmt.executeQuery(qperiod);
				rset.next();
				current_period = rset.getString("CURRENT_PERIOD");
				i_year = rset.getInt("YEAR");
				rset.close();
				
				active_period = current_period;
				if(period_param != null)
				{
					current_period = period_param;
				}
				else
				{
					current_period = current_period;
				}
				
				// Get Record Count
				if ((par_search1 != null && !par_search1.equals("")) || (par_search2 != null && !par_search2.equals("")))
				{
					par_search1 = par_search1.trim().toUpperCase();
					par_search2 = par_search2.trim().toUpperCase();
					qcount = "SELECT COUNT(*) rcount FROM MAP_T2_UNIT_PRICE WHERE DISTRIBUTOR_CD LIKE '%"+par_search1+"%' AND PRODUCT_CD LIKE '%"+par_search2+"%' AND PERIOD = '"+current_period+"'";
				}
				else
				{
					qcount = "SELECT COUNT(*) rcount FROM MAP_T2_UNIT_PRICE WHERE PERIOD = '"+current_period+"'";
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
				
				if ((par_search1 != null && !par_search1.equals("")) || (par_search2 != null && !par_search2.equals("")))
				{
					par_search1 = par_search1.trim().toUpperCase();
					par_search2 = par_search2.trim().toUpperCase();
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, PERIOD, DISTRIBUTOR_CD, PRODUCT_CD, T2_UNIT_PRICE FROM (SELECT * FROM MAP_T2_UNIT_PRICE ORDER BY DISTRIBUTOR_CD, PRODUCT_CD) WHERE DISTRIBUTOR_CD LIKE '%"+par_search1+"%' AND PRODUCT_CD LIKE '%"+par_search2+"%' AND PERIOD = '"+current_period+"') WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
				}
				else
				{
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, PERIOD, DISTRIBUTOR_CD, PRODUCT_CD, T2_UNIT_PRICE FROM (SELECT * FROM MAP_T2_UNIT_PRICE ORDER BY DISTRIBUTOR_CD, PRODUCT_CD) WHERE PERIOD = '"+current_period+"' ORDER BY DISTRIBUTOR_CD, PRODUCT_CD) WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
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
				request.setAttribute("active_period", active_period);
				
				return new ActionForward(forward.getPath()+"?Period="+current_period);
				//return forward;
			}
			catch(Exception e)
			{
				return new ActionForward(forward.getPath()+"?message="+e.getMessage().trim());
			}
	  }
}
