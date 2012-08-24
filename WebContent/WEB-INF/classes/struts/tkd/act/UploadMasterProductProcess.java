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

public class UploadMasterProductProcess extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			request.setCharacterEncoding("UTF-8");
			FormUploadPage myForm = (FormUploadPage) form;
			String dbTableName = "MST_PRODUCT";
			
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
			
			UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Upload File", "Master Product", fileName, "", request.getSession().getAttribute("USER_NAME").toString());
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
			String[] fileHeader = {"PERIOD", "PRODUCT CODE", "PRODUCT NAME (EN)", "PRODUCT NAME (CN)", "PRODUCT FAMILY CODE", "PRODUCT FAMILY NAME (EN)", "PRODUCT FAMILY NAME (CN)", "PRODUCT GROUP CODE", "PRODUCT GROUP NAME (EN)", "PRODUCT GROUP NAME (CN)", "USAGE", "PACKAGING", "AVERAGE PRICE"};
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
			String qv_prdcd = "SELECT DISTINCT PRODUCT_CD FROM "+dbTableName+" WHERE PERIOD = (SELECT CURRENT_PERIOD FROM PAR_SYSTEM)";
			String qv_period = "SELECT DISTINCT CURRENT_PERIOD FROM PAR_SYSTEM";
			String period;
			
			rs = stmt.executeQuery(qv_prdcd);
			while(rs.next())
			{
				vld_prdcd.add(rs.getString("PRODUCT_CD"));
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
			String query = "INSERT INTO "+dbTableName+"(PERIOD, PRODUCT_CD, PRODUCT_NM, PRODUCT_NM_CN, PRODUCT_FAMILY_CD, PRODUCT_FAMILY_NM, PRODUCT_FAMILY_NM_CN, PRODUCT_GROUP_CD, PRODUCT_GROUP, PRODUCT_GROUP_CN, USAGE, PACKAGING, AVG_UNIT_PRICE, CREATED_USER) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'TKD_ADM')";
			PreparedStatement pstmt = myConnection.prepareStatement(query);
			for(int x = 1; x < dataHolder.size(); x++)
			{
				Vector cellStoreVector = (Vector) dataHolder.elementAt(x);
				Cell myCell0 = (0 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(0);
				Cell myCell1 = (1 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(1);
				Cell myCell2 = (2 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(2);
				Cell myCell3 = (3 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(3);
				Cell myCell4 = (4 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(4);
				Cell myCell5 = (5 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(5);
				Cell myCell6 = (6 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(6);
				Cell myCell7 = (7 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(7);
				Cell myCell8 = (8 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(8);
				Cell myCell9 = (9 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(9);
				Cell myCell10 = (10 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(10);
				Cell myCell11 = (11 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(11);
				Cell myCell12 = (12 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(12);
				
				String CellValue0 = (myCell0==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell0)));
				String CellValue1 = (myCell1==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell1)));
				String CellValue2 = (myCell2==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell2)));
				String CellValue3 = (myCell3==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell3)));
				String CellValue4 = (myCell4==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell4)));
				String CellValue5 = (myCell5==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell5)));
				String CellValue6 = (myCell6==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell6)));
				String CellValue7 = (myCell7==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell7)));
				String CellValue8 = (myCell8==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell8)));
				String CellValue9 = (myCell9==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell9)));
				String CellValue10 = (myCell10==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell10)));
				String CellValue11 = (myCell10==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell11)));
				String CellValue12 = (myCell10==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell12)));
				
				QCheck = "SELECT COUNT(*) roCount FROM MST_PRODUCT WHERE PERIOD = '"+period+"' AND PRODUCT_CD = '"+CellValue1+"'";
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
						//Product code cannot be blank
						if(CellValue1.equals(""))
							throw new Exception("Product code cannot be blank");
						//Product code duplicate in File
						if(resultcheck > 0)
							throw new Exception("Product code duplicate in File");
						//numeric check
						if(!CellValue12.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+"))
							throw new Exception("Average Unit Price should be numeric");
						//Product code already exists in table
						for(String t_prdcd : vld_prdcd)
						{
							if(t_prdcd != null && t_prdcd.equals(CellValue1))
								throw new Exception("Product code already exists");
						}
					
					pstmt = myConnection.prepareStatement(query);
					
					pstmt.setString(1, CellValue0);
					pstmt.setString(2, CellValue1);
					pstmt.setString(3, CellValue2);
					pstmt.setString(4, CellValue3);
					pstmt.setString(5, CellValue4);
					pstmt.setString(6, CellValue5);
					pstmt.setString(7, CellValue6);
					pstmt.setString(8, CellValue7);
					pstmt.setString(9, CellValue8);
					pstmt.setString(10, CellValue9);
					pstmt.setString(11, CellValue10);
					pstmt.setString(12, CellValue11);
					pstmt.setString(13, CellValue12);
					
				
					pstmt.executeUpdate();
					uploaded_record++;
				}
				catch (Exception eins)
				{
					logLine = CellValue0+'\t'+CellValue1+'\t'+CellValue2+'\t'+CellValue3+'\t'+CellValue4+'\t'+CellValue5+'\t'+CellValue6+'\t'+CellValue7+'\t'+CellValue8+'\t'+CellValue9+'\t'+CellValue10+'\t'+CellValue11+'\t'+CellValue12+'\t'+eins.getMessage().trim();
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
			String logFileName = "MasterProduct_RejectedRecord.xls";
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
					logRow.createCell(5).setCellValue(arLine[5]);
					logRow.createCell(6).setCellValue(arLine[6]);
					logRow.createCell(7).setCellValue(arLine[7]);
					logRow.createCell(8).setCellValue(arLine[8]);
					logRow.createCell(9).setCellValue(arLine[9]);
					logRow.createCell(10).setCellValue(arLine[10]);
					logRow.createCell(11).setCellValue(arLine[11]);
					logRow.createCell(12).setCellValue(arLine[12]);
					logRow.createCell(13).setCellValue(arLine[13]);
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
