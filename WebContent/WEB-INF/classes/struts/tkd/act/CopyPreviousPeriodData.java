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

import struts.tkd.function.UISummaryFunc;
import struts.tkd.function.getPath;
import struts.tkd.actfrm.ActionFormValidated;

public class CopyPreviousPeriodData extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {

			ActionForward forward = null;
		try
		{
			String module_name	= request.getParameter("m");
			String table_name	= "";
			String table_column	= "";
			String q_delete		= "";
			String q_insert		= "";
			
			if(module_name.equalsIgnoreCase("masterproduct"))
			{
				table_name	= "MST_PRODUCT";
				q_delete	= "DELETE MST_PRODUCT WHERE PERIOD = (SELECT MAX(CURRENT_PERIOD) FROM PAR_SYSTEM)";
				q_insert	= "INSERT INTO MST_PRODUCT(PERIOD, PRODUCT_CD,PRODUCT_NM,PRODUCT_FAMILY_CD,PRODUCT_FAMILY_NM,PRODUCT_GROUP_CD,PRODUCT_GROUP,USAGE,PACKAGING,AVG_UNIT_PRICE,PRODUCT_NM_CN,PRODUCT_FAMILY_NM_CN,PRODUCT_GROUP_CN) SELECT (SELECT MAX(CURRENT_PERIOD) FROM PAR_SYSTEM) PERIOD, PRODUCT_CD,PRODUCT_NM,PRODUCT_FAMILY_CD,PRODUCT_FAMILY_NM,PRODUCT_GROUP_CD,PRODUCT_GROUP,USAGE,PACKAGING,AVG_UNIT_PRICE,PRODUCT_NM_CN,PRODUCT_FAMILY_NM_CN,PRODUCT_GROUP_CN FROM MST_PRODUCT WHERE PERIOD = TO_CHAR(ADD_MONTHS(TO_DATE((SELECT MAX(CURRENT_PERIOD) FROM PAR_SYSTEM),'YYYYMM'),-1),'YYYYMM')";
				forward = mapping.findForward("masterproduct");
				
				/* ** Start of UI Summary ** */
				javax.sql.DataSource	summaryDataSource;
				summaryDataSource	= getDataSource(request, "TKD_DIST_STG");
				
				UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Upload File", "Master Product", "", "Copy from previous reporting period", request.getSession().getAttribute("USER_NAME").toString());
				/* ** End of UI Summary ** */
			}
			else if(module_name.equalsIgnoreCase("masterdistributorprice"))
			{
				table_name	= "MAP_T2_UNIT_PRICE";
				q_delete	= "DELETE MAP_T2_UNIT_PRICE WHERE PERIOD = (SELECT MAX(CURRENT_PERIOD) FROM PAR_SYSTEM)";
				q_insert	= "INSERT INTO MAP_T2_UNIT_PRICE(PERIOD,DISTRIBUTOR_CD,PRODUCT_CD,T2_UNIT_PRICE) SELECT (SELECT MAX(CURRENT_PERIOD) FROM PAR_SYSTEM) PERIOD,DISTRIBUTOR_CD,PRODUCT_CD,T2_UNIT_PRICE FROM MAP_T2_UNIT_PRICE WHERE PERIOD = TO_CHAR(ADD_MONTHS(TO_DATE((SELECT MAX(CURRENT_PERIOD) FROM PAR_SYSTEM),'YYYYMM'),-1),'YYYYMM')";
				forward = mapping.findForward("masterdistributorprice");
				
				/* ** Start of UI Summary ** */
				javax.sql.DataSource	summaryDataSource;
				summaryDataSource	= getDataSource(request, "TKD_DIST_STG");
				
				UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Upload File", "Master Distributor Unitprice", "", "Copy from previous reporting period", request.getSession().getAttribute("USER_NAME").toString());
				/* ** End of UI Summary ** */
			}
			else
			{
				forward = mapping.findForward("others");
				return forward;
			}
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			Statement				stmt;
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			
			stmt.executeUpdate(q_delete);
			stmt.executeUpdate(q_insert);
			stmt.close();
			
			String deletecomment = "Data has been copied.";
			request.setAttribute("deletecomment", deletecomment);
		}
		catch(Exception e)
		{
			throw new ServletException(e.getMessage());
		}
		return forward;
	  }
}
