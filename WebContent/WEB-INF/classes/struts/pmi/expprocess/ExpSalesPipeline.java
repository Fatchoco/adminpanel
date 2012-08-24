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

public class ExpSalesPipeline extends Action  {

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
		String ExpName="ExpSalesPipeline.txt";
		String row="";
		String testNull="";
		String Header="PRODUCT_SEGMENT_CODE\tORIGINATION_COUNTRY_CODE\tBOOKING_LOCATION\tDATE_FIRST\tCLOSE_MONTH\tLOST_MONTH\tDEAL_STATUS\t12_M_REVENUE\tLEID\tSUB_PROFILE_ID";
		String writeFilePath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+ExpName);;
		PrintWriter pw = new PrintWriter(new FileOutputStream(writeFilePath));
		pw.println(Header);

	
		
		//write message code to file
		String query = "select PRODUCT_SEGMENT_CODE, ORIGINATION_COUNTRY_CODE, BOOKING_LOCATION, DATE_FIRST, CLOSE_MONTH, LOST_MONTH, DEAL_STATUS, REVENUE_12_M, LEID, SUB_PROFILE_ID from PMI_SALES_PIPELINE " +
				" order by product_segment_code";
		try
		{
		
		
			r = stmt.executeQuery(query);
			while (r.next())
			{    

	    		testNull = r.getString(1);
	    		if (!r.wasNull()) {	row=r.getString(1)+"\t"; } else { row = "\t"; }
	    		testNull = r.getString(2);
	    		if (!r.wasNull()) {	row=row+r.getString(2)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(3);
	    		if (!r.wasNull()) {	row=row+r.getString(3)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(4);
	    		if (!r.wasNull()) {	row=row+"01"+"/"+r.getString(4).substring(4,6)+"/"+r.getString(4).substring(0,4)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(5);
	    		if (!r.wasNull()) {	row=row+r.getString(5).substring(0,6)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(6);
	    		if (!r.wasNull()) {	row=row+r.getString(6).substring(0,6)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(7);
	    		if (!r.wasNull()) {	row=row+r.getString(7)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(8);
	    		if (!r.wasNull()) {	row=row+r.getString(8)+"\t"; } else { row = row+"\t"; }
	    		testNull = r.getString(9);
	    		if (!r.wasNull()) {	row=row+r.getString(9)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(10);
	    		if (!r.wasNull()) {	row=row+r.getString(10)+"\t"; } else { row =row+ "\t"; }


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