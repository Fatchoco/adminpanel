package struts.tkd.actfrm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class ActFrmUpload extends ActionForm {
	private FormFile uploadFile;

	public void setUploadFile(FormFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public FormFile getUploadFile() {
		return uploadFile;
	}
}
