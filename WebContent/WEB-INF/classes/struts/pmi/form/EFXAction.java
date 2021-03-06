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

public class EFXAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String tableName="PMI_EFX";
		String messageCode1="";
		ArrayList messageCode = new ArrayList();
		int uploaded_record=0;
		int rejected_record=0;
		int i=0;
		String sql="";
		String num="";
		javax.sql.DataSource dataSource;
		java.sql.Connection myConnection = null;
		String logFile="EFX_EXP.txt";
		String readFileStat = "";
		String tabReadStat = "";

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
		int fieldNum=5;	
		String HeaderValid="Period\tMarketer Country\tSCI ID\tCurrency\tTMC";	
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
		String partition=part.execute(2, myConnection);

		
		//add partition with the last date
		if(!(parti.equals("")))
		{
		System.out.println("part " + parti);
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
		IsDouble checkDouble=new IsDouble();
		for(i=0;i<lines.size();i++)
		{

		 String[] temp=((String)(lines.get(i))).split("\t",fieldNum);
		 num=temp[4].trim();
		 num=num.replace("\"","");
		 num=num.replace(",",".");	

			 if(!((temp[0].trim()).equals(partition.substring(0,6))))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Reporting Date");

				rejected_record++;

			 }


			 else if(checkDouble.execute(num))
			 {

				messageCode.add((String)(lines.get(i))+"\t:Invalid Numeric Format");
				rejected_record++;


			 }						 					 

			else
			{
				 
					//insert to table
					sql="insert into PMI_EFX(partition_key,booking_location_id,client_id,tmc,currency) values(?,?,?,?,?)";
					PreparedStatement pstmt =myConnection.prepareStatement(sql);

					
					
					pstmt.setString(1,partition);
					pstmt.setString(2,temp[1]);
					pstmt.setString(3,temp[2]);
					pstmt.setDouble(4,Double.parseDouble(num));
					pstmt.setString(5,temp[3]);


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
		String ctlFile="EFX";
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
		
		//get total tmc
		if(uploaded_record>0)
		{
			Statement stmt=myConnection.createStatement();
			ResultSet r=stmt.executeQuery ("select sum(tmc) from pmi_efx where partition_key='"+partition+"'");
			if(r.next())
			{
				request.setAttribute("TMC",r.getBigDecimal(1));
			}
			try
			{
				stmt.close();
			}
			catch(SQLException e)
			{
				System.out.println(e);
			}
		}
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