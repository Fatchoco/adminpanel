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

public class ExpPmiSwitch extends Action  {

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
		String opMode = request.getParameter("m");
		String opModeStr ="";
		
		if (opMode.equals("p")){
			opModeStr = "PMI";
		}else if (opMode.equals("t")){
			opModeStr = "TBMI";
		}else if (opMode.equals("b")){
			opModeStr = "BR";
		};
		
		//initiate variable
		String ExpName="Exp" + opModeStr + "Switch.txt";
		String row="";
		String Header="COUNTRY_ID\tSOURCE_CODE\tEFFECTIVE_PERIOD";
		String writeFilePath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+ExpName);;
		PrintWriter pw = new PrintWriter(new FileOutputStream(writeFilePath));
		pw.println(Header);
		
		//write message code to file
		String query = "select COUNTRY_ID cty, nvl(SOURCE_CODE,'-') src, nvl(substr(EFFECTIVE_PERIOD,1,6),'-') prd " +
				" from PMI_COUNTRY_SWITCH where TARGET_CODE = '" + opModeStr + "' order by COUNTRY_ID";
		try
		{
		
		
			r = stmt.executeQuery(query);
			while (r.next())
			{    
				row=r.getString(1)+"\t"+r.getString(2)+"\t"+r.getString(3); 	
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