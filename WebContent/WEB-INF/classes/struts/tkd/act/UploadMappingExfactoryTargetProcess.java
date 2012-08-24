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

public class UploadMappingExfactoryTargetProcess extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			request.setCharacterEncoding("UTF-8");
			FormUploadPage myForm = (FormUploadPage) form;
			String dbTableName = "MAP_EX_FACTORY_TARGET";
			
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
			
			UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Upload File", "Mapping ExFactory Target", fileName, "", request.getSession().getAttribute("USER_NAME").toString());
			/* ** End of UI Summary ** */
			
			//Read from excel files
			Vector dataHolder = null;
	        dataHolder = excelFunc.getExcelContent(filePath, fileName);
			
			//Check file header
			Vector cellStoreVectorHeader = (Vector) dataHolder.elementAt(0);
			List<Cell> listCellHeader = new ArrayList<Cell>();
			for(int hx = 0; hx < cellStoreVectorHeader.size(); hx++)
			{
				listCellHeader.add(hx, (Cell) cellStoreVectorHeader.elementAt(hx));
			}
			String[] fileHeader = {"YEAR","MONTH","PRODUCT CODE","PRODUCT NAME (EN)","PRODUCT NAME (CN)","PRODUCT FAMILY","BRAND NAME","PRODUCT CATEOGRY","PACK SIZE","QUANTITY","NET SALES"};
			for(int hi = 0; hi < fileHeader.length; hi++)
			{
				if(!fileHeader[hi].equalsIgnoreCase(listCellHeader.get(hi).toString()))
					throw new Exception("File header format is incorrect ("+fileHeader[hi]+")("+listCellHeader.get(hi).toString()+")");
			}
			
			//Get All Period in File
			List<String[]> allperiodinfile = new ArrayList<String[]>();
			for(int x = 1; x < dataHolder.size(); x++)
			{
				Vector cellStoreVector = (Vector) dataHolder.elementAt(x);
				Cell myCell0 = (0 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(0);
				Cell myCell1 = (1 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(1);
				String CellValue0 = (myCell0==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(myCell0.toString()));
				String CellValue1 = (myCell1==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(myCell1.toString()));
				
				String[] getYearMonth = new String[2];
				getYearMonth[0] = CellValue0;
				getYearMonth[1] = CellValue1;
				
				allperiodinfile.add(getYearMonth);
			}
			allperiodinfile = removeListDuplicate.remove(allperiodinfile);

				//DB Connection
				javax.sql.DataSource	dataSource;
				java.sql.Connection		myConnection;
				dataSource		= getDataSource(request,"TKD_DIST_STG");
				myConnection	= dataSource.getConnection();
				Statement stmt	= myConnection.createStatement();
				
				//Delete Table first where Period equal to all Period in file
				for(String[] del:allperiodinfile)
				{
					stmt.execute("DELETE FROM "+dbTableName+" WHERE YEAR = '"+del[0]+"' AND MONTH = '"+del[1]+"'");
					stmt.execute("COMMIT");
				}
				stmt.close();
				myConnection.close();
			
			//Prepare for Validation
			int uploaded_record = 0;
			int rejected_record = 0;
			List<String> vld_hospid = new ArrayList<String>();
			List<String> vld_prdtid = new ArrayList<String>();
			List<String[]>  vld_dup	= new ArrayList<String[]>();
			
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			ResultSet rs	= null;
			String qv_hospid = "SELECT DISTINCT CUSTOMER_CD FROM MST_CUSTOMER";
			String qv_prdtid = "SELECT DISTINCT PRODUCT_CD FROM MST_PRODUCT";
			
			rs = stmt.executeQuery(qv_hospid);
			while(rs.next())
			{
				vld_hospid.add(rs.getString("CUSTOMER_CD"));
			}
			rs.close();
			
			rs = stmt.executeQuery(qv_prdtid);
			while(rs.next())
			{
				vld_prdtid.add(rs.getString("PRODUCT_CD"));
			}
			rs.close();
			
			stmt.close();
			myConnection.close();
			
			//Read per rows and Insert to Table
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			
			ArrayList<String> logLines = new ArrayList();
			String logLine = "";
			String query = "INSERT INTO "+dbTableName+"(YEAR,MONTH,PRODUCT_CD,PRODUCT_NM,PRODUCT_NM_CN,PRODUCT_FAMILY,BRAND_NM,PRODUCT_CATEGORY,PACK_SIZE,QUANTITY,NET_SALES) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
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
				
				//0,1,2,9,10
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
				
				try
				{
					// Validation
						//Year cannot be blank
						if(CellValue0.equals(""))
							throw new Exception("Year cannot be blank");
						//Year should be a valid year
						try
						{
							int temp = Integer.parseInt(CellValue0);
							if(!(temp>=1000 && temp<=9999))
								throw new Exception("Year should be a valid year");
						}
						catch(Exception eyear)
						{
							throw new Exception("Year should be a valid year");
						}
						//Month cannot be blank
						if(CellValue1.equals(""))
							throw new Exception("Month cannot be blank");
						//Month should be a valid month
						try
						{
							int temp = Integer.parseInt(CellValue1);
							if(!(temp>=1 && temp<=12))
								throw new Exception("Month should be a valid month");
						}
						catch(Exception emonth)
						{
							throw new Exception("Month should be a valid month");
						}
						//Product ID cannot be blank
						if(CellValue2.equals(""))
							throw new Exception("Product Code cannot be blank");
						//Hospital ID is not in Customer Master
						//if(!vld_prdtid.contains(CellValue2))
						//	throw new Exception("Product Code is not in Product Master");
						String[] temp = new String[3];
						temp[0] = CellValue0;
						temp[1] = CellValue1;
						temp[2] = CellValue2;
						//Period, Hospital ID and Product ID is duplicate in uploaded file
						for(String[] test_dup : vld_dup)
						{
							if (test_dup[0].equals(temp[0]) && test_dup[1].equals(temp[1]) && test_dup[2].equals(temp[2]))
								throw new Exception("Year, Month and Product Code is duplicate in uploaded file");
						}
						//Quantity cannot be blank
						if(CellValue9.equals(""))
							throw new Exception("Quantity cannot be blank");
						//Net Sales cannot be blank
						if(CellValue10.equals(""))
							throw new Exception("Net Sales cannot be blank");
					
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
				
					pstmt.executeUpdate();
					
					vld_dup.add(temp);
					
					uploaded_record++;
				}
				catch (Exception eins)
				{
					logLine = CellValue0+'\t'+CellValue1+'\t'+CellValue2+'\t'+CellValue3+'\t'+CellValue4+'\t'+CellValue5+'\t'+CellValue6+'\t'+CellValue7+'\t'+CellValue8+'\t'+CellValue9+'\t'+CellValue10+'\t'+eins.getMessage().trim();
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
			String logFileName = "MappingExfactoryTarget_RejectedRecord.xls";
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
