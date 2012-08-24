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

<title>Upload Master Distributor Price - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<html:form action="/UploadMasterDistributorPriceProcess" method="post" enctype="multipart/form-data">
		<p class="txtTitleContent" align="left">
		<b><u>Distributor Price Master Upload</u></b>
		</p>
		<table border="0" style="font-size: 15px;">
			<tr>
				<td class="txtSubTitleContent" colspan="3">
					Active Period : <c:out value="${result}" />
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
					<html:file property="uploadFile" size="100" styleClass="uploadBox" />
				<!-- </fieldset> -->
				</td>
			</tr>
			<tr align="right">
				<!-- <td>File to Upload </td>
				<td><html:file property="uploadFile" size="100" styleClass="uploadBox" /></td> -->
				<td colspan="3">
					<div class="classbuttonrow">
					<c:if test="${(logfile == 1) && (upload_record == null)}">
						<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
							<tr>
								<td class="tkd_button_left">&nbsp;</td>
					            <td align="center" class="tkd_button_middle"><a href="#" onclick="window.open('ExportLogMasterDistributorPrice.tkd')">LAST LOG</a></td>
					            <td class="tkd_button_right">&nbsp;</td>
							</tr>
						</table>
					</c:if>
					</div>
					<div class="classbuttonrow">
					<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
						<tr>
							<td class="tkd_button_left">&nbsp;</td>
				            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='ViewMasterDistributorPrice.tkd'">RESULT PAGE</a></td>
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
		
		
		<!-- <button type="button" class="btn" id='btnImport' name='btnImport' onclick="Import()">
			<span><span>Import</span></span>
		</button> -->
	</html:form>
	<!-- <button type="button" id='btnLog' name='btnLog' onclick="javascript:window.location.href='ViewMasterProduct.tkd'">Results</button>
	<c:if test="${(logfile == 1) && (upload_record == null)}">
		<button type="button" id='btnLog' name='btnLog' onclick="window.open('ExportLogMasterProduct.tkd')">Last Log</button>
	</c:if> -->
	<c:if test="${upload_record != null}">
		Successful data upload : <c:out value="${upload_record}" /> records <br/>
		<c:if test="${reject_record > 0}">
			Rejected data from the last upload : <c:out value="${reject_record}" /> records <a onclick="window.open('ExportLogMasterDistributorPrice.tkd')" style="cursor: hand;cursor: pointer;">(Click here to see the rejected records)</a>
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