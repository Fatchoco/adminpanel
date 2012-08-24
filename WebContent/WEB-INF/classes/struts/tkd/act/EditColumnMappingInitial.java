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
import java.text.SimpleDateFormat;

import org.apache.struts.upload.FormFile;

import struts.tkd.actfrm.ActionFormValidated;
import struts.tkd.actfrm.FormUploadPage;
import struts.tkd.function.getPath;

import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EditColumnMappingInitial extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			String fileName = request.getParameter("EditFile");
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			Statement stmt	= myConnection.createStatement();
			ResultSet rset;
			Result result;
			Result dataresult;
			
			rset = stmt.executeQuery("SELECT DISTRIBUTOR_CD FROM MST_DISTRIBUTOR ORDER BY 1");
			result = ResultSupport.toResult(rset);
			rset.close();
			
			rset = stmt.executeQuery("SELECT * FROM MAP_FILE_COLUMN WHERE FILE_NAME = '"+fileName+"'");
			dataresult = ResultSupport.toResult(rset);
			rset.close();
			
			stmt.close();
			myConnection.close();
			
			request.setAttribute("items_distributorcode", result);
			request.setAttribute("items_data", dataresult);
			
			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage().trim(), true);
		}
	}
}
