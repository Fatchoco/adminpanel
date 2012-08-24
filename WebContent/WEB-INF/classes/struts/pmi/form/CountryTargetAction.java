package struts.pmi.form;

import struts.pmi.form.process.*;
import struts.pmi.function.IsDouble;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;

import org.apache.struts.upload.FormFile;
import java.util.*;

public class CountryTargetAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String tableName="PMI_COUNTRY_TARGET";
		String messageCode1="";
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection = null;
		ArrayList messageCode = new ArrayList();
		int uploaded_record=0;
		int rejected_record=0;
		int i=0;
		String sql="";
		String num="";
		String num2="";
		String num3="";
		String num4="";
		String num5="";
		String num6="";
		String num7="";
		String num8="";
		String num9="";
		String num10="";
		String num11="";
		String num12="";
		String num13="";
		String num14="";
		String type_book="";
		String readFileStat = "";
		String tabReadStat = "";
		String logFile="COUNTRY_TARGET_EXP.txt";
	

		//check login stat
		if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != ""))
		{
			ActionForward login=mapping.findForward("loginpage");
			return login;
			
		}

try {		
		//initiate db connection
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		
		UploadFormWithPart myForm = (UploadFormWithPart)form;
	    ArrayList lines = new ArrayList();
	    String Header="";
	    String year=myForm.getPart();

        // Process the FormFile
        FormFile myFile = myForm.getUploadFile();
        ReadFile reader = new ReadFile();
        String fileName=myFile.getFileName();
        String path=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+fileName);
        
        //read upload file and write to disk
        readFileStat=reader.readStat(myFile, path,fileName);
        //System.out.println("read yupload");
        //upload error

		//read tab uploaded file
		int fieldNum=19;	
		String HeaderValid="CLIENT_SUB_SEGMENT_GROUP\tPRODUCT_SEGMENT_CODE\tCOUNTRY_CODE\tTYPE\tYEAR\tTARGET_REVENUE_NUMERATOR\tTARGET_REVENUE_DENOMINATOR\tTARGET_LIABILITIES_NUMERATOR\tTARGET_LIABILITIES_DENOMINATOR\tTARGET_CONTINGENTS_NUMERATOR\tTARGET_CONTINGENTS_DENOMINATOR\tTARGET_ASSETS_NUMERATOR\tTARGET_ASSETS_DENOMINATOR\tTARGET_AUA_NUMERATOR\tTARGET_AUA_DENOMINATOR\tTARGET_RORWA_NUMERATOR\tTARGET_RORWA_DENOMINATOR\tTarget_Revenue_per_Salesperson_Num\tTarget_Revenue_per_Salesperson_Den";	
		ReadTabTxt tabReader=new ReadTabTxt();
		tabReadStat=tabReader.readTabStat(path, fieldNum, HeaderValid);
		//System.out.println("readtab");
        //read tab error
        //get line and header
		lines=tabReader.getLines();
		Header=tabReader.getHeader();
		
	if (readFileStat.equals("") && tabReadStat.equals("")) {	
		
		//get current partition
		GetPartition part=new GetPartition();
		String partition=part.execute(1, myConnection);

		
		//add partition with the last date
		if(!(year.equals("")))
		{

		
		partition=year+"12310000";

		}
		
		//delete table content
		TruncDel deleteTable=new TruncDel();
		deleteTable.execute(4, myConnection, year, tableName, "");
		
        //insert process
		IsDouble checkDouble=new IsDouble();
		for(i=0;i<lines.size();i++)
		{

		 String[] temp=((String)(lines.get(i))).split("\t",fieldNum);
		 num=temp[5].trim();
		 num=num.replace("\"","");

		 num=num.trim();
		 if(num.equals("")){num="0";}
		 num2=temp[6].trim();
		 num2=num2.replace("\"","");

		 num2=num2.trim();
		 if(num2.equals("")){num2="0";}
		 num3=temp[7].trim();
		 num3=num3.replace("\"","");

		 num3=num3.trim();
		 if(num3.equals("")){num3="0";}
		 num4=temp[8].trim();
		 num4=num4.replace("\"","");

		 num4=num4.trim();
		 if(num4.equals("")){num4="0";}
		 num5=temp[9].trim();
		 num5=num5.replace("\"","");

		 num5=num5.trim();
		 if(num5.equals("")){num5="0";}
		 num6=temp[10].trim();
		 num6=num6.replace("\"","");

		 num6=num6.trim();
		 if(num6.equals("")){num6="0";}
		 num7=temp[11].trim();
		 num7=num7.replace("\"","");

		 num7=num7.trim();
		 if(num7.equals("")){num7="0";}
		 num8=temp[12].trim();
		 num8=num8.replace("\"","");

		 num8=num8.trim();
		 if(num8.equals("")){num8="0";}
		 num9=temp[13].trim();
		 num9=num9.replace("\"","");

		 num9=num9.trim();
		 if(num9.equals("")){num9="0";}
		 num10=temp[14].trim();
		 num10=num10.replace("\"","");

		 num10=num10.trim();
		 if(num10.equals("")){num10="0";}
		 num11=temp[15].trim();
		 num11=num11.replace("\"","");

		 num11=num11.trim();
		 if(num11.equals("")){num11="0";}
		 num12=temp[16].trim();
		 num12=num12.replace("\"","");

		 num12=num12.trim();
		 if(num12.equals("")){num12="0";}
		 num13=temp[17].trim();
		 num13=num13.replace("\"","");

		 num13=num13.trim();
		 if(num13.equals("")){num13="0";}
		 num14=temp[18].trim();
		 num14=num14.replace("\"","");

		 num14=num14.trim();
		 if(num14.equals("")){num14="0";}
		 type_book="";		 

			 if((temp[0].trim()).equals("") || temp[0]== null)
			 {

				messageCode.add((String)(lines.get(i))+"\t:Client sub segment code cannot be empty");

				rejected_record++;

			 }
			 else if((temp[1].trim()).equals("") || temp[1]== null)
			 {

				messageCode.add((String)(lines.get(i))+"\t:Product segment code cannot be empty");

				rejected_record++;

			 }
			 else if((temp[2].trim()).equals("") || temp[2]== null)
			 {

				messageCode.add((String)(lines.get(i))+"\t:Country code cannot be empty");

				rejected_record++;

			 }
			 else if(!(((temp[3].trim()).toUpperCase()).equals("BOOKED")) && !(((temp[3].trim()).toUpperCase()).equals("MANAGED")))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Type must be booked/managed");

				rejected_record++;

			 }
			 else if(!((temp[4].trim()).equals(year)) )
			 {

				messageCode.add((String)(lines.get(i))+"\t:Year is not valid");

				rejected_record++;

			 }
			 else if(checkDouble.execute(num))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 6");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num2))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 7");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num3))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 8");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num4))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 9");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num5))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 10");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num6))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 11");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num7))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 12");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num8))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 13");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num9))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 14");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num10))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 15");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num11))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 16");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num12))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 17");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num13))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 18");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num14))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format column 19");
				rejected_record++;

			 }	
				 
			else
			{
					type_book=(temp[3].trim()).toLowerCase();
					type_book=type_book.substring(0,1).toUpperCase() + type_book.substring(1);
					//insert to table
					sql="insert into "+tableName+"(CLIENT_SUB_SEGMENT_CODE, PRODUCT_SEGMENT_CODE, COUNTRY_CODE, TYPE_BOOK, YEAR_DATA, TARGET_REVENUE_NUMERATOR,TARGET_REVENUE_DENOMINATOR, TARGET_LIABILITIES_NUMERATOR, TARGET_LIABILITIES_DENOMINATOR,TARGET_CONTINGENTS_NUMERATOR, TARGET_CONTINGENTS_DENOMINATOR, TARGET_ASSETS_NUMERATOR, TARGET_ASSETS_DENOMINATOR, TARGET_AUA_NUMERATOR, TARGET_AUA_DENOMINATOR,TARGET_RORWA_NUMERATOR, TARGET_RORWA_DENOMINATOR, TARGET_REVENUE_SALES_NUM, TARGET_REVENUE_SALES_DEN,CREATE_DATE,CREATE_USER) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,'PMI_UPLOAD_INSERT')";
					PreparedStatement pstmt = myConnection.prepareStatement(sql);
						
					
					pstmt.setString(1,temp[0].trim());
					pstmt.setString(2,temp[1].trim());
					pstmt.setString(3,temp[2].trim());
					pstmt.setString(4,type_book);
					pstmt.setString(5,temp[4].trim());
					pstmt.setDouble(6,Double.parseDouble(num));
					pstmt.setDouble(7,Double.parseDouble(num2));
					pstmt.setDouble(8,Double.parseDouble(num3));
					pstmt.setDouble(9,Double.parseDouble(num4));
					pstmt.setDouble(10,Double.parseDouble(num5));
					pstmt.setDouble(11,Double.parseDouble(num6));
					pstmt.setDouble(12,Double.parseDouble(num7));
					pstmt.setDouble(13,Double.parseDouble(num8));
					pstmt.setDouble(14,Double.parseDouble(num9));
					pstmt.setDouble(15,Double.parseDouble(num10));
					pstmt.setDouble(16,Double.parseDouble(num11));
					pstmt.setDouble(17,Double.parseDouble(num12));
					pstmt.setDouble(18,Double.parseDouble(num13));
					pstmt.setDouble(19,Double.parseDouble(num14));


						try{
						pstmt.executeUpdate();
						uploaded_record++;
						}
						catch(Exception se)
						{
						messageCode.add((String)(lines.get(i))+"\t: " + se);
						rejected_record++;
						}
					pstmt.close();
			}
		}
		
		if(rejected_record>0)
		{
		//write export error log file
		
		WriteExpFile pmiExp=new WriteExpFile();
		String expPath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+logFile);
		pmiExp.writeExp(expPath, messageCode, Header);
		}
		String ctlFile="COUNTRY_TARGET";
		//delete control table row
		deleteTable.execute(1, myConnection, partition, tableName, ctlFile);
		
		//insert file control
		String username=(String)request.getSession().getAttribute("username");
		InsertFileCtl insertCtl=new InsertFileCtl();
		insertCtl.execute(myConnection, ctlFile, fileName, uploaded_record, rejected_record, username, partition);
		
		//delete temp file
		DeleteFile del=new DeleteFile();
		del.deleteStat(path);
		
		if(rejected_record==0){messageCode1="Import Successful Without Any Error";}
		else{messageCode1="Import Finish With Log";}
		
		
	
	}	

} catch ( Exception e ) {
	
	System.out.println(e.getMessage());
	messageCode1 = e.getMessage();
	
} finally {
	
	if (myConnection != null && !myConnection.isClosed()) myConnection.close();
	
    if(!readFileStat.equals(""))
    {
    	return new ActionForward(nextPage.getPath() + "?message=" + readFileStat); 
    } else if (!tabReadStat.equals("")) {
        return new ActionForward(nextPage.getPath() + "?message=" + tabReadStat); 
    } else {
		return new ActionForward(nextPage.getPath() + "?message="+messageCode1+"&uploaded_record="+uploaded_record+"&rejected_record="+rejected_record+"&file="+logFile);
    }
	
}
	}

}