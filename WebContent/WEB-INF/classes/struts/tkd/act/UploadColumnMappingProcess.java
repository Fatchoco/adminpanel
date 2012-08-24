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

public class UploadColumnMappingProcess extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			request.setCharacterEncoding("UTF-8");
			FormUploadPage myForm = (FormUploadPage) form;
			String dbTableName = "MAP_FILE_COLUMN";
			
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
			
			UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Upload File", "Distributor File Setup", fileName, "", request.getSession().getAttribute("USER_NAME").toString());
			/* ** End of UI Summary ** */
			
			//Read from excel files
			Vector dataHolder = null;
	        dataHolder = excelFunc.getExcelContent(filePath, fileName);
	        
				//DB Connection
				javax.sql.DataSource	dataSource;
				java.sql.Connection		myConnection;
				Statement stmt	= null;
			
			//Check file header
			Vector cellStoreVectorHeader = (Vector) dataHolder.elementAt(0);
			List<Cell> listCellHeader = new ArrayList<Cell>();
			for(int hx = 0; hx < cellStoreVectorHeader.size(); hx++)
			{
				listCellHeader.add(hx, (Cell) cellStoreVectorHeader.elementAt(hx));
			}
			String[] fileHeader = {"Dist Code","File Name","Dist Code in Consolidated File","Trans Date","Product Code","Product Name","Specification","Lot / Batch No","Unit","Invoice No","Expiry Date","Customer Code","Customer Name","Customer City","Customer Province","Qty","Total Sales","Trans Date Format","Expiry Date Format","Function"};
			for(int hi = 0; hi < fileHeader.length; hi++)
			{
				if(!fileHeader[hi].equalsIgnoreCase(listCellHeader.get(hi).toString()))
					throw new Exception("File header format is incorrect ("+fileHeader[hi]+")("+listCellHeader.get(hi).toString()+")");
			}
			
			//Prepare for Validation
			int uploaded_record = 0;
			int rejected_record = 0;
			int deleted_record	= 0;
			ArrayList<String> vld_filename = new ArrayList<String>();

			//Read per rows and Insert to Table
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();

			ArrayList<String> logLines = new ArrayList();
			String logLine = "";
			String query = "INSERT INTO "+dbTableName+"(DISTRIBUTOR_CODE,FILE_NAME,TRANSACTION_DATE,PRODUCT_CODE,PRODUCT_NAME,SPECIFICATION_NAME,LOT_NUMBER,UNIT_NAME,INVOICE_NUMBER,EXPIRY_DATE,CUSTOMER_CODE,CUSTOMER_NAME,QUANTITY,TRANSACTION_DATE_FMT,EXPIRY_DATE_FMT,DISTRIBUTOR_CODE_INFILE,CUSTOMER_CITY,CUSTOMER_PROVINCE,TOTAL_SALES,CREATED_USER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'TKD_ADM')";
			String queryDel = "DELETE FROM "+dbTableName+" WHERE FILE_NAME = ?";
			PreparedStatement pstmt = null;
			PreparedStatement pstmtDel = null;
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
				Cell myCell13 = (13 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(13);
				Cell myCell14 = (14 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(14);
				Cell myCell15 = (15 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(15);
				Cell myCell16 = (16 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(16);
				Cell myCell17 = (17 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(17);
				Cell myCell18 = (18 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(18);
				Cell myCell19 = (19 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(19);
				
				String p_distCode = (myCell0==null)?"":cleanColumn.execute(myCell0.toString());
				String p_fileName = (myCell1==null)?"":cleanColumn.execute(myCell1.toString());
				String p_distCodeInfile = (myCell2==null)?"":cleanColumn.execute(myCell2.toString());
				String p_transDate = (myCell3==null)?"":cleanColumn.execute(myCell3.toString());
				String p_productCode = (myCell4==null)?"":cleanColumn.execute(myCell4.toString());
				String p_productName = (myCell5==null)?"":cleanColumn.execute(myCell5.toString());
				String p_specification = (myCell6==null)?"":cleanColumn.execute(myCell6.toString());
				String p_batchNo = (myCell7==null)?"":cleanColumn.execute(myCell7.toString());
				String p_unitName = (myCell8==null)?"":cleanColumn.execute(myCell8.toString());
				String p_invoiceNo = (myCell9==null)?"":cleanColumn.execute(myCell9.toString());
				String p_expiryDate = (myCell10==null)?"":cleanColumn.execute(myCell10.toString());
				String p_customerCode = (myCell11==null)?"":cleanColumn.execute(myCell11.toString());
				String p_customerName = (myCell12==null)?"":cleanColumn.execute(myCell12.toString());
				String p_customerCity = (myCell13==null)?"":cleanColumn.execute(myCell13.toString());
				String p_customerProvince = (myCell14==null)?"":cleanColumn.execute(myCell14.toString());
				String p_qty = (myCell15==null)?"":cleanColumn.execute(myCell15.toString());
				String p_totalsales = (myCell16==null)?"":cleanColumn.execute(myCell16.toString());
				String p_transDateFormat = (myCell17==null)?"":cleanColumn.execute(myCell17.toString());
				String p_expiryDateFormat = (myCell18==null)?"":cleanColumn.execute(myCell18.toString());
				String p_functionString = (myCell19==null)?"":cleanColumn.execute(myCell19.toString());
				
				try
				{					
					if(p_distCode.equals("") && p_distCodeInfile.equals(""))
						throw new Exception("Please select Distributor Code");
					else if(!p_distCode.equals("") && !p_distCodeInfile.equals(""))
						throw new Exception("Please choose only one Distributor Code source (either from file or input)");
					/*else if(p_fileName.length()<p_distCode.length()+1)
						throw new Exception("Please insert filename in correct format");
					else if(!p_fileName.substring(0,8).equals(p_distCode+'_'))
						throw new Exception("Please insert filename in correct format");*/
					else if(p_transDate.equals("") || p_transDateFormat.equals(""))
						throw new Exception("Please choose column for Transaction Date and Transaction Date Format");
					else if(p_productCode.equals("") && p_productName.equals(""))
						throw new Exception("Please choose column for Product Code or Product Name");
					else if(!p_expiryDate.equals("") && p_expiryDateFormat.equals(""))
						throw new Exception("Please choose column for Expiry Date Format");
					else if(p_customerCode.equals("") && p_customerName.equals(""))
						throw new Exception("Please choose column for Customer Code or Customer name");
					else if(p_qty.equals("") && p_totalsales.equals(""))
						throw new Exception("Please choose column for Quantity or Total Sales");
					
					for(String vld_fm:vld_filename)
					{
						if(p_fileName.equalsIgnoreCase(vld_fm))
							throw new Exception("Duplicate entry existed for this filename in the uploaded file");
					}
					
					pstmtDel = myConnection.prepareStatement(queryDel);
					pstmtDel.setString(1, p_fileName);
					pstmtDel.executeUpdate();
						
					if(!p_functionString.equalsIgnoreCase("delete"))
					{
						pstmt = myConnection.prepareStatement(query);
						
						pstmt.setString(1, p_distCode);
						pstmt.setString(2, p_fileName);
						pstmt.setString(3, p_transDate);
						pstmt.setString(4, p_productCode);
						pstmt.setString(5, p_productName);
						pstmt.setString(6, p_specification);
						pstmt.setString(7, p_batchNo);
						pstmt.setString(8, p_unitName);
						pstmt.setString(9, p_invoiceNo);
						pstmt.setString(10, p_expiryDate);
						pstmt.setString(11, p_customerCode);
						pstmt.setString(12, p_customerName);
						pstmt.setString(13, p_qty);
						pstmt.setString(14, p_transDateFormat);
						pstmt.setString(15, p_expiryDateFormat);
						pstmt.setString(16, p_distCodeInfile);
						pstmt.setString(17, p_customerCity);
						pstmt.setString(18, p_customerProvince);
						pstmt.setString(19, p_totalsales);
					
						pstmt.executeUpdate();
						
						vld_filename.add(p_fileName);
						uploaded_record++;
					}
					else
					{
						deleted_record++;
					}
				}
				catch (Exception eins)
				{
					logLine = p_distCode+'\t'+p_fileName+'\t'+p_distCodeInfile+'\t'+p_transDate+'\t'+p_productCode+'\t'+p_productName+'\t'+p_specification+'\t'+p_batchNo+'\t'+p_unitName+'\t'+p_invoiceNo+'\t'+p_expiryDate+'\t'+p_customerCode+'\t'+p_customerName+'\t'+p_customerCity+'\t'+p_customerProvince+'\t'+p_qty+'\t'+p_totalsales+'\t'+p_transDateFormat+'\t'+p_expiryDateFormat+'\t'+p_functionString+'\t'+eins.getMessage().trim();
					logLines.add(logLine);
					rejected_record++;
				}
				finally
				{
					if(pstmt!=null)pstmt.close();
					if(pstmtDel!=null)pstmtDel.close();
				}
			}
			myConnection.close();
			
			//LogFiles
			String logFileName = "ColumnMappingMaster_RejectedRecords.xls";
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
					logRow.createCell(14).setCellValue(arLine[14]);
					logRow.createCell(15).setCellValue(arLine[15]);
					logRow.createCell(16).setCellValue(arLine[16]);
					logRow.createCell(17).setCellValue(arLine[17]);
					logRow.createCell(18).setCellValue(arLine[18]);
					logRow.createCell(19).setCellValue(arLine[19]);
					logRow.createCell(20).setCellValue(arLine[20]);
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
			request.setAttribute("delete_record", deleted_record);
			
			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage().trim(), true);
		}
	}
}
