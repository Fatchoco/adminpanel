<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<script language="javascript">
function getCountry() {
	
	document.getElementsByName("hdcntr")[0].value=document.getElementById("slcountr").value;
	document.getElementsByName("hdcntr2")[0].value=document.getElementById("slcountr").value;
	}
	
function getCountry2() {
	
	document.getElementsByName("hdcntr")[0].value=document.getElementById("slcountr").value;
	document.getElementById("frm").submit();
	}
	
function submit2() {
	document.getElementsByName("hdcntr2")[0].value=document.getElementById("slcountr").value;
	document.getElementById("frm2").submit();
	}
</script>

<title>Load Market Analyzer Files - Takeda</title>
<body onload="getCountry();">
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header2.tkd"></c:import>
		<p class="txtTitleContent" align="left">
		<b><u>Market Analyzer Files Upload</u></b>
		</p>
		<table border="0">
			<tr>
				<td colspan="3">
					Active Period : <c:out value="${current_period}" />
				</td>
			</tr>
			<tr>
				<td colspan="3">
					Country : 
					<html-el:select property="country_cod" value="${fcountry}" styleId="slcountr" onchange="getCountry2();">
						<c:forEach var="item" items="${cn_result.rows}">
							<html-el:option value="${item.country_cod}">
								<c:out value="${item.country_des}"/>
							</html-el:option>
						</c:forEach>
					</html-el:select>
				</td>
			</tr><tr></tr><tr></tr><tr>
			</tr>
			<tr>
				<td colspan="3">
				<!-- <fieldset>
					<legend>Upload File</legend> -->
					<c:if test="${tes == 1}">
		File Not Found
		</c:if>
		<c:if test="${tes== 0}">
		File Found
		</c:if>
				<!-- </fieldset> -->
				</td>
			</tr>
			<tr>
				<td class="txtSubSubTitleContent" colspan="3">
			File Path : <c:out value="${filepath}" />
			</td>
			</tr>
			<html:form action="/IMSFilesProcess" method="post" enctype="multipart/form-data" styleId="frm">
				<html:hidden property="hdcntr" />
				</html:form>
				<html:form action="/IMSProcess" method="post" enctype="multipart/form-data" styleId="frm2">
				<html:hidden property="hdcntr2" />
				<c:if test="${tes == 0 && process == null}">
			<tr align="right">
				<!-- <td>File to Upload </td>
				<td><html:file property="uploadFile" size="100" styleClass="uploadBox" /></td> -->
				<td colspan="3">
					<div class="classbuttonrow">
					<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
						<tr>
							<td class="tkd_button_left">&nbsp;</td>
				            <td align="center" class="tkd_button_middle"><a href="#" onclick="submit2();">LOAD</a></td>
				            <td class="tkd_button_right">&nbsp;</td>
						</tr>
					</table>
					</div>					
				</td>
			</tr>
			</c:if>
			</html:form>
			<tr>
			<td colspan="3">
			<c:if test="${param.process != null}">
		<c:out value="${param.process} " />.
		</c:if>
		<c:if test="${status != 'Executed' && status != 'RUNNING' && status != 'INITIAL'}">
		Last Process for period <c:out value="${current_period}" /> was <c:out value="${status} " />.
		</c:if>
		<c:if test="${process != null}">
		<c:out value="${process} " />
		</c:if>
		<c:if test="${job != null && param.process == 'FAILED'}">
		<c:out value="${job}" />
		</c:if>
			</td>
			</tr>
		</table>
		
		
		<!-- <button type="button" class="btn" id='btnImport' name='btnImport' onclick="Import()">
			<span><span>Import</span></span>
		</button> -->
	<!-- <button type="button" id='btnLog' name='btnLog' onclick="javascript:window.location.href='ViewMappingProduct.tkd'">Results</button>
	<c:if test="${(logfile == 1) && (upload_record == null)}">
		<button type="button" id='btnLog' name='btnLog' onclick="window.open('ExportLogMappingProduct.tkd')">Last Log</button>
	</c:if> -->
	<!-- <c:import url="Footer.jsp"></c:import> -->
</div>
</body>
</html:html>

<c:if test="${param.message != null}">
	<script language="JavaScript">
		alert("<c:out value='${param.message}'/>"); 
	</script>
</c:if>