package struts.pmi.listpopulate;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.pmi.form.process.GetPartition;

import java.sql.*;
import java.util.Calendar;

public class ListClientTargetPopulate extends Action {
	
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
		String currYear="";
		
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
		 ResultSet r;
		 
			//get current partition
			GetPartition part=new GetPartition();
			String partition=part.execute(1, myConnection);
			
			Calendar cal= Calendar.getInstance();
			int month2=Integer.parseInt(partition.substring(4,6));
			int year2=Integer.parseInt(partition.substring(0,4));
			cal.set(Calendar.MONTH, month2-1);
			cal.set(Calendar.YEAR, year2);			
			cal.set(Calendar.DATE,1);						
			int lastDate = cal.getActualMaximum(cal.DATE);
			
		    String part2 = partition.substring(0,4) + partition.substring(4,6);

		    
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
					 
					 if((request.getParameter("Year") != null && request.getParameter("Year") != ""))
						{
								currYear=(request.getParameter("Year"));
						}
					 else {
						   currYear = partition.substring(0,4);
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
						query+= "(select * " +
								" from PMI_CLIENT_TARGET where " +
								" group_segment like '%"+search+"%' and YEAR_DATA = '" + currYear + "' order by group_segment) a where rownum<="+end+")"; 
						query+= " where rnum>"+start;
						queryCount="select count(*) from PMI_CLIENT_TARGET where " +
								" group_segment like '%"+search+"%' and YEAR_DATA = '" + currYear + "'";
					}
					 
					else
					{
						query = "select * from (select a.*, rownum rnum from ";
						query+= "(select * " +
						" from PMI_CLIENT_TARGET where year_data = '" + currYear + "'" +
						" order by group_segment) a where rownum<="+end+")"; 
						query+=" where rnum>"+start;
						queryCount="select count(*) from PMI_CLIENT_TARGET " +
								" where YEAR_DATA = '" + currYear + "'";
						
					}
					 
					//System.out.println(query);
					//System.out.println(queryCount);
					
					r=stmt.executeQuery(queryCount);
					r.next();
					count=r.getInt(1);
					tot_page=(int)Math.floor(count/totalRowPerPage);
					if(count%totalRowPerPage>0)tot_page+=1;
					request.setAttribute("searchq",search);
					request.setAttribute("count",count );	
					request.setAttribute("tot_page",tot_page );	
					request.setAttribute("page",page );	
					request.setAttribute("month2",month2);
					request.setAttribute("year2",year2);
					request.setAttribute("partition",currYear);
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

				
				return new ActionForward(nextPage.getPath() + "?message=" + message ); 
	}
	

}
