package struts.pmi.form ;

import org.apache.struts.action.ActionForm;


public class Login2Form extends ActionForm {

	 private String txtuname;
	 private String txtpwd;

	 public String getTxtuname() {
	    return txtuname;
	  }

	  public void setTxtuname(String txtUser) {
	    this.txtuname = txtUser;
	  }

	  public String getTxtpwd() {
	    return txtpwd;
	  }

	  public void setTxtpwd(String txtPassword) {
	    this.txtpwd = txtPassword;
	  }
}