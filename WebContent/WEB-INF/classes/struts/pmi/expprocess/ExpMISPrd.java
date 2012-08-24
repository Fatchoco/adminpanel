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

public class ExpMISPrd extends Action  {

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
		String ExpName="ExpMISPrd.txt";
		String row="";
		//new = MIS_PRODUCT_CODE\tMIS_PRODUCT_DESC\tPRODUCT_ID\tPRODUCT_DESC\tCHANNEL\tREVENUE_TYPE\tPRODUCT_FAMILY_CODE\tPRODUCT_FAMILY_DESC\tPRODUCT_SEGMENT_CODE\tPRODUCT_SEGMENT_DESC\tPRODUCT_AREA_CODE\tPRODUCT_AREA_DESC\tPRODUCT_TAG_1\tPRODUCT_TAG_2\tPRODUCT_TAG_3\tPRODUCT_TAG_4\tPRODUCT_TAG_5\tFLAG_NEW\tREMARK
		//old = STANDAR_PRODUCT_CODE\tREVENUE_TYPE\tCHANNEL\tMIS_PRODUCT_CODE\tMIS_PRODUCT_DESC\tPRODUCT_TAG_1\tPRODUCT_TAG_2\tPRODUCT_TAG_3\tPRODUCT_TAG_4\tPRODUCT_TAG_5\tFLAG_NEW\tREMARK\tSTANDARD_PRODUCT_DESC\tPRODUCT_FAMILY_CODE\tPRODUCT_FAMILY_DESC\tPRODUCT_SEGMENT_CODE\tPRODUCT_SEGMENT_DESC\tPRODUCT_AREA_CODE\tPRODUCT_AREA_DESC";	
		
		String Header="MIS_PRODUCT_CODE\tMIS_PRODUCT_DESC\tPRODUCT_ID\tPRODUCT_DESC\tCHANNEL\tREVENUE_TYPE\tPRODUCT_FAMILY_CODE\tPRODUCT_FAMILY_DESC\tPRODUCT_SEGMENT_CODE\tPRODUCT_SEGMENT_DESC\tPRODUCT_AREA_CODE\tPRODUCT_AREA_DESC\tPRODUCT_TAG_1\tPRODUCT_TAG_2\tPRODUCT_TAG_3\tPRODUCT_TAG_4\tPRODUCT_TAG_5\tFLAG_NEW\tREMARK";
		String writeFilePath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+ExpName);;
		PrintWriter pw = new PrintWriter(new FileOutputStream(writeFilePath));
		pw.println(Header);
		
		//write message code to file
		String query = "select STANDARD_PRODUCT_CODE, REVENUE_TYPE, CHANNEL, " +
						" MIS_PRODUCT_CODE, MIS_PRODUCT_DESC, PRODUCT_TAG_1, " +
						" PRODUCT_TAG_2, PRODUCT_TAG_3, PRODUCT_TAG_4, PRODUCT_TAG_5, " +
						" FLAG_NEW, REMARK, STANDARD_PRODUCT_DESC, PRODUCT_FAMILY_CODE, " +
						" PRODUCT_SEGMENT_CODE, PRODUCT_SEGMENT_DESC, PRODUCT_AREA_CODE, " +
						" PRODUCT_AREA_DESC, PRODUCT_FAMILY_DESC " +
						" from PMI_MIS_PRODUCT_DIM WHERE PARTITION_KEY = (SELECT MAX(PARTITION_KEY) from PMI_PERIOD_DIM) order by STANDARD_PRODUCT_CODE, REVENUE_TYPE, CHANNEL";
		try
		{
		
			r = stmt.executeQuery(query);
			while (r.next())
			{    
				//new = MIS_PRODUCT_CODE\tMIS_PRODUCT_DESC\tPRODUCT_ID\tPRODUCT_DESC\tCHANNEL\tREVENUE_TYPE\tPRODUCT_FAMILY_CODE\tPRODUCT_FAMILY_DESC\tPRODUCT_SEGMENT_CODE\tPRODUCT_SEGMENT_DESC\tPRODUCT_AREA_CODE\tPRODUCT_AREA_DESC\tPRODUCT_TAG_1\tPRODUCT_TAG_2\tPRODUCT_TAG_3\tPRODUCT_TAG_4\tPRODUCT_TAG_5\tFLAG_NEW\tREMARK
				//old = STANDAR_PRODUCT_CODE\tREVENUE_TYPE\tCHANNEL\tMIS_PRODUCT_CODE\tMIS_PRODUCT_DESC\tPRODUCT_TAG_1\tPRODUCT_TAG_2\tPRODUCT_TAG_3\tPRODUCT_TAG_4\tPRODUCT_TAG_5\tFLAG_NEW\tREMARK\tSTANDARD_PRODUCT_DESC\tPRODUCT_FAMILY_CODE\tPRODUCT_FAMILY_DESC\tPRODUCT_SEGMENT_CODE\tPRODUCT_SEGMENT_DESC\tPRODUCT_AREA_CODE\tPRODUCT_AREA_DESC";	

	    		testNull = r.getString(4);
	    		if (!r.wasNull()) {	row=r.getString(4)+"\t"; } else { row = "\t"; }

	    		testNull = r.getString(5);
	    		if (!r.wasNull()) {	row=row+r.getString(5)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(1);
	    		if (!r.wasNull()) {	row=row+r.getString(1)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(13);
	    		if (!r.wasNull()) {	row=row+r.getString(13)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(3);
	    		if (!r.wasNull()) {	row=row+r.getString(3)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(2);
	    		if (!r.wasNull()) {	row=row+r.getString(2)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(14);
	    		if (!r.wasNull()) {	row=row+r.getString(14)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(19);
	    		if (!r.wasNull()) {	row=row+r.getString(19)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(15);
	    		if (!r.wasNull()) {	row=row+r.getString(15)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(16);
	    		if (!r.wasNull()) {	row=row+r.getString(16)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(17);
	    		if (!r.wasNull()) {	row=row+r.getString(17)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(18);
	    		if (!r.wasNull()) {	row=row+r.getString(18)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(6);
	    		if (!r.wasNull()) {	row=row+r.getString(6)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(7);
	    		if (!r.wasNull()) {	row=row+r.getString(7)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(8);
	    		if (!r.wasNull()) {	row=row+r.getString(8)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(9);
	    		if (!r.wasNull()) {	row=row+r.getString(9)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(10);
	    		if (!r.wasNull()) {	row=row+r.getString(10)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(11);
	    		if (!r.wasNull()) {	row=row+r.getString(11)+"\t"; } else { row = row + "\t"; }

	    		testNull = r.getString(12);
	    		if (!r.wasNull()) {	row=row+r.getString(12); };
				
/*
				row=r.getString(4)+"\t"+r.getString(5)+"\t"+r.getString(1)+"\t"+r.getString(13)+"\t"+r.getString(3)+"\t"+
					r.getString(2)+"\t"+r.getString(14)+"\t"+r.getString(19)+"\t"+r.getString(15)+"\t"+r.getString(16)+"\t"+
					r.getString(17)+"\t"+r.getString(18)+"\t"+r.getString(6)+"\t"+r.getString(7)+"\t"+r.getString(8)+"\t"+
					r.getString(9)+"\t"+r.getString(10)+"\t"+r.getString(11)+"\t"+r.getString(12); 	
*/
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