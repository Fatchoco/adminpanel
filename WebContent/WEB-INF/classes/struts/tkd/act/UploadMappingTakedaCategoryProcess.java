package struts.tkd.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.DATE;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.struts.upload.FormFile;

import struts.tkd.actfrm.FormUploadPage;
import struts.tkd.function.*;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import struts.tkd.actfrm.ActionFormValidated;

public class UploadMappingTakedaCategoryProcess extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			request.setCharacterEncoding("UTF-8");
			FormUploadPage myForm = (FormUploadPage) form;
			String dbTableName = "MAP_TAKEDA_CATEGORY";
			String CountryCod = request.getParameter("country_cod");
			
			//Get File properties
			FormFile myFile = myForm.getUploadFile();
			String fileName = new String(myFile.getFileName().getBytes(), "UTF-8");
			
			//Get Server Realpath Save
			String filePath = getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/IMS/"));
			
			//Save file on Server
			if(!fileName.equals(""))
			{
				excelFunc.saveExcelToServer(myFile, filePath, fileName);
			}
			
			/* ** Start of UI Summary ** */
			javax.sql.DataSource	summaryDataSource;
			summaryDataSource	= getDataSource(request, "IMS_STG");
			
			//DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			
			dataSource		= getDataSource(request,"IMS_STG");
			myConnection	= dataSource.getConnection();
			Statement stmt	= myConnection.createStatement();
			ResultSet rset;
			rset = stmt.executeQuery("SELECT MAX(CURRENT_PERIOD) CURRENT_PERIOD FROM PAR_SYSTEM");
			rset.next();
			String current_period=rset.getString("CURRENT_PERIOD").substring(0,6);
			rset.close();
			stmt.close();
			myConnection.close();
			//Calendar cal = Calendar.getInstance();
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			//String current_period = sdf.format(cal.getTime());
			
			UISummaryFuncIMS.insertLog(summaryDataSource, request.getSession().getAttribute("USER_NAME").toString(),current_period, CountryCod,"Upload Mapping Takeda Category",fileName);
			/* ** End of UI Summary ** */
			
			//Read from excel files
			Vector dataHolder = null;
	        dataHolder = excelFunc.getExcelContent(filePath, fileName);
	        
				//DB Connection
				//javax.sql.DataSource	dataSource;
				//java.sql.Connection		myConnection;
				stmt	= null;
			
			Vector cellStoreVectorHeader = (Vector) dataHolder.elementAt(0);
			List<Cell> listCellHeader = new ArrayList<Cell>();
			for(int hx = 0; hx < cellStoreVectorHeader.size(); hx++)
			{
				listCellHeader.add(hx, (Cell) cellStoreVectorHeader.elementAt(hx));
			}
			String[] fileHeader = {"TAKEDA_CATEGORY_COD","TAKEDA_CATEGORY_DES","PACK_COD","PACK_DES"};
			for(int hi = 0; hi < fileHeader.length; hi++)
			{
				if(!fileHeader[hi].equalsIgnoreCase(listCellHeader.get(hi).toString()))
					throw new Exception("File header format is incorrect ("+fileHeader[hi]+")("+listCellHeader.get(hi).toString()+")");
			}
			
			//Prepare for Validation
			int uploaded_record = 0;
			int rejected_record = 0;
			int deleted_record = 0;
			List<String[]>  vld_dup	= new ArrayList<String[]>();

			//Read per rows and Insert to Table
			dataSource		= getDataSource(request,"IMS_STG");
			myConnection	= dataSource.getConnection();
			stmt	= myConnection.createStatement();
			stmt.execute("DELETE FROM "+dbTableName+" WHERE COUNTRY_COD = '"+CountryCod+"'");
			stmt.execute("COMMIT");
			stmt.close();
			myConnection.close();			
			
			dataSource		= getDataSource(request,"IMS_STG");
			myConnection	= dataSource.getConnection();
			
			ArrayList<String> logLines = new ArrayList();
			String logLine = "";
			String query = "INSERT INTO "+dbTableName+"(TAKEDA_CATEGORY_COD,TAKEDA_CATEGORY_DES,PACK_COD,PACK_DES,COUNTRY_COD,SOURCE_FILE_NAME,CREATED_USER) VALUES (?,?,?,?,'"+CountryCod+"','"+fileName+"','Default')";
			String queryDel = "Delete from "+dbTableName+" where COUNTRY_COD = '"+CountryCod+"'";
			PreparedStatement pstmt  	= null;
			PreparedStatement pstmt1 	= null;
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
				
				try
				{
					// Validation
						if(CellValue0.equals(""))
							throw new Exception("Takeda Category Cod cannot be blank");
						if(CellValue1.equals(""))
							throw new Exception("Takeda Category Des cannot be blank");
						if(CellValue2.equals(""))
							throw new Exception("Pack Cod cannot be blank");
						String[] temp = new String[4];
						temp[0] = CellValue0;
						temp[1] = CellValue1;
						temp[2] = CellValue2;
						temp[3] = CellValue3;
						
						//Validate with file data
						for(String[] vld_data_dup : vld_dup)
						{
							if(vld_data_dup[2].equalsIgnoreCase(temp[2]))
								throw new Exception("Same PACK_COD found in file");
						}
											
						pstmt = myConnection.prepareStatement(query);
						pstmt.setString(1, CellValue0);
						pstmt.setString(2, CellValue1);
						pstmt.setString(3, CellValue2);
						pstmt.setString(4, CellValue3);
					
						vld_dup.add(temp);
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
					if(pstmt!=null)pstmt.close();
					if(pstmt1!=null)pstmt1.close();
				}
			}
			myConnection.close();
			
			//LogFiles
			String logFileName = "MappingTakedaCategory_RejectedRecord.xls";
			String writeFilePath	= getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/IMS/"))+logFileName;
			
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
					logRowIter++;
				}
				FileOutputStream fileOut = new FileOutputStream(writeFilePath);
				logWB.write(fileOut);
				fileOut.close();
			}

			dataSource		= getDataSource(request,"IMS_RPT");
			myConnection	= dataSource.getConnection();
			stmt	= myConnection.createStatement();
			CallableStatement cs = null;
	    	try
			{
	    		cs	= myConnection.prepareCall("{call pkg_tkd_ims_rpt.map_takeda_category(?,?)}");
	    		cs.setString(1, CountryCod);
	    		cs.setString(2, request.getSession().getAttribute("USER_NAME").toString());
				cs.execute();
			}
			catch(Exception e_del_job)
			{
				throw new Exception(e_del_job.getMessage());
			}
			finally
			{
				if(cs!=null)cs.close();
				if(myConnection!=null)myConnection.close();
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
