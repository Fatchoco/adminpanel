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

public class PmiSwitchAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String tableName="PMI_COUNTRY_SWITCH";
		String messageCode1="";
		String opMode = request.getParameter("m");
		String partition = "";
		javax.sql.DataSource dataSource;
		java.sql.Connection myConnection = null;
		String readFileStat = "";
		String tabReadStat = "";
		ArrayList messageCode = new ArrayList();
		int uploaded_record=0;
		int rejected_record=0;
		int i=0;
		String sql="";
		String logFile="PMI_SWITCH_EXP.txt";


		
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
        
        //System.out.println("before read");
        //read upload file and write to disk
        readFileStat=reader.readStat(myFile, path,fileName);
        //System.out.println("read yupload");


        //System.out.println("read tab");
		//read tab uploaded file
		int fieldNum=3;	
		String HeaderValid="COUNTRY_ID\tSOURCE_CODE\tEFFECTIVE_PERIOD";	
		ReadTabTxt tabReader=new ReadTabTxt();
		tabReadStat=tabReader.readTabStat(path, fieldNum, HeaderValid);

	if (readFileStat.equals("") && tabReadStat.equals("")) {	
		
		//get line and header
		lines=tabReader.getLines();
		Header=tabReader.getHeader();

		//get current partition
		//System.out.println("get partition");
		GetPartition part=new GetPartition();

		if (opMode.equals("p")){
			partition=part.execute(1, myConnection);
		}else if (opMode.equals("t")){
			partition=part.execute(1, myConnection);
		}else if (opMode.equals("b")){
			partition=part.execute(3, myConnection);
		};
		
		//delete table content
		//System.out.println("del table");
		TruncDel deleteTable=new TruncDel();
		
		if (opMode.equals("p")){
			deleteTable.execute(3, myConnection, "", tableName, "PMI");
		}else if (opMode.equals("t")){
			deleteTable.execute(3, myConnection, "", tableName, "TBMI");
		}else if (opMode.equals("b")){
			deleteTable.execute(3, myConnection, "", tableName, "BR");
		};
		
        //insert process

		Calendar calCurr = GregorianCalendar.getInstance();
		
		for(i=0;i<lines.size();i++)
		{

		 String[] temp=((String)(lines.get(i))).split("\t",4);

			//insert to table
			sql="insert into " +tableName+" (SOURCE_CODE, TARGET_CODE, COUNTRY_ID, EFFECTIVE_PERIOD, " +
					"create_date,create_user) values(?,?,?,?,sysdate,'PMI_UPLOAD_INSERT')";
			PreparedStatement pstmt = myConnection.prepareStatement(sql);
		 
		 try{
					//validation
					if ((temp[0].trim().equals("")) && ((temp[0].trim()).length()>3)) 
					{
						throw new Exception ("incorrect Country ID");
					}else if (!temp[1].trim().equals("CP") && !temp[1].trim().equals("BNCPR")) 
					{
						throw new Exception ("incorrect Source Code");
					}else if (temp[1].trim().equals("BNCPR"))
					{
						if (temp[2].trim().equals("")) 
						{
							throw new Exception ("missing Effective period");
						} else if ((temp[2].trim()).length()!=6)
						{
							throw new Exception ("incorrect period format");
						}
					};
					
					pstmt.setString(1,temp[1].trim());

					if (opMode.equals("p")){
						pstmt.setString(2,"PMI");
					}else if (opMode.equals("t")){
						pstmt.setString(2,"TBMI");
					}else if (opMode.equals("b")){
						pstmt.setString(2,"BR");
					};

					pstmt.setString(3,temp[0].trim());
					if (temp[1].trim().equals("CP")) {
						pstmt.setString(4,"");
					} else {
						calCurr.set(Integer.parseInt(temp[2].substring(0,4)), Integer.parseInt(temp[2].substring(4))-1, 1); // Months are 0 to 11
						pstmt.setString(4,temp[2].trim()+ calCurr.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) +"0000");
					}
						
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
		
		//delete control table row
		if (opMode.equals("p")){
			deleteTable.execute(1, myConnection, partition, tableName, "PMI_SWITCH");
		}else if (opMode.equals("t")){
			deleteTable.execute(1, myConnection, partition, tableName, "TBMI_SWITCH");
		}else if (opMode.equals("b")){
			deleteTable.execute(1, myConnection, partition, tableName, "BR_SWITCH");
		};
		
		
		//insert file control
		String username=(String)request.getSession().getAttribute("username");
		InsertFileCtl insertCtl=new InsertFileCtl();

		if (opMode.equals("p")){
			insertCtl.execute(myConnection, "PMI_SWITCH", fileName, uploaded_record, rejected_record, username, partition);
		}else if (opMode.equals("t")){
			insertCtl.execute(myConnection, "TBMI_SWITCH", fileName, uploaded_record, rejected_record, username, partition);
		}else if (opMode.equals("b")){
			insertCtl.execute(myConnection, "BR_SWITCH", fileName, uploaded_record, rejected_record, username, partition);
		};
		
		//delete temp file
		DeleteFile del=new DeleteFile();
		del.deleteStat(path);
		
		if(rejected_record==0){messageCode1="Import Successful Without Any Error";}
		else{messageCode1="Import Finish With Log";}
		
	}	
} catch (Exception e) {
	System.out.println (e.getMessage());
	messageCode1 = e.getMessage();	
	
} finally  {
	//redirect

	if (myConnection != null && !myConnection.isClosed()) myConnection.close();
	
    if(!readFileStat.equals(""))
    {
    	return new ActionForward(nextPage.getPath() + "?message=" + readFileStat+"&m="+opMode); 
    } else if (!tabReadStat.equals("")) {
        return new ActionForward(nextPage.getPath() + "?message=" + tabReadStat+"&m="+opMode); 
    } else {
		return new ActionForward(nextPage.getPath() + "?message="+messageCode1+"&uploaded_record="+uploaded_record+"&rejected_record="+rejected_record+"&file="+logFile+"&m="+opMode);
    }
	
}

	}

}