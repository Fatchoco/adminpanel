<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<script language="javascript">
	function getFileType(filename)
	{
		return (/[.]/.exec(filename)) ? /[^.]+$/.exec(filename) : undefined;
	}
	function Import()
	{
		var fileFullPath=document.forms[0].uploadFile.value;
		if(fileFullPath=="")
		{
		alert("Please select the file to upload first");
		}
		else
		{
		var fileExtension=getFileType(document.getElementsByName("uploadFile")[0].value);
		if(fileExtension != "zip" && fileExtension != "rar")
			alert("Please upload only zipped file.");
		else
			document.forms[0].submit();
		}
	}
	function ProcessToReport()
	{
		//document.forms[1].submit();
		window.location="ProcessToReport.tkd";
	}
</script>

<title>Upload Distributor Files - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<p align="left" class="txtTitleContent">
		<b><u>Distributor Files Upload</u></b>
	</p>
	<table border="0">
		<html:form action="/UploadDistributorFilesProcess" method="post" enctype="multipart/form-data">
		<tr>
			<td class="txtSubTitleContent" colspan="3">
				Active Period : <c:out value="${current_period}" />
			</td>
		</tr>
		<tr>
			<td colspan="3">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="txtSubSubTitleContent" colspan="3">
			<!-- <fieldset>
				<legend>Upload File</legend> -->
				<span>File to Upload</span>
				<html:file property="uploadFile" size="70" styleClass="uploadBox" />
			<!-- </fieldset> -->
			</td>
		</tr>
		<tr align="right">
			<td colspan="3">
				<div class="classbuttonrow">
				<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
					<tr>
						<td class="tkd_button_left">&nbsp;</td>
						<c:if test="${processed_files != null}">
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.open('ViewDistributorFiles.tkd?Search2=N','_blank');">RESULT PAGE</a></td>
			            </c:if>
						<c:if test="${processed_files == null}">
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:location.href='ViewDistributorFiles.tkd?Search2=N';">RESULT PAGE</a></td>
			            </c:if>
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
		</html:form>
		<c:if test="${processed_files != null}">
		<tr align="left">
			<td class="content" colspan="3">
				Successful file upload : <c:out value="${processed_files}" /> files <br/>
					<c:if test="${unprocessed_files > 0}">
						Rejected file from the last upload : <c:out value="${unprocessed_files}" /> files
					</c:if>
			</td>
		</tr>
		<tr align="right">
			<td colspan="3">
				<html:form action="/ProcessToReportProcess.tkd" method="post" enctype="multipart/form-data">
				<html-el:hidden property="cur_period" value="${current_period}" />
				<div class="classbuttonrow">
					<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
						<tr>
							<td class="tkd_button_left">&nbsp;</td>
							<td align="center" class="tkd_button_middle"><a href="#" onclick="ProcessToReport()">RUN MONTHLY PROCESS</a></td>
							<td class="tkd_button_right">&nbsp;</td>
						</tr>
					</table>
				</div>
				<div class="classbuttonrow">
					<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
						<tr>
							<td class="tkd_button_left">&nbsp;</td>
							<td align="center" class="tkd_button_middle"><a href="#" onclick="window.open('ExportLogDistributorFiles.tkd')">EXCEPTION</a></td>
							<td class="tkd_button_right">&nbsp;</td>
						</tr>
					</table>
				</div>
				</html:form>
			</td>
		</tr>
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