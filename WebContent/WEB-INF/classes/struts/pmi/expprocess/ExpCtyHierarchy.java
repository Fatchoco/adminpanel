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

//levin17lacustre 25092009
public class ExpCtyHierarchy extends Action  {

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
		
		String testNull = "";
		
		//initiate variable
		String ExpName="ExpCtyHierarchy.txt";
		String row="";
		String Header="COUNTRY_CODE\tCOUNTRY_NAME\tREGION_ID\tREGION_NAME\tTOP_COUNTRY_TAG\tREGION_CLUSTER";
		String writeFilePath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+ExpName);;
		PrintWriter pw = new PrintWriter(new FileOutputStream(writeFilePath));
		pw.println(Header);
		
		//write message code to file
		String query = "select COUNTRY_CODE ctid, COUNTRY_NAME ctyname, REGION_ID regid, REGION_NAME regname, TOP_COUNTRY_TAG topcty, " +
				" REGION_CLUSTER clst from PMI_COUNTRY_HIERARCHY order by COUNTRY_CODE";
		try
		{
		
		
			r = stmt.executeQuery(query);
			while (r.next())
			{    
	    		testNull = r.getString(1);
	    		if (!r.wasNull()) {	row=r.getString(1)+"\t"; } else { row = "\t"; }

	    		testNull = r.getString(2);
	    		if (!r.wasNull()) {	row=row+r.getString(2)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(3);
	    		if (!r.wasNull()) {	row=row+r.getString(3)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(4);
	    		if (!r.wasNull()) {	row=row+r.getString(4)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(5);
	    		if (!r.wasNull()) {	row=row+r.getString(5)+"\t"; } else { row = row + "\t"; }
	    		
	    		testNull = r.getString(6);
	    		if (!r.wasNull()) {	row=row+r.getString(6); };
				
				//row=r.getString(1)+"\t"+r.getString(2)+"\t"+r.getString(3)+"\t"+r.getString(4)+"\t"+r.getString(5)+"\t"+r.getString(6) ; 	
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