package struts.pmi.process;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

//levin17lacustre 07102009

public class DoLogout extends Action {


  public ActionForward execute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception  {
		if(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != "")
		{
			request.getSession().removeAttribute("username");
			request.getSession().removeAttribute("role");
			request.getSession().invalidate();
		}
		ActionForward forward = null;
		forward = mapping.findForward("loginpage");
		return forward;
  } // End execute()


} // End class
