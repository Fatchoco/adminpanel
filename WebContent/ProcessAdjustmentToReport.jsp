<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<script language="javascript">
	function ProcessToReport(flag)
	{
		if(document.forms[0].cur_period.value=="")
		{
			alert("Please select the adjustment period");
		}
		else
		{
			 var r=confirm("This will process period "+document.forms[0].cur_period.value+" adjustment data. Proceed to process data?");
			 if (r==true)
			 {
				document.forms[0].submit();
			 }
		}
	}
	
	function getState()
	{
		var strres;
		if (window.XMLHttpRequest)
	  	{// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp=new XMLHttpRequest();
		}
		else
	  	{// code for IE6, IE5
	  		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  	}
		xmlhttp.onreadystatechange=function()
	  	{
			if (xmlhttp.readyState==4 && xmlhttp.status==200)
			{
		    	strres = xmlhttp.responseText.replace(/^\s+|\s+$/g,"");
			}
	  	}
		xmlhttp.open("GET","JobStatus.tkd",false);
		xmlhttp.send();
		
		return strres;
	}
	function processState(page)
	{
		var qryReturn	= getState();
		var state	= qryReturn.split(',')[0];
		var runtype	= qryReturn.split(',')[1];
		
		switch(state)
		{
		case "SCHEDULED":
		case "RUNNING":
			if(page!="INITIAL")
				return true;
			else
				window.location = "ProcessLoading.tkd";
			break;
		case "SUCCEEDED":
			if(page!="INITIAL")
				if(runtype=="SALES")
					window.location = "ProcessToReportResult.tkd?process=sales";
				else
					window.location = "ProcessToReportResult.tkd?process=adjustment";
			break;
		case "FAILED":
			if(page!="INITIAL")
				if(runtype=="SALES")
					window.location = "ProcessToReport.tkd?eq=1";
				else
					window.location = "ProcessAdjustmentToReport.tkd?eq=1";
			break;
		default:
			if(page!="INITIAL")
				window.location = "MenuProcess.jsp";
			break;
		}
	}
	function startLoading(page)
	{
		var flag;
		flag = processState(page);
		if(flag && page != "INITIAL")
			setInterval('processState();', 3000);
	}
</script>

<title>Run Adjustment Process - Takeda</title>
<body onload="startLoading('INITIAL')">
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<table>
		<tr><td class="txtTitleContent"><b><u>Run Adjustment Process</u></b></td></tr>
	</table>
	<html:form action="/ProcessAdjustmentToReportProcess" method="post" enctype="multipart/form-data">
	<!--<html-el:hidden property="cur_period" value="${current_period}" />-->
	<!--<html-el:hidden property="partialrun" value="" />-->
	<table align="center">

		<tr align="center">
			<td class="txtSubTitleContent" colspan="3">
				Adjustment Period : <html-el:select property="cur_period" value="${param.Period}">
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
			<td colspan="3"></td>
		</tr>
		<tr align="right">
			<td>
				<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
					<tr>
						<td class="tkd_button_left">&nbsp;</td>
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="ProcessToReport('all')">RUN ADJUSTMENT PROCESS</a></td>
			            <td class="tkd_button_right">&nbsp;</td>
						<!--
						<td class="tkd_button_left">&nbsp;</td>
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="ProcessToReport('partial')">RUN DATA</a></td>
			            <td class="tkd_button_right">&nbsp;</td>
			             -->
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</html:form>
	<c:if test="${errormsg != null}">
	<table width="50%" align="center">
		<tr><th>Procedure Error Message</th></tr>
		<tr><td><c:out value='${param.message}'/></td></tr>
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