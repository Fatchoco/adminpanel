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

public class ListMISPrdPopulate extends Action {
	
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
					 if((request.getParameter("Search") != null && request.getParameter("Search") != ""))
					{
						search=request.getParameter("Search").toUpperCase().replaceAll("'","''");	
						
						query = "select * from (select a.*, rownum rnum from "; 
						query+= "(select PARTITION_KEY||MIS_PRODUCT_CODE key, PARTITION_KEY pkey, MIS_PRODUCT_CODE pcode, " +
								"MIS_PRODUCT_DESC pdesc, PRODUCT_TAG_1 tag1, PRODUCT_TAG_2 tag2, " +
								"PRODUCT_TAG_3 tag3, PRODUCT_TAG_4 tag4, PRODUCT_TAG_5 tag5, " +
								"REMARK rmk, FLAG_NEW flag " +
								" from PMI_MIS_PRODUCT_DIM where PARTITION_KEY = (SELECT MAX(PARTITION_KEY) from PMI_PERIOD_DIM) AND MIS_PRODUCT_CODE LIKE '%"+search+"%' order by STANDARD_PRODUCT_CODE, REVENUE_TYPE, CHANNEL) a where rownum<="+end+")"; 
						query+= " where rnum>"+start;
						queryCount="select count(1) from PMI_MIS_PRODUCT_DIM where PARTITION_KEY = (SELECT MAX(PARTITION_KEY) from PMI_PERIOD_DIM) AND MIS_PRODUCT_CODE LIKE '%"+search+"%'";
					    //System.out.println(query);
					}
					 
					else
					{
						query = "select * from (select a.*, rownum rnum from "; 
						query+= "(select PARTITION_KEY||MIS_PRODUCT_CODE key, PARTITION_KEY pkey, MIS_PRODUCT_CODE pcode, " +
								"MIS_PRODUCT_DESC pdesc, PRODUCT_TAG_1 tag1, PRODUCT_TAG_2 tag2, " +
								"PRODUCT_TAG_3 tag3, PRODUCT_TAG_4 tag4, PRODUCT_TAG_5 tag5, " +
								"REMARK rmk, FLAG_NEW flag " +
								" from PMI_MIS_PRODUCT_DIM where PARTITION_KEY = (SELECT MAX(PARTITION_KEY) from PMI_PERIOD_DIM) order by STANDARD_PRODUCT_CODE, REVENUE_TYPE, CHANNEL) a where rownum<="+end+")"; 
						query+= " where rnum>"+start;
						queryCount="select count(1) from PMI_MIS_PRODUCT_DIM where PARTITION_KEY = (SELECT MAX(PARTITION_KEY) from PMI_PERIOD_DIM)";
						
					}
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

				
				return new ActionForward(nextPage.getPath() + "?message=" + message ); 
	}
	

}
