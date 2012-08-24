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
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.portlet.*;
import java.io.*;
import java.util.*;
//levin17lacustre 25092009
public class CtySwitchAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String tableName="PMI_COUNTRY_SWITCH";
		String messageCode1="";

		
		//check login stat
		if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != ""))
		{
			ActionForward login=mapping.findForward("loginpage");
			return login;
			
		}		
		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
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
        String readFileStat=reader.readStat(myFile, path,fileName);
        //System.out.println("read yupload");
        //upload error
        if(!readFileStat.equals(""))
        {
        return new ActionForward(nextPage.getPath() + "?message=" + readFileStat); 
        }

		//read tab uploaded file
		int fieldNum=6;	
		String HeaderValid="SOURCE_CODE\tTARGET_CODE\tCOUNTRY_ID\tATTRIBUTE1\tATTRIBUTE2\tATTRIBUTE3";	
		ReadTabTxt tabReader=new ReadTabTxt();
		String tabReadStat=tabReader.readTabStat(path, fieldNum, HeaderValid);
		//System.out.println("readtab");
        //read tab error
		if(!tabReadStat.equals(""))
        {
        return new ActionForward(nextPage.getPath() + "?message=" + tabReadStat); 
        }
        //get line and header
		lines=tabReader.getLines();
		Header=tabReader.getHeader();
		
		//get current partition
		GetPartition part=new GetPartition();
		String partition=part.execute(1, myConnection);
		
		//delete table content
		TruncDel deleteTable=new TruncDel();
		deleteTable.execute(0, myConnection, partition, tableName, "");
		
        //insert process
		ArrayList messageCode = new ArrayList();
		int uploaded_record=0;
		int rejected_record=0;
		int i=0;
		String sql="";
		for(i=0;i<lines.size();i++)
		{

		 String[] temp=((String)(lines.get(i))).split("\t",fieldNum);
			 if(!(temp[0].equals("BNCPR")||temp[0].equals("CP")))
			 {
			 messageCode.add((String)(lines.get(i))+"\t:Invalid Source Code");
			 	
	
				rejected_record++;
	
			 }
	
			 else if(!(temp[1].equals("TBMI")||temp[1].equals("BR")))
			 {
			 messageCode.add((String)(lines.get(i))+"\t:Invalid Target Code");
			 	
				rejected_record++;
	
			 }
	
	
			else
			{
	 
					//insert to table
					sql="insert into " +tableName+"(SOURCE_CODE,TARGET_CODE,COUNTRY_ID,ATTRIBUTE1,ATTRIBUTE2,ATTRIBUTE3,CREATE_DATE,CREATE_USER) values(?,?,?,?,?,?,SYSDATE,'PMI_UPLOAD_INSERT')";
					PreparedStatement pstmt =myConnection.prepareStatement(sql);

					pstmt.setString(1,temp[0]);

					pstmt.setString(2,temp[1]);
					pstmt.setString(3,temp[2]);
					pstmt.setString(4,temp[3].trim());
					pstmt.setString(5,temp[4].trim());
					pstmt.setString(6,temp[5].trim());
	
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
		
		String logFile="CTY_SWITCH_EXP.txt";
		if(rejected_record>0)
		{
		//write export error log file
		
		WriteExpFile pmiExp=new WriteExpFile();
		String expPath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+logFile);
		pmiExp.writeExp(expPath, messageCode, Header);
		}
		String ctlFile="CTY_SWITCH";
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
		
		//close connection
		try
		{

			myConnection.close();
		}
		catch(Exception e)
		{
		}
		
		//redirect

		return new ActionForward(nextPage.getPath() + "?message="+messageCode1+"&uploaded_record="+uploaded_record+"&rejected_record="+rejected_record+"&file="+logFile);
	}

}