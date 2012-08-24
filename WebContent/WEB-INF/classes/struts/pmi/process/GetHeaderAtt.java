package struts.pmi.process;


import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.*;
//levin17lacustre 25092009
public class GetHeaderAtt extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String role="";
		String [] roles=new String[0];
		String greetings="";
		//check login stat
		if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != ""))
		{
			ActionForward login=mapping.findForward("loginpage");
			return login;
			
		}	
		String username=(String)request.getSession().getAttribute("username");
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

/*		
		Calendar cal =new GregorianCalendar();
		int hour24 = cal.get(Calendar.HOUR_OF_DAY);
		if(username.equals("admin")){username="Admin";}
		if(hour24>=0 && hour24<6)
			greetings="Hi, "+username+", got something to be done early in the morning?";
		else if(hour24>=6 && hour24<11)
			greetings="Good morning, "+username;
		else if(hour24>=11 && hour24<15)
			greetings="Welcome, "+username+", have a nice day.";
		else if(hour24>=15 && hour24<18)
			greetings="Good afternoon, "+username;
		else if(hour24>=18 && hour24<22)
			greetings="Good evening, "+username+", working late today? food?";
		else if(hour24>=22 && hour24<=23)							
			greetings="Hi, "+username+", it's late at night.";
*/		

		greetings = request.getSession().getAttribute("fullName").toString();
		request.setAttribute("greetings",greetings);
		//redirect
		return nextPage;
	}

}