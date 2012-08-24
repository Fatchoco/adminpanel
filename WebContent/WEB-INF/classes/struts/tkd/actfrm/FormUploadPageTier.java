package struts.tkd.actfrm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class FormUploadPageTier extends ActionForm {
	private FormFile uploadFile;
	private Integer tier;

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}

	public void setUploadFile(FormFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public FormFile getUploadFile() {
		return uploadFile;
	}
}
