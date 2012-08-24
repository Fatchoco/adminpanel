package struts.pmi.validation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import java.sql.*;
import java.util.*;
//levin17lacustre 29092009

public class CheckAdm extends Action {


  public ActionForward execute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception  {
		String role="";

		

		 
		if(request.getSession().getAttribute("role") != null && request.getSession().getAttribute("role")!="" )
		{
			role = (String)request.getSession().getAttribute("role");

		}

		

		//check whether user is   admin	

		
		if(!(role.equals("ADM")))
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
