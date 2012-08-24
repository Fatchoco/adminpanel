package struts.pmi.validation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

//levin17lacustre 29092009

public class CheckTbMi extends Action {


  public ActionForward execute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception  {
		String role="";
		String [] roles=new String[0];
		if(request.getSession().getAttribute("role") != null && request.getSession().getAttribute("role")!="" )
		{
			role = (String)request.getSession().getAttribute("role");
			roles=role.split("_jsepj_");
		}
		int flag=0;
		
		for(int i=0;i<roles.length;i++)
		{
			if(roles[i].equals("TB-MI")){
				flag=1;}
							
		}
		//check whether user is 'role' or admin	

		
		if(flag!=1&&!(role.equals("ADM")))
		{
			ActionForward nextPage = null;
			nextPage = mapping.findForward("UploadMenu");
			return nextPage;

		}
		ActionForward forward = null;
		forward = mapping.findForward("forward");
		return forward;
  } // End execute()


} // End class
