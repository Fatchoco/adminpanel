package struts.pmi.form.process;


import java.sql.*;
//levin17lacustre 25092009
public class TruncDel {

	public void execute(int flag,java.sql.Connection myConnection,String partition,String tableName,String fieldName)
			throws Exception {

			 String sql="";
			 //choose delete statement
			 if(flag==0||flag==2)
			 {
				 sql="delete "+tableName;
				 if(flag==2)
				 {
					 sql+=" where partition_key='"+partition+"'";
				 }
			 }
			 else if(flag==1)
			 {
				 sql="delete pmi_file_ctl where file_ID ='"+fieldName+"' and partition_key='"+partition+"'";
			 } 
			 else if (flag==3)
			 {
				 sql="delete "+tableName+" where TARGET_CODE='"+ fieldName + "'";
			 }
			 else if (flag==4)
			 {
				 sql="delete "+tableName+" where YEAR_DATA='"+ partition + "'";
			 }		 

			 //delete it
			 try { 
				PreparedStatement ppStmtDel=myConnection.prepareStatement(sql);
				ppStmtDel.executeUpdate();
				ppStmtDel.close();	
				
			
			 } catch (SQLException sqle) {
			    System.out.println(sqle);
			 } 


	}

}