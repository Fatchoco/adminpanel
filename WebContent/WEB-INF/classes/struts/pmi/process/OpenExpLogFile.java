package struts.pmi.process;


import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import java.io.*;
import javax.servlet.*;



//levin17lacustre 25092009
public class OpenExpLogFile extends Action  {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String filename=(String)request.getParameter("filename");
		   // you  can get your base and parent from the database

		// you can  write http://localhost
		String filepath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+filename);

		BufferedInputStream buf=null;
		ServletOutputStream myOut=null;

		try{

			myOut = response.getOutputStream( );
		     File myfile = new File(filepath);
		     
		     //set response headers
		     response.setContentType("text/plain");
		     
		     response.addHeader(
		        "Content-Disposition","attachment; filename="+filename );

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