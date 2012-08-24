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

public class ExpCountryTarget extends Action  {

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
		String ExpName="ExpCountryTarget.txt";
		String row="";
		String testNull="";
		String Header="CLIENT_SUB_SEGMENT_GROUP\tPRODUCT_SEGMENT_CODE\tCOUNTRY_CODE\tTYPE\tYEAR\tTARGET_REVENUE_NUMERATOR\tTARGET_REVENUE_DENOMINATOR\tTARGET_LIABILITIES_NUMERATOR\tTARGET_LIABILITIES_DENOMINATOR\tTARGET_CONTINGENTS_NUMERATOR\tTARGET_CONTINGENTS_DENOMINATOR\tTARGET_ASSETS_NUMERATOR\tTARGET_ASSETS_DENOMINATOR\tTARGET_AUA_NUMERATOR\tTARGET_AUA_DENOMINATOR\tTARGET_RORWA_NUMERATOR\tTARGET_RORWA_DENOMINATOR\tTarget_Revenue_per_Salesperson_Num\tTarget_Revenue_per_Salesperson_Den";
		String writeFilePath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+ExpName);;
		PrintWriter pw = new PrintWriter(new FileOutputStream(writeFilePath));
		pw.println(Header);
		String yeark="";

		if((request.getParameter("ykey") != null && request.getParameter("ykey") != "")) 
		{
			yeark = request.getParameter("ykey");
	
		
		//write message code to file
		String query = "select * from PMI_COUNTRY_TARGET where year_data = '" + yeark + "'" +
				" order by client_sub_segment_code";
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
	    		if (!r.wasNull()) {	row=row+r.getString(4)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(5);
	    		if (!r.wasNull()) {	row=row+r.getString(5)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(6);
	    		if (!r.wasNull()) {	row=row+r.getString(6)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(7);
	    		if (!r.wasNull()) {	row=row+r.getString(7)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(8);
	    		if (!r.wasNull()) {	row=row+r.getString(8)+"\t"; } else { row = row+"\t"; }
	    		testNull = r.getString(9);
	    		if (!r.wasNull()) {	row=row+r.getString(9)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(10);
	    		if (!r.wasNull()) {	row=row+r.getString(10)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(11);
	    		if (!r.wasNull()) {	row=row+r.getString(11)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(12);
	    		if (!r.wasNull()) {	row=row+r.getString(12)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(13);
	    		if (!r.wasNull()) {	row=row+r.getString(13)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(14);
	    		if (!r.wasNull()) {	row=row+r.getString(14)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(15);
	    		if (!r.wasNull()) {	row=row+r.getString(15)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(16);
	    		if (!r.wasNull()) {	row=row+r.getString(16)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(17);
	    		if (!r.wasNull()) {	row=row+r.getString(17)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(18);
	    		if (!r.wasNull()) {	row=row+r.getString(18)+"\t"; } else { row =row+ "\t"; }
	    		testNull = r.getString(19);
	    		if (!r.wasNull()) {	row=row+r.getString(19)+"\t"; } else { row =row+ "\t"; }
		
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