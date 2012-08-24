package struts.pmi.listpopulate;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;

public class ListPmiSwitchPopulate extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String query="";
		String queryCount="";
		int totalRowPerPage = 20;
		String search="";
		String message="";

		int page=1;
		int start=0;
		int end=0;
		int count=0;
		int tot_page=0;
		String opMode = request.getParameter("m");
		String opModeStr = "";
		
		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		 Statement stmt = myConnection.createStatement();
		 ResultSet r;

		 //check login stat
			if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != ""))
			{
				ActionForward login=mapping.findForward("loginpage");
				return login;
				
			}			

				try
				{
					 if((request.getParameter("message") != null && request.getParameter("message") != ""))
					{
							message=(request.getParameter("message"));
							
						
					}

					 if((request.getParameter("Page") != null && request.getParameter("Page") != ""))
					{
						page=Integer.parseInt(request.getParameter("Page"));
						
					
					}
					 start=totalRowPerPage*(page-1);
					 end=totalRowPerPage*(page);

						if (opMode.equals("p")){
							opModeStr = "PMI";
						}else if (opMode.equals("t")){
							opModeStr = "TBMI";
						}else if (opMode.equals("b")){
							opModeStr = "BR";
						};

					 
					 if((request.getParameter("Search") != null && request.getParameter("Search") != ""))
					{
						search=request.getParameter("Search").toUpperCase().replaceAll("'","''");	
						
						query = "select * from (select a.*, rownum rnum from "; 
						query+= "(select TARGET_CODE||COUNTRY_ID key, TARGET_CODE tgt, COUNTRY_ID cty, nvl(SOURCE_CODE,'-') src, " +
								" nvl(EFFECTIVE_PERIOD,'-') prd, nvl(substr(EFFECTIVE_PERIOD,1,6),'-') prd2, nvl(substr(EFFECTIVE_PERIOD,1,4),'-') yr, " +
								" nvl(substr(EFFECTIVE_PERIOD,5,2),'-') mn " +
								" from PMI_COUNTRY_SWITCH where TARGET_CODE = '" + opModeStr + "' and COUNTRY_ID LIKE '%"+search+"%' order by COUNTRY_ID) a where rownum<="+end+")"; 
						query+= " where rnum>"+start;
						queryCount="select count(*) from PMI_COUNTRY_SWITCH where TARGET_CODE = '" + opModeStr + "' AND COUNTRY_ID LIKE '%"+search+"%'";
					    //System.out.println(query);
					}
					 
					else
					{
						query = "select * from (select a.*, rownum rnum from ";
						query+= "(select TARGET_CODE||COUNTRY_ID key, TARGET_CODE tgt, COUNTRY_ID cty, nvl(SOURCE_CODE,'-') src, " +
								" nvl(EFFECTIVE_PERIOD,'-') prd, nvl(substr(EFFECTIVE_PERIOD,1,6),'-') prd2, nvl(substr(EFFECTIVE_PERIOD,1,4),'-') yr, " +
								" nvl(substr(EFFECTIVE_PERIOD,5,2),'-') mn " +
								" from PMI_COUNTRY_SWITCH where TARGET_CODE = '" + opModeStr + "' order by COUNTRY_ID) a where rownum<="+end+")";
						query+=" where rnum>"+start;
						queryCount="select count(*) from PMI_COUNTRY_SWITCH WHERE TARGET_CODE = '" + opModeStr + "'";
						
					}
					//System.out.println(query);
					r=stmt.executeQuery(queryCount);
					r.next();
					count=r.getInt(1);
					tot_page=(int)Math.floor(count/totalRowPerPage);
					if(count%totalRowPerPage>0)tot_page+=1;
					request.setAttribute("searchq",search);
					request.setAttribute("count",count );	
					request.setAttribute("tot_page",tot_page );	
					request.setAttribute("page",page );	
					r.close();
					r = stmt.executeQuery(query);
					Result result = ResultSupport.toResult(r);
					request.setAttribute("listResult",result );	
					
					r.close();
					stmt.close();
					myConnection.close();
				}
				catch(SQLException e)
				{
					System.out.println("ERROR : " + e);
					e.printStackTrace(System.out);
				}

				
				return new ActionForward(nextPage.getPath() + "?message=" + message + "&m=" + opMode ); 
	}
	

}
