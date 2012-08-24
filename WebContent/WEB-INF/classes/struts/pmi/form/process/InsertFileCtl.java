package struts.pmi.form.process;


import java.sql.*;
import java.util.*;

public class InsertFileCtl {

	public void execute(java.sql.Connection myConnection,String fieldName,String fileName,int uploaded_record,int rejected_record, String username,String partition)
			throws Exception {

		Calendar cal= Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		int month=cal.get(Calendar.MONTH)+1;
		int year=cal.get(Calendar.YEAR);
		java.sql.Date date = new java.sql.Date(0000-00-00);
		String sql="insert into pmi_file_ctl(file_id,file_name,uploaded_record,rejected_record,create_date,create_user,partition_key)";
		sql+=" values(?,?,?,?,?,?,?)";
		PreparedStatement pstmt=myConnection.prepareStatement(sql);
		pstmt.setString(1,fieldName);
		pstmt.setString(2,fileName);
		pstmt.setInt(3,uploaded_record);
		pstmt.setInt(4,rejected_record);
		pstmt.setDate(5,date.valueOf(year+"-"+month+"-"+day));
		pstmt.setString(6,username);
		pstmt.setString(7,partition);
		try{
		pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
			
		}
		pstmt.close();	




	}

}