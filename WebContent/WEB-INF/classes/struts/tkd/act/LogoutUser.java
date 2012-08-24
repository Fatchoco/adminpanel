package struts.tkd.act;

//import java.sql.PreparedStatement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.jsp.jstl.sql.Result;
//import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LogoutUser extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ActionForward forward = null;
		forward = mapping.findForward("forward");
		request.getSession().setAttribute("USER_NAME",null);
		request.getSession().setAttribute("FULL_NAME",null);
		request.getSession().setAttribute("APP_ACCESS",null);
		request.getSession().setAttribute("FORM_ACCESS",null);
		request.getSession().setAttribute("IS_ADMIN",null);

		return new ActionForward(forward.getPath());


	}
}
