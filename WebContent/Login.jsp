<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Login - Takeda</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>
<body onload="setFocus()">
<script language="javascript">

function setFocus() {
	document.getElementById("txtuname").focus();
}

function submitForm()
{
	document.getElementById("frm").submit();
}

function validateLogin()
{
	 document.getElementById("err2").innerHTML = "";
	 document.getElementById("err1").innerHTML = "";
	 var uname = document.getElementById("txtuname").value ;
	 var pwd = document.getElementById("txtpwd").value;
	 if (uname!="" && pwd!="") {
		 submitForm();
	 } else {
		 document.getElementById("err1").innerHTML = "user name and password must be filled";
	 };
}

function readenter2(e,readfunc)
{
	var keycode;
	if (window.event) keycode = window.event.keyCode;
	else if (e) keycode = e.which;
	else return false;
	if (keycode == 13)
	{
		eval(readfunc);
		return false;
	}
}

</script>


   
    <div id="header">
    	<img src="images/header.jpg" width="100%" />
    </div>
    <div style="background: #9B0101; height:10px;">
	</div>
<div class="content">
<div class="tkd_login_box">
  <html:form action="/LoginProcess" method="post"
	enctype="multipart/form-data" styleId="frm">

    <table width="332" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td colspan="3" class="tkd_login_header" >Sign In</td>
        </tr>
      <tr>
        <td class="tkd_login_box_t_l">&nbsp;</td>
        <td class="tkd_login_box_t_m">&nbsp;</td>
        <td class="tkd_login_box_t_r">&nbsp;</td>
      </tr>
      <tr>
        <td class="tkd_login_box_m_l">&nbsp;</td>
        <td class="none">
        <table width="100%" height="103" border="0" cellpadding="0" cellspacing="0" align="center">
          <tr>
            <td width="37%" height="27">USER ID</td>
            <td width="4%">:</td>
            <td width="59%">
              <html:text property="txtuname" styleId="txtuname"
				onkeypress="return readenter2(event,'validateLogin()');"
				styleClass="LoginTextBox" size="20" />
            </td>
          </tr>
          <tr>
            <td height="30">PASSWORD</td>
            <td>:</td>
            <td><html:password property="txtpwd" styleId="txtpwd"
				onkeypress="return readenter2(event,'validateLogin()');"
				styleClass="LoginTextBox" size="21" /></td>
          </tr>
           <tr>
            <td height="30">APPLICATION</td>
            <td>:</td>
            <td>
            <html-el:select property="menu">
							<html-el:option value="1">China Data Mart</html-el:option>
							<html-el:option value="2">Market Analyzer</html-el:option>
						</html-el:select>  
            </td>
          </tr>
		<tr>
            <td height="18"></td>
            <td></td>
            <td class="err">
             <span id="err1" class="LoginError"><c:out value="${param.e}"/></span>
          <span id="err2" class="LoginError"></span>
		</td>
          </tr>
          <tr>
            <td height="28">&nbsp;</td>
            <td>&nbsp;</td>
            <td><table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
              <tr>
                <td class="tkd_button_left">&nbsp;</td>
                <td align="center" class="tkd_button_middle">
                	<a href="#" onclick="javascript:validateLogin()"> LOGIN</a>
                </td>
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
</div>
</div>
  
<!-- <c:import url="Footer.jsp"></c:import> -->


</body>
</html>
