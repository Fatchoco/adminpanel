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

<title>Master File - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<p class="txtTitleContent">
		<b><u>Master File</u></b>
	</p>
	<table align="center">
		<tr class="txtTitleMenu" align="center"><th style="border-bottom: 1px solid black">MONTHLY</th></tr>
		<c:choose>
			<c:when test="${IS_ADMIN == '1' }">
				<tr class="txtSubTitleMenu" align="center"><td><a href="ParSystem.tkd">REPORTING PERIOD</a></td></tr>
				<tr class="txtSubTitleMenu" align="center"><td><a href="UploadMasterProduct.tkd">PRODUCT MASTER</a></td></tr>
				<tr class="txtSubTitleMenu" align="center"><td><a href="UploadMasterDistributorPrice.tkd">DISTRIBUTOR PRICE MASTER</a></td></tr>
			</c:when>
			<c:otherwise>
				<c:if test="${APP_ACCESS_DIST > 0 && FORM_ACCESS_PARMMST > 0}">
					<tr class="txtSubTitleMenu" align="center"><td><a href="ParSystem.tkd">REPORTING PERIOD</a></td></tr>
				</c:if>
				<c:if test="${APP_ACCESS_DIST > 0 && FORM_ACCESS_PRDMST > 0}">
					<tr class="txtSubTitleMenu" align="center"><td><a href="UploadMasterProduct.tkd">PRODUCT MASTER</a></td></tr>
				</c:if>
				<c:if test="${APP_ACCESS_DIST > 0 && FORM_ACCESS_DISTPRICEMST > 0}">
					<tr class="txtSubTitleMenu" align="center"><td><a href="UploadMasterDistributorPrice.tkd">DISTRIBUTOR PRICE MASTER</a></td></tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr class="txtTitleMenu" align="center"><th style="border-bottom: 1px solid black">ONCE</th></tr>
		<c:choose>
			<c:when test="${IS_ADMIN == '1' }">
				<tr class="txtSubTitleMenu" align="center"><td><a href="UploadMasterDistributor.tkd">DISTRIBUTOR MASTER</a></td></tr>
				<tr class="txtSubTitleMenu" align="center"><td><a href="UploadMasterCustomer.tkd">CUSTOMER MASTER</a></td></tr>
				<tr class="txtSubTitleMenu" align="center"><td><a href="UploadMasterGeographic.tkd">GEOGRAPHIC MASTER</a></td></tr>
			</c:when>
			<c:otherwise>
				<c:if test="${APP_ACCESS_DIST > 0 && FORM_ACCESS_DISTMST > 0}">
					<tr class="txtSubTitleMenu" align="center"><td><a href="UploadMasterDistributor.tkd">DISTRIBUTOR MASTER</a></td></tr>
				</c:if>
				<c:if test="${APP_ACCESS_DIST > 0 && FORM_ACCESS_CUSTMST > 0}">
					<tr class="txtSubTitleMenu" align="center"><td><a href="UploadMasterCustomer.tkd">CUSTOMER MASTER</a></td></tr>
				</c:if>
				<c:if test="${APP_ACCESS_DIST > 0 && FORM_ACCESS_GEOMST > 0}">
					<tr class="txtSubTitleMenu" align="center"><td><a href="UploadMasterGeographic.tkd">GEOGRAPHIC MASTER</a></td></tr>
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