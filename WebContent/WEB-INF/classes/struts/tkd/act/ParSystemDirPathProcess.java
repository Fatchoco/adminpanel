package struts.tkd.act;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.actfrm.FormParSystem;
import struts.tkd.actfrm.ActionFormValidated;

public class ParSystemDirPathProcess extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			
			try
			{
				// Get variable from Form
				FormParSystem myForm = (FormParSystem) form;
				String dirpath = myForm.getCur_period();
				dirpath = dirpath.trim();
				
				if(!dirpath.equals(null) && !dirpath.equals(""))
				{
					// Create DB Connection
					javax.sql.DataSource	dataSource;
					java.sql.Connection		myConnection;
					dataSource		= getDataSource(request,"IMS_STG");
					myConnection	= dataSource.getConnection();
					Statement stmt	= myConnection.createStatement();
					stmt	= myConnection.createStatement();
					
					stmt.executeUpdate("UPDATE PAR_SYSTEM SET SOURCE_FILE_PATH = '"+ dirpath +"'");
	
					stmt.close();
					myConnection.close();
					
					return new ActionForward(forward.getPath()+"?message=Source File Directory Path has been updated");
				}
				else
				{
					return new ActionForward(forward.getPath()+"?message=Source File Directory Path cannot be blank");
				}
			}
			catch(Exception e)
			{
				return new ActionForward(forward.getPath()+"?message="+e.getMessage());
			}
	  }
}