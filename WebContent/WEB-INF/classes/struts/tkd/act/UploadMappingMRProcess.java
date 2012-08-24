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

public class UploadMappingMRProcess extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			request.setCharacterEncoding("UTF-8");
			FormUploadPage myForm = (FormUploadPage) form;
			String dbTableName = "MST_SALESPERSON";
			
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
			
			UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Upload File", "Master Salesperson", fileName, "", request.getSession().getAttribute("USER_NAME").toString());
			/* ** End of UI Summary ** */
			
			//Read from excel files
			Vector dataHolder = null;
	        dataHolder = excelFunc.getExcelContent(filePath, fileName);
	        
				//DB Connection
				javax.sql.DataSource	dataSource;
				java.sql.Connection		myConnection;
				Statement stmt	= null;
			
			//Check file header
			//String fileHeader = "PRODUCT CODE\tPRODUCT NAME (EN)\tPRODUCT NAME (CN)\tPRODUCT FAMILY CODE\tPRODUCT FAMILY NAME (EN)\tPRODUCT FAMILY NAME (CN)\tPRODUCT GROUP CODE\tPRODUCT GROUP NAME (EN)\tPRODUCT GROUP NAME (CN)\tUSAGE\tPACKAGING\tAVERAGE PRICE";
			Vector cellStoreVectorHeader = (Vector) dataHolder.elementAt(0);
			List<Cell> listCellHeader = new ArrayList<Cell>();
			for(int hx = 0; hx < cellStoreVectorHeader.size(); hx++)
			{
				listCellHeader.add(hx, (Cell) cellStoreVectorHeader.elementAt(hx));
			}
			String[] fileHeader = {"Hospital ID","Product ID","MR ID","MR Employee ID","MR Name (EN)","MR Name (CN)","DSM ID","DSM Employee ID","DSM NAME (EN)","DSM NAME (CN)","RSM ID","RSM Employee ID","RSM NAME (EN)","RSM NAME (CN)","RSD ID","RSD Employee ID","RSD NAME (EN)","RSD NAME (CN)","Region ID","Region Name (EN)","Region Name (CN)","FUNCTION"};
			for(int hi = 0; hi < fileHeader.length; hi++)
			{
				if(!fileHeader[hi].equalsIgnoreCase(listCellHeader.get(hi).toString()))
					throw new Exception("File header format is incorrect ("+fileHeader[hi]+")("+listCellHeader.get(hi).toString()+")");
			}
			
			//Prepare for Validation
			int uploaded_record = 0;
			int rejected_record = 0;
			int deleted_record  = 0;
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

			ArrayList<String> logLines = new ArrayList();
			String logLine = "";
			String query = "INSERT INTO "+dbTableName+"(HOSPITAL_CD,PRODUCT_CD,MR_CD,MR_EMPLOYEE_CD,MR_NM,MR_NM_CN,DSM_CD,DSM_EMPLOYEE_CD,DSM_NM,DSM_NM_CN,RSM_CD,RSM_EMPLOYEE_CD,RSM_NM,RSM_NM_CN,RSD_CD,RSD_EMPLOYEE_CD,RSD_NM,RSD_NM_CN,REGION_CD,REGION_NM,REGION_NM_CN, CREATED_USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, 'TKD_ADM')";
			PreparedStatement pstmt = null;
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
				Cell myCell20 = (20 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(20);
				Cell myCell21 = (21 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(21);
				
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
				String CellValue11 = (myCell11==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell11)));
				String CellValue12 = (myCell12==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell12)));
				String CellValue13 = (myCell13==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell13)));
				String CellValue14 = (myCell14==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell14)));
				String CellValue15 = (myCell15==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell15)));
				String CellValue16 = (myCell16==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell16)));
				String CellValue17 = (myCell17==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell17)));
				String CellValue18 = (myCell18==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell18)));
				String CellValue19 = (myCell19==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell19)));
				String CellValue20 = (myCell20==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell20)));
				String CellValue21 = (myCell21==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell21)));
				
				try
				{
					// Validation
						//Hospital ID cannot be blank
						if(CellValue0.equals(""))
							throw new Exception("Hospital ID cannot be blank");
						//Hospital ID is not in Customer Master
						//if(!vld_hospid.contains(CellValue0))
						//	throw new Exception("Hospital ID is not in Customer Master");
						//Product ID cannot be blank
						if(CellValue1.equals(""))
							throw new Exception("Product ID cannot be blank");
						//Product ID is not in Product Master
						//if(!vld_prdtid.contains(CellValue1))
						//	throw new Exception("Product Code is not in Product Master");
						//MR ID cannot be blank
						if(CellValue2.equals(""))
							throw new Exception("MR ID cannot be blank");
						//Region ID cannot be blank
						if(CellValue18.equals(""))
							throw new Exception("Region ID cannot be blank");
						//Region Name (EN) cannot be blank
						if(CellValue19.equals(""))
							throw new Exception("Region Name (EN) cannot be blank");
						//Region Name (CN) cannot be blank
						if(CellValue20.equals(""))
							throw new Exception("Region Name (CN) cannot be blank");
						
						String[] temp = new String[2];
						temp[0] = CellValue0;
						temp[1] = CellValue1;
						
						//Hospital ID and Product ID is duplicate in uploaded file
						for(String[] test_dup : vld_dup)
						{
							if (test_dup[0].equals(temp[0]) && test_dup[1].equals(temp[1]))
								throw new Exception("Hospital ID and Product ID is duplicate in uploaded file");
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
					pstmt.setString(14, CellValue13);
					pstmt.setString(15, CellValue14);
					pstmt.setString(16, CellValue15);
					pstmt.setString(17, CellValue16);
					pstmt.setString(18, CellValue17);
					pstmt.setString(19, CellValue18);
					pstmt.setString(20, CellValue19);
					pstmt.setString(21, CellValue20);
					
										
					//Delete Record
					stmt			= myConnection.createStatement();
					stmt.executeUpdate("DELETE FROM MST_SALESPERSON WHERE HOSPITAL_CD = '"+CellValue0+"' AND PRODUCT_CD = '"+CellValue1+"'");
					stmt.execute("COMMIT");
					stmt.close();
					
					if(!CellValue21.toUpperCase().equals("DELETE"))
					{
						vld_dup.add(temp);
						pstmt.executeUpdate();
						uploaded_record++;
					}
					else
					{
						deleted_record++;
					}
				}
				catch (Exception eins)
				{
					logLine = CellValue0+'\t'+CellValue1+'\t'+CellValue2+'\t'+CellValue3+'\t'+CellValue4+'\t'+CellValue5+'\t'+CellValue6+'\t'+CellValue7+'\t'+CellValue8+'\t'+CellValue9+'\t'+CellValue10+'\t'+CellValue11+'\t'+CellValue12+'\t'+CellValue13+'\t'+CellValue14+'\t'+CellValue15+'\t'+CellValue16+'\t'+CellValue17+'\t'+CellValue18+'\t'+CellValue19+'\t'+CellValue20+'\t'+CellValue21+'\t'+eins.getMessage().trim();
					logLines.add(logLine);
					rejected_record++;
				}
				finally
				{
					if(pstmt!=null)pstmt.close();
					if(stmt!=null)stmt.close();
				}
			}
			myConnection.close();
			
			//LogFiles
			String logFileName = "MappingMR_RejectedRecord.xls";
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
				for(int hi = 0; hi < fileHeader.length-1; hi++)
				{
					logRow.createCell(hi).setCellValue(fileHeader[hi]);
					if(hi==fileHeader.length-2)
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
					logRow.createCell(21).setCellValue(arLine[21]);
					logRow.createCell(22).setCellValue(arLine[22]);
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
