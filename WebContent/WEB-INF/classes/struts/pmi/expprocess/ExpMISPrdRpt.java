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
public class ExpMISPrdRpt extends Action  {

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
		String ExpName="ExpMISPrdRpt.txt";
		String row="";
		String Header="MIS_PRODUCT_CODE\tREPORT_ID\tLABEL_CATEGORY_ID\tLABEL_CATEGORY_DESC\tLABEL_ID\tLABEL DESC";
		String writeFilePath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+ExpName);;
		PrintWriter pw = new PrintWriter(new FileOutputStream(writeFilePath));
		pw.println(Header);
		
		//write message code to file
		String query = "select nvl(MIS_PRODUCT_CODE,'-') mis, NVL(REPORT_ID,'-') rpid, " +
							" LABEL_CATEGORY_ID catd, NVL(LABEL_CATEGORY_DESC,'-') cat, " +
							" LABEL_ID lblid,  NVL(LABEL,'-') lbl " +
							" from PMI_MIS_PRD_RPT_MAPPING order by MIS_PRODUCT_CODE";
		try
		{
		
		
			r = stmt.executeQuery(query);
			while (r.next())
			{    
				row=r.getString(1)+"\t"+r.getString(2)+"\t"+r.getString(3)+"\t"+r.getString(4)+"\t"+r.getString(5)+"\t"+r.getString(6) ; 	
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