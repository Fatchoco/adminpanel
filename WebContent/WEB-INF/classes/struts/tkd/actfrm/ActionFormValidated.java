package struts.tkd.actfrm;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.function.userSessionCheck;


public abstract class ActionFormValidated extends Action {




	public ActionForward execute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception  {

		if (mapping.getParameter()!= null)
		{
			String actionParam = mapping.getParameter().toString();
			ActionForward login=mapping.findForward("gf_loginpage");
			if (actionParam.equals("MustLogin"))
			{
				if(userSessionCheck.isLogin(request)==0 )
				{
					return login;
				}
			}else 
			{
				String appAccess="";
				String formAccess="";
				appAccess= actionParam.split("\\.")[0].toString();
				formAccess= actionParam.split("\\.")[1].toString();
				if(userSessionCheck.isvalid(request, formAccess, appAccess)!=1 )
				{
					return login;
				}
			}

		}
		return doExecute(mapping, form, request, response) ;
	} // End execute()

	public abstract ActionForward doExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception;


} // End class
