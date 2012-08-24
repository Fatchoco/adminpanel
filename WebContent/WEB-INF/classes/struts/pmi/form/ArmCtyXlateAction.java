package struts.pmi.form;

import struts.pmi.form.process.*;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;

import org.apache.struts.upload.FormFile;
import java.util.*;

public class ArmCtyXlateAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String tableName="PMI_ARM_CITY_XLATE";
		String messageCode1="";
		ArrayList messageCode = new ArrayList();
		int uploaded_record=0;
		int rejected_record=0;
		int day1=0;
		int month1=0;
		int year1=0;
		String [] BusinessDate=new String[3];
		String sql="";
		javax.sql.DataSource dataSource;
		java.sql.Connection myConnection = null;
		String readFileStat = "";
		String tabReadStat = "";
		String logFile="ARM_CITY_XLATE_EXP.txt";
		
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
	    String parti=myForm.getPart();

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
		int fieldNum=12;	
		String HeaderValid="BUSINESS_DATE\tCOUNTRY_CODE\tARM_CD\tRM_ID\tRM_NAME\tCUST_SEGMENT\tCITY\tREGION\tPSGL_CUST_CLASS\tBIP_CODE\tCREATED_DATE\tCREATED_USER";	
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
		String partition=part.execute(3, myConnection);

		
		//add partition with the last date
		if(!(parti.equals("")))
		{
		Calendar cal= Calendar.getInstance();
		int month2=Integer.parseInt(parti.substring(4,6));
		int year2=Integer.parseInt(parti.substring(0,4));
		cal.set(Calendar.MONTH, month2-1);
		cal.set(Calendar.YEAR, year2);			
		cal.set(Calendar.DATE,1);						
		int lastDate = cal.getActualMaximum(cal.DATE);
		
		partition=parti+lastDate+"0000";

		}
		
		//delete table content
		TruncDel deleteTable=new TruncDel();
		deleteTable.execute(2, myConnection, partition, tableName, "");
		
        //insert process
		int i=0;
		for(i=0;i<lines.size();i++)
		{
		
			 String[] temp=((String)(lines.get(i))).split("\t",fieldNum);
			 BusinessDate=temp[0].split("/",3);
			 if(BusinessDate[0].length()==1)
			 {
			 BusinessDate[0]="0"+BusinessDate[0];
			 }					

				 if(!((BusinessDate[2].substring(0,4)+BusinessDate[0]).equals(partition.substring(0,6))))
				 {

					messageCode.add((String)(lines.get(i))+"\t:Invalid Reporting Date"+BusinessDate[2]+BusinessDate[0]);

					rejected_record++;

				 }

				else
				{
					 
						//insert to table
						sql="insert into PMI_ARM_CITY_XLATE (PARTITION_KEY,BUSINESS_DATE,COUNTRY_CODE,ARM_CD,";
						sql+="RM_ID,RM_NAME,CUST_SEGMENT,CITY,REGION,PSGL_CUST_CLASS,BIP_CODE,CREATE_DATE,CREATE_USER) values(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,'PMI_UPLOAD_INSERT')";
						PreparedStatement pstmt =myConnection.prepareStatement(sql);
						
						Calendar cal= Calendar.getInstance();
						day1=Integer.parseInt(BusinessDate[1]);
						month1=Integer.parseInt(BusinessDate[0]);
						year1=Integer.parseInt(BusinessDate[2].substring(0,4));
						java.sql.Date date = new java.sql.Date(0000-00-00);		
									
						pstmt.setString(1,partition);
						pstmt.setDate(2,date.valueOf(year1+"-"+month1+"-"+day1));
						pstmt.setString(3,temp[1].trim());
						pstmt.setString(4,temp[2].trim());
						pstmt.setString(5,temp[3].trim());
						pstmt.setString(6,temp[4].trim());
						pstmt.setString(7,temp[5].trim());
						pstmt.setString(8,temp[6]);
						pstmt.setString(9,temp[7]);
						pstmt.setString(10,temp[8].trim());
						pstmt.setString(11,temp[9].trim());
						


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
		String ctlFile="ARM_CITY_XLATE";
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

} catch (Exception e) {
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