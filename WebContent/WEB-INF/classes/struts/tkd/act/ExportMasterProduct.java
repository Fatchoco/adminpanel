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

public class ExportMasterProduct extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {

		try
		{
			// Variables
			String FileName = "MasterProductExport.xls";
			//String expFileName = "MasterProductExport.txt";
			//String header = "PRODUCT CODE\tPRODUCT NAME (EN)\tPRODUCT NAME (CN)\tPRODUCT FAMILY CODE\tPRODUCT FAMILY NAME (EN)\tPRODUCT FAMILY NAME (CN)\tPRODUCT GROUP CODE\tPRODUCT GROUP NAME (EN)\tPRODUCT GROUP NAME (CN)\tUSAGE\tPACKAGING\tAVERAGE PRICE";
			String[] fileHeader = {"PERIOD", "PRODUCT CODE", "PRODUCT NAME (EN)", "PRODUCT NAME (CN)", "PRODUCT FAMILY CODE", "PRODUCT FAMILY NAME (EN)", "PRODUCT FAMILY NAME (CN)", "PRODUCT GROUP CODE", "PRODUCT GROUP NAME (EN)", "PRODUCT GROUP NAME (CN)", "USAGE", "PACKAGING", "AVERAGE PRICE"};
			String line;
			String par_search = request.getParameter("Search");
			String period_param = request.getParameter("Period");
			String query = "SELECT PERIOD, PRODUCT_CD, PRODUCT_NM, PRODUCT_NM_CN, PRODUCT_FAMILY_CD, PRODUCT_FAMILY_NM, PRODUCT_FAMILY_NM_CN, PRODUCT_GROUP_CD, PRODUCT_GROUP, PRODUCT_GROUP_CN, USAGE, PACKAGING, AVG_UNIT_PRICE FROM MST_PRODUCT WHERE PRODUCT_CD LIKE '%"+par_search+"%' AND PERIOD = '"+period_param+"' ORDER BY PRODUCT_CD";
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			Statement				stmt;
			ResultSet				rset;
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			
			// Writing to file
			//String writeFilePath = request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+expFileName);
			String writeFilePath = getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/"))+FileName;
			//PrintWriter pw		 = new PrintWriter(writeFilePath);
			//FileOutputStream outstrm = new FileOutputStream(writeFilePath);
			//PrintWriter pw		 	 = new PrintWriter(new OutputStreamWriter(outstrm, "UTF-8"));
			//pw.println(header);
			
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
				logRow.createCell(0).setCellValue(rset.getString("PERIOD"));
				logRow.createCell(1).setCellValue(rset.getString("PRODUCT_CD"));
				logRow.createCell(2).setCellValue(rset.getString("PRODUCT_NM"));
				logRow.createCell(3).setCellValue(rset.getString("PRODUCT_NM_CN"));
				logRow.createCell(4).setCellValue(rset.getString("PRODUCT_FAMILY_CD"));
				logRow.createCell(5).setCellValue(rset.getString("PRODUCT_FAMILY_NM"));
				logRow.createCell(6).setCellValue(rset.getString("PRODUCT_FAMILY_NM_CN"));
				logRow.createCell(7).setCellValue(rset.getString("PRODUCT_GROUP_CD"));
				logRow.createCell(8).setCellValue(rset.getString("PRODUCT_GROUP"));
				logRow.createCell(9).setCellValue(rset.getString("PRODUCT_GROUP_CN"));
				logRow.createCell(10).setCellValue(rset.getString("USAGE"));
				logRow.createCell(11).setCellValue(rset.getString("PACKAGING"));
				logRow.createCell(12).setCellValue(rset.getFloat("AVG_UNIT_PRICE"));
				logRowIter++;
			}
			FileOutputStream fileOut = new FileOutputStream(writeFilePath);
			logWB.write(fileOut);
			fileOut.close();
			
			/*while(rset.next())
			{
				line = rset.getString("PRODUCT_CD")+"\t";
				line += rset.getString("PRODUCT_NM")+"\t";
				line += rset.getString("PRODUCT_NM_CN")+"\t";
				line += rset.getString("PRODUCT_FAMILY_CD")+"\t";
				line += rset.getString("PRODUCT_FAMILY_NM")+"\t";
				line += rset.getString("PRODUCT_FAMILY_NM_CN")+"\t";
				line += rset.getString("PRODUCT_GROUP_CD")+"\t";
				line += rset.getString("PRODUCT_GROUP")+"\t";
				line += rset.getString("PRODUCT_GROUP_CN")+"\t";
				line += rset.getString("USAGE")+"\t";
				line += rset.getString("PACKAGING")+"\t";
				line += rset.getString("AVG_UNIT_PRICE");
							
				pw.println(line);
			}
			pw.close();*/
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
			
			/*// File to download
			BufferedInputStream buf		= null;
			ServletOutputStream myOut	= null;
			
			try
			{
				myOut = response.getOutputStream();
				File myFile = new File(writeFilePath);
				
				response.setContentType("text/plain");
				response.addHeader("Content-Disposition", "attachment; filename="+expFileName);
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
			}*/
		}
		catch(Exception e)
		{
			throw new ServletException(e.getMessage());
		}
		return null;
	  }
}
