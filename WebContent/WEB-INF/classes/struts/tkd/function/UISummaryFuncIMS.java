package struts.tkd.function;

import java.sql.*;

import javax.sql.*;

public class UISummaryFuncIMS {
	public static boolean insertLog(DataSource pDataSource, String pUser, String pPeriod, String pCountry, String pAction, String pFilename) throws SQLException
	{
		boolean					returnresult	= true;
		java.sql.Connection		myConnection	= null;
		PreparedStatement		stmt			= null;
		String					insertQry;
		
		try
		{
			insertQry		= "INSERT INTO IMS_SUMMARY(CREATED_DATE, CREATED_USER, PERIOD, COUNTRY, ACTION, FILENAME) VALUES (SYSDATE, ?, ?, ?, ?, ?)";
			myConnection	= pDataSource.getConnection();
			stmt			= myConnection.prepareStatement(insertQry);
			
			stmt.setString(1, pUser);
			stmt.setString(2, pPeriod);
			stmt.setString(3, pCountry);	
			stmt.setString(4, pAction);	
			stmt.setString(5, pFilename);	
			
			stmt.execute();
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
			returnresult = false;
		}
		finally
		{
			if(stmt!=null)stmt.close();
			if(myConnection!=null)myConnection.close();
		}
		
		return returnresult;
	}
}
