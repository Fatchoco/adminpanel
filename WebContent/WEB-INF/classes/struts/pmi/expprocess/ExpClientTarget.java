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

public class ExpClientTarget extends Action  {

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
		String ExpName="ExpClientTarget.txt";
		String row="";
		String testNull="";
		String Header="GROUP_SEGMENT_CODE\tCOUNTRY_CODE\tYEAR\tTARGET_50M\tTARGET_25M\tTARGET_5M\tTARGET_1M\tTARGET_2TB\tTARGET_2BL";
		String writeFilePath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+ExpName);;
		PrintWriter pw = new PrintWriter(new FileOutputStream(writeFilePath));
		pw.println(Header);
		String yeark="";

		if((request.getParameter("ykey") != null && request.getParameter("ykey") != "")) 
		{
			yeark = request.getParameter("ykey");
	
		
		//write message code to file
		String query = "select GROUP_SEGMENT, COUNTRY_CODE, YEAR_DATA, TARGET_50M, TARGET_25M, TARGET_5M, TARGET_1M, TARGET_2TB, TARGET_2BL from PMI_CLIENT_TARGET where year_data = '" + yeark + "'" +
				" order by group_segment";
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