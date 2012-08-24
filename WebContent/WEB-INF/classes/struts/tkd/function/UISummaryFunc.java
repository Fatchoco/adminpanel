package struts.tkd.function;

import java.sql.*;

import javax.sql.*;

public class UISummaryFunc {
	public static boolean insertLog(DataSource pDataSource, int pOrderNo, String pPeriod, String pAction, String pActionCode, String pFileName, String pNotes, String pUser) throws SQLException
	{
		boolean					returnresult	= true;
		java.sql.Connection		myConnection	= null;
		PreparedStatement		stmt			= null;
		String					insertQry;
		
		try
		{
			insertQry		= "INSERT INTO TKD_APP_SUMMARY(ORDER_NO, PERIOD, ACTION, ACTION_CODE, FILE_NAME, NOTES, CREATED_DATE, CREATED_USER) VALUES (?, (SELECT MAX(CURRENT_PERIOD) FROM PAR_SYSTEM), ?, ?, ?, ?, SYSDATE, ?)";
			myConnection	= pDataSource.getConnection();
			stmt			= myConnection.prepareStatement(insertQry);
			
			stmt.setInt(1, pOrderNo);		//OrderNo
			stmt.setString(2, pAction);		//Action
			stmt.setString(3, pActionCode);	//ActionCode
			stmt.setString(4, pFileName);	//FileName
			stmt.setString(5, pNotes);		//Notes
			stmt.setString(6, pUser);		//User
			
			stmt.execute();
		}
		catch(Exception e)
		{
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
