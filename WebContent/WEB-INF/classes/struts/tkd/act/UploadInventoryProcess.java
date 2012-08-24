package struts.tkd.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.apache.struts.upload.FormFile;

import struts.tkd.actfrm.FormUploadPage;
import struts.tkd.function.*;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import struts.tkd.actfrm.*;

public class UploadInventoryProcess extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			/**Distributor Tier is now ignored, but the code is not completely removed*/
			request.setCharacterEncoding("UTF-8");
			FormUploadPageTier myForm = (FormUploadPageTier) form;
			String dbTableName = "INVENTORY_STG1";
			
			//Get Page values
			Integer tier = myForm.getTier();
			
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
			
			UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Upload File", "Inventory", fileName, "", request.getSession().getAttribute("USER_NAME").toString());
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
			String[] fileHeader = {"Date","Distributor Code","Takeda Product Code","Takeda Product Name","Batch No","Expired Date","Qty in-transfer","Qty in-hand","Qty out-transfer"};
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
				ResultSet rset;
				
				//Get Current Period
				String current_period;
				rset = stmt.executeQuery("SELECT MAX(CURRENT_PERIOD) CURRENT_PERIOD FROM PAR_SYSTEM");
				rset.next();
				current_period = rset.getString("CURRENT_PERIOD");
				
				//Delete Table first where Period equal to Active Period
				stmt.execute("DELETE FROM "+dbTableName+" WHERE PERIOD = (SELECT DISTINCT CURRENT_PERIOD FROM PAR_SYSTEM)");
				stmt.execute("COMMIT");
				stmt.close();
				myConnection.close();
			
				//Prepare for Validation
				int uploaded_record = 0;
				int rejected_record = 0;
				/*List<String[]> vld_distlvl = new ArrayList<String[]>();
				
				dataSource		= getDataSource(request,"TKD_DIST_STG");
				myConnection	= dataSource.getConnection();
				stmt			= myConnection.createStatement();
				ResultSet rs	= null;
				String qv_distlvl = "SELECT DISTINCT DISTRIBUTOR_CD, LEVELS FROM MST_DISTRIBUTOR";
				
				rs = stmt.executeQuery(qv_distlvl);
				while(rs.next())
				{
					String[] temp = new String[2];
					temp[0] = rs.getString("DISTRIBUTOR_CD");
					temp[1] = rs.getString("LEVELS");
					vld_distlvl.add(temp);
				}
				rs.close();
				
				stmt.close();
				myConnection.close();*/
			
			//Read per rows and Insert to Table
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			
			ArrayList<String> logLines = new ArrayList();
			String logLine = "";
			String query = "INSERT INTO "+dbTableName+"(FILE_NAME,RID,PERIOD,INVENTORY_DATE,DISTRIBUTOR_CD,DISTRIBUTOR_LVL,TAKEDA_PRODUCT_CD,TAKEDA_PRODUCT_NM,LOT_NUMBER,EXPIRY_DATE,QTY_IN_TRANS,QTY_IN_HAND,QTY_OUT_TRANS,CREATED_USER) VALUES (?,?,?,?,?,NULL,?,?,?,?,?,?,?,'TKD_ADM')";
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
				
				String CellValue0 = (myCell0==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell0)));
				String CellValue1 = (myCell1==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell1)));
				String CellValue2 = (myCell2==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell2)));
				String CellValue3 = (myCell3==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell3)));
				String CellValue4 = (myCell4==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell4)));
				String CellValue5 = (myCell5==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell5)));
				String CellValue6 = (myCell6==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell6)));
				String CellValue7 = (myCell7==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell7)));
				String CellValue8 = (myCell8==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell8)));
				
				Date InventoryDate = null;
				Date ExpiryDate = null;
				try
				{
					// Validation
						//Date availability
						if(CellValue0.equals(""))
							throw new Exception("Date cannot be blank");
						//Date validation
						try
						{
							DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
							InventoryDate = df.parse(CellValue0);
						}
						catch(Exception date1)
						{
							try
							{
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
								InventoryDate = df.parse(CellValue0);
							}
							catch(Exception date2)
							{
								throw new Exception("Date is not valid");
							}
						}
						//Date validation with current Period
						DateFormat dfmt = new SimpleDateFormat("yyyyMM");
						if(!dfmt.format(InventoryDate).equalsIgnoreCase(current_period))
						{
							throw new Exception("Date is not in active period");
						}
						
						//ExpiryDate
						if(!CellValue5.equals(""))
						{
							//Expiry Date validation
							try
							{
								DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
								ExpiryDate = df.parse(CellValue5);
							}
							catch(Exception date1)
							{
								try
								{
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
									ExpiryDate = df.parse(CellValue5);
								}
								catch(Exception date2)
								{
									throw new Exception("Expiry Date is not valid");
								}
							}
						}
						
						//Distributor Code
						if(CellValue1.equals(""))
							throw new Exception("Distributor Code cannot be blank");
						//Takeda Product Code
						if(CellValue2.equals(""))
							throw new Exception("Takeda Product Code cannot be blank");
						//Qty in-hand
						if(CellValue7.equals(""))
							throw new Exception("Qty in-hand cannot be blank");
						if(!CellValue7.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+"))
							throw new Exception("Qty in-hand should be in numeric");
						//Qty in-transfer
						if(CellValue6.equals(""))
							CellValue6 = "0";
						if(!CellValue6.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+"))
							throw new Exception("Qty in-transfer should be in numeric");
						//Qty out-transfer
						if(CellValue8.equals(""))
							CellValue8 = "0";
						if(!CellValue8.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+"))
							throw new Exception("Qty out-transfer should be in numeric");
						
						String[] temp = new String[2];
						temp[0] = CellValue1;
						temp[1] = Integer.toString(tier);
						/*//Check DistributorCode Level
						boolean inmst	  = false;
						for(String[] test_dist : vld_distlvl)
						{
							if (test_dist[0].equalsIgnoreCase(temp[0]))
							{
								inmst = true;
								break;
							}
						}
						if(inmst)
						{
							boolean passornot = false;
							for(String[] test_dup : vld_distlvl)
							{
								if (test_dup[0].equalsIgnoreCase(temp[0]) && test_dup[1].equalsIgnoreCase(temp[1]))
									passornot = true;
							}
							if(!passornot)
								throw new Exception("Distributor Code's level does not match the description in Master Distributor");
						}
						else
						{
							tier = 2;
						}*/
					
					pstmt = myConnection.prepareStatement(query);
					
					pstmt.setString(1, fileName);
					pstmt.setInt(2, x+1);
					pstmt.setString(3, current_period);
					pstmt.setDate(4, new java.sql.Date(InventoryDate.getTime()));
					pstmt.setString(5, CellValue1);
					//pstmt.setInt(6, tier);
					pstmt.setString(6, CellValue2);
					pstmt.setString(7, CellValue3);
					pstmt.setString(8, CellValue4);
					if(ExpiryDate == null)
						pstmt.setNull(9, java.sql.Types.DATE);
					else
						pstmt.setDate(9, new java.sql.Date(ExpiryDate.getTime()));
					pstmt.setString(10, CellValue6);
					pstmt.setString(11, CellValue7);
					pstmt.setString(12, CellValue8);
				
					pstmt.executeUpdate();
					
					uploaded_record++;
				}
				catch (Exception eins)
				{
					logLine = CellValue0+'\t'+CellValue1+'\t'+CellValue2+'\t'+CellValue3+'\t'+CellValue4+'\t'+CellValue5+'\t'+CellValue6+'\t'+CellValue7+'\t'+CellValue8+'\t'+eins.getMessage().trim();
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
			String logFileName = "Inventory_RejectedRecord.xls";
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
