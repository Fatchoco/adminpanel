<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<script language="javascript">
	function Save()
	{
		obj=document.forms[0];
		document.forms[0].submit();
	}
</script>

<title>Reporting Period - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<p align="left" class="txtTitleContent">
		<b><u>Reporting Period</u></b>
	</p>
	<br/>
	<br/>
	<html:form action="/ParSystemProcess" method="post">
	<table align="center">
		<tr>
			<th class="txtSubTitleContent">Reporting Period</th>
			<td class="txtSubTitleContent">:</td>
			<td class="txtSubTitleContent">
				<html-el:select property="cur_period" value="${current_period}">
					<c:forEach var="i" begin="${i_year-1}" end="${i_year+2}" step="1">
						<c:forEach var="j" begin="1" end="12" step="1">
							<c:choose>
								<c:when test="${j<10}"><c:set var="opt_period" value="${i}0${j}" /></c:when>
								<c:otherwise><c:set var="opt_period" value="${i}${j}" /></c:otherwise>
							</c:choose>
							<html-el:option value="${opt_period}">
								<c:out value="${opt_period}"/>
							</html-el:option>
						</c:forEach>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<tr>
			<td colspan="3" align="right">
				<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
					<tr>
						<td class="tkd_button_left">&nbsp;</td>
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="Save()">SAVE</a></td>
			            <td class="tkd_button_right">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</html:form>
	<!-- <c:import url="Footer.jsp"></c:import> -->
</div>
</body>
</html:html>

<c:if test="${param.message != null}">
	<script language="JavaScript">
		alert("<c:out value='${param.message}'/>"); 
	</script>
</c:if>