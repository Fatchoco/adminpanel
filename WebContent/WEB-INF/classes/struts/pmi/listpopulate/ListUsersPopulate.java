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

public class ListUsersPopulate extends Action {
	
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
		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		 Statement stmt = myConnection.createStatement();
		 ResultSet r = null;

	     	//check login stat
			if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != "" && !request.getSession().getAttribute("isAdmin").equals("0")))
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
					 if((request.getParameter("Search") != null && request.getParameter("Search") != ""))
					{
						search=request.getParameter("Search").toUpperCase().replaceAll("'","''");	
						
						query = "select * from (select a.*, rownum rnum from "; 
						query+= "(select USERID usid, ROLE, NVL(FULL_NAME,'-') full, NVL(OU,'-') ou, " +
								"(case instr(nvl(role,'-'),'OCC') when 0 then 'FALSE' ELSE 'TRUE' end) OCC, " +
								"(case instr(nvl(role,'-'),'TB-MI') when 0 then 'FALSE' ELSE 'TRUE' end) TB, " +
								"(case instr(nvl(role,'-'),'PMI') when 0 then 'FALSE' ELSE 'TRUE' end) PMI, " +
								"(case instr(nvl(role,'-'),'BR') when 0 then 'FALSE' ELSE 'TRUE' end) BR " +
								" from PMI_USER where UPPER(USERID) like '%"+search+"%' order by USERID) a where rownum<="+end+")"; 
						query+= " where rnum>"+start;
						queryCount="select count(*) from PMI_USER where upper(USERID) like '%"+search+"%'";
					}
					 
					else
					{
						query = "select * from (select a.*, rownum rnum from ";
						query+= "(select USERID usid, ROLE, NVL(FULL_NAME,'-') full, NVL(OU,'-') ou, " +
							"(case instr(nvl(role,'-'),'OCC') when 0 then 'FALSE' ELSE 'TRUE' end) OCC, " +
							"(case instr(nvl(role,'-'),'TB-MI') when 0 then 'FALSE' ELSE 'TRUE' end) TB, " +
							"(case instr(nvl(role,'-'),'PMI') when 0 then 'FALSE' ELSE 'TRUE' end) PMI, " +
							"(case instr(nvl(role,'-'),'BR') when 0 then 'FALSE' ELSE 'TRUE' end) BR " +
							" from PMI_USER order by USERID) a where rownum<="+end+")";
						query+=" where rnum>"+start;
						queryCount="select count(*) from PMI_USER";
						
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
					
				}
				catch(SQLException e)
				{
					System.out.println("ERROR : " + e);
					e.printStackTrace(System.out);
				}
				finally {

					if (r != null) r.close();
					if (stmt != null) stmt.close();
					if (myConnection != null && !myConnection.isClosed()) myConnection.close();
					
				}

				
				return new ActionForward(nextPage.getPath() + "?message=" + message ); 
	}
	

}
