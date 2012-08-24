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

public class MISPrdAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String tableName="";
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
		String sqls="";
		String logFile="MIS_PRD_TAG_EXP.txt";
		int refreshFlag=0;
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
		int fieldNum=19;
		//new = MIS_PRODUCT_CODE\tMIS_PRODUCT_DESC\tPRODUCT_ID\tPRODUCT_DESC\tCHANNEL\tREVENUE_TYPE\tPRODUCT_FAMILY_CODE\tPRODUCT_FAMILY_DESC\tPRODUCT_SEGMENT_CODE\tPRODUCT_SEGMENT_DESC\tPRODUCT_AREA_CODE\tPRODUCT_AREA_DESC\tPRODUCT_TAG_1\tPRODUCT_TAG_2\tPRODUCT_TAG_3\tPRODUCT_TAG_4\tPRODUCT_TAG_5\tFLAG_NEW\tREMARK
		//old = STANDAR_PRODUCT_CODE\tREVENUE_TYPE\tCHANNEL\tMIS_PRODUCT_CODE\tMIS_PRODUCT_DESC\tPRODUCT_TAG_1\tPRODUCT_TAG_2\tPRODUCT_TAG_3\tPRODUCT_TAG_4\tPRODUCT_TAG_5\tFLAG_NEW\tREMARK\tSTANDARD_PRODUCT_DESC\tPRODUCT_FAMILY_CODE\tPRODUCT_FAMILY_DESC\tPRODUCT_SEGMENT_CODE\tPRODUCT_SEGMENT_DESC\tPRODUCT_AREA_CODE\tPRODUCT_AREA_DESC";	
		String HeaderValid="MIS_PRODUCT_CODE\tMIS_PRODUCT_DESC\tPRODUCT_ID\tPRODUCT_DESC\tCHANNEL\tREVENUE_TYPE\tPRODUCT_FAMILY_CODE\tPRODUCT_FAMILY_DESC\tPRODUCT_SEGMENT_CODE\tPRODUCT_SEGMENT_DESC\tPRODUCT_AREA_CODE\tPRODUCT_AREA_DESC\tPRODUCT_TAG_1\tPRODUCT_TAG_2\tPRODUCT_TAG_3\tPRODUCT_TAG_4\tPRODUCT_TAG_5\tFLAG_NEW\tREMARK";
		ReadTabTxt tabReader=new ReadTabTxt();
		
		tabReadStat=tabReader.readTabStat(path, fieldNum, HeaderValid);
		//System.out.println("readtab");

	if (readFileStat.equals("") && tabReadStat.equals("")) {	
		//get line and header
		lines=tabReader.getLines();
		Header=tabReader.getHeader();
		
		//get current partition
		GetPartition part=new GetPartition();
		String partition=part.execute(1, myConnection);
		
        //insert process
		for(i=0;i<lines.size();i++)
		{

		 String[] temp=((String)(lines.get(i))).split("\t",fieldNum);

			 if(!(temp[17].equals("")||temp[17].equals("Y")||temp[17].equals("N")))
			 {
			 	messageCode.add((String)(lines.get(i))+"\t:Invalid Flag: ");
	
				rejected_record++;
	
			 }
	
			else
			{
						//update to table
				sql="Update PMI_MIS_PRODUCT_DIM SET MIS_PRODUCT_DESC=?,PRODUCT_TAG_1  =?,";
				sql+="PRODUCT_TAG_2  =?,PRODUCT_TAG_3  =?,";
				sql+="PRODUCT_TAG_4  =?,PRODUCT_TAG_5  =?,REMARK = ?,FLAG_NEW= ?";
				sql+="WHERE MIS_PRODUCT_CODE =? AND PARTITION_KEY=(SELECT MAX(PARTITION_KEY) from PMI_PERIOD_DIM)";
				//MIS_PRODUCT_CODE\tMIS_PRODUCT_DESC\tPRODUCT_ID\tPRODUCT_DESC\tCHANNEL\tREVENUE_TYPE\t
				//PRODUCT_FAMILY_CODE\tPRODUCT_FAMILY_DESC\tPRODUCT_SEGMENT_CODE\tPRODUCT_SEGMENT_DESC\t
				//PRODUCT_AREA_CODE\tPRODUCT_AREA_DESC\tPRODUCT_TAG_1\tPRODUCT_TAG_2\tPRODUCT_TAG_3\tPRODUCT_TAG_4\t
				//PRODUCT_TAG_5\tFLAG_NEW\tREMARK
				
				//System.out.println(sql);
				
				PreparedStatement pstmt =myConnection.prepareStatement(sql);

				pstmt.setString(1,temp[1].trim());
				//System.out.println("1-"+temp[1].trim());
				pstmt.setString(2,temp[12].trim());
				//System.out.println("1-"+temp[12].trim());
				pstmt.setString(3,temp[13].trim());
				//System.out.println("1-"+temp[13].trim());
				pstmt.setString(4,temp[14].trim());
				//System.out.println("1-"+temp[14].trim());
				pstmt.setString(5,temp[15].trim());
				//System.out.println("1-"+temp[15].trim());
				pstmt.setString(6,temp[16].trim());
				//System.out.println("1-"+temp[16].trim());
				pstmt.setString(7,temp[18].trim());
				//System.out.println("1-"+temp[18].trim());
				pstmt.setString(8,temp[17].trim());
				//System.out.println("1-"+temp[17].trim());
				pstmt.setString(9,temp[0].trim());
				//System.out.println("1-"+temp[0].trim());

				
				sqls="Update PMI_MIS_PRODUCT_MAP SET MIS_PRODUCT_DESC=?,PRODUCT_TAG_1  =?,";
				sqls+="PRODUCT_TAG_2  =?,PRODUCT_TAG_3  =?,";
				sqls+="PRODUCT_TAG_4  =?,PRODUCT_TAG_5  =?,REMARK = ?,FLAG_NEW= ?, modify_date=SYSDATE, modify_user='PMI_UPLOAD_IUPDATE'";
				sqls+="WHERE MIS_PRODUCT_CODE =? ";
				PreparedStatement pstmt2 =myConnection.prepareStatement(sqls);

				//System.out.println(sqls);
				
				pstmt2.setString(1,temp[1].trim());
				//System.out.println("1-"+temp[1].trim());
				pstmt2.setString(2,temp[12].trim());
				//System.out.println("1-"+temp[12].trim());
				pstmt2.setString(3,temp[13].trim());
				//System.out.println("1-"+temp[13].trim());
				pstmt2.setString(4,temp[14].trim());
				//System.out.println("1-"+temp[14].trim());
				pstmt2.setString(5,temp[15].trim());
				//System.out.println("1-"+temp[15].trim());
				pstmt2.setString(6,temp[16].trim());
				//System.out.println("1-"+temp[16].trim());
				pstmt2.setString(7,temp[18].trim());
				//System.out.println("1-"+temp[18].trim());
				pstmt2.setString(8,temp[17].trim());
				//System.out.println("1-"+temp[17].trim());
				pstmt2.setString(9,temp[0].trim());
				//System.out.println("1-"+temp[0].trim());
				
					try{
					pstmt.executeUpdate();
					pstmt2.executeUpdate();
					uploaded_record++;
					}
					catch(Exception se)
					{
					messageCode.add((String)(lines.get(i))+"\t: " + se);
					rejected_record++;

					}
				pstmt.close();
				pstmt2.close();
			}
		}
/*
		CallableStatement cstmt = myConnection.prepareCall("{call DBMS_MVIEW.REFRESH('MV_PMI_MIS_PRODUCT_DIM')}");
		try
		{
		cstmt.execute();
		}
		catch(Exception se)
		{
			refreshFlag=1;
			messageCode.add("Refresh MV failed: "+se);
		}
		cstmt.close();	
*/
		if(rejected_record>0||refreshFlag==1)
		{
		//write export error log file
		
		WriteExpFile pmiExp=new WriteExpFile();
		String expPath=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+logFile);
		pmiExp.writeExp(expPath, messageCode, Header);
		}
		String ctlFile="TBMI_PRD_TAG";
		//delete control table row
		TruncDel deleteTable=new TruncDel();
		deleteTable.execute(1, myConnection, partition, tableName, ctlFile);
		
		//insert file control
		String username=(String)request.getSession().getAttribute("username");
		InsertFileCtl insertCtl=new InsertFileCtl();
		insertCtl.execute(myConnection, ctlFile, fileName, uploaded_record, rejected_record, username, partition);
		
		//delete temp file
		DeleteFile del=new DeleteFile();
		del.deleteStat(path);
		
		if(rejected_record==0&&refreshFlag==0){messageCode1="Import Successful Without Any Error";}
		else{messageCode1="Import Finish With Log";}

	}
	
} catch (Exception e) {
	System.out.println (e.getMessage());
	messageCode1 = e.getMessage();	
	
} finally {
	//redirect

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