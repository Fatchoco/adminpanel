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

public class UploadMappingProductProcess extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			request.setCharacterEncoding("UTF-8");
			FormUploadPage myForm = (FormUploadPage) form;
			String dbTableName = "MAP_PRODUCT";
			
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
			
			UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Upload File", "Mapping Product", fileName, "", request.getSession().getAttribute("USER_NAME").toString());
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
			String[] fileHeader = {"DISTRIBUTOR CODE","DISTRIBUTOR PRODUCT CODE","DISTRIBUTOR PRODUCT NAME (CN)","TAKEDA PRODUCT CODE","TAKEDA PRODUCT NAME (CN)","FUNCTION"};
			for(int hi = 0; hi < fileHeader.length; hi++)
			{
				if(!fileHeader[hi].equalsIgnoreCase(listCellHeader.get(hi).toString()))
					throw new Exception("File header format is incorrect ("+fileHeader[hi]+")("+listCellHeader.get(hi).toString()+")");
			}
			
			//Prepare for Validation
			int uploaded_record = 0;
			int rejected_record = 0;
			int deleted_record = 0;
			List<String[]>  vld_tbl = new ArrayList<String[]>();
			List<String[]>  vld_dup	= new ArrayList<String[]>();

			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			ResultSet rs	= null;
			String qv_tbl = "SELECT DISTINCT NVL(DISTRIBUTOR_ID,'')DISTRIBUTOR_ID, NVL(DISTRIBUTOR_PRODUCT_CD,'')DISTRIBUTOR_PRODUCT_CD, NVL(DISTRIBUTOR_PRODUCT_NM,'')DISTRIBUTOR_PRODUCT_NM, NVL(PRODUCT_CD,'')PRODUCT_CD, NVL(PRODUCT_NM,'')PRODUCT_NM FROM MAP_PRODUCT";
			
			rs = stmt.executeQuery(qv_tbl);
			while(rs.next())
			{
				String[] temp = new String[4];
				temp[0] = rs.getString("DISTRIBUTOR_ID") != null?rs.getString("DISTRIBUTOR_ID"):"";
				temp[1] = rs.getString("DISTRIBUTOR_PRODUCT_CD") != null?rs.getString("DISTRIBUTOR_PRODUCT_CD"):"";
				temp[2] = rs.getString("DISTRIBUTOR_PRODUCT_NM") != null?rs.getString("DISTRIBUTOR_PRODUCT_NM"):"";
				temp[3] = rs.getString("PRODUCT_CD") != null?rs.getString("PRODUCT_CD"):"";
				vld_tbl.add(temp);
			}
			rs.close();
			stmt.close();
			myConnection.close();
			
			//Read per rows and Insert to Table
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			
			ArrayList<String> logLines = new ArrayList();
			String logLine = "";
			String query = "INSERT INTO "+dbTableName+"(DISTRIBUTOR_ID, DISTRIBUTOR_PRODUCT_CD, DISTRIBUTOR_PRODUCT_NM, PRODUCT_CD, PRODUCT_NM) VALUES (?, ?, ?, ?, ?)";
			String queryDel = "Delete from "+dbTableName+" where NVL(DISTRIBUTOR_ID, 'ocirenull') = ? AND NVL(DISTRIBUTOR_PRODUCT_CD,'ocirenull') = ? AND NVL(DISTRIBUTOR_PRODUCT_NM,'ocirenull') = ?";
			PreparedStatement pstmt  	= null;
			PreparedStatement pstmt1 	= null;
			for(int x = 1; x < dataHolder.size(); x++)
			{
				Vector cellStoreVector = (Vector) dataHolder.elementAt(x);
				Cell myCell0 = (0 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(0);
				Cell myCell1 = (1 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(1);
				Cell myCell2 = (2 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(2);
				Cell myCell3 = (3 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(3);
				Cell myCell4 = (4 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(4);
				Cell myCell5 = (5 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(5);

				
				String CellValue0 = (myCell0==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell0)));
				String CellValue1 = (myCell1==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell1)));
				String CellValue2 = (myCell2==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell2)));
				String CellValue3 = (myCell3==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell3)));
				String CellValue4 = (myCell4==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell4)));
				String CellValue5 = (myCell5==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell5)));
				
				try
				{
					// Validation
						//DistributorCode cannot be blank
						if(CellValue0.equals(""))
							throw new Exception("Distributor code cannot be blank");
						//TakedaProductCode cannot be blank
						if(CellValue3.equals(""))
							throw new Exception("Takeda Product Code cannot be blank");
						String[] temp = new String[4];
						temp[0] = CellValue0;
						temp[1] = CellValue1;
						temp[2] = CellValue2;
						temp[3] = CellValue3;
						//Validate with table data
						boolean r_vld1	= true;
						boolean r_vld2	= true;
						boolean	r_vld3	= true;
						for(String[] vld_data_tbl : vld_tbl)
						{
							//if(vld_data_tbl[0].equalsIgnoreCase(temp[0]) && vld_data_tbl[1].equalsIgnoreCase(temp[1]) && vld_data_tbl[2].equalsIgnoreCase(temp[2]) && !vld_data_tbl[3].equalsIgnoreCase(temp[3]))
								//r_vld1 = false;
							//if(vld_data_tbl[0].equalsIgnoreCase(temp[0]) && vld_data_tbl[1].equalsIgnoreCase(temp[1]) && !vld_data_tbl[1].equalsIgnoreCase("") && !vld_data_tbl[3].equalsIgnoreCase(temp[3]))
								//r_vld2 = false;
							//if(vld_data_tbl[0].equalsIgnoreCase(temp[0]) && !vld_data_tbl[2].equalsIgnoreCase("") && vld_data_tbl[2].equalsIgnoreCase(temp[2]) && !vld_data_tbl[3].equalsIgnoreCase(temp[3]))
								//r_vld3 = false;
						}
						//if(!r_vld1)
							//throw new Exception("Same DistributorProductCode and DistributorProductName mapped to different ProductCode found in table");
						//if(!r_vld2)
							//throw new Exception("Same DistributorProductCode mapped to different ProductCode found in table");
						//if(!r_vld3)
							//throw new Exception("Same DistributorProductName mapped to different ProductCode found in table");

						//Validate with file data
						for(String[] vld_data_dup : vld_dup)
						{
							if(vld_data_dup[0].equalsIgnoreCase(temp[0]) && vld_data_dup[1].equalsIgnoreCase(temp[1]) && vld_data_dup[2].equalsIgnoreCase(temp[2]) && vld_data_dup[3].equalsIgnoreCase(temp[3]))
								throw new Exception("Same DistributorProductCode, DistributorProductName, TakedaProductCode found in file");
							if(vld_data_dup[0].equalsIgnoreCase(temp[0]) && vld_data_dup[1].equalsIgnoreCase(temp[1]) && vld_data_dup[2].equalsIgnoreCase(temp[2]) && !vld_data_dup[3].equalsIgnoreCase(temp[3]))
								r_vld1 = false;
							//if(vld_data_dup[0].equalsIgnoreCase(temp[0]) && vld_data_dup[1].equalsIgnoreCase(temp[1]) && !vld_data_dup[1].equalsIgnoreCase("") && !vld_data_dup[3].equalsIgnoreCase(temp[3]))
								//r_vld2 = false;
							//if(vld_data_dup[0].equalsIgnoreCase(temp[0]) && !vld_data_dup[2].equalsIgnoreCase("") && vld_data_dup[2].equalsIgnoreCase(temp[2]) && !vld_data_dup[3].equalsIgnoreCase(temp[3]))
								//r_vld3 = false;
						}
						if(!r_vld1)
							throw new Exception("Same DistributorProductCode and DistributorProductName mapped to different TakedaProductCode found in file");
						//if(!r_vld2)
							//throw new Exception("Same DistributorProductCode mapped to different ProductCode found in file");
						//if(!r_vld3)
							//throw new Exception("Same DistributorProductName mapped to different ProductCode found in file");

						
						pstmt1 = myConnection.prepareStatement(queryDel);
						pstmt1.setString(1, CellValue0.equalsIgnoreCase("")?"ocirenull":CellValue0);
						pstmt1.setString(2, CellValue1.equalsIgnoreCase("")?"ocirenull":CellValue1);
						pstmt1.setString(3, CellValue2.equalsIgnoreCase("")?"ocirenull":CellValue2);
						pstmt1.executeUpdate();
					
						pstmt = myConnection.prepareStatement(query);
						pstmt.setString(1, CellValue0);
						pstmt.setString(2, CellValue1);
						pstmt.setString(3, CellValue2);
						pstmt.setString(4, CellValue3);
						pstmt.setString(5, CellValue4);
					
					if(!CellValue5.equalsIgnoreCase("DELETE"))
					{
						vld_dup.add(temp);
						pstmt.executeUpdate();
						uploaded_record++;
					} else {
						deleted_record++;
						//Delete Record from vld_temp
						for (Iterator<String[]> iter = vld_tbl.iterator(); iter.hasNext();)
						{
							String[] stemp = iter.next();
							if(stemp[0].equalsIgnoreCase(temp[0]) && stemp[1].equalsIgnoreCase(temp[1]) && stemp[2].equalsIgnoreCase(temp[2]) && stemp[3].equalsIgnoreCase(temp[3]))
							{
								iter.remove();
							}
						}
					}
						
				}
				catch (Exception eins)
				{
					logLine = CellValue0+'\t'+CellValue1+'\t'+CellValue2+'\t'+CellValue3+'\t'+CellValue4+'\t'+CellValue5+'\t'+eins.getMessage().trim();
					logLines.add(logLine);
					rejected_record++;
				}
				finally
				{
					if(pstmt!=null)pstmt.close();
					if(pstmt1!=null)pstmt1.close();
				}
			}
			myConnection.close();
			
			//LogFiles
			String logFileName = "MappingProduct_RejectedRecord.xls";
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
					logRow.createCell(0).setCellValue(cleanStringNumeric.execute(arLine[0]));
					logRow.createCell(1).setCellValue(cleanStringNumeric.execute(arLine[1]));
					logRow.createCell(2).setCellValue(cleanStringNumeric.execute(arLine[2]));
					logRow.createCell(3).setCellValue(cleanStringNumeric.execute(arLine[3]));
					logRow.createCell(4).setCellValue(cleanStringNumeric.execute(arLine[4]));
					logRow.createCell(5).setCellValue(cleanStringNumeric.execute(arLine[5]));
					logRow.createCell(6).setCellValue(cleanStringNumeric.execute(arLine[6]));
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
			request.setAttribute("deleted_record", deleted_record);
			request.setAttribute("reject_record", rejected_record);
			
			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage().trim(), true);
		}
	}
}
