package struts.pmi.form;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;
import java.math.*;

public class TaxRateAction extends Action {

	@SuppressWarnings("finally")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		javax.sql.DataSource dataSource;
		java.sql.Connection myConnection = null;
		PreparedStatement pstmt = null;
		ResultSet r = null;
		boolean stat = false;

		//check login stat
		if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != ""))
		{
			ActionForward login=mapping.findForward("loginpage");
			return login;
			
		}	

		//get pass
		TaxRateForm myForm=(TaxRateForm) form;
		BigDecimal trate = new BigDecimal(myForm.gettxtTaxRate());
		
try{		
		//initiate db connection
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		 
		 Statement stmt=myConnection.createStatement();
		 

			r = stmt.executeQuery ("select count(*) from pmi_calc_parameter");
			r.next();
			if(r.getInt(1)==0)
			{
				String sql="insert into pmi_calc_parameter(tax_rate) values(?)";
				pstmt = myConnection.prepareStatement(sql);	
				pstmt.setBigDecimal(1, trate);
				pstmt.executeUpdate();
			}
			else
			{
				String sql="update pmi_calc_parameter set tax_rate = ?";
				pstmt = myConnection.prepareStatement(sql);	
				pstmt.setBigDecimal(1, trate);;
				pstmt.executeUpdate();
			}
			
			stat = true;


} catch (Exception e){
	System.out.println(e);
} finally {
	if (r != null) r.close();
	if (pstmt != null) pstmt.close();
	if (myConnection != null && !myConnection.isClosed()) myConnection.close();
	
	if (stat) {
		return new ActionForward(nextPage.getPath()+"?message=Tax Rate Successfully Updated");
	} else {
		return new ActionForward(nextPage.getPath()+"?message=Error occured ");
	}

}

	}

}