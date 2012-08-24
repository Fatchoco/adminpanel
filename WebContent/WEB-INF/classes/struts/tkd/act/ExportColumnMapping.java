package struts.tkd.act;

import java.io.*;
import java.sql.ResultSet;
import java.sql.Statement;

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

public class ExportColumnMapping extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {

		try
		{
			// Variables
			String FileName = "DistributorFileSetupExport.xls";
			String[] fileHeader = {"Dist Code","File Name","Dist Code in Consolidated File","Trans Date","Product Code","Product Name","Specification","Lot / Batch No","Unit","Invoice No","Expiry Date","Customer Code","Customer Name","Customer City","Customer Province","Qty","Total Sales","Trans Date Format","Expiry Date Format","Function"};
			String line;
			String par_search = request.getParameter("Search");
			String query;
			//String current_period = request.getParameter("Period");
			if(par_search==null || par_search.equals(""))
			{
				query = "SELECT ROWNUM MYROWNUM, DISTRIBUTOR_CODE, FILE_NAME, DISTRIBUTOR_CODE_INFILE, TRANSACTION_DATE, PRODUCT_CODE, PRODUCT_NAME, SPECIFICATION_NAME, CUSTOMER_CODE, CUSTOMER_NAME, CUSTOMER_CITY, CUSTOMER_PROVINCE, LOT_NUMBER, UNIT_NAME, EXPIRY_DATE, INVOICE_NUMBER, QUANTITY, TOTAL_SALES, TRANSACTION_DATE_FMT, EXPIRY_DATE_FMT FROM MAP_FILE_COLUMN ORDER BY DISTRIBUTOR_CODE NULLS FIRST, FILE_NAME";
			}
			else
			{
				query = "SELECT ROWNUM MYROWNUM, DISTRIBUTOR_CODE, FILE_NAME, DISTRIBUTOR_CODE_INFILE, TRANSACTION_DATE, PRODUCT_CODE, PRODUCT_NAME, SPECIFICATION_NAME, CUSTOMER_CODE, CUSTOMER_NAME, CUSTOMER_CITY, CUSTOMER_PROVINCE, LOT_NUMBER, UNIT_NAME, EXPIRY_DATE, INVOICE_NUMBER, QUANTITY, TOTAL_SALES, TRANSACTION_DATE_FMT, EXPIRY_DATE_FMT FROM MAP_FILE_COLUMN WHERE DISTRIBUTOR_CODE LIKE '%"+par_search+"%' ORDER BY DISTRIBUTOR_CODE, FILE_NAME";
			}
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			Statement				stmt;
			ResultSet				rset;
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			
			// Writing to file
			String writeFilePath = getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/"))+FileName;
			
			rset = stmt.executeQuery(query);
						
			HSSFWorkbook logWB = new HSSFWorkbook();
			HSSFSheet logSheet = logWB.createSheet();
			
			HSSFRow logRow = logSheet.createRow(0);
			for(int hi = 0; hi < fileHeader.length; hi++)
			{
				logRow.createCell(hi).setCellValue(fileHeader[hi]);
			}
			
			int logRowIter = 1;
			while(rset.next())
			{
				logRow = logSheet.createRow(logRowIter);
				logRow.createCell(0).setCellValue(rset.getString("DISTRIBUTOR_CODE"));
				logRow.createCell(1).setCellValue(rset.getString("FILE_NAME"));
				logRow.createCell(2).setCellValue(rset.getString("DISTRIBUTOR_CODE_INFILE"));
				logRow.createCell(3).setCellValue(rset.getString("TRANSACTION_DATE"));
				logRow.createCell(4).setCellValue(rset.getString("PRODUCT_CODE"));
				logRow.createCell(5).setCellValue(rset.getString("PRODUCT_NAME"));
				logRow.createCell(6).setCellValue(rset.getString("SPECIFICATION_NAME"));
				logRow.createCell(7).setCellValue(rset.getString("LOT_NUMBER"));
				logRow.createCell(8).setCellValue(rset.getString("UNIT_NAME"));
				logRow.createCell(9).setCellValue(rset.getString("INVOICE_NUMBER"));
				logRow.createCell(10).setCellValue(rset.getString("EXPIRY_DATE"));
				logRow.createCell(11).setCellValue(rset.getString("CUSTOMER_CODE"));
				logRow.createCell(12).setCellValue(rset.getString("CUSTOMER_NAME"));
				logRow.createCell(13).setCellValue(rset.getString("CUSTOMER_CITY"));
				logRow.createCell(14).setCellValue(rset.getString("CUSTOMER_PROVINCE"));
				logRow.createCell(15).setCellValue(rset.getString("QUANTITY"));
				logRow.createCell(16).setCellValue(rset.getString("TOTAL_SALES"));
				logRow.createCell(17).setCellValue(rset.getString("TRANSACTION_DATE_FMT"));
				logRow.createCell(18).setCellValue(rset.getString("EXPIRY_DATE_FMT"));
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
