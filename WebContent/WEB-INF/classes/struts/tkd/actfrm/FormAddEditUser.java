package struts.tkd.actfrm;

import org.apache.struts.action.ActionForm;


public class FormAddEditUser extends ActionForm {
	private String useridName;
	private String userFullName;
	private String userPassword;
	private String[] userAppAccess;
	private String[] userFormAccess;
	private String appAccess;
	private String formAccess;
	private String userAdmin="0";
	private String newUserPassword ;

	public String getuseridName() {
		return this.useridName;
	}

	public void setuseridName(String useridName) {
		this.useridName= useridName;
	}
	
	public void setNewUserPassword(String newUserPassword) {
		this.newUserPassword= newUserPassword;
	}
	
	public String getNewUserPassword() {
		return this.newUserPassword;
	}
	
	
	public void setuserFullName(String userFullName) {
		this.userFullName= userFullName;
	}
	
	public String getuserFullName() {
		return this.userFullName;
	}

	public void setuserPassword(String userPassword) {
		this.userPassword= userPassword;
	}
	
	
	public String getuserPassword() {
		return this.userPassword;
	}

	public void setuserAppAccess(String[] userAppAccess) {
		this.userAppAccess= userAppAccess;
	}
	
	public String[] getuserAppAccess() {
		return this.userAppAccess;
	}
	
	public void setuserFormAccess(String[] userFormAccess) {
		this.userFormAccess= userFormAccess;
	}
	
	public String[] getuserFormAccess() {
		return this.userFormAccess;
	}
	
	public void setuserAdmin(String userAdmin) {
		this.userAdmin= userAdmin;
	}
	
	public String getuserAdmin() {
		return this.userAdmin;
	}
	
	public void setAppAccess(String appAccess) {
		this.appAccess= appAccess;
	}
	
	public String getAppAccess() {
		
		return this.appAccess;
	}
	
	public String getAppAccessString() {
		
		String appacc="";
		if (userAppAccess!=null )
		{
			 for (String item : userAppAccess) {
				 	appacc+="!"+ item;
		          }
			 
		}
		return appacc;
	}	
	
	
	
	public void setFormAccess(String formAccess) {
		
		this.formAccess= formAccess;
		
	}
	
	
	public void setDBFormAccess(String formAccess) {
		 
		if (formAccess!=null )
		{
			this.userFormAccess =formAccess.split("!");
		 
		}
		
	}
	

	public void setDBAppAccess(String appAccess) {
		 
		if (appAccess!=null )
		{
			this.userAppAccess =appAccess.split("!");
		 
		}
		
		
		
	}
	
	public String getFormAccess() {
		return this.formAccess;
	}
	
	public String getFormAccessString() {
		
		String appacc="";
		if (userFormAccess!=null )
		{
			 for (String item : userFormAccess) {
				 	appacc+="!"+ item;
		          }
			 
		}
		return appacc;
	}	

}


