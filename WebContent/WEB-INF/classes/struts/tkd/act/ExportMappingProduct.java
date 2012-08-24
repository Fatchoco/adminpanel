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

public class ExportMappingProduct extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {

		try
		{
			// Variables
			String FileName = "MappingProductExport.xls";
			//String expFileName = "MasterProductExport.txt";
			//String header = "PRODUCT CODE\tPRODUCT NAME (EN)\tPRODUCT NAME (CN)\tPRODUCT FAMILY CODE\tPRODUCT FAMILY NAME (EN)\tPRODUCT FAMILY NAME (CN)\tPRODUCT GROUP CODE\tPRODUCT GROUP NAME (EN)\tPRODUCT GROUP NAME (CN)\tUSAGE\tPACKAGING\tAVERAGE PRICE";
			String[] fileHeader = {"DISTRIBUTOR CODE", "DISTRIBUTOR PRODUCT CODE", "DISTRIBUTOR PRODUCT NAME (CN)", "TAKEDA PRODUCT CODE", "TAKEDA PRODUCT NAME (CN)","FUNCTION"};
			String line;
			String query;
			String par_search = request.getParameter("Search");
			if (!par_search.equals(""))
				query = "SELECT DISTRIBUTOR_ID, DISTRIBUTOR_PRODUCT_CD, DISTRIBUTOR_PRODUCT_NM, PRODUCT_CD, PRODUCT_NM  FROM MAP_PRODUCT WHERE DISTRIBUTOR_ID like '%"+par_search+"%'";
			else
				query = "SELECT DISTRIBUTOR_ID, DISTRIBUTOR_PRODUCT_CD, DISTRIBUTOR_PRODUCT_NM, PRODUCT_CD, PRODUCT_NM  FROM MAP_PRODUCT";
				
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
				logRow.createCell(0).setCellValue(rset.getString("DISTRIBUTOR_ID"));
				logRow.createCell(1).setCellValue(rset.getString("DISTRIBUTOR_PRODUCT_CD"));
				logRow.createCell(2).setCellValue(rset.getString("DISTRIBUTOR_PRODUCT_NM"));
				logRow.createCell(3).setCellValue(rset.getString("PRODUCT_CD"));
				logRow.createCell(4).setCellValue(rset.getString("PRODUCT_NM"));
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
