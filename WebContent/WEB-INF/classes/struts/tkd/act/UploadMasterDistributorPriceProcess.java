package struts.tkd.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.struts.upload.FormFile;

import struts.tkd.actfrm.FormUploadPage;
import struts.tkd.function.*;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import struts.tkd.actfrm.ActionFormValidated;

public class UploadMasterDistributorPriceProcess extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			request.setCharacterEncoding("UTF-8");
			FormUploadPage myForm = (FormUploadPage) form;
			String dbTableName = "MAP_T2_UNIT_PRICE";
			
			//Get File properties
			FormFile myFile = myForm.getUploadFile();
			String fileName = new String(myFile.getFileName().getBytes(), "UTF-8");
			
			//Get Server Realpath Save
			String filePath = getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/"));
			
			//Save file on Server
			if(!fileName.equals(""))
			{
				excelFunc.saveExcelToServer(myFile, filePath, fileName);
			}
			
			/* ** Start of UI Summary ** */
			javax.sql.DataSource	summaryDataSource;
			summaryDataSource	= getDataSource(request, "TKD_DIST_STG");
			
			UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Upload File", "Master Distributor Unitprice", fileName, "", request.getSession().getAttribute("USER_NAME").toString());
			/* ** End of UI Summary ** */
			
			//Read from excel files
			Vector dataHolder = null;
	        dataHolder = excelFunc.getExcelContent(filePath, fileName);
			
			//Check file header
			//String fileHeader = "PRODUCT CODE\tPRODUCT NAME (EN)\tPRODUCT NAME (CN)\tPRODUCT FAMILY CODE\tPRODUCT FAMILY NAME (EN)\tPRODUCT FAMILY NAME (CN)\tPRODUCT GROUP CODE\tPRODUCT GROUP NAME (EN)\tPRODUCT GROUP NAME (CN)\tUSAGE\tPACKAGING\tAVERAGE PRICE";
			Vector cellStoreVectorHeader = (Vector) dataHolder.elementAt(0);
			List<Cell> listCellHeader = new ArrayList<Cell>();
			for(int hx = 0; hx < cellStoreVectorHeader.size(); hx++)
			{
				listCellHeader.add(hx, (Cell) cellStoreVectorHeader.elementAt(hx));
			}
			String[] fileHeader = {"PERIOD", "DISTRIBUTOR CODE", "PRODUCT CODE", "UNIT PRICE"};
			for(int hi = 0; hi < fileHeader.length; hi++)
			{
				if(!fileHeader[hi].equalsIgnoreCase(listCellHeader.get(hi).toString()))
					throw new Exception("File header format is incorrect ("+fileHeader[hi]+")("+listCellHeader.get(hi).toString()+")");
			}
			
			//DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			Statement stmt	= myConnection.createStatement();
			
			//Delete Table first where Period equal to Active Period
			stmt.execute("DELETE FROM "+dbTableName+" WHERE PERIOD = (SELECT DISTINCT CURRENT_PERIOD FROM PAR_SYSTEM)");
			stmt.execute("COMMIT");
			stmt.close();
			myConnection.close();
			
			//Prepare for Validation
			int uploaded_record = 0;
			int rejected_record = 0;
			ArrayList<String> vld_prdcd = new ArrayList<String>();
			
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			ResultSet rs	= null;
			ResultSet rs2	= null; 
			String qv_prdcd = "SELECT DISTINCT (DISTRIBUTOR_CD || PRODUCT_CD) DIST_PROD_CD FROM "+dbTableName+" WHERE PERIOD = (SELECT CURRENT_PERIOD FROM PAR_SYSTEM)";
			String qv_period = "SELECT DISTINCT CURRENT_PERIOD FROM PAR_SYSTEM";
			String period;
			
			rs = stmt.executeQuery(qv_prdcd);
			while(rs.next())
			{
				vld_prdcd.add(rs.getString("DIST_PROD_CD"));
			}
			rs.close();
			
			rs2 = stmt.executeQuery(qv_period);
			rs2.next();
			period = rs2.getString("CURRENT_PERIOD");
			rs2.close();
			
			stmt.close();
			myConnection.close();
			
			//request.setAttribute("period", period);
			
			//Read per rows and Insert to Table
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			ResultSet rs3	= null;
			String QCheck;
			int resultcheck;

			ArrayList<String> logLines = new ArrayList();
			String logLine = "";
			String query = "INSERT INTO "+dbTableName+"(PERIOD, DISTRIBUTOR_CD, PRODUCT_CD, T2_UNIT_PRICE, CREATED_USER) VALUES (?, ?, ?, ?, 'TKD_ADM')";
			PreparedStatement pstmt = myConnection.prepareStatement(query);
			for(int x = 1; x < dataHolder.size(); x++)
			{
				Vector cellStoreVector = (Vector) dataHolder.elementAt(x);
				Cell myCell0 = (0 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(0);
				Cell myCell1 = (1 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(1);
				Cell myCell2 = (2 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(2);
				Cell myCell3 = (3 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(3);
				
				String CellValue0 = (myCell0==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell0)));
				String CellValue1 = (myCell1==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell1)));
				String CellValue2 = (myCell2==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell2)));
				String CellValue3 = (myCell3==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell3)));
								
				QCheck = "SELECT COUNT(*) roCount FROM MAP_T2_UNIT_PRICE WHERE PERIOD = '"+period+"' AND DISTRIBUTOR_CD = '"+CellValue1+"' AND PRODUCT_CD = '"+CellValue2+"'";
				rs3 = stmt.executeQuery(QCheck);
				rs3.next();
				resultcheck = rs3.getInt("roCount");
				rs3.close();
				
				try
				{
					// Validation
						//Period cannot be blank
						if(CellValue0.equals(""))
							throw new Exception("Period cannot be blank");
						//Period should be the same as Active Period
						if(!CellValue0.equals(period))
							throw new Exception("Period should be in active period");
						//Distributor code cannot be blank
						if(CellValue1.equals(""))
							throw new Exception("Distributor code cannot be blank");
						//Product code cannot be blank
						if(CellValue2.equals(""))
							throw new Exception("Product code cannot be blank");
						//Unit Price cannot be blank
						if(CellValue3.equals(""))
							throw new Exception("Unit Price cannot be blank");
						//Unit Price should be numeric
						if(!CellValue3.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+"))
							throw new Exception("Unit Price should be numeric");
						//Distributor Code and Product code duplicate in File
						if(resultcheck > 0)
							throw new Exception("Distributor code and Product code duplicate in File");
						//Distributor code already exists
						for(String t_prdcd : vld_prdcd)
						{
							if(t_prdcd != null && t_prdcd.equals(CellValue1))
								throw new Exception("Distributor code already exists");
						}
					
					pstmt = myConnection.prepareStatement(query);
					
					pstmt.setString(1, CellValue0);
					pstmt.setString(2, CellValue1);
					pstmt.setString(3, CellValue2);
					pstmt.setString(4, CellValue3);
										
				
					pstmt.executeUpdate();
					uploaded_record++;
				}
				catch (Exception eins)
				{
					logLine = CellValue0+'\t'+CellValue1+'\t'+CellValue2+'\t'+CellValue3+'\t'+eins.getMessage().trim();
					logLines.add(logLine);
					rejected_record++;
				}
				finally
				{
					pstmt.close();
				}
			}
			myConnection.close();
			
			//LogFiles
			String logFileName = "MasterDistributorPrice_RejectedRecord.xls";
			String writeFilePath	= getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/"))+logFileName;
			
			//Delete LogFiles
			File myLogFile = new File(writeFilePath);
			try
			{
				myLogFile.delete();
			}
			catch(Exception eLog)
			{
				throw new Exception(eLog.getMessage()+"["+writeFilePath+"]");
			}
			
			//Create LogFiles
			if(rejected_record > 0)
			{
				HSSFWorkbook logWB = new HSSFWorkbook();
				HSSFSheet logSheet = logWB.createSheet();
				
				HSSFRow logRow = logSheet.createRow(0);
				for(int hi = 0; hi < fileHeader.length; hi++)
				{
					logRow.createCell(hi).setCellValue(fileHeader[hi]);
					if(hi==fileHeader.length-1)
						logRow.createCell(hi+1).setCellValue("ERROR_MESSAGE");
				}
				
				int logRowIter = 1;
				for(String printLine : logLines)
				{
					String[] arLine = printLine.split("\t");
					logRow = logSheet.createRow(logRowIter);
					logRow.createCell(0).setCellValue(arLine[0]);
					logRow.createCell(1).setCellValue(arLine[1]);
					logRow.createCell(2).setCellValue(arLine[2]);
					logRow.createCell(3).setCellValue(arLine[3]);
					logRow.createCell(4).setCellValue(arLine[4]);
					logRowIter++;
				}
				FileOutputStream fileOut = new FileOutputStream(writeFilePath);
				logWB.write(fileOut);
				fileOut.close();
			}

			//Delete file on Server
			File fileToDelete = new File(filePath+fileName);
			fileToDelete.delete();
			
			request.setAttribute("upload_record", uploaded_record);
			request.setAttribute("reject_record", rejected_record);
			
			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage().trim(), true);
		}
	}
}
