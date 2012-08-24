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

<title>Distributor File - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<p class="txtTitleContent" >
		<b><u>Distributor File</u></b>
	</p>
	<table align="center">
		<tr class="txtTitleMenu" align="center"><th style="border-bottom: 1px solid black">INVENTORY</th></tr>
		<c:choose>
			<c:when test="${IS_ADMIN == '1' }">
				<tr class="txtSubTitleMenu" align="center"><td><a href="UploadInventory.tkd">INVENTORY UPLOAD</a></td></tr>
			</c:when>
			<c:otherwise>
				<c:if test="${APP_ACCESS_DIST > 0 && FORM_ACCESS_INVTFILE > 0}">
					<tr class="txtSubTitleMenu" align="center"><td><a href="UploadInventory.tkd">INVENTORY UPLOAD</a></td></tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr class="txtTitleMenu" align="center"><th style="border-bottom: 1px solid black">SALES</th></tr>
		<c:choose>
			<c:when test="${IS_ADMIN == '1' }">
				<tr class="txtSubTitleMenu" align="center"><td><a href="ViewColumnMapping.tkd">DISTRIBUTOR FILE SETUP</a></td></tr>
				<tr class="txtSubTitleMenu" align="center"><td><a href="UploadDistributorFiles.tkd">DISTRIBUTOR FILES UPLOAD</a></td></tr>
			</c:when>
			<c:otherwise>
				<c:if test="${APP_ACCESS_DIST > 0 && FORM_ACCESS_COLMAP > 0}">
					<tr class="txtSubTitleMenu" align="center"><td><a href="ViewColumnMapping.tkd">DISTRIBUTOR FILE SETUP</a></td></tr>
				</c:if>
				<c:if test="${APP_ACCESS_DIST > 0 && FORM_ACCESS_DISTFILE > 0}">
					<tr class="txtSubTitleMenu" align="center"><td><a href="UploadDistributorFiles.tkd">DISTRIBUTOR FILES UPLOAD</a></td></tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr class="txtTitleMenu" align="center"><th style="border-bottom: 1px solid black">ADJUSTMENT</th></tr>
		<c:choose>
			<c:when test="${IS_ADMIN == '1' }">
				<tr class="txtSubTitleMenu" align="center"><td><a href="UploadDistributorAdjustmentFiles.tkd">DISTRIBUTOR ADJUSTMENT FILES UPLOAD</a></td></tr>
			</c:when>
			<c:otherwise>
				<c:if test="${APP_ACCESS_DIST > 0 && FORM_ACCESS_DISTADJFILE > 0}">
					<tr class="txtSubTitleMenu" align="center"><td><a href="UploadDistributorAdjustmentFiles.tkd">DISTRIBUTOR ADJUSTMENT FILES UPLOAD</a></td></tr>
				</c:if>
			</c:otherwise>
		</c:choose>
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