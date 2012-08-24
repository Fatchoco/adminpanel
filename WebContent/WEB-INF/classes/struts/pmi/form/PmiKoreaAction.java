package struts.pmi.form;

import struts.pmi.form.process.*;
import struts.pmi.function.Nvl;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;

import org.apache.struts.upload.FormFile;

import java.util.*;

public class PmiKoreaAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String tableName="PMI_KOREA_CLIENT";
		String messageCode1="";
		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection = null;

			// allocate listtext to each field
			String cl_code;
			String book_loc;
			String cl_desc;
			String grp_seg_id;
			String cl_seg_id;
			String int_crg;
			String cl_crg;
			String fam_code;
			String fam_desc;
			String fam_loc;
			String ctr_reg;
			String ctr_dom;
			String grp_code;
			String grp_desc;
			String grp_type;
			String cl_ind_id;
			String fam_code2;
			String ram_code;
			String tbs_code;
			String rts_code;		
	        //insert process
			ArrayList messageCode = new ArrayList();
			int uploaded_record=0;
			int rejected_record=0;
			int fieldNum=21;	

			String sql="";

			String readFileStat="";
			String fcReadStat="";
			
			String logFile="KOREA_STATIC_EXP.txt";
			
		//check login stat
		if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != ""))
		{
			ActionForward login=mapping.findForward("loginpage");
			return login;
			
		}
		
		try 
		{
		
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		
		UploadFormWithPart myForm = (UploadFormWithPart)form;
	    ArrayList lines = new ArrayList();
	    String Header="CLIENT_CODE\tBOOKING_LOCATION\tCLIENT_DESC\tGROUP_SEGMENT_ID\tCLIENT_SEGMENT_ID\tINTERNAL_CRG\tCLIENT_CRG\tFAM_CODE\tFAM_DESC\tFAM_LOCATION\tCOUNTRY_REGISTRATION\tCOUNTRY_DOMICILE\tGROUP_CODE\tGROUP_DESC\tGROUP_TYPE\tCLIENT_INDUSTRY_ID\tFAM_CODE2\tRAM_CODE\tTBS_CODE\tRTS_CODE\tERROR_CODE";
	    String parti=myForm.getPart();

        // Process the FormFile
        FormFile myFile = myForm.getUploadFile();
        ReadFile reader = new ReadFile();
        String fileName=myFile.getFileName();
        String path=request.getSession().getServletContext().getRealPath("/WEB-INF/temp/"+fileName);
        
        //read upload file and write to disk
        readFileStat = reader.readStat(myFile, path,fileName);
        
        //upload error
        //if(!readFileStat.equals(""))
        //{
        //return new ActionForward(nextPage.getPath() + "?message=" + readFileStat); 
        //}

		//read tab uploaded file
		
		ReadFCTxt fcReader=new ReadFCTxt();
		fcReadStat=fcReader.readFCStat(path);
		
		//System.out.println("read tab: " + fcReadStat);
        //read tab error
		//if(!fcReadStat.equals(""))
        //{
        //return new ActionForward(nextPage.getPath() + "?message=" + fcReadStat); 
        //}
        //get line
		lines=fcReader.getLines();

		//block code from running when errors w/ input file occured
	if (readFileStat.equals("") && fcReadStat.equals("")) {
			
		
		
		//get current partition
		GetPartition part=new GetPartition();
		String partition=part.execute(1, myConnection);

		
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

		
		int i=0;
		Nvl nvl=new Nvl();
		for(i=0;i<lines.size();i++)
		{
			 cl_code=(((String)lines.get(i)).substring(0,15)).trim();
			 
			 book_loc=(((String)lines.get(i)).substring(15,17)).trim();
			 cl_desc=(nvl.execute((((String)lines.get(i)).substring(17,117)),book_loc)).trim();
			 grp_seg_id=(((String)lines.get(i)).substring(117,119)).trim();
			 cl_seg_id=(((String)lines.get(i)).substring(119,121)).trim();
			 int_crg=(((String)lines.get(i)).substring(121,131)).trim();
			 cl_crg=(nvl.execute((((String)lines.get(i)).substring(131,134)),"NA")).trim();
			 fam_code=(nvl.execute((((String)lines.get(i)).substring(134,144)),"9970")).trim();
			 fam_desc=(nvl.execute((((String)lines.get(i)).substring(144,224)),"RM-Not Available")).trim();
			 fam_loc=(nvl.execute((((String)lines.get(i)).substring(224,226)),"KR")).trim();
			 ctr_reg=(((String)lines.get(i)).substring(226,228)).trim();
			 ctr_dom=(((String)lines.get(i)).substring(228,230)).trim();
			 grp_code=(((String)lines.get(i)).substring(230,250)).trim();
			 grp_desc=(((String)lines.get(i)).substring(250,400)).trim();
			 grp_type=(((String)lines.get(i)).substring(400,406)).trim();
			 cl_ind_id=(((String)lines.get(i)).substring(406,410)).trim();
			 fam_code2=(nvl.execute((((String)lines.get(i)).substring(410,417)),"9970")).trim();
			 ram_code=(((String)lines.get(i)).substring(417,424)).trim();
			 tbs_code=(((String)lines.get(i)).substring(424,431)).trim();
			 rts_code=(((String)lines.get(i)).substring(431,438)).trim();	
			 
				//insert to pmi_korea_client
				String sqls="insert into pmi_korea_client(client_code,BOOKING_LOCATION,CLIENT_DESC,GROUP_SEGMENT_ID,CLIENT_SEGMENT_ID,INTERNAL_CRG,CLIENT_CRG,FAM_CODE,FAM_DESC,FAM_LOCATION,COUNTRY_REGISTRATION,COUNTRY_DOMICILE,GROUP_CODE,GROUP_DESC,GROUP_TYPE,CLIENT_INDUSTRY_ID,FAM_CODE2,RAM_CODE,TBS_CODE,RTS_CODE,partition_key) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pstmt =myConnection.prepareStatement(sqls);

				pstmt.setString(1,cl_code);
				pstmt.setString(2,book_loc);
				pstmt.setString(3,cl_desc);
				pstmt.setString(4,grp_seg_id);
				pstmt.setString(5,cl_seg_id);
				pstmt.setString(6,int_crg);
				pstmt.setString(7,cl_crg);
				pstmt.setString(8,fam_code);
				pstmt.setString(9,fam_desc);
				pstmt.setString(10,fam_loc);
				pstmt.setString(11,ctr_reg);
				pstmt.setString(12,ctr_dom);
				pstmt.setString(13,grp_code);
				pstmt.setString(14,grp_desc);
				pstmt.setString(15,grp_type);
				pstmt.setString(16,cl_ind_id);
				pstmt.setString(17,fam_code2);
				pstmt.setString(18,ram_code);
				pstmt.setString(19,tbs_code);
				pstmt.setString(20,rts_code);
				pstmt.setString(21,partition);
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
		String ctlFile="KOREA_STATIC";
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
		
	}
		catch (Exception e) {
			System.out.println(e.getMessage());
			messageCode1 = e.getMessage();
		}
		finally {
			if (myConnection != null && !myConnection.isClosed()) myConnection.close();
			
	        if(!readFileStat.equals(""))
	        {
	        	return new ActionForward(nextPage.getPath() + "?message=" + readFileStat); 
	        } else if (!fcReadStat.equals("")) {
		        return new ActionForward(nextPage.getPath() + "?message=" + fcReadStat); 
	        } else {
				return new ActionForward(nextPage.getPath() + "?message="+messageCode1+"&uploaded_record="+uploaded_record+"&rejected_record="+rejected_record+"&file="+logFile);
	        }


		}
			
		
		}

}