package struts.pmi.form;


import org.apache.struts.action.ActionForm;


//levin17lacustre 07102009
public class ChangeAdmPassForm extends ActionForm  {


	 private String oldPass;
	 private String pass;
	 private String pass2;
	 
	 //getter
	  public String getOldPass() {
		    return oldPass;
	  }

	  public String getPass() {
	    return pass;
	  }
	  
	  public String getPass2(){
		  return pass2;
	  }

	  
	  //setter
	  public void setOldPass(String oldPass) {
	    this.oldPass = oldPass;
	  }

	  public void setPass(String pass){
		  this.pass=pass;
	  }

	  public void setPass2(String pass2){
		  this.pass2=pass2;
	  }

}