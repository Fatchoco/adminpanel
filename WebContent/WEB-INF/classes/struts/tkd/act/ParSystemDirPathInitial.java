package struts.tkd.act;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.actfrm.ActionFormValidated;

public class ParSystemDirPathInitial extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception{
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			
			// Variables
			String dirpath;
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			dataSource		= getDataSource(request,"IMS_STG");
			myConnection	= dataSource.getConnection();
			Statement stmt	= myConnection.createStatement();
			ResultSet rset;
			
			rset = stmt.executeQuery("SELECT MAX(SOURCE_FILE_PATH) SOURCE_FILE_PATH FROM PAR_SYSTEM");
			
			rset.next();
			dirpath = rset.getString("SOURCE_FILE_PATH");
			
			// Close DB Connection
			rset.close();
			stmt.close();
			myConnection.close();
			
			// Set Variable for next page
			request.setAttribute("dirpath", dirpath);
			return forward;
	  }
}
