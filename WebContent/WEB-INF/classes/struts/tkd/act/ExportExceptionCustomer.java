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

public class ExportExceptionCustomer extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {

		try
		{
			// Variables
			String FileName = "CustomerMapping.xls";
			//String expFileName = "MasterProductExport.txt";
			//String header = "PRODUCT CODE\tPRODUCT NAME (EN)\tPRODUCT NAME (CN)\tPRODUCT FAMILY CODE\tPRODUCT FAMILY NAME (EN)\tPRODUCT FAMILY NAME (CN)\tPRODUCT GROUP CODE\tPRODUCT GROUP NAME (EN)\tPRODUCT GROUP NAME (CN)\tUSAGE\tPACKAGING\tAVERAGE PRICE";
			String[] fileHeader = {"DISTRIBUTOR CODE", "DISTRIBUTOR CUSTOMER CODE", "DISTRIBUTOR CUSTOMER NAME (CN)", "DISTRIBUTOR CUSTOMER CITY", "DISTRIBUTOR CUSTOMER PROVINCE"};
			String line;
			String period_param = request.getParameter("Period");
			String query = "SELECT a.DISTRIBUTOR_CD,   a.CUSTOMER_CD,   a.CUSTOMER_NM,   a.CUSTOMER_CITY,   a.CUSTOMER_PROVINCE,   a.TAKEDA_CUSTOMER_CD,   b.FUNCTION FROM   (SELECT DISTINCT DISTRIBUTOR_CD,     CUSTOMER_CD,     CUSTOMER_NM,     CUSTOMER_CITY,     CUSTOMER_PROVINCE,     TAKEDA_CUSTOMER_CD   FROM EXCP_MAPPING_CUSTOMER   WHERE TO_DATE(PERIOD, 'YYYYMM') <=     (SELECT TO_DATE(MAX(CURRENT_PERIOD),'YYYYMM') FROM PAR_SYSTEM     )   AND TO_DATE(PERIOD, 'YYYYMM') >=     (SELECT ADD_MONTHS(TO_DATE(MAX(CURRENT_PERIOD),'YYYYMM'),-12) FROM PAR_SYSTEM     )   ) a LEFT OUTER JOIN   (SELECT DISTINCT DISTRIBUTOR_CD,     CUSTOMER_CD,     CUSTOMER_NM,     CUSTOMER_CITY,     CUSTOMER_PROVINCE,     TAKEDA_CUSTOMER_CD,     'New' FUNCTION   FROM EXCP_MAPPING_CUSTOMER   WHERE PERIOD =     (SELECT MAX(CURRENT_PERIOD) FROM PAR_SYSTEM     )   ) b ON NVL(a.DISTRIBUTOR_CD,'xnullnullx') = NVL(b.DISTRIBUTOR_CD,'xnullnullx') AND NVL(a.CUSTOMER_CD,'xnullnullx')   = NVL(b.CUSTOMER_CD,'xnullnullx') AND NVL(a.CUSTOMER_NM,'xnullnullx')   = NVL(b.CUSTOMER_NM,'xnullnullx') ORDER BY TAKEDA_CUSTOMER_CD NULLS FIRST, FUNCTION,   DISTRIBUTOR_CD,   CUSTOMER_CD,   CUSTOMER_NM";
			
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
				if(hi==fileHeader.length-1)
				{
					logRow.createCell(hi+1).setCellValue("TAKEDA CUSTOMER CODE");
					logRow.createCell(hi+2).setCellValue("TAKEDA CUSTOMER NAME (CN)");
					logRow.createCell(hi+2).setCellValue("TAKEDA CUSTOMER NAME (CN)");
					logRow.createCell(hi+3).setCellValue("FUNCTION");
				}
			}
			
			int logRowIter = 1;
			while(rset.next())
			{
				//String[] arLine = printLine.split("\t");
				logRow = logSheet.createRow(logRowIter);
				logRow.createCell(0).setCellValue(rset.getString("DISTRIBUTOR_CD"));
				logRow.createCell(1).setCellValue(rset.getString("CUSTOMER_CD"));
				logRow.createCell(2).setCellValue(rset.getString("CUSTOMER_NM"));
				logRow.createCell(3).setCellValue(rset.getString("CUSTOMER_CITY"));
				logRow.createCell(4).setCellValue(rset.getString("CUSTOMER_PROVINCE"));
				logRow.createCell(5).setCellValue(rset.getString("TAKEDA_CUSTOMER_CD"));
				logRow.createCell(5+2).setCellValue(rset.getString("FUNCTION"));
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
