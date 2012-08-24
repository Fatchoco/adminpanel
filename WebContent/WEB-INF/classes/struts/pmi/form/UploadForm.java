package struts.pmi.form;


import org.apache.struts.action.ActionForm;

import org.apache.struts.upload.FormFile;

//levin17lacustre 05102009
public class UploadForm extends ActionForm  {


	 private FormFile uploadFile;
	 private String part;
	  public String getPart() {
		    return part;
	  }
	  /**
	   * @return Returns the theFile.
	   */
	  public FormFile getUploadFile() {
	    return uploadFile;
	  }
	  /**
	   * @param theFile The FormFile to set.
	   */
	  public void setUploadFile(FormFile theFile) {
	    this.uploadFile = theFile;
	  }

	
	  

}