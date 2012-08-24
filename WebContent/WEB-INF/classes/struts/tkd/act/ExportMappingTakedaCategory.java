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

public class ExportMappingTakedaCategory extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {

		try
		{
			// Variables
			String FileName = "MappingTakedaCategory_Export.xls";
			//String expFileName = "MasterProductExport.txt";
			//String header = "PRODUCT CODE\tPRODUCT NAME (EN)\tPRODUCT NAME (CN)\tPRODUCT FAMILY CODE\tPRODUCT FAMILY NAME (EN)\tPRODUCT FAMILY NAME (CN)\tPRODUCT GROUP CODE\tPRODUCT GROUP NAME (EN)\tPRODUCT GROUP NAME (CN)\tUSAGE\tPACKAGING\tAVERAGE PRICE";
			String[] fileHeader = {"TAKEDA_CATEGORY_COD","TAKEDA_CATEGORY_DES","PACK_COD","PACK_DES"};
			String line;
			String par_search1 = request.getParameter("Search1");
			String period_param = request.getParameter("Period");
			String query = "SELECT TAKEDA_CATEGORY_COD,TAKEDA_CATEGORY_DES,PACK_COD,PACK_DES,COUNTRY_COD FROM MAP_TAKEDA_CATEGORY WHERE PACK_COD LIKE '%"+par_search1+"%' AND COUNTRY_COD = '"+period_param+"' ORDER BY TAKEDA_CATEGORY_COD, PACK_COD";
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			Statement				stmt;
			ResultSet				rset;
			dataSource		= getDataSource(request,"IMS_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			
			// Writing to file
			//String writeFilePath = request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+expFileName);
			String writeFilePath = getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/IMS"))+FileName;
			
			rset = stmt.executeQuery(query);
						
			HSSFWorkbook logWB = new HSSFWorkbook();
			HSSFSheet logSheet = logWB.createSheet();
			
			HSSFRow logRow = logSheet.createRow(0);
			for(int hi = 0; hi < fileHeader.length; hi++)
			{
				logRow.createCell(hi).setCellValue(fileHeader[hi]);
				/*if(hi==fileHeader.length-1)
					logRow.createCell(hi+1).setCellValue("FUNCTION");*/
			}
			
			int logRowIter = 1;
			while(rset.next())
			{
				//String[] arLine = printLine.split("\t");
				logRow = logSheet.createRow(logRowIter);
				logRow.createCell(0).setCellValue(rset.getString("TAKEDA_CATEGORY_COD"));
				logRow.createCell(1).setCellValue(rset.getString("TAKEDA_CATEGORY_DES"));
				logRow.createCell(2).setCellValue(rset.getString("PACK_COD"));
				logRow.createCell(3).setCellValue(rset.getString("PACK_DES"));
				logRowIter++;
			}
			FileOutputStream fileOut = new FileOutputStream(writeFilePath);
			logWB.write(fileOut);
			fileOut.close();
			
			rset.close();
			stmt.close();
			
			// File to download
			BufferedInputStream buf		= null;
			ServletOutputStream myOut	= null;
			
			try
			{

				myOut = response.getOutputStream();
				File myFile = new File(writeFilePath);
				
				if(!myFile.exists())
				{
					return new ActionForward(mapping.findForward("forward").getPath()+"?message=Log file not found");
				}
				
				response.setContentType("application/x-excel");
				response.addHeader("Content-Disposition", "attachment; filename="+FileName);
				response.setContentLength((int)myFile.length());
				
				FileInputStream input = new FileInputStream(myFile);
				buf	= new BufferedInputStream(input);
				int readBytes = 0;
				
				// Read from file write to output
				while((readBytes = buf.read()) != -1)
					myOut.write(readBytes);
			}
			catch(IOException ioe)
			{
				throw new ServletException(ioe.getMessage());
			}
			finally
			{
				if(myOut!=null)
					myOut.close();
				if(buf!=null)
					buf.close();
			}
		}
		catch(Exception e)
		{
			throw new ServletException(e.getMessage());
		}
		return null;
	  }
}
