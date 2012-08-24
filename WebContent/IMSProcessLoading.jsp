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
  	};
	xmlhttp.open("GET","IMSProcess3.tkd",false);
	xmlhttp.send();
	
	return strres;
}
/*
function getState2()
{
	var strres2;
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
	    	strres2 = xmlhttp.responseText.replace(/^\s+|\s+$/g,"");
		}
  	};
	xmlhttp.open("GET","IMSProcess4.tkd",false);
	xmlhttp.send();
	
	return strres2;
}*/

function processState(page)
{
	var qryReturn	= getState();
	var state	= qryReturn.split(',')[0];
	var runtype	= qryReturn.split(',')[1];
	
	/*var qryReturn2	= getState2();
	var state2[];
	var i;
	for(i=0;i<qryReturn2.split(',').lenght;i++)
	state2[i]	= qryReturn2.split(',')[i];
	*/
	switch(state)
	{
	case "INITIAL":
	case "RUNNING":
			window.location = "IMSProcessLoading.tkd";
		break;
	case "SUCCEEDED":
				window.location = "IMSFiles.tkd?process=SUCCEEDED";
		break;
	case "FAILED":
				window.location = "IMSFiles.tkd?process=FAILED";
		break;
	default:
			window.location = "MenuIMS.jsp";
		break;
	}
}
function startLoading(page)
{
		setInterval('processState();', 3000);
}
</script>

<title>Market Analyzer Process Loading - Takeda</title>
<body onload="startLoading();">
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header2.tkd"></c:import>
	<p align="left" class="txtTitleContent">
		<b><u></u></b>
	</p>
	<table border="0" align="center">
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td></td></tr>
		<tr aling="center">
			<td class="txtSubTitleContent" colspan="3" align="center">
				Now Loading...
			</td>
		</tr>
		<tr align="center">
			<td colspan="3" align="center"><img src='images/ajax-loader.gif' /></td>
			<c:out value="${state}"/>
	</table>
	<table border="0">
	<c:forEach var="j" begin="0" end="${j}" step="1">
	<tr><td>

	<tr><td>
	</c:forEach>
	</table>
	<!-- <c:import url="Footer.jsp"></c:import> -->
</div>
</body>
</html:html>