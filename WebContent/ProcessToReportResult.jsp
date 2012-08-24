<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<title>
<c:if test="${process == 'sales'}">
Run Monthly Process - Takeda
</c:if>
<c:if test="${process == 'adjustment'}">
Run Adjustment Process - Takeda
</c:if>
</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<table>
		<tr><td class="txtTitleContent"><b><u>
		<c:if test="${process == 'sales'}">
		Run Monthly Process Result
		</c:if>
		<c:if test="${process == 'adjustment'}">
		Run Adjustment Process Result
		</c:if>
		</u></b></td></tr>
	</table>
	<c:if test="${umcust != null}">
		<table align="center">
			<tr><th class="txtSubTitleContent" align="center" colspan="2"><c:if test="${process == 'sales'}">Run Monthly Process Result</c:if><c:if test="${process == 'adjustment'}">Run Adjustment Process Result</c:if></th></tr>
			<tr class="txtSubSubTitleContent"><td align="right">Unmapped Customer :</td><td><a href="ExportExceptionCustomer.tkd" /><c:out value="${umcust}"/> records</a></td></tr>
			<tr class="txtSubSubTitleContent"><td align="right">Unmapped Product :</td><td><a href="ExportExceptionProduct.tkd" /><c:out value="${umprd}"/> records</a></td></tr>
		</table>
	</c:if>
	<!-- <c:import url="Footer.jsp"></c:import> -->
</div>
</body>
</html:html>

<c:if test="${param.message != null}">
	<script language="JavaScript">
		alert("<c:out value='${param.message}'/>"); 
	</script>
</c:if>