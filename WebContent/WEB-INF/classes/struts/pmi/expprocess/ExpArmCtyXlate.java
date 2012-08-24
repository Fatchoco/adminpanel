package struts.pmi.expprocess;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.io.*;

import javax.servlet.*;

import java.sql.*;

public class ExpArmCtyXlate extends Action  {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		Statement stmt = myConnection.createStatement();
		ResultSet r = null;
		
		//initiate variable
		String ExpName="ExpArmCtyXlate.txt";
		String row="";
		String Header="BUSINESS_DATE\tCOUNTRY_CODE\tARM_CD\tRM_ID\tRM_NAME\tCUST_SEGMENT\tCITY\tREGION\tPSGL_CUST_CLASS\tBIP_CODE\tCREATED_DATE\tCREATED_USER";
		String writeFilePath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+ExpName);;
		PrintWriter pw = new PrintWriter(new FileOutputStream(writeFilePath));
		pw.println(Header);
		String partk="";

		if((request.getParameter("pkey") != null && request.getParameter("pkey") != "")) 
		{
			partk = request.getParameter("pkey") + "%";
	
		
		//write message code to file
		String query =  "select to_char(BUSINESS_DATE,'MM/DD/YYYY') bizd, NVL(COUNTRY_CODE,'-'), NVL(ARM_CD,'-'), " +
					    " NVL(RM_ID,'-'), NVL(RM_NAME,'-'), NVL(CUST_SEGMENT,'-'), NVL(CITY,'-'), " +
					    " NVL(REGION,'-'), NVL(PSGL_CUST_CLASS,'-'), NVL(BIP_CODE,'-'), CREATE_DATE, CREATE_USER " +
						" from PMI_ARM_CITY_XLATE where PARTITION_KEY like '" + partk + "'" +
						" order by COUNTRY_CODE ASC, BUSINESS_DATE DESC";
		try
		{
			r = stmt.executeQuery(query);
			while (r.next())
			{    
				row=r.getString(1)+"\t"+r.getString(2)+"\t"+r.getString(3)+"\t"+r.getString(4)+"\t"+
					r.getString(5)+"\t"+r.getString(6)+"\t"+r.getString(7)+"\t"+r.getString(8)+"\t"+
					r.getString(9)+"\t"+r.getString(10)+"\t"+r.getString(11)+"\t"+r.getString(12);
				pw.println(row);
			}
			pw.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally {
			if (r != null) r.close();
			if (stmt != null) stmt.close();
			if (myConnection != null && !myConnection.isClosed()) myConnection.close();
		};

		}
		
		BufferedInputStream buf=null;
		ServletOutputStream myOut=null;

		try{

			myOut = response.getOutputStream( );
		     File myfile = new File(writeFilePath);
		     
		     //set response headers
		     response.setContentType("text/plain");
		     
		     response.addHeader(
		        "Content-Disposition","attachment; filename="+ExpName );

		     response.setContentLength( (int) myfile.length( ) );
		     
		     FileInputStream input = new FileInputStream(myfile);
		     buf = new BufferedInputStream(input);
		     int readBytes = 0;

		     //read from the file; write to the ServletOutputStream
		     while((readBytes = buf.read( )) != -1)
		       myOut.write(readBytes);

		} catch (IOException ioe){
		     
		        throw new ServletException(ioe.getMessage( ));
		         
		     } finally {
		         
		     //close the input/output streams
		         if (myOut != null)
		             myOut.close( );
		          if (buf != null)
		          buf.close( );
		         
		     }
		return null;
		}

}