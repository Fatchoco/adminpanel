package struts.tkd.act;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.function.getPath;
import struts.tkd.actfrm.ActionFormValidated;

public class DeleteMappingMR extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {

		try
		{
			String period_param = request.getParameter("Period");
			String query = "TRUNCATE TABLE MST_SALESPERSON";
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			Statement				stmt;
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			
			stmt.executeUpdate(query);
						
			stmt.close();
			String deletecomment = "Delete Successfully!";
			request.setAttribute("deletecomment", deletecomment);
		}
		catch(Exception e)
		{
			throw new ServletException(e.getMessage());
		}
		return mapping.findForward("forward");
	  }
}
