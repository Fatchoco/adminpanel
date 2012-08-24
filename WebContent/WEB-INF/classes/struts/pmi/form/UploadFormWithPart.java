package struts.pmi.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionFormBean;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.ImageButtonBean;
import org.apache.struts.util.MessageResources;
import org.apache.struts.upload.FormFile;

//levin17lacustre 05102009
public class UploadFormWithPart extends ActionForm  {


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

	  public void setPart(String part){
		  this.part=part;
	  }
	  

}