package struts.tkd.actfrm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class FormUploadPage extends ActionForm {
	private FormFile uploadFile;

	public void setUploadFile(FormFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public FormFile getUploadFile() {
		return uploadFile;
	}
	
	private String hdcntr;
	
	public void setHdcntr(String hdcntr) {
		this.hdcntr = hdcntr;
	}
	public String getHdcntr() {
		return hdcntr;	
	}	
	
	private String hdcntr2;
	
	public void setHdcntr2(String hdcntr2) {
		this.hdcntr2 = hdcntr2;
	}
	public String getHdcntr2() {
		return hdcntr2;	
	}
	
}
