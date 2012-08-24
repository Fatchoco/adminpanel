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

public class ViewMappingTakedaCategoryInitial extends ActionFormValidated {
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
				
				Result result;
				Result cn_list;
				String qdata;
				String qcount;
				String qperiod;
				String current_period;
				String par_search1 = request.getParameter("Search1");
				String period_param = request.getParameter("Period");
				String active_period;
				String country_des;
				
				// Create DB Connection
				javax.sql.DataSource	dataSource;
				java.sql.Connection		myConnection;
				Statement				stmt;
				ResultSet				rset;
				dataSource		= getDataSource(request,"IMS_STG");
				myConnection	= dataSource.getConnection();
				stmt			= myConnection.createStatement();

				qperiod = "SELECT * FROM COUNTRY";
				rset = stmt.executeQuery(qperiod);
				cn_list = ResultSupport.toResult(rset);
				rset.close();
				
				qperiod = "SELECT * FROM COUNTRY WHERE COUNTRY_DES = 'China'";
				rset = stmt.executeQuery(qperiod);
				rset.next();
				current_period = rset.getString("COUNTRY_COD");
				country_des	   = rset.getString("COUNTRY_DES");
				rset.close();
				
				active_period = current_period;
				if(period_param != null)
				{
					current_period = period_param;
				}
				
				// Get Record Count
				if ((par_search1 != null && !par_search1.equals("")))
				{
					par_search1 = par_search1.trim().toUpperCase();
					qcount = "SELECT COUNT(*) rcount FROM MAP_TAKEDA_CATEGORY WHERE PACK_COD LIKE '%"+par_search1+"%'AND COUNTRY_COD = '"+current_period+"'";
				}
				else
				{
					qcount = "SELECT COUNT(*) rcount FROM MAP_TAKEDA_CATEGORY WHERE COUNTRY_COD = '"+current_period+"'";
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
				
				if ((par_search1 != null && !par_search1.equals("")))
				{
					par_search1 = par_search1.trim().toUpperCase();
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, TAKEDA_CATEGORY_COD, TAKEDA_CATEGORY_DES, PACK_COD, PACK_DES FROM (SELECT * FROM MAP_TAKEDA_CATEGORY ORDER BY TAKEDA_CATEGORY_COD, PACK_COD) WHERE PACK_COD LIKE '%"+par_search1+"%' AND COUNTRY_COD = '"+current_period+"') WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
				}
				else
				{
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, TAKEDA_CATEGORY_COD, TAKEDA_CATEGORY_DES, PACK_COD, PACK_DES FROM (SELECT * FROM MAP_TAKEDA_CATEGORY ORDER BY TAKEDA_CATEGORY_COD, PACK_COD) WHERE COUNTRY_COD = '"+current_period+"') WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
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
				request.setAttribute("active_period", active_period);
				request.setAttribute("cn_result", cn_list);
				request.setAttribute("country_des", country_des);
				
				return new ActionForward(forward.getPath()+"?Period="+current_period);
				//return forward;
			}
			catch(Exception e)
			{
				return new ActionForward(forward.getPath()+"?message="+e.getMessage().trim());
			}
	  }
}