package struts.tkd.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class UploadDistributorFilesProcess extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			request.setCharacterEncoding("UTF-8");
			FormUploadPage myForm = (FormUploadPage) form;
			String dbTableName = "DATA_STG0";
			
			//Get File properties
			FormFile myFile = myForm.getUploadFile();
			String fileName = new String(myFile.getFileName().getBytes(), "UTF-8").replaceAll(" ", "");

			//File size validation
			int maxUploadSize= 2560; //in kilobyte
			int uploadedSize = myFile.getFileSize()/1024; //in kilobyte
			if(uploadedSize > maxUploadSize)
				throw new Exception("Uploaded "+fileName+" file size ("+uploadedSize+" KB) exceeds the maximum allowed file size ("+maxUploadSize+" KB).");
			
			//Get Server Realpath Save
			String filePath = getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/"));
			
			//Save file on Server
			if(!fileName.equals(""))
			{
				excelFunc.saveExcelToServer(myFile, filePath, fileName);
			}
			
			//Unzip File fromDir, toDir
			String temp    = "";
			String r_input = "";
			String r_error = "";
			String zipFileSrc = filePath + fileName;
			String zipFileTrg = filePath + "extractDistFiles";
			try
			{
				//Process p = Runtime.getRuntime().exec("C:\\Program Files\\7-Zip\\7z.exe x \""+zipFileSrc+"\" -o\""+zipFileTrg+"\" -y");
				//Process p = Runtime.getRuntime().exec("/home/oracle/Erico/7za x "+zipFileSrc+" -o"+zipFileTrg+" -y");
				Process p = Runtime.getRuntime().exec("unzip -o "+zipFileSrc+" -d "+zipFileTrg+"");
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				while((temp = stdInput.readLine())!=null)
					r_input += temp;
				while((temp = stdError.readLine())!=null)
					r_error += temp;
				p.waitFor();
			}
			catch(Exception ezip)
			{
				throw new Exception(ezip.getMessage());
			}
//			if(true)
//				throw new Exception(r_input+"::"+r_error+"::("+"unzip -o "+zipFileSrc+" -d "+zipFileTrg+""+")");

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
				if(rset.next())
				{
					current_period = rset.getString("CURRENT_PERIOD");
				}
				else
				{
					stmt.close();
					myConnection.close();
					throw new Exception("No CURRENT_PERIOD is found on table PAR_SYSTEM");
				}
				rset.close();
				
				//Get registered FileName
				List<String> listMapFile = new ArrayList<String>();
				rset			= stmt.executeQuery("SELECT FILE_NAME FROM MAP_FILE_COLUMN");
				while(rset.next())
				{
					listMapFile.add(rset.getString("FILE_NAME"));
				}
				rset.close();
				stmt.close();
				myConnection.close();
			
			//Get list of all File in toDir
		    File startingDirectory= new File(zipFileTrg);
		    List<File> files = FileListing.getFileListing(startingDirectory);
		    List<File> processed_files	= new ArrayList<File>();
		    List<File> unprocessed_files= new ArrayList<File>();
		    List<String> unprocessed_log= new ArrayList<String>();
		    for(File file:files)
		    {
		    	//Only process File
		    	if(file.isFile())
		    	{
		    		try
		    		{
		    			/* ** Start of UI Summary ** */
		    			javax.sql.DataSource	summaryDataSource;
		    			summaryDataSource	= getDataSource(request, "TKD_DIST_STG");
		    			
		    			UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Upload File", "Distributor File", file.getName(), "", request.getSession().getAttribute("USER_NAME").toString());
		    			/* ** End of UI Summary ** */
		    			
		    			//Check whether the files matches the format
		    			if(!excelFunc.checkDistFileNameFormat(file))
		    				throw new Exception("File name format is invalid");
		    			
		    			//Check whether the files registered or not
						String successFileName = excelFunc.removeDistFileNamePeriod(file.getName());
						if(!listMapFile.contains(successFileName))
							throw new Exception("File is not registered");
		    			
						//Read from excel files
						Vector dataHolder = null;
				        dataHolder = excelFunc.getExcelContent(file.getParent()+file.separator, file.getName());
								
							//Delete Current File + Current Period
				        	dataSource		= getDataSource(request,"TKD_DIST_STG");
				        	myConnection	= dataSource.getConnection();
				        	stmt			= myConnection.createStatement();
							stmt.execute("DELETE "+dbTableName+" WHERE FILE_NAME = '"+excelFunc.removeDistFileNamePeriod(file.getName())+"' AND PERIOD = '"+current_period+"' AND FLAG_ADJUSTMENT = 'N'");
							stmt.close();
							myConnection.close();
						
						//Variables
						Vector cellStoreVectorHeader = (Vector) dataHolder.elementAt(0);
						List<Cell> listCellHeader = new ArrayList<Cell>();
						int uploaded_record = 0;
						int rejected_record = 0;
						
						//Read per rows and Insert to Table
						dataSource		= getDataSource(request,"TKD_DIST_STG");
						myConnection	= dataSource.getConnection();
			
						ArrayList<String> logLines = new ArrayList();
						String logLine = "";
						String query = "INSERT INTO "+dbTableName+"(RID,FILE_NAME,C1,C2,C3,C4,C5,C6,C7,C8,C9,C10,C11,C12,C13,C14,C15,C16,C17,C18,C19,C20,C21,C22,C23,C24,C25,C26,C27,C28,C29,C30,C31,C32,C33,C34,C35,C36,C37,C38,C39,C40,C41,C42,C43,C44,C45,C46,C47,C48,C49,C50,PERIOD,CREATED_USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, 'TKD_ADM')";
						PreparedStatement pstmt = myConnection.prepareStatement(query);
						for(int x = 0; x < dataHolder.size(); x++)
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
							Cell myCell22 = (22 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(22);
							Cell myCell23 = (23 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(23);
							Cell myCell24 = (24 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(24);
							Cell myCell25 = (25 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(25);
							Cell myCell26 = (26 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(26);
							Cell myCell27 = (27 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(27);
							Cell myCell28 = (28 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(28);
							Cell myCell29 = (29 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(29);
							Cell myCell30 = (30 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(30);
							Cell myCell31 = (31 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(31);
							Cell myCell32 = (32 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(32);
							Cell myCell33 = (33 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(33);
							Cell myCell34 = (34 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(34);
							Cell myCell35 = (35 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(35);
							Cell myCell36 = (36 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(36);
							Cell myCell37 = (37 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(37);
							Cell myCell38 = (38 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(38);
							Cell myCell39 = (39 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(39);
							Cell myCell40 = (40 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(40);
							Cell myCell41 = (41 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(41);
							Cell myCell42 = (42 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(42);
							Cell myCell43 = (43 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(43);
							Cell myCell44 = (44 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(44);
							Cell myCell45 = (45 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(45);
							Cell myCell46 = (46 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(46);
							Cell myCell47 = (47 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(47);
							Cell myCell48 = (48 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(48);
							Cell myCell49 = (49 > cellStoreVector.size()-1)?null:(Cell) cellStoreVector.elementAt(49);
							
							String C0 = (myCell0==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell0)));
							String C1 = (myCell1==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell1)));
							String C2 = (myCell2==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell2)));
							String C3 = (myCell3==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell3)));
							String C4 = (myCell4==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell4)));
							String C5 = (myCell5==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell5)));
							String C6 = (myCell6==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell6)));
							String C7 = (myCell7==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell7)));
							String C8 = (myCell8==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell8)));
							String C9 = (myCell9==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell9)));
							String C10 = (myCell10==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell10)));
							String C11 = (myCell11==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell11)));
							String C12 = (myCell12==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell12)));
							String C13 = (myCell13==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell13)));
							String C14 = (myCell14==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell14)));
							String C15 = (myCell15==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell15)));
							String C16 = (myCell16==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell16)));
							String C17 = (myCell17==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell17)));
							String C18 = (myCell18==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell18)));
							String C19 = (myCell19==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell19)));
							String C20 = (myCell20==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell20)));
							String C21 = (myCell21==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell21)));
							String C22 = (myCell22==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell22)));
							String C23 = (myCell23==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell23)));
							String C24 = (myCell24==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell24)));
							String C25 = (myCell25==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell25)));
							String C26 = (myCell26==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell26)));
							String C27 = (myCell27==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell27)));
							String C28 = (myCell28==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell28)));
							String C29 = (myCell29==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell29)));
							String C30 = (myCell30==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell30)));
							String C31 = (myCell31==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell31)));
							String C32 = (myCell32==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell32)));
							String C33 = (myCell33==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell33)));
							String C34 = (myCell34==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell34)));
							String C35 = (myCell35==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell35)));
							String C36 = (myCell36==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell36)));
							String C37 = (myCell37==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell37)));
							String C38 = (myCell38==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell38)));
							String C39 = (myCell39==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell39)));
							String C40 = (myCell40==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell40)));
							String C41 = (myCell41==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell41)));
							String C42 = (myCell42==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell42)));
							String C43 = (myCell43==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell43)));
							String C44 = (myCell44==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell44)));
							String C45 = (myCell45==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell45)));
							String C46 = (myCell46==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell46)));
							String C47 = (myCell47==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell47)));
							String C48 = (myCell48==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell48)));
							String C49 = (myCell49==null)?"":cleanStringNumeric.remove00Precision(cleanColumn.execute(excelFunc.allToString(myCell49)));
							
							try
							{	
								pstmt = myConnection.prepareStatement(query);
			
								pstmt.setString(1, Integer.toString(x+1));
								pstmt.setString(2, excelFunc.removeDistFileNamePeriod(file.getName()));
								pstmt.setString(3, C0);
								pstmt.setString(4, C1);
								pstmt.setString(5, C2);
								pstmt.setString(6, C3);
								pstmt.setString(7, C4);
								pstmt.setString(8, C5);
								pstmt.setString(9, C6);
								pstmt.setString(10, C7);
								pstmt.setString(11, C8);
								pstmt.setString(12, C9);
								pstmt.setString(13, C10);
								pstmt.setString(14, C11);
								pstmt.setString(15, C12);
								pstmt.setString(16, C13);
								pstmt.setString(17, C14);
								pstmt.setString(18, C15);
								pstmt.setString(19, C16);
								pstmt.setString(20, C17);
								pstmt.setString(21, C18);
								pstmt.setString(22, C19);
								pstmt.setString(23, C20);
								pstmt.setString(24, C21);
								pstmt.setString(25, C22);
								pstmt.setString(26, C23);
								pstmt.setString(27, C24);
								pstmt.setString(28, C25);
								pstmt.setString(29, C26);
								pstmt.setString(30, C27);
								pstmt.setString(31, C28);
								pstmt.setString(32, C29);
								pstmt.setString(33, C30);
								pstmt.setString(34, C31);
								pstmt.setString(35, C32);
								pstmt.setString(36, C33);
								pstmt.setString(37, C34);
								pstmt.setString(38, C35);
								pstmt.setString(39, C36);
								pstmt.setString(40, C37);
								pstmt.setString(41, C38);
								pstmt.setString(42, C39);
								pstmt.setString(43, C40);
								pstmt.setString(44, C41);
								pstmt.setString(45, C42);
								pstmt.setString(46, C43);
								pstmt.setString(47, C44);
								pstmt.setString(48, C45);
								pstmt.setString(49, C46);
								pstmt.setString(50, C47);
								pstmt.setString(51, C48);
								pstmt.setString(52, C49);
								pstmt.setString(53, current_period);
							
								pstmt.executeUpdate();
								uploaded_record++;
							}
							catch (Exception eins)
							{
								logLine = eins.getMessage();
								logLines.add(logLine);
								rejected_record++;
							}
							finally
							{
								pstmt.close();
							}
						}
						myConnection.close();
						processed_files.add(file);
		    		}
		    		catch(Exception esf)
		    		{
		    			unprocessed_files.add(file);
		    			unprocessed_log.add(esf.getMessage());
		    		}
		    	}
		    }

		    //Run PROC_STG0_TO_STG1
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			CallableStatement cs = myConnection.prepareCall("{ call proc_stg0_to_stg1(?) }");
			cs.setString(1, current_period);
			
			try
			{
				cs.execute();
				cs.close();
				myConnection.close();
			}
			catch(Exception esp)
			{
				cs.close();
				myConnection.close();
				throw new Exception("Stored Procedure failed ("+esp.getMessage()+")");
			}
		    
			//LogFiles
			String logFileName = "Distributor_Files_Exception.xls";
			String writeFilePath	= getPath.execute(getServlet().getServletContext().getRealPath("/WEB-INF/temp/"))+logFileName;
			String[] fileHeader = {"FILE_NAME"};
			String[] fileHeader2 = {"PERIOD","FILE_NAME","RID","TRANSACTION_DATE","PRODUCT_CODE","PRODUCT_NAME","SPECIFICATION_NAME","CUSTOMER_CODE","CUSTOMER_NAME","CUSTOMER_CITY","CUSTOMER_PROVINCE","DISTRIBUTOR_CODE","LOT_NUMBER","UNIT_NAME","EXPIRY_DATE","INVOICE_NUMBER","QUANTITY","UNIT_PRICE","TOTAL_SALES","REMARK","CREATED_DATE","CREATED_USER"};
			String[] fileHeader3 = {"FILE_NAME"};
			
			//Delete LogFiles
			File myLogFile = new File(writeFilePath);
			myLogFile.delete();
			
			//Create LogFiles
			if(true)	//Mau ilangin if nya tapi malas jadinya set true dulu
			{
				HSSFWorkbook logWB = new HSSFWorkbook();
				//Write Unsuccess FileUpload
				HSSFSheet logSheet = logWB.createSheet("Unsuccess FileUpload");
				HSSFRow logRow = logSheet.createRow(0);
				for(int hi = 0; hi < fileHeader.length; hi++)
				{
					logRow.createCell(hi).setCellValue(fileHeader[hi]);
					if(hi==fileHeader.length-1)
						logRow.createCell(hi+1).setCellValue("ERROR_MESSAGE");
				}
				
				int logRowIter = 1;
				for(File logFile:unprocessed_files)
				{
					logRow = logSheet.createRow(logRowIter);
					logRow.createCell(0).setCellValue(logFile.getName().toString());
					logRow.createCell(1).setCellValue(unprocessed_log.get(logRowIter-1));
					logRowIter++;
				}
				
				//Write SuccessUploadFile-RejectedData
				logSheet = logWB.createSheet("Success FileUpload-RejectedData");
				logRow = logSheet.createRow(0);
				for(int hi = 0; hi < fileHeader2.length; hi++)
				{
					logRow.createCell(hi).setCellValue(fileHeader2[hi]);
				}
				dataSource		= getDataSource(request,"TKD_DIST_STG");
				myConnection	= dataSource.getConnection();
				stmt			= myConnection.createStatement();
				rset			= stmt.executeQuery("SELECT * FROM EXCP_DATA_STG1 WHERE PERIOD = '"+current_period+"' AND FLAG_ADJUSTMENT = 'N' ORDER BY FILE_NAME, RID");
				logRowIter = 1;
				while(rset.next())
				{
					logRow = logSheet.createRow(logRowIter);
					logRow.createCell(0).setCellValue(rset.getString("PERIOD"));
					logRow.createCell(1).setCellValue(rset.getString("FILE_NAME"));
					logRow.createCell(2).setCellValue(rset.getString("RID"));
					logRow.createCell(3).setCellValue(rset.getString("TRANSACTION_DATE"));
					logRow.createCell(4).setCellValue(rset.getString("PRODUCT_CODE"));
					logRow.createCell(5).setCellValue(rset.getString("PRODUCT_NAME"));
					logRow.createCell(6).setCellValue(rset.getString("SPECIFICATION_NAME"));
					logRow.createCell(7).setCellValue(rset.getString("CUSTOMER_CODE"));
					logRow.createCell(8).setCellValue(rset.getString("CUSTOMER_NAME"));
					logRow.createCell(9).setCellValue(rset.getString("CUSTOMER_CITY"));
					logRow.createCell(10).setCellValue(rset.getString("CUSTOMER_PROVINCE"));
					logRow.createCell(11).setCellValue(rset.getString("DISTRIBUTOR_CODE"));
					logRow.createCell(12).setCellValue(rset.getString("LOT_NUMBER"));
					logRow.createCell(13).setCellValue(rset.getString("UNIT_NAME"));
					logRow.createCell(14).setCellValue(rset.getString("EXPIRY_DATE"));
					logRow.createCell(15).setCellValue(rset.getString("INVOICE_NUMBER"));
					logRow.createCell(16).setCellValue(rset.getString("QUANTITY"));
					logRow.createCell(17).setCellValue(rset.getString("UNIT_PRICE"));
					logRow.createCell(18).setCellValue(rset.getString("TOTAL_SALES"));
					logRow.createCell(19).setCellValue(rset.getString("REMARK"));
					logRow.createCell(20).setCellValue(rset.getString("CREATED_DATE"));
					logRow.createCell(21).setCellValue(rset.getString("CREATED_USER"));
					logRowIter++;
				}
				rset.close();
				stmt.close();
				myConnection.close();
				
				/*//Write Success FileUpload-Unregistered
				logSheet = logWB.createSheet("Success FileUpload-Unregistered");
				logRow = logSheet.createRow(0);
				for(int hi = 0; hi < fileHeader3.length; hi++)
				{
					logRow.createCell(hi).setCellValue(fileHeader3[hi]);
				}
				List<String> listMapFile = new ArrayList<String>();
				rset			= stmt.executeQuery("SELECT FILE_NAME FROM MAP_FILE_COLUMN");
				while(rset.next())
				{
					listMapFile.add(rset.getString("FILE_NAME"));
				}
				rset.close();
				stmt.close();
				myConnection.close();
				
				logRowIter = 1;
				for(File successFile : processed_files)
				{
					String successFileName = excelFunc.removeDistFileNamePeriod(successFile.getName());
					boolean write = true;
					for(String mapFileName:listMapFile)
					{
						if(mapFileName.equalsIgnoreCase(successFileName))
						{
							write = false;
							break;
						}
					}
					if(write)
					{
						logRow = logSheet.createRow(logRowIter);
						logRow.createCell(0).setCellValue(successFileName);
						logRowIter++;
					}
				}*/
				FileOutputStream fileOut = new FileOutputStream(writeFilePath);
				logWB.write(fileOut);
				fileOut.close();
			}

		    //Delete extracted file on Server
		    FileListing.removeDirectory(startingDirectory);
		    //Delete zip file on Server
		    File zipFile = new File(zipFileSrc);
		    zipFile.delete();
		    
			request.setAttribute("processed_files", processed_files.size());
			request.setAttribute("unprocessed_files", unprocessed_files.size());
			request.setAttribute("unprocessed_log", unprocessed_log.size());
			
			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage(), true);
		}
	}
}
