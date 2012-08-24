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

public class ViewMappingExfactoryTargetInitial extends ActionFormValidated {
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
				String from_period;
				String to_period;
				String par_search = request.getParameter("Search");
				String period_param_1 = request.getParameter("Period1");
				String period_param_2 = request.getParameter("Period2");
				
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
				
				if(period_param_1 == null || period_param_2 == null)
				{
					from_period = current_period;
					to_period = current_period;
				}
				else
				{
					from_period = period_param_1;
					to_period = period_param_2;
				}
				
				// Get Record Count
				if ((par_search != null && !par_search.equals("")))
				{
					par_search = par_search.trim().toUpperCase();
					qcount = "SELECT COUNT(*) rcount FROM MAP_EX_FACTORY_TARGET WHERE PRODUCT_CD LIKE '%"+par_search+"%' AND YEAR||LPAD(MONTH,2,'0') BETWEEN '"+from_period+"' AND '"+to_period+"'";
				}
				else
				{
					qcount = "SELECT COUNT(*) rcount FROM MAP_EX_FACTORY_TARGET WHERE YEAR||LPAD(MONTH,2,'0') BETWEEN '"+from_period+"' AND '"+to_period+"'";
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
				
				if ((par_search != null && !par_search.equals("")))
				{
					par_search = par_search.trim().toUpperCase();
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, YEAR,MONTH,PRODUCT_CD,PRODUCT_NM_CN,PRODUCT_NM,PACK_SIZE,PRODUCT_FAMILY,BRAND_NM,PRODUCT_CATEGORY,QUANTITY,NET_SALES FROM MAP_EX_FACTORY_TARGET WHERE PRODUCT_CD LIKE '%"+par_search+"%' AND YEAR||LPAD(MONTH,2,'0') BETWEEN '"+from_period+"' AND '"+to_period+"') WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
				}
				else
				{
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, YEAR,MONTH,PRODUCT_CD,PRODUCT_NM_CN,PRODUCT_NM,PACK_SIZE,PRODUCT_FAMILY,BRAND_NM,PRODUCT_CATEGORY,QUANTITY,NET_SALES FROM MAP_EX_FACTORY_TARGET WHERE YEAR||LPAD(MONTH,2,'0') BETWEEN '"+from_period+"' AND '"+to_period+"') WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
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
				request.setAttribute("from_period", from_period);
				request.setAttribute("to_period", to_period);
				
				return new ActionForward(forward.getPath()+"?Period1="+from_period+"&Period2="+to_period);
				//return forward;
			}
			catch(Exception e)
			{
				return new ActionForward(forward.getPath()+"?message="+e.getMessage().trim());
			}
	  }
}
