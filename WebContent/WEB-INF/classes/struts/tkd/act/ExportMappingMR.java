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

public class ExportMappingMR extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {

		try
		{
			// Variables
			String FileName = "MappingMRExport.xls";
			//String expFileName = "MasterProductExport.txt";
			//String header = "PRODUCT CODE\tPRODUCT NAME (EN)\tPRODUCT NAME (CN)\tPRODUCT FAMILY CODE\tPRODUCT FAMILY NAME (EN)\tPRODUCT FAMILY NAME (CN)\tPRODUCT GROUP CODE\tPRODUCT GROUP NAME (EN)\tPRODUCT GROUP NAME (CN)\tUSAGE\tPACKAGING\tAVERAGE PRICE";
			String[] fileHeader = {"Hospital ID","Product ID","MR ID","MR Employee ID","MR Name (EN)","MR Name (CN)","DSM ID","DSM Employee ID","DSM NAME (EN)","DSM NAME (CN)","RSM ID","RSM Employee ID","RSM NAME (EN)","RSM NAME (CN)","RSD ID","RSD Employee ID","RSD NAME (EN)","RSD NAME (CN)","Region ID","Region Name (EN)","Region Name (CN)","FUNCTION"};
			String line;
			String par_search = request.getParameter("Search");
			String par_search2 = request.getParameter("Search2");
			String period_param = request.getParameter("Period");
			//String query = "SELECT PERIOD,PRODUCT_CD,PRODUCT_NM,HOSPITAL_CD,HOSPITAL_NM,CITY_CD,CITY_NM,PROVINCE_CD,PROVINCE_NM,REGION_CD,REGION_NM,TARGET_QUANTITY FROM MAP_HOSPITAL_TARGET WHERE PRODUCT_CD LIKE '%"+par_search+"%' AND PERIOD = '"+period_param+"'";
			String query = "SELECT HOSPITAL_CD,PRODUCT_CD,MR_CD,MR_EMPLOYEE_CD,MR_NM,MR_NM_CN,DSM_CD,DSM_EMPLOYEE_CD,DSM_NM,DSM_NM_CN,RSM_CD,RSM_EMPLOYEE_CD,RSM_NM,RSM_NM_CN,RSD_CD,RSD_EMPLOYEE_CD,RSD_NM,RSD_NM_CN,REGION_CD,REGION_NM,REGION_NM_CN FROM MST_SALESPERSON WHERE PRODUCT_CD LIKE '%"+par_search+"%' AND HOSPITAL_CD LIKE '%"+par_search2+"%'";
			
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
				logRow.createCell(0).setCellValue(rset.getString("HOSPITAL_CD"));
				logRow.createCell(1).setCellValue(rset.getString("PRODUCT_CD"));
				logRow.createCell(2).setCellValue(rset.getString("MR_CD"));
				logRow.createCell(3).setCellValue(rset.getString("MR_EMPLOYEE_CD"));
				logRow.createCell(4).setCellValue(rset.getString("MR_NM"));
				logRow.createCell(5).setCellValue(rset.getString("MR_NM_CN"));
				logRow.createCell(6).setCellValue(rset.getString("DSM_CD"));
				logRow.createCell(7).setCellValue(rset.getString("DSM_EMPLOYEE_CD"));
				logRow.createCell(8).setCellValue(rset.getString("DSM_NM"));
				logRow.createCell(9).setCellValue(rset.getString("DSM_NM_CN"));
				logRow.createCell(10).setCellValue(rset.getString("RSM_CD"));
				logRow.createCell(11).setCellValue(rset.getString("RSM_EMPLOYEE_CD"));
				logRow.createCell(12).setCellValue(rset.getString("RSM_NM"));
				logRow.createCell(13).setCellValue(rset.getString("RSM_NM_CN"));
				logRow.createCell(14).setCellValue(rset.getString("RSD_CD"));
				logRow.createCell(15).setCellValue(rset.getString("RSD_EMPLOYEE_CD"));
				logRow.createCell(16).setCellValue(rset.getString("RSD_NM"));
				logRow.createCell(17).setCellValue(rset.getString("RSD_NM_CN"));
				logRow.createCell(18).setCellValue(rset.getString("REGION_CD"));
				logRow.createCell(19).setCellValue(rset.getString("REGION_NM"));
				logRow.createCell(20).setCellValue(rset.getString("REGION_NM_CN"));
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
