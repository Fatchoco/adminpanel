<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<script language="javascript">
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

<title>Process Loading - Takeda</title>
<body onload="startLoading()">
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<p align="left" class="txtTitleContent">
		<b><u></u></b>
	</p>
	<table border="0" align="center">
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr aling="center">
			<td class="txtSubTitleContent" colspan="3" align="center">
				Now Loading...
			</td>
		</tr>
		<tr align="center">
			<td colspan="3" align="center"><img src='images/ajax-loader.gif' /></td>
		</tr>
	</table>
	<!-- <c:import url="Footer.jsp"></c:import> -->
</div>
</body>
</html:html>