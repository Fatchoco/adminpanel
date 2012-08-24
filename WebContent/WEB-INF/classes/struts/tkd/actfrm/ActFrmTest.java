package struts.tkd.actfrm;

import org.apache.struts.action.ActionForm;

public class ActFrmTest extends ActionForm {
	private String dummystr;

	public void setDummystr(String dummystr) {
		this.dummystr = dummystr;
	}

	public String getDummystr() {
		return dummystr;
	}
}
