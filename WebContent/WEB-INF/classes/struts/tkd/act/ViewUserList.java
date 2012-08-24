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
import struts.tkd.function.userSessionCheck;

public class ViewUserList extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		forward = mapping.findForward("forward");

		// Variables
		int rcount = 0;
		int pg_row_per_page = 15;
		int pg_current = 1;
		int pg_rownum_start;
		int pg_rownum_end;
		int pg_total_page;

		Result result;
		String qdata;
		String qcount;
		String par_search = request.getParameter("Search");

		// Create DB Connection
		javax.sql.DataSource dataSource;
		java.sql.Connection myConnection;
		Statement stmt;
		ResultSet rset = null;
		dataSource = getDataSource(request, "TKD_DIST_STG");
		myConnection = dataSource.getConnection();
		stmt = myConnection.createStatement();
		try {
			// Get Record Count
			if (par_search != null && !par_search.equals("")) {
				par_search = par_search.trim().toUpperCase();
				qcount = "SELECT COUNT(*) rcount FROM TKD_USER_LIST WHERE USER_NAME LIKE '%"
						+ par_search + "%' order by user_name";
			} else {
				qcount = "SELECT COUNT(*) rcount FROM TKD_USER_LIST order by user_name ";
			}
			rset = stmt.executeQuery(qcount);
			rset.next();
			rcount = rset.getInt("rcount");
			rset.close();

			// Get TotalPage
			pg_total_page = (int) Math.floor(rcount / pg_row_per_page) + 1;

			// Get All Record Result
			if (request.getParameter("Page") != null
					&& !request.getParameter("Page").equals("")) {
				try {
					pg_current = Integer.parseInt(request.getParameter("Page"));
				} catch (Exception e) {
					pg_current = 1;
				}
			}

			pg_rownum_start = pg_row_per_page * (pg_current - 1);
			pg_rownum_end = pg_row_per_page * (pg_current);

			if (par_search != null && !par_search.equals("")) {
				par_search = par_search.trim().toUpperCase();
				qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, USER_NAME, FULL_NAME, decode(IS_ADMIN,0,'NO',1,'YES') IS_ADMIN FROM TKD_USER_LIST WHERE USER_NAME LIKE '%"
						+ par_search
						+ "%') WHERE MYROWNUM > "
						+ pg_rownum_start + " AND MYROWNUM <= " + pg_rownum_end;

			} else {
				qdata = "SELECT * FROM (SELECT ROWNUM MYROWNUM, USER_NAME, FULL_NAME, decode(IS_ADMIN,0,'NO',1,'YES') IS_ADMIN FROM TKD_USER_LIST ) WHERE MYROWNUM > "
						+ pg_rownum_start + " AND MYROWNUM <= " + pg_rownum_end;
			}
			request.setAttribute("qdata", qdata);
			rset = stmt.executeQuery(qdata);
			result = ResultSupport.toResult(rset);

			rset.close();

			// Set Attribute for Page
			request.setAttribute("totalpage", pg_total_page);
			request.setAttribute("rcount", rcount);
			request.setAttribute("result", result);

		} catch (Exception e) {

			return new ActionForward(forward.getPath() + "?message="
					+ e.getMessage().trim());
		} finally {
			
			stmt.close();
			myConnection.close();
		}
		return forward;
	}
}
