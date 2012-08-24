package struts.tkd.act;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;	

import struts.tkd.actfrm.ActionFormValidated;

public class Header extends ActionFormValidated{
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response){
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			if (request.getSession().getAttribute("FULL_NAME")!=null)
			{
				request.setAttribute("LoginName", request.getSession().getAttribute("FULL_NAME").toString());
			}
			return forward; 
			
	  }
}