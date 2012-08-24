package struts.tkd.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.struts.upload.FormFile;

import struts.tkd.actfrm.FormUploadPage;
import struts.tkd.function.getPath;

import java.util.*;
import java.util.Date;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import struts.tkd.actfrm.ActionFormValidated;

public class UploadDistributorAdjustmentFilesInitial extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
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

			//Get Server Realpath
			String logName = "MasterProduct_RejectedRecord.xls";
			String filePath = getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/"));

			File myFile = new File(filePath+logName);
			if(myFile.exists())
			{
				request.setAttribute("logfile", 1);
			}
			
			request.setAttribute("current_period", current_period);
			request.setAttribute("i_year", i_year);
			request.setAttribute("i_month", i_month);
			
			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage().trim(), true);
		}
	}
}
