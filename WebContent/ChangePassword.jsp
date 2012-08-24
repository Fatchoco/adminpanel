<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />



<script language="javascript">

	function Cancel() {
		window.location.href="ViewUserList.tkd";
	}
	
	
	function SaveData() {
		
		
		
		if (document.forms[0].userPassword.value =='' )
		{
		   alert('Old Password must be filled!');
		   return;
		}
		
		if (document.forms[0].newUserPassword.value =='' )
		{
		   alert('New Password must be filled!');
		   return;
		}
		
		if (document.forms[0].newUserPassword.value !=document.forms[0].confirmPwd.value )
		{
		   alert('New Password must be the same with password confirmation!');
		   return;
		}
		
		document.forms[0].submit();
		
	}

	
</script>

<title>User Management - Takeda</title>

<body>
	<div class="content">
		<script type="text/javascript" src="wz_tooltip.js"></script>
			<c:choose>
		<c:when test="${LOGIN_APP == 'IMS' }">
		<c:import url="Header2.tkd"></c:import>
		</c:when>
		<c:otherwise>
		<c:import url="Header.tkd"></c:import>
		</c:otherwise>
	</c:choose>
	</div>
	<html:form  action="/ChangePasswordProcess" method="post">
		<table border=0>
			<tr>
				<td class="txtTitleContent" ><b><u>User Management - Change Password</u>
				</b>
				</td>
			</tr>
		</table>	
	<table width="332" border="0" cellspacing="0" cellpadding="0" align="center">
      
      <tr>
        <td class="tkd_login_box_t_l">&nbsp;</td>
        <td class="tkd_login_box_t_m">&nbsp;</td>
        <td class="tkd_login_box_t_r">&nbsp;</td>
      </tr>
      <tr>
        <td class="tkd_login_box_m_l">&nbsp;</td>
        <td class="none"><table width="100%" height="103" border="0" cellpadding="0" cellspacing="0" align="center">
          
          <tr>
            <td class="txtSubSubTitleContent" height="30">Old Password</td>
            <td class="txtSubSubTitleContent">:</td>
            <td>
            <html-el:password	property="userPassword" value="${FormAddEditUser.userPassword}" />
						
            </td>
            
            <tr>
            <td class="txtSubSubTitleContent" height="30">New Password</td>
            <td class="txtSubSubTitleContent">:</td>	
            <td>
	            <html-el:password	property="newUserPassword"  />  
            </td>
          </tr>
          	
          	<tr>
            <td class="txtSubSubTitleContent" height="30">Confirm Password</td>
            <td class="txtSubSubTitleContent">:</td>
            <td>
            	<input type="password"	name="confirmPwd">			
            </td>
          </tr>

<tr>
            <td height="18"></td>
            <td></td>
            <td class="err">
             <span id="err1" class="LoginError"><c:out value="${param.message}"/></span>
          	 <span id="err2" class="LoginError"><c:out value="${param.messageDone}"/></span>
		</td>
          </tr>
          <tr>
            <td height="28">&nbsp;</td>
            <td>&nbsp;</td>
            <td><table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
              <tr>
                <td class="tkd_button_left">&nbsp;</td>
                <td align="center" class="tkd_button_middle">
                
                <a href="#" onclick="javascript:SaveData()"> Change Password</a></td>
                <td class="tkd_button_right">&nbsp;</td>
              </tr>
            </table></td>
          </tr>
        </table></td>
        <td class="tkd_login_box_m_r">&nbsp;</td>
      </tr>
      <tr>
        <td class="tkd_login_box_b_l">&nbsp;</td>
        <td class="tkd_login_box_b_m">&nbsp;</td>
        <td class="tkd_login_box_b_r">&nbsp;</td>
      </tr>
    </table>
    
		
	</html:form>
<!-- <c:import url="Footer.jsp"></c:import> -->

</body>
</html:html>


