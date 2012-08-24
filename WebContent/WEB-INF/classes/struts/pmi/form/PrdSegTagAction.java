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

public class PrdSegTagAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String tableName="PMI_PRD_SEG_TAG_MAPPING";
		String messageCode1="";
		javax.sql.DataSource dataSource;
		java.sql.Connection myConnection = null;
		String readFileStat = "";
		String tabReadStat = "";
		ArrayList messageCode = new ArrayList();
		int uploaded_record=0;
		int rejected_record=0;
		int i=0;
		String sql="";
		String logFile="PRD_SEG_TAG_EXP.txt";
		
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

		//read tab uploaded file
		int fieldNum=3;	
		String HeaderValid="PRODUCT_SEGMENT_ID\tPRODUCT_SEGMENT_TAG1\tPRODUCT_SEGMENT_TAG2";	
		ReadTabTxt tabReader=new ReadTabTxt();
		tabReadStat=tabReader.readTabStat(path, fieldNum, HeaderValid);

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
		for(i=0;i<lines.size();i++)
		{

		 String[] temp=((String)(lines.get(i))).split("\t",fieldNum);

	 
					//insert to table
					sql="insert into " +tableName+"(PRODUCT_Segment_ID,product_segment_tag1,product_segment_tag2,CREATE_DATE,CREATE_USER) values(?,?,?,SYSDATE,'PMI_UPLOAD_INSERT')";
					PreparedStatement pstmt =myConnection.prepareStatement(sql);

					pstmt.setString(1,temp[0]);
					pstmt.setString(2,temp[1].trim());
					pstmt.setString(3,temp[2].trim());
	
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
		
		if(rejected_record>0)
		{
		//write export error log file
		
		WriteExpFile pmiExp=new WriteExpFile();
		String expPath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+logFile);
		pmiExp.writeExp(expPath, messageCode, Header);
		}
		String ctlFile="PRD_SEG_TAG";
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
	System.out.println (e.getMessage());
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