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
		else if(document.forms[0].cur_period.value=="")
		{
		alert("Please select the period for adjustment file");
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
		window.location="ProcessAdjustmentToReport.tkd?Period="+document.forms[1].adj_period.value;
	}
	function ViewResultPage(flag)
	{
		if(flag=="tab")
			window.open("ViewDistributorFiles.tkd?Period="+document.forms[1].adj_period.value+"&Search2=Y","_blank");
		else
			location.href="ViewDistributorFiles.tkd?Search2=Y";
	}
</script>

<title>Upload Distributor Adjustment Files - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<p align="left" class="txtTitleContent">
		<b><u>Distributor Adjustment Files Upload</u></b>
	</p>
	<table border="0">
		<html:form action="/UploadDistributorAdjustmentFilesProcess" method="post" enctype="multipart/form-data">
		<tr>
			<td class="txtSubTitleContent" colspan="3">
				Active Period : <c:out value="${current_period}" />
			</td>
		</tr>
		<tr>
			<td class="txtSubTitleContent" colspan="3">
				<!--<c:choose>
					<c:when test="${i_month-1<10}"><c:set var="valdrop" value="${i_year}0${i_month-1}"></c:set></c:when>
					<c:otherwise><c:set var="valdrop" value="${i_year}${i_month-1}"></c:set></c:otherwise>
				</c:choose>-->
				Upload to Period : 
				<html-el:select property="cur_period" value="">
					<html-el:option value=""><c:out value="(Select)"/></html-el:option>
					<c:forEach var="i" begin="${i_year-1}" end="${i_year}" step="1">
						<c:forEach var="j" begin="1" end="12" step="1">
							<c:choose>
								<c:when test="${i_year==i&&j>=i_month}"></c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${j<10}"><c:set var="opt_period" value="${i}0${j}" /></c:when>
										<c:otherwise><c:set var="opt_period" value="${i}${j}" /></c:otherwise>
									</c:choose>
									<html-el:option value="${opt_period}">
										<c:out value="${opt_period}"/>
									</html-el:option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:forEach>
				</html-el:select>
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
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="ViewResultPage('tab')">RESULT PAGE</a></td>
			            </c:if>
						<c:if test="${processed_files == null}">
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="ViewResultPage('open')">RESULT PAGE</a></td>
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
				<html-el:hidden property="adj_period" value="${adj_period}" />
				<div class="classbuttonrow">
					<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
						<tr>
							<td class="tkd_button_left">&nbsp;</td>
							<td align="center" class="tkd_button_middle"><a href="#" onclick="ProcessToReport()">RUN ADJUSTMENT PROCESS</a></td>
							<td class="tkd_button_right">&nbsp;</td>
						</tr>
					</table>
				</div>
				<div class="classbuttonrow">
					<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
						<tr>
							<td class="tkd_button_left">&nbsp;</td>
							<td align="center" class="tkd_button_middle"><a href="#" onclick="window.open('ExportLogDistributorAdjustmentFiles.tkd')">EXCEPTION</a></td>
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