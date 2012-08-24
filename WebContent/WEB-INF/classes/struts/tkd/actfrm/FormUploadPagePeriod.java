package struts.tkd.actfrm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class FormUploadPagePeriod extends ActionForm {
	private FormFile uploadFile;
	private	String cur_period;

	public String getCur_period() {
		return cur_period;
	}

	public void setCur_period(String cur_period) {
		this.cur_period = cur_period;
	}

	public void setUploadFile(FormFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public FormFile getUploadFile() {
		return uploadFile;
	}
}
