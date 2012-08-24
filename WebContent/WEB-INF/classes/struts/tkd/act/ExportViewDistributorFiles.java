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

public class ExportViewDistributorFiles extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {

		try
		{
			// Variables
			String FileName = "DistributorFileResult_per_Period.xls";
			String[] fileHeader = {"DIST_CODE", "FILE_NAME", "ACCEPTED RECORD(S)", "REJECTED RECORD(S)", "ACCEPTED QUANTITY", "ADJUSTMENT"};
			String line;
			String current_period = request.getParameter("Period");
			String par_search = request.getParameter("Search");
			String par_search2 = request.getParameter("Search2");
			String query = "SELECT ROWNUM MYROWNUM, DISTRIBUTOR_CODE, FILE_NAME, RACCEPT, RREJECT, RQTY, FLAG_ADJUSTMENT  FROM(SELECT NVL(b.DISTRIBUTOR_CODE,'-') DISTRIBUTOR_CODE, a.FILE_NAME, a.REJECTED_COUNT RREJECT, a.ACCEPTED_COUNT RACCEPT, a.ACCEPTED_QTY RQTY, FLAG_ADJUSTMENT FROM CTL_DATA_STG1 a LEFT OUTER JOIN MAP_FILE_COLUMN b ON a.FILE_NAME = b.FILE_NAME WHERE PERIOD = '"+current_period+"' AND b.DISTRIBUTOR_CODE LIKE '%"+par_search+"%' AND FLAG_ADJUSTMENT LIKE '%"+par_search2+"%' ORDER BY 2,3)";
			
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
				//String[] arLine = printLine.split("\t");
				logRow = logSheet.createRow(logRowIter);
				logRow.createCell(0).setCellValue(rset.getString("DISTRIBUTOR_CODE"));
				logRow.createCell(1).setCellValue(rset.getString("FILE_NAME"));
				logRow.createCell(2).setCellValue(rset.getString("RACCEPT"));
				logRow.createCell(3).setCellValue(rset.getString("RREJECT"));
				logRow.createCell(4).setCellValue(rset.getString("RQTY"));
				logRow.createCell(5).setCellValue(rset.getString("FLAG_ADJUSTMENT"));
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
