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
import javax.sql.DataSource;

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

public class IMSProcess extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {
		try
		{
			String fcountry=request.getParameter("hdcntr2");
			String ncountry;
			String current_period;
			String current_periodo;
	    	String sfp=null;
	    	Calendar cal;
			
			//DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			
			dataSource		= getDataSource(request,"IMS_STG");
			myConnection	= dataSource.getConnection();
			Statement stmt	= myConnection.createStatement();
			ResultSet rset;
			ResultSet rset2;
			
			stmt.execute("TRUNCATE TABLE PRO_STATUS");
			stmt.execute("COMMIT");
			rset = stmt.executeQuery("SELECT COUNTRY_DES FROM COUNTRY WHERE COUNTRY_COD = '"+fcountry+"'");
			rset.next();
			ncountry=rset.getString("COUNTRY_DES");
			rset.close();
			rset2 = stmt.executeQuery("SELECT MAX(CURRENT_PERIOD) CURRENT_PERIOD, SOURCE_FILE_PATH FROM PAR_SYSTEM GROUP BY SOURCE_FILE_PATH");
			
			// Get CURRENT_PERIOD
			if(rset2.next())
			{
				current_periodo = rset2.getString("CURRENT_PERIOD");
				current_period = current_periodo.substring(0,6);
				sfp=rset2.getString("SOURCE_FILE_PATH");
			}
			else
			{
				cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				
				current_period = sdf.format(cal.getTime());
			}
			rset2.close();
			stmt.execute("INSERT INTO PRO_STATUS VALUES ('"+fcountry+"','"+ncountry.toUpperCase()+"','INITIAL','Validating Files','"+current_period+"','"+sfp+"',sysdate)");
			stmt.execute("COMMIT");
			stmt.close();
			myConnection.close();
			
			javax.sql.DataSource	summaryDataSource;
			javax.sql.DataSource	summaryDataSource2;
			summaryDataSource	= getDataSource(request, "IMS_STG");
			summaryDataSource2	= getDataSource(request, "IMS_RPT");
			String filePath = sfp;
			String usr=request.getSession().getAttribute("USER_NAME").toString();
			String zipFileSrc = filePath + "INPUTFILE_STG_SET_" + ncountry + "_" + current_period;
			String zipFileTrg = getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/IMS/")) + "extractDistFiles";
			UISummaryFuncIMS.insertLog(summaryDataSource, usr, current_period, fcountry, "Load IMS File", zipFileSrc);
			
			IMSProcess2 p = new IMSProcess2(143, summaryDataSource, summaryDataSource2, zipFileTrg, usr);
		    new Thread(p).start();

			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage().trim(), true);
		}
	  }
}
