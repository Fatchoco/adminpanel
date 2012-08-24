package struts.pmi.expprocess;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.pmi.form.process.AddSpace;

import java.io.*;

import javax.servlet.*;

import java.sql.*;

public class ExpPmiKorea extends Action  {

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
		String ExpName="ExpPmiKorea.txt";
		String row="";
/*		
		String Header="CLIENT_CODE\tBOOKING_LOCATION\tCLIENT_DESC\tGROUP_SEGMENT_ID\tCLIENT_SEGMENT_ID\t" +
					  "INTERNAL_CRG\tCLIENT_CRG\tFAM_CODE\tFAM_DESC\tFAM_LOCATION\tCOUNTRY_REGISTRATION\t" +
					  "COUNTRY_DOMICILE\tGROUP_CODE\tGROUP_DESC\tGROUP_TYPE\tCLIENT_INDUSTRY_ID\tFAM_CODE2\t" +
					  "RAM_CODE\tTBS_CODE\tRTS_CODE\tERROR_CODE";
*/
		String writeFilePath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+ExpName);;
		PrintWriter pw = new PrintWriter(new FileOutputStream(writeFilePath));
		//pw.println(Header);
		String partk="";
		
		String testNull = "";

		if((request.getParameter("pkey") != null && request.getParameter("pkey") != "")) 
		{
			partk = request.getParameter("pkey") + "%";
	
		
		//write message code to file
		String query =  "select CLIENT_CODE client, BOOKING_LOCATION bookloc, CLIENT_DESC cldesc, " +
		" GROUP_SEGMENT_ID grpseg, CLIENT_SEGMENT_ID clseg, INTERNAL_CRG icrg, CLIENT_CRG ccrg, " +
		" FAM_CODE fam, FAM_DESC famd, FAM_LOCATION famloc, " +
		" COUNTRY_REGISTRATION ctyreg, COUNTRY_DOMICILE ctydom, GROUP_CODE grpcd, GROUP_DESC grpdesc, " +
		" GROUP_TYPE grptp, CLIENT_INDUSTRY_ID ids, FAM_CODE2 FAM2, RAM_CODE ram,   " +
		" TBS_CODE tbs, RTS_CODE rts " +
		" from PMI_KOREA_CLIENT where " +
		" PARTITION_KEY like '" + partk + "%' order by CLIENT_CODE"; 
		try
		{
			int i = 1;
			int j = 1;
			String spcTemp = "";

			r = stmt.executeQuery(query);
			
			AddSpace spc = new AddSpace();
			
			while (r.next())
			{    
//				row=r.getString(1)+"\t"+r.getString(2)+"\t"+r.getString(3)+"\t"+r.getString(4)+"\t"+
//					r.getString(5)+"\t"+r.getString(6)+"\t"+r.getString(7)+"\t"+r.getString(8)+"\t"+
//					r.getString(9)+"\t"+r.getString(10)+"\t"+r.getString(11)+"\t"+r.getString(12)+"\t"+
//					r.getString(13)+"\t"+r.getString(14)+"\t"+r.getString(15)+"\t"+r.getString(16)+"\t"+
//					r.getString(17)+"\t"+r.getString(18)+"\t"+r.getString(19)+"\t"+r.getString(20)+"\t";

			    while (i <= 432){
			    	if (i == 1) {
			    		testNull = r.getString(1);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(1);
				    		i = i + r.getString(1).length();
				    		spcTemp = spc.execute(r.getString(1), 1, 16);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 1, 16);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //client

			    	if (i == 16) {
			    		testNull = r.getString(2);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(2);
				    		i = i + r.getString(2).length();
				    		spcTemp = spc.execute(r.getString(2), 16, 18);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 16, 18);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //booking loc

			    	if (i == 18) {
			    		testNull = r.getString(3);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(3);
				    		i = i + r.getString(3).length();
				    		spcTemp = spc.execute(r.getString(3), 18, 118);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 18, 118);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //client desc

			    	if (i == 118) {
			    		testNull = r.getString(4);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(4);
				    		i = i + r.getString(4).length();
				    		spcTemp = spc.execute(r.getString(4), 118, 120);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 118, 120);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //group segment id

			    	if (i == 120) {
			    		testNull = r.getString(5);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(5);
				    		i = i + r.getString(5).length();
				    		spcTemp = spc.execute(r.getString(5), 120, 122);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 120, 122);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //client segment id

			    	if (i == 122) {
			    		testNull = r.getString(6);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(6);
				    		i = i + r.getString(6).length();
				    		spcTemp = spc.execute(r.getString(6), 122, 132);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 122, 132);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; // internal crg

			    	if (i == 132) {
			    		testNull = r.getString(7);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(7);
				    		i = i + r.getString(7).length();
				    		spcTemp = spc.execute(r.getString(7), 132, 135);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 132, 135);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; // client crg
			    	
			    	if (i == 135) {
			    		testNull = r.getString(8);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(8);
				    		i = i + r.getString(8).length();
				    		spcTemp = spc.execute(r.getString(8), 135, 145);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 135, 145);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //fam code

			    	if (i == 145) {
			    		testNull = r.getString(9);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(9);
				    		i = i + r.getString(9).length();
				    		spcTemp = spc.execute(r.getString(9), 145, 225);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 145, 225);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; // fam desc
			    	
			    	if (i == 225) {
			    		testNull = r.getString(10);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(10);
				    		i = i + r.getString(10).length();
				    		spcTemp = spc.execute(r.getString(10), 225, 227);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 225, 227);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; // fam location

			    	if (i == 227) {
			    		testNull = r.getString(11);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(11);
				    		i = i + r.getString(11).length();
				    		spcTemp = spc.execute(r.getString(11), 227, 229);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 227, 229);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //country regsitration

			    	if (i == 229) {
			    		testNull = r.getString(12);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(12);
				    		i = i + r.getString(12).length();
				    		spcTemp = spc.execute(r.getString(12), 229, 231);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 229, 231);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	};  //country domicile
			    	
			    	if (i == 231) {
			    		testNull = r.getString(13);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(13);
				    		i = i + r.getString(13).length();
				    		spcTemp = spc.execute(r.getString(13), 231, 401);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 231, 401);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //group code

			    	if (i == 251) {
			    		testNull = r.getString(14);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(14);
				    		i = i + r.getString(14).length();
				    		spcTemp = spc.execute(r.getString(14), 251, 401);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 251, 401);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; // group desc 

			    	if (i == 401) {
			    		testNull = r.getString(15);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(15);
				    		i = i + r.getString(15).length();
				    		spcTemp = spc.execute(r.getString(15), 401, 407);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 401, 407);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //grp type

			    	if (i == 407) {
			    		testNull = r.getString(16);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(16);
				    		i = i + r.getString(16).length();
				    		spcTemp = spc.execute(r.getString(16), 407, 411);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else {
				    		spcTemp = spc.execute("", 407, 411);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //client ind id

			    	if (i == 411) {
			    		testNull = r.getString(17);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(17);
				    		i = i + r.getString(17).length();
				    		spcTemp = spc.execute(r.getString(17), 411, 418);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    			
			    		} else {
				    		spcTemp = spc.execute("", 411, 418);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //fam code 2
			    	
			    	if (i == 418) {
			    		testNull = r.getString(18);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(18);
				    		i = i + r.getString(18).length();
				    		spcTemp = spc.execute(r.getString(18), 418, 425);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}else {
				    		spcTemp = spc.execute("", 418, 425);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	};  //ram code 

			    	if (i == 425) {
			    		testNull = r.getString(19);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(19);
				    		i = i + r.getString(19).length();
				    		spcTemp = spc.execute(r.getString(19), 425, 432);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}else {
				    		spcTemp = spc.execute("", 425, 432);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; //tbs code

			    	if (i == 432) {
			    		testNull = r.getString(20);
			    		if (!r.wasNull()) {
				    		row = row + r.getString(20);
				    		i = i + r.getString(20).length();
				    		spcTemp = spc.execute(r.getString(20), 432, 439);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		} else{
				    		spcTemp = spc.execute("", 432, 439);
				    		row = row + spcTemp;
				    		i = i + spcTemp.length();
			    		}
			    	}; // rts code

			    	i += 1;
			    	
			    };
				
				pw.println(row);
				row = "";
				i = 1;
				j = 1;
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