package struts.tkd.act;

import java.sql.CallableStatement;
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
import struts.tkd.function.UISummaryFunc;

public class ProcessAdjustmentToReportProcess extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {
			ActionForward forward = null;
			forward = mapping.findForward("loading");
			
			try
			{
				FormParSystem myForm = (FormParSystem) form;
				String current_period = myForm.getCur_period();
				String runtype = request.getParameter("partialrun")==null?"":request.getParameter("partialrun");
				
				/* ** Start of UI Summary ** */
				javax.sql.DataSource	summaryDataSource;
				summaryDataSource	= getDataSource(request, "TKD_DIST_STG");
				
				UISummaryFunc.insertLog(summaryDataSource, 1, "nullfornow", "Run Process", "Run Adjustment Process", "", "Adjustment Period = "+current_period, request.getSession().getAttribute("USER_NAME").toString());
				/* ** End of UI Summary ** */
				
				//DB Connection
				javax.sql.DataSource	dataSource;
				java.sql.Connection		myConnection;
				dataSource		= getDataSource(request,"TKD_DIST");
				myConnection	= dataSource.getConnection();
				Statement stmt = null;
				ResultSet rset = null;
				CallableStatement cs = null;
				
				//Job Variables
				String j_name	= "RUNPROCESSJOB";
				String j_state	= "";
				String qcheck	= "SELECT * FROM USER_SCHEDULER_JOBS WHERE UPPER(JOB_NAME) = UPPER('"+j_name+"')";
				
				// Get variable from Form
				if(current_period == null)
				{
					myConnection.close();
					forward = mapping.findForward("error");
					return new ActionForward(forward.getPath(),true);
				}
				else
				{
					//Check whether the Job is running or not.
					stmt	= myConnection.createStatement();
					rset	= stmt.executeQuery(qcheck);
					
					//If The Job exists and in SCHEDULED or RUNNING state, go to AjaxCheckPage. If not then Run process
					if(rset.next())
					{
						j_state = rset.getString("STATE");
						rset.close();
						stmt.close();
						myConnection.close();
						if(j_state.equalsIgnoreCase("SCHEDULED") || j_state.equalsIgnoreCase("RUNNING"))
						{
							forward = mapping.findForward("loading");
							return forward;
						}
					}
					else
					{
						rset.close();
						stmt.close();
						myConnection.close();
					}
					
					//Running the Process in background using DBMS_SCHEDULER
					dataSource		= getDataSource(request,"TKD_DIST");
					myConnection	= dataSource.getConnection();
					
					//Try to delete the Job
					try
					{
						cs	= myConnection.prepareCall("{call dbms_scheduler.drop_job(?)}");
						cs.setString(1, j_name);
						cs.execute();
					}
					catch(Exception e_del_job)
					{
						System.out.print(e_del_job.getMessage());
					}
					finally
					{
						if(cs!=null)cs.close();
						if(myConnection!=null)myConnection.close();
					}
					
					dataSource		= getDataSource(request,"TKD_DIST");
					myConnection	= dataSource.getConnection();
					//Submit ProcessJob
					try
					{
						cs	= myConnection.prepareCall("{call dbms_scheduler.create_job(?,job_type=>'plsql_block',job_action=>'DECLARE erc VARCHAR2(255); erm VARCHAR2(4000);begin UPDATE RUN_PROCESS_STATUS SET FLAG_RUNNING=''Y'', ERROR_CD=NULL, ERROR_MSG=NULL, UPDATED_DATE=SYSDATE, UPDATED_USER=USER;COMMIT; sp_exec_dimension(); sp_fact_sales_temp_adj(''"+current_period+"''); sp_fact_sales_new(); sp_excp_customer_product(); UPDATE RUN_PROCESS_STATUS SET FLAG_RUNNING=''N'', ERROR_CD=NULL, ERROR_MSG=NULL, UPDATED_DATE=SYSDATE, UPDATED_USER=USER;COMMIT; EXCEPTION WHEN OTHERS THEN erc := SQLCODE; erm := SUBSTR(SQLERRM,1,4000); UPDATE RUN_PROCESS_STATUS SET FLAG_RUNNING=''Y'', ERROR_CD=erc, ERROR_MSG=erm, UPDATED_DATE=SYSDATE, UPDATED_USER=USER;COMMIT; RAISE; end;', enabled=>true, auto_drop=>false, comments=>'ADJUSTMENT')}");
						cs.setString(1, j_name);
						cs.execute();
					}
					catch(Exception e_del_job)
					{
						System.out.print(e_del_job.getMessage());
					}
					finally
					{
						if(cs!=null)cs.close();
						if(myConnection!=null)myConnection.close();
					}
				}
				
				return forward;
			}
			catch(Exception e)
			{
				request.setAttribute("errormsg", "error");
				forward = mapping.findForward("error");
				return new ActionForward(forward.getPath()+"?message="+e.getMessage());
			}
	  }
}