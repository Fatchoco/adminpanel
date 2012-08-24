package struts.pmi.process;


import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

//levin17lacustre 25092009
public class GetMenu extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String role="";
		String [] roles=new String[0];
		
		//check login stat
		if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != ""))
		{
			ActionForward login=mapping.findForward("loginpage");
			return login;
			
		}	

		if(request.getSession().getAttribute("role") != null && request.getSession().getAttribute("role")!="" )
		{
			role = (String)request.getSession().getAttribute("role");
			roles=role.split("_jsepj_");
		}
		int flagPMI=0;
		int flagTBMI=0;
		int flagOCC=0;
		int flagBR=0;
		int flagADM=0;
		
		for(int i=0;i<roles.length;i++)
		{
			if(roles[i].equals("ADM"))
			{
				flagADM=1;
			}
			if(roles[i].equals("PMI"))
			{
				flagPMI=1;
			}
			if(roles[i].equals("TB-MI"))
			{
				flagTBMI=1;
			}
			if(roles[i].equals("OCC"))
			{
				flagOCC=1;
			}
			if(roles[i].equals("BR"))
			{
				flagBR=1;
			}
				
		}

		request.setAttribute("flagPMI",flagPMI);
		request.setAttribute("flagTBMI",flagTBMI);
		request.setAttribute("flagOCC",flagOCC);
		request.setAttribute("flagBR",flagBR);
		request.setAttribute("flagADM",flagADM);
		//redirect

		return nextPage;
	}

}