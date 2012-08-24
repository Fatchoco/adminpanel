package struts.tkd.act;


import java.io.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

import struts.tkd.function.FileListing;
import struts.tkd.function.UISummaryFuncIMS;
import struts.tkd.function.getPath;
import struts.tkd.actfrm.ActionFormValidated;

public class IMSProcess4 extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {
		try
		{
			String[] job=null;
			String status=null;
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			dataSource		= getDataSource(request,"IMS_STG");
			myConnection	= dataSource.getConnection();
			Statement stmt	= myConnection.createStatement();
			ResultSet rset;
			
			rset = stmt.executeQuery("SELECT JOB FROM PRO_STATUS");
			int i;
			// Get CURRENT_PERIOD
			if(rset.next()){
			for(i=0;rset.next();i++)
				job[i] = rset.getString("JOB");
			
			for(int j=0;j<i;j++)
				status=status+","+job[i];
			}
			// Close DB Connection
			rset.close();
			stmt.close();
			myConnection.close();

		    request.setAttribute("state", status);
			
			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage().trim(), true);
		}
	  }
}
