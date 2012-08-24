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
		var path=document.getElementsByName("cur_period")[0].value;
		if(path == "")
		{
			alert("Source File Directory cannot be blank");
			return;
		}
		if((path.substring(0,3) == "C:\\" && path.match(["/"]))||((path.charAt(0)!="/") && path.substring(0,3) != "C:\\")){
			alert("Invalid File Directory\nDirectory must start with '/''");
			return;
		}
		var r=confirm("Are you sure you want to change the Source File Directory?");
		if (r==true)
		{
			if(path.substring(0,3) == "C:\\" && path.charAt(path.length-1)!="\\"){
				document.getElementsByName("cur_period")[0].value=path+"\\";
				obj=document.forms[0];
				document.forms[0].submit();
		}else if(path.charAt(0)=="/" && path.charAt(path.length-1)!="/"){
			document.getElementsByName("cur_period")[0].value=path+"/";
			obj=document.forms[0];
			document.forms[0].submit();
		}
			else{
			obj=document.forms[0];
			document.forms[0].submit();
			}
		}
	}
</script>

<title>Source File Directory Path - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header2.tkd"></c:import>
	<p align="left" class="txtTitleContent">
		<b><u>Source File Directory Path</u></b>
	</p>
	<br/>
	<br/>
	<html:form action="/ParSystemDirPathProcess" method="post">
	<table align="center">
		<tr>
			<th class="txtSubTitleContent">Directory Path</th>
			<td class="txtSubTitleContent">:</td>
			<td class="txtSubTitleContent">
				<html-el:text property="cur_period" value="${dirpath}"/>
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