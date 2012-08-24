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

public class ClientTargetAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String tableName="PMI_CLIENT_TARGET";
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

		
		String readFileStat = "";
		String tabReadStat = "";
		String logFile="CLIENT_TARGET_EXP.txt";
	

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
		int fieldNum=9;	
		String HeaderValid="GROUP_SEGMENT_CODE\tCOUNTRY_CODE\tYEAR\tTARGET_50M\tTARGET_25M\tTARGET_5M\tTARGET_1M\tTARGET_2TB\tTARGET_2BL";	
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
		 num=temp[3].trim();
		 num=num.replace("\"","");
		 
		 if(num.equals("")||num==null){num="0";}
		 num2=temp[4].trim();
		 num2=num2.replace("\"","");
		
		 if(num2.equals("")||num2==null){num2="0";}
		 num3=temp[5].trim();
		 num3=num3.replace("\"","");
		 
		 if(num3.equals("")||num3==null){num3="0";}
		 num4=temp[6].trim();
		 num4=num4.replace("\"","");

		 if(num4.equals("")||num4==null){num4="0";}
		 num5=temp[7].trim();
		 num5=num5.replace("\"","");

		 if(num5.equals("")||num5==null){num5="0";}
		 num6=temp[8].trim();
		 num6=num6.replace("\"","");

		 if(num6.equals("")||num6==null){num6="0";}

				 

			 if((temp[0].trim()).equals("") || temp[0]== null)
			 {

				messageCode.add((String)(lines.get(i))+"\t:Sub segment cannot be empty");

				rejected_record++;

			 }
			 else if((temp[1].trim()).equals("") || temp[1]== null)
			 {

				messageCode.add((String)(lines.get(i))+"\t:Country code cannot be empty");

				rejected_record++;

			 }

			 else if(!((temp[2].trim()).equals(year)) )
			 {

				messageCode.add((String)(lines.get(i))+"\t:Year is not valid");

				rejected_record++;

			 }
			 else if(checkDouble.execute(num))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num2))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num3))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num4))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num5))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format");
				rejected_record++;

			 }	
			 else if(checkDouble.execute(num6))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format");
				rejected_record++;

			 }	
			 
			else
			{
				 
					//insert to table
					sql="insert into "+tableName+"(GROUP_SEGMENT, COUNTRY_CODE, YEAR_DATA, TARGET_50M, TARGET_25M, TARGET_5M, TARGET_1M, TARGET_2TB, TARGET_2BL, CREATE_DATE, CREATE_USER) values(?,?,?,?,?,?,?,?,?,SYSDATE,'PMI_UPLOAD_INSERT')";
					PreparedStatement pstmt = myConnection.prepareStatement(sql);

					
					pstmt.setString(1,temp[0].trim());
					pstmt.setString(2,temp[1].trim());
					pstmt.setString(3,temp[2].trim());
					pstmt.setDouble(4,Double.parseDouble(num));
					pstmt.setDouble(5,Double.parseDouble(num2));
					pstmt.setDouble(6,Double.parseDouble(num3));
					pstmt.setDouble(7,Double.parseDouble(num4));
					pstmt.setDouble(8,Double.parseDouble(num5));
					pstmt.setDouble(9,Double.parseDouble(num6));



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
		String ctlFile="CLIENT_TARGET";
		//delete control table row5
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