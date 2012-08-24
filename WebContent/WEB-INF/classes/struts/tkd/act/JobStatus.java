package struts.tkd.act;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.function.getPath;
import struts.tkd.actfrm.ActionFormValidated;

public class JobStatus extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {

		try
		{
			String j_name	= "RUNPROCESSJOB";
			String query	= "SELECT * FROM USER_SCHEDULER_JOBS WHERE UPPER(JOB_NAME) = UPPER('"+j_name+"')";
			String res_db	= "";
			String res_db_comment = "";
			String result_to_print = "";
			
			//Disable Caching!!
			response.setHeader( "Pragma", "no-cache" );
			response.addHeader( "Cache-Control", "must-revalidate" );
			response.addHeader( "Cache-Control", "no-cache" );
			response.addHeader( "Cache-Control", "no-store" );
			response.setDateHeader("Expires", 0);  
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			Statement				stmt=null;
			ResultSet				rset=null;
			dataSource		= getDataSource(request,"TKD_DIST");
			myConnection	= dataSource.getConnection();
			stmt			= myConnection.createStatement();
			
			try
			{
				rset = stmt.executeQuery(query);
				if(rset.next())
				{
					res_db = rset.getString("STATE");
					res_db_comment = rset.getString("COMMENTS");
					result_to_print = res_db+','+res_db_comment;
				}
				else
				{
					result_to_print = "NOJOBENTRY,NOJOBENTRY";
				}
			}
			catch(Exception ee)
			{
				throw new Exception(ee);
			}
			finally
			{
				if(rset!=null)rset.close();
				if(stmt!=null)stmt.close();
				if(myConnection!=null)myConnection.close();
			}
			
			request.setAttribute("state", result_to_print.toUpperCase());
			
			return mapping.findForward("forward");
		}
		catch(Exception e)
		{
			return new ActionForward(mapping.findForward("forward").getPath()+"?message="+e.getMessage().trim(), true);
		}
	  }
}
