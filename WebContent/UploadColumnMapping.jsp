<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<script language="javascript">
	function Import()
	{
		var fileFullPath=document.forms[0].uploadFile.value;
		if(fileFullPath=="")
		{
		alert("Please select the file to upload first");
		}
		else
		{
		var fileExtension=fileFullPath.substring(document.forms[0].uploadFile.value.length-3).toLowerCase();
		document.forms[0].submit();
		}
	}
</script>

<title>Distributor File Setup - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<p align="left" class="txtTitleContent">
		<b><u>Upload Distributor File Setup</u></b>
	</p>
	<html:form action="/UploadColumnMappingProcess" method="post" enctype="multipart/form-data">
	<table>
		<tr>
			<td class="txtSubSubTitleContent" colspan="3">
			<!--<fieldset>
				<legend>Upload File</legend>-->
				<span>File to Upload</span>
				<html:file property="uploadFile" size="70" styleClass="uploadBox" />
			<!--</fieldset>-->
			</td>
		</tr>
		<tr align="right">
			<td colspan="3">
			<c:if test="${(logfile == 1) && (upload_record == null)}">
				<div class="classbuttonrow">
				<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
					<tr>
						<td class="tkd_button_left">&nbsp;</td>
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='ExportLogColumnMapping.tkd'">LAST LOG</a></td>
			            <td class="tkd_button_right">&nbsp;</td>
					</tr>
				</table>
				</div>
			</c:if>
				<div class="classbuttonrow">
				<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
					<tr>
						<td class="tkd_button_left">&nbsp;</td>
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='ViewColumnMapping.tkd'">RESULT PAGE</a></td>
			            <td class="tkd_button_right">&nbsp;</td>
					</tr>
				</table>
				</div>
				<div class="classbuttonrow">
				<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
					<tr>
						<td class="tkd_button_left">&nbsp;</td>
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="Import()">UPLOAD</a></td>
			            <td class="tkd_button_right">&nbsp;</td>
					</tr>
				</table>
				</div>
			</td>
		</tr>
	</table>
	</html:form>
	<c:if test="${upload_record != null}">
		Successful data upload : <c:out value="${upload_record}" /> records <br/>
	<c:if test="${delete_record > 0}">
		Successful data deleted : <c:out value="${delete_record}" /> records <br/>
	</c:if>
	<c:if test="${reject_record > 0}">
		Rejected data from the last upload : <c:out value="${reject_record}" /> records <a onclick="window.open('ExportLogColumnMapping.tkd')">(Click here to see the rejected records)</a>
	</c:if>
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