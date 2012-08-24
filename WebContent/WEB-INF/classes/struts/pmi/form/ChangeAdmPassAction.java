package struts.pmi.form;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;


import java.security.*;
//levin17lacustre 07102009
public class ChangeAdmPassAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection = null;
		 ResultSet r = null;
		 Statement stmt = null;
		 String messageCode1 = "";
		
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
		 stmt=myConnection.createStatement();
		 
		//get pass
		ChangeAdmPassForm myForm=(ChangeAdmPassForm) form;
		String pass=myForm.getPass();
		String oldpass=myForm.getOldPass();


					/*MD5 logic*/
					String plainText = oldpass;
					MessageDigest md = MessageDigest.getInstance("MD5");
					md.reset();
					md.update(plainText.getBytes());
					
					byte[] digest = md.digest();
					StringBuffer hexString = new StringBuffer();
					
					for (int i = 0; i < digest.length; i++) 
					{
						plainText = Integer.toHexString(0xFF & digest[i]);
						if (plainText.length() < 2) 
						{
							plainText = '0' + plainText;
						}
						hexString.append(plainText);
					}
					plainText=pass;
					md.reset();
					md.update(plainText.getBytes());
					
					byte[] digest2 = md.digest();
					StringBuffer hexString2 = new StringBuffer();
					
					for (int i = 0; i < digest2.length; i++) 
					{
						plainText = Integer.toHexString(0xFF & digest2[i]);
						if (plainText.length() < 2) 
						{
							plainText = '0' + plainText;
						}
						hexString2.append(plainText);
					}
					
					
					//change pass
			try
			{
				
				myConnection.setAutoCommit(false);
				if(!(pass.equals(null)))
				{
					r = stmt.executeQuery ("select value_ from pmi_appvariables where varname='ADMIN_ACCESS'");
					
					if(r.next())
					{
						//System.out.println("ketemu yg admin");
						if((hexString.toString()).equals(r.getString(1)))
						{
						//System.out.println("pwd nya sama proceed update");

						String sql="update pmi_appvariables set value_=? where varname='ADMIN_ACCESS'";
						PreparedStatement pstmt =myConnection.prepareStatement(sql);	

						pstmt.setString(1,hexString2.toString());
									try{
									pstmt.executeUpdate();
									messageCode1 = "?message=Admin Password Successfully Updated";
									pstmt.close();
									}
									catch(SQLException se)
									{
										System.out.println(se);
									}		
						}
						else
						{
							//System.out.println("pwd gak sama");
							messageCode1 = "?message=You fill wrong old password";
						}
					}
					else
					{
						//System.out.println("gak ketemu yg admin");
						String sql="insert pmi_appvariables(varname,value_) values(?,?)";
						PreparedStatement pstmt =myConnection.prepareStatement(sql);	

						pstmt.setString(1,"ADMIN_ACCESS");
						pstmt.setString(2,hexString2.toString());
									try{
									pstmt.executeUpdate();
									pstmt.close();
									}
									catch(SQLException se)
									{
										System.out.println(se);
									}		
					}
			
			try
					{
						stmt.close();
						myConnection.commit();
						myConnection.close();
						
					}
					catch(SQLException e)
					{
						System.out.println(e);
					}
								
				}
				else
				{
					messageCode1 = "?message=Please insert your password";
				}	

			}
			catch(Exception e)
			{
				myConnection.rollback();
				System.out.println(e);
		
			}			

			return null;
			
} catch (Exception e) {
	System.out.println(e.getMessage());
} finally {
	if (r != null) r.close();
	if (stmt != null) stmt.close();
	if (myConnection != null && !myConnection.isClosed()) myConnection.close();
	//System.out.println("masuk block finally: "+messageCode1 );
	return new ActionForward(nextPage.getPath()+ messageCode1);
}
	}

}