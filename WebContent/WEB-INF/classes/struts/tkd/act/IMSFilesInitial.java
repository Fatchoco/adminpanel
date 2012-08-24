package struts.tkd.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import org.apache.struts.upload.FormFile;

import struts.tkd.actfrm.FormUploadPage;
import struts.tkd.function.FileListing;
import struts.tkd.function.getPath;

import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import struts.tkd.actfrm.ActionFormValidated;

public class IMSFilesInitial extends ActionFormValidated {
	public ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			// Variables
			String current_period;
			String current_periodo=null;
			String per=null;
			Calendar cal;
			String sfp=null;
			int tes=0;
			String fcountry=request.getParameter("hdcntr");
			if(fcountry==null) fcountry="CN";
			String ncountry=null;
			String process=null;
			String status="Executed";
			String job=null;
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			dataSource		= getDataSource(request,"IMS_STG");
			myConnection	= dataSource.getConnection();
			Statement stmt	= myConnection.createStatement();
			ResultSet rset;
			Result cn_result;
			
			rset = stmt.executeQuery("SELECT trim(to_char(to_date(MAX(CURRENT_PERIOD),'RRRRMMDD'),'Month')) || ' ' || trim(to_char(to_date(MAX(CURRENT_PERIOD),'RRRRMMDD'),'YYYY')) CURRENT_PERIOD, MAX(CURRENT_PERIOD) current_periodo, SOURCE_FILE_PATH FROM PAR_SYSTEM GROUP BY SOURCE_FILE_PATH");
			
			// Get CURRENT_PERIOD
			if(rset.next())
			{
				current_period = rset.getString("CURRENT_PERIOD");
				current_periodo = rset.getString("CURRENT_PERIODO").substring(0,6);
				sfp=rset.getString("SOURCE_FILE_PATH");
			}
			else
			{
				cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				
				current_period = sdf.format(cal.getTime());
			}
			
			rset.close();
			
			ResultSet rset2;
			rset2 = stmt.executeQuery("SELECT COUNTRY_DES FROM COUNTRY WHERE COUNTRY_COD = '"+fcountry+"'");
			rset2.next();
			ncountry=rset2.getString("COUNTRY_DES");
			rset2.close();

			ResultSet rset3;
			rset3 = stmt.executeQuery("SELECT * FROM COUNTRY");
			cn_result = ResultSupport.toResult(rset3);
			rset3.close();
			stmt.close();
			myConnection.close();
			
			dataSource		= getDataSource(request,"IMS_STG");
			myConnection	= dataSource.getConnection();
			stmt	= myConnection.createStatement();
			ResultSet rset1;
			rset1 = stmt.executeQuery("SELECT STATUS,JOB,PERIOD FROM PRO_STATUS ORDER BY CREATED_DATE");
			//rset1.next();
				while(rset1.next()) {
					per=rset1.getString("PERIOD");
					status=rset1.getString("STATUS");
					job=rset1.getString("JOB");
					//System.out.println(per);
					//System.out.println(current_periodo);
					//System.out.println(status);
					if(!current_periodo.equals(per)&&status.equals("INITIAL")) break;
				}
			
			rset1.close();
			stmt.close();
			myConnection.close();
			
			if(status.equals("RUNNING")||status.equals("INITIAL")){
				tes=1;
				process="Process is running";
			}

			//Get Server Realpath
			String filePath = sfp;
			String zipFileSrc = filePath + "INPUTFILE_STG_SET_" + ncountry.toUpperCase() + "_" + current_periodo+ ".ZIP";
			try
    		{
			File startingDirectory= new File(filePath);
		    List<File> files = FileListing.getFileListing(startingDirectory);
		    for(File file:files)
		    {
		    	//Only process File
		    	if(file.isFile())
		    	{
		    		if(file.toString().toUpperCase().contains("INPUTFILE_STG_SET_" + ncountry.toUpperCase() + "_" + current_periodo + ".ZIP")){
		    			tes=0;
		    			break;
		    		}
		    		else tes=1;
		    	}
	    	}
	    }catch(Exception esf)
		{
	    	tes=1;
		}
	    
	    	request.setAttribute("fcountry", fcountry);
	    	request.setAttribute("cn_result", cn_result);
			request.setAttribute("current_period", current_period);
			request.setAttribute("filepath", zipFileSrc);
			request.setAttribute("tes", tes);
			request.setAttribute("process", process);
			request.setAttribute("job", job);
			request.setAttribute("status", status);
			
			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage().trim(), true);
		}
	}
}
