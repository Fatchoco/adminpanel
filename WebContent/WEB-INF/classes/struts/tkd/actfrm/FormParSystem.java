package struts.tkd.actfrm;

import org.apache.struts.action.ActionForm;

public class FormParSystem extends ActionForm {
	private String cur_period;

	public void setCur_period(String cur_period) {
		this.cur_period = cur_period;
	}

	public String getCur_period() {
		return cur_period;
	}
}
