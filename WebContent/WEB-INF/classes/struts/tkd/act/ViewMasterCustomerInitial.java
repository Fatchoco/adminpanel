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

public class ViewMasterCustomerInitial extends ActionFormValidated {
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
				String par_search = request.getParameter("Search");
				
				// Create DB Connection
				javax.sql.DataSource	dataSource;
				java.sql.Connection		myConnection;
				Statement				stmt;
				ResultSet				rset;
				dataSource		= getDataSource(request,"TKD_DIST_STG");
				myConnection	= dataSource.getConnection();
				stmt			= myConnection.createStatement();
				
				// Get Record Count
				if (par_search != null && !par_search.equals(""))
				{
					par_search = par_search.trim().toUpperCase();
					qcount = "SELECT COUNT(*) rcount FROM MST_CUSTOMER WHERE CUSTOMER_CD LIKE '%"+par_search+"%'";
				}
				else
				{
					qcount = "SELECT COUNT(*) rcount FROM MST_CUSTOMER";
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
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, CUSTOMER_CD, CUSTOMER_NM, CUSTOMER_NM_CN, CUSTOMER_TYPE, CUSTOMER_GROUP, CUSTOMER_ADDRESS, CUSTOMER_ADDRESS_CN, CUSTOMER_POSTAL_CD, CUSTOMER_POSTAL_CD_CN, CITY_CD, END_USER, CUSTOMER_GRADE FROM (SELECT * FROM MST_CUSTOMER ORDER BY CUSTOMER_CD) WHERE CUSTOMER_CD LIKE '%"+par_search+"%') WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
				}
				else
				{
					qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, CUSTOMER_CD, CUSTOMER_NM, CUSTOMER_NM_CN, CUSTOMER_TYPE, CUSTOMER_GROUP, CUSTOMER_ADDRESS, CUSTOMER_ADDRESS_CN, CUSTOMER_POSTAL_CD, CUSTOMER_POSTAL_CD_CN, CITY_CD, END_USER, CUSTOMER_GRADE FROM (SELECT * FROM MST_CUSTOMER ORDER BY CUSTOMER_CD)) WHERE MYROWNUM > "+pg_rownum_start+" AND MYROWNUM <= "+pg_rownum_end;
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
				
				//return new ActionForward(forward.getPath()+"?Period="+current_period);
				return forward;
			}
			catch(Exception e)
			{
				return new ActionForward(forward.getPath()+"?message="+e.getMessage().trim());
			}
	  }
}
