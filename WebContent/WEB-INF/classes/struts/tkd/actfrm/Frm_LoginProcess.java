package struts.tkd.actfrm;

import org.apache.struts.action.ActionForm;

public class Frm_LoginProcess extends ActionForm {
	private String txtuname;
	private String txtpwd;
	
	public void setTxtuname(String txtuname) {
		this.txtuname = txtuname;
	}
	public String getTxtuname() {
		return txtuname;
	}
	public void setTxtpwd(String txtpwd) {
		this.txtpwd = txtpwd;
	}
	public String getTxtpwd() {
		return txtpwd;
	}
	public String menu;
	   
    public String getMenu() {
        return menu;
    }
   
    public void setMenu(String menu) {
        this.menu = menu;
    }
}
