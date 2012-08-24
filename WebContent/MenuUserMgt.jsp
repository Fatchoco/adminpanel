<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<c:if test="${USER_NAME == null}">
<script language="JavaScript">
	window.location = "Login.tkd";
</script>
</c:if>

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
	<p class="txtTitleContent" >
		<b><u>User Management</u></b>
	</p>
	<table align="center">
		<tr class="txtSubTitleMenu" align="center"><td><a href="ChangePassword.tkd">CHANGE PASSWORD</a></td></tr>
		<c:if test="${IS_ADMIN == '1' }">
			<tr class="txtSubTitleMenu" align="center"><td><a href="ViewUserList.tkd">VIEW USER LIST</a></td></tr>
		</c:if>
	</table>
	<!-- <c:import url="Footer.jsp"></c:import> -->
</div>
</body>
</html:html>

<c:if test="${param.message != null}">
	<script language="JavaScript">
		alert("<c:out value='${param.message}'/>"); 
	</script>
</c:if>