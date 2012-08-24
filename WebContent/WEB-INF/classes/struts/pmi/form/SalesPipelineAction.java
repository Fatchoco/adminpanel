package struts.pmi.form;

import struts.pmi.form.process.*;
import struts.pmi.function.IsDouble;
import struts.pmi.function.IsDate;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;

import org.apache.struts.upload.FormFile;
import java.util.*;

public class SalesPipelineAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String tableName="PMI_Sales_Pipeline";
		String messageCode1="";
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection = null;
		ArrayList messageCode = new ArrayList();
		int uploaded_record=0;
		int rejected_record=0;
		int i=0;
		String sql="";
		String num="";
		String date1="";
		String date2="";
		String date3="";
		String readFileStat = "";
		String tabReadStat = "";
		String logFile="SALES_PIPELINE_EXP.txt";
		String dealStatTemp="";
		String dealStat="";
		int spaceFlag=0;
		int month2=0;
		int year2=0;
		int lastDate=0;
	

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
		
		UploadForm myForm = (UploadForm)form;
	    ArrayList lines = new ArrayList();
	    String Header="";


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
		int fieldNum=10;	
		String HeaderValid="PRODUCT_SEGMENT_CODE\tORIGINATION_COUNTRY_CODE\tBOOKING_LOCATION\tDATE_FIRST\tCLOSE_MONTH\tLOST_MONTH\tDEAL_STATUS\t12_M_REVENUE\tLEID\tSUB_PROFILE_ID";	
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

		

		
		//delete table content
		TruncDel deleteTable=new TruncDel();
		deleteTable.execute(0, myConnection, partition, tableName, "");
		
        //insert process
		IsDouble checkDouble=new IsDouble();
		IsDate checkDate=new IsDate();
		IsDate checkDate2=new IsDate();
		IsDate checkDate3=new IsDate();
		for(i=0;i<lines.size();i++)
		{

		 String[] temp=((String)(lines.get(i))).split("\t",fieldNum);
		 num=temp[7].trim();
		 num=num.replace("\"","");
		 checkDate.setDate();
		 checkDate2.setDate();
		 checkDate3.setDate();
		 dealStatTemp="";
		 dealStat="";
		 spaceFlag=0;
			 if((temp[0].trim()).equals("")|| temp[0]== null)
			 {

				messageCode.add((String)(lines.get(i))+"\t:Product segment code cannot be empty");

				rejected_record++;

			 }

			 else if((temp[1].trim()).equals("")|| temp[1]== null)
			 {

				messageCode.add((String)(lines.get(i))+"\t:Origination country code cannot be empty");

				rejected_record++;

			 }

			 else if((temp[2].trim()).equals("")|| temp[2]== null)
			 {

				messageCode.add((String)(lines.get(i))+"\t:Booking location cannot be empty");

				rejected_record++;

			 }
			 else if(!(checkDate.execute(temp[3].trim(),1)))
			 {
				 	
				messageCode.add((String)(lines.get(i))+"\t:Date first not valid");

				rejected_record++;

			 }
			 else if(!(checkDate2.execute(temp[4].trim(),2))&&(!((temp[4].trim()).equals(""))&&!(temp[4]==null)))
			 {
				 	
				messageCode.add((String)(lines.get(i))+"\t:Close month not valid");

				rejected_record++;

			 }
			 else if(!(checkDate3.execute(temp[5].trim(),2))&&(!((temp[5].trim()).equals(""))&&!(temp[5]==null)))
			 {
				 	
				messageCode.add((String)(lines.get(i))+"\t:Lost month not valid");

				rejected_record++;

			 }
			 else if(!((temp[6].trim().toUpperCase()).equals("IN PIPELINE"))&&!((temp[6].trim().toUpperCase()).equals("LOST"))&&!((temp[6].trim().toUpperCase()).equals("CLOSED")))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Deal status must be In Pipeline or Lost or Closed");

				rejected_record++;

			 }
			 else if(checkDouble.execute(num))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format for 12_M_Revenue");
				rejected_record++;

			 }	



			else
			{

				 	date1=checkDate.getDate();
				 
					Calendar cal= Calendar.getInstance();
					month2=Integer.parseInt(date1.substring(4,6));
					year2=Integer.parseInt(date1.substring(0,4));
					cal.set(Calendar.MONTH, month2-1);
					cal.set(Calendar.YEAR, year2);			
					cal.set(Calendar.DATE,1);	
					
					lastDate = cal.getActualMaximum(cal.DATE);
					date1=date1.substring(0,6)+lastDate+"0000";
					
					
					
				 	date2=checkDate2.getDate();
				 	if(date2.length()>=6)
				 	{
					
					month2=Integer.parseInt(date2.substring(4,6));
					year2=Integer.parseInt(date2.substring(0,4));
					cal.set(Calendar.MONTH, month2-1);
					cal.set(Calendar.YEAR, year2);			
					cal.set(Calendar.DATE,1);	

					lastDate = cal.getActualMaximum(cal.DATE);
					date2=date2.substring(0,6)+lastDate+"0000";
					
				 	}
				 	
				 	
				 	date3=checkDate3.getDate();
				 	if(date3.length()>=6){
				 	
					month2=Integer.parseInt(date3.substring(4,6));
					year2=Integer.parseInt(date3.substring(0,4));
					cal.set(Calendar.MONTH, month2-1);
					cal.set(Calendar.YEAR, year2);			
					cal.set(Calendar.DATE,1);	

					lastDate = cal.getActualMaximum(cal.DATE);
					date3=date3.substring(0,6)+lastDate+"0000";
				 	}
				 	
					dealStatTemp=(temp[6].trim()).toLowerCase();
					for(int j=0;j<dealStatTemp.length();j++)
					{

						if(j==0||spaceFlag==1)
						{
							dealStat+=dealStatTemp.substring(j,j+1).toUpperCase();
						}
						else
						{
							dealStat+=dealStatTemp.substring(j,j+1);
						}
						spaceFlag=0;
						if(dealStatTemp.substring(j,j+1).equals(" "))
						{
							spaceFlag=1;
						}
					}
					//insert to table
					sql="insert into "+tableName+"(PRODUCT_SEGMENT_CODE, ORIGINATION_COUNTRY_CODE, BOOKING_LOCATION, DATE_FIRST, CLOSE_MONTH, LOST_MONTH, DEAL_STATUS,revenue_12_m, LEID,  SUB_PROFILE_ID,deal_owner,  CREATE_DATE,  CREATE_USER) values(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,'PMI_UPLOAD_INSERT')";
					PreparedStatement pstmt = myConnection.prepareStatement(sql);

					
					pstmt.setString(1,temp[0]);
					pstmt.setString(2,temp[1]);
					pstmt.setString(3,temp[2]);
					pstmt.setString(4,date1);
					pstmt.setString(5,date2);
					pstmt.setString(6,date3);
					pstmt.setString(7,dealStat);
					pstmt.setDouble(8,Double.parseDouble(num));
					pstmt.setString(9,temp[8]);
					pstmt.setString(10,temp[9]);
					pstmt.setString(11,"");



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
		String ctlFile="SALES_PIPELINE";
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