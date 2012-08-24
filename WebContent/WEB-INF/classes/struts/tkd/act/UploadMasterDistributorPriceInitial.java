package struts.tkd.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.io.*;
import java.sql.*;

import org.apache.struts.upload.FormFile;

import struts.tkd.actfrm.FormUploadPage;
import struts.tkd.function.getPath;

import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import struts.tkd.actfrm.ActionFormValidated;

public class UploadMasterDistributorPriceInitial extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			//Get Server Realpath
			String logName = "MasterDistributorPrice_RejectedRecord.xls";
			String filePath = getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/"));

			File myFile = new File(filePath+logName);
			if(myFile.exists())
			{
				request.setAttribute("logfile", 1);
			}
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			Statement				stmt;
			ResultSet				rset;
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			
			String qdata;
			String result;
			
			qdata = "SELECT trim(to_char(to_date(MAX(CURRENT_PERIOD),'RRRRMM'),'Month')) || ' ' || trim(to_char(to_date(MAX(CURRENT_PERIOD),'RRRRMM'),'YYYY'))  CURRENT_PERIOD FROM PAR_SYSTEM";
			//request.setAttribute("qdata", qdata);
			rset = stmt.executeQuery(qdata);
			rset.next();
			result = rset.getString("CURRENT_PERIOD");
			
			
			rset.close();
			stmt.close();
			myConnection.close();
			
			request.setAttribute("result", result);
			
			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage().trim(), true);
		}
	}
}
