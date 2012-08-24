package struts.tkd.act;

import java.sql.PreparedStatement;
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

import struts.tkd.actfrm.ActionFormValidated;

public class DeleteColumnMapping extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			
			try
			{
				request.setCharacterEncoding("UTF-8");
				// Variables
				String qdelete;
				
				// Create DB Connection
				javax.sql.DataSource	dataSource;
				java.sql.Connection		myConnection;
				Statement				stmt;
				ResultSet				rset;
				dataSource		= getDataSource(request,"TKD_DIST_STG");
				myConnection	= dataSource.getConnection();
				
				qdelete = "DELETE FROM MAP_FILE_COLUMN WHERE FILE_NAME=?";
				PreparedStatement ppStmtDelete = myConnection.prepareStatement(qdelete);
				if(request.getParameter("DeleteList")!=null && !request.getParameter("DeleteList").equals(""))
				{
					String[] deleteList = request.getParameter("DeleteList").split("_xDelimitx_");
					for(int i=0; i<deleteList.length; i++)
					{
						if(!deleteList[i].equals(""))
						{
							ppStmtDelete.setString(1,deleteList[i].toString());
							ppStmtDelete.executeUpdate();
						}
					}
					
					ppStmtDelete.close();
					myConnection.close();
					//response.sendRedirect(forward.getPath()+"?message=Delete success");
					return new ActionForward(forward.getPath()+"?message=Delete success",true);
				}
				else
				{
					ppStmtDelete.close();
					myConnection.close();
					//response.sendRedirect(forward.getPath()+"?message=No row deleted");
					return new ActionForward(forward.getPath()+"?message=No row deleted", true);
				}
			}
			catch(Exception e)
			{
				return new ActionForward(forward.getPath()+"?message="+e.getMessage().trim());
			}
	  }
}
