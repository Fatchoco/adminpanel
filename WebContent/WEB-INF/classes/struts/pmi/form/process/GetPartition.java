package struts.pmi.form.process;


import java.sql.*;
//levin17lacustre 25092009
public class GetPartition {

	public String execute(int flagPartition,java.sql.Connection myConnection)
			throws Exception {


			 String selectField="";
			 String partition="";
			 //check the select partition field
			 if(flagPartition==1)
			 {
				 selectField="current_partition";
			 }
			 else if(flagPartition==2)
			 {
				 selectField="occ_current_partition";
			 }
			 else if(flagPartition==3)
			 {
				 selectField="br_current_partition";
			 }
			 //get partition
			 try { 
				 Statement stmt = myConnection.createStatement();
				 ResultSet r;
				 r = stmt.executeQuery ("select "+selectField+" from pmi_system_parameter");

				 if(r.next())
				 {
				 	 partition=r.getString(1);
					
				 }
				 stmt.close();
			 } catch (SQLException sqle) {
			    System.out.println(sqle);
			 } 

			 return partition;
	}

}