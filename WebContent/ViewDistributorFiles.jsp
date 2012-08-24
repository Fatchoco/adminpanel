<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<script language="javascript">
	function NavigatePage()
	{
		 var currentPage=document.getElementsByName("cmbPage")[0].value;
		 var periodQ=document.getElementsByName("cur_period")[0].value;
		 var searchQ=document.getElementsByName("searchq")[0].value;
		 var searchQ2=document.getElementsByName("searchq2")[0].value;
		 window.location.href="ViewDistributorFiles.tkd?Period="+periodQ+"&Page="+currentPage+"&Search="+searchQ+"&Search2="+searchQ2;
	}
	function Search()
	{
		 var searchQ=document.getElementById("txtSearch").value;
		 var searchQ2=document.getElementsByName("txtSearch2")[0].value;
		 var periodQ=document.getElementsByName("cur_period")[0].value;
		 window.location.href="ViewDistributorFiles.tkd?Search="+searchQ+"&Search2="+searchQ2+"&Period="+periodQ;
	}
	function NavigatePeriod()
	{
		 var periodQ=document.getElementsByName("cur_period")[0].value;
		 var searchQ2=document.getElementsByName("txtSearch2")[0].value;
		 window.location.href="ViewDistributorFiles.tkd?Period="+periodQ+"&Search2="+searchQ2;
	}
	function submitForm()
	{
		var periodQ=document.getElementsByName("cur_period")[0].value;
		var searchQ2=document.getElementsByName("txtSearch2")[0].value;
		if(searchQ2=="")
			var r=confirm("Are you sure you want to delete all Data for period "+periodQ+"?");
		else if(searchQ2=="N")
			var r=confirm("Are you sure you want to delete all Sales Data for period "+periodQ+"?");
		else
			var r=confirm("Are you sure you want to delete all Adjustment Data for period "+periodQ+"?");
		if (r==true)
		{
		  document.forms[0].submit();
		}
	}
	function Export()
	{
		var periodQ=document.getElementsByName("cur_period")[0].value;
		var searchQ=document.getElementById("txtSearch").value;
		var searchQ2=document.getElementsByName("txtSearch2")[0].value;
		window.location.href="ExportViewDistributorFiles.tkd?Search="+searchQ+"&Search2="+searchQ2+"&Period="+periodQ;
	}
	function readenter2(e,readfunc)
	{
		var keycode;
		if (window.event) keycode = window.event.keyCode;
		else if (e) keycode = e.which;
		else return false;
		if (keycode == 13)
		{
			eval(readfunc);
			return false;
		}
	}
</script>

<title>Distributor File Upload - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<p class="txtTitleContent">
		<b><u>Distributor File Upload - Result Page</u></b>
	</p>
	<html:form action="/DeleteDistributorFiles" method="post">
	<html-el:hidden property="searchq" value="${param.Search}"></html-el:hidden>
	<html-el:hidden property="searchq2" value="${param.Search2}"></html-el:hidden>
	<table border="0" style="font-size: 15px;">
		<tr>
			<td class="txtSubSubTitleContent">Period</td>
			<td class="txtSubSubTitleContent" colspan="2">
				<html-el:select property="cur_period" value="${current_period}" onchange="NavigatePeriod()">
					<c:forEach var="i" begin="${i_year-2}" end="${i_year+2}" step="1">
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
			<td class="txtSubSubTitleContent">Data </td>
			<td class="txtSubSubTitleContent">
						<html-el:select property="txtSearch2" value="${param.Search2}" onchange="Search()">
							<html-el:option value="">All</html-el:option>
							<html-el:option value="N">Sales</html-el:option>
							<html-el:option value="Y">Adjustment</html-el:option>
						</html-el:select>
			</td>
			<td class="txtSubSubTitleContent">Distributor Code </td>
			<td class="txtSubSubTitleContent"><input name="txtSearch" type="text"
			id="txtSearch" onKeyPress="return readenter2(event,'Search()');"
			value='<c:out value="${param.Search}"/>' /></td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
					<tr>
						<td class="tkd_button_left">&nbsp;</td>
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="Search()">SEARCH</a></td>
			            <td class="tkd_button_right">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</html:form>
	<table border="0">
		<tr><td align="right" class="txtSubSubTitleContent">
			Page <html-el:select
			property="cmbPage" value="${param.Page}" onchange="NavigatePage()" styleClass="TableText">
			<c:forEach var="i" begin="1" end="${totalpage}" step="1">
				<html-el:option value="${i}">
					<c:out value="${i}" />
				</html-el:option>
			</c:forEach>
		</html-el:select>
		of <c:out value="${totalpage}" default="1" />. Total <c:out value="${rcount}"/> record(s).
		</td></tr>
		<tr><td>
			<table border="0" cellspacing="0" cellpadding="0" class="data">
				<c:set var="i" value="0" />
				<tr>
					<th scope="col">DIST CODE</th>
					<th scope="col">FILE NAME</th>
					<th scope="col">ACCEPTED RECORD(S)</th>
					<th scope="col">REJECTED RECORD(S)</th>
					<th scope="col">ACCEPTED QUANTITY</th>
					<th scope="col">ADJUSTMENT</th>
				</tr>
				<c:forEach var="item" items="${result.rows}">
					<c:set var="i" value="${i+1}" />
					<tr id='tr_<c:out value="${item.product_cd}" />' <c:if test="${i % 2 == 1}"> class="oddRow" </c:if><c:if test="${i % 2 == 0}"> class="evenRow" </c:if>>
						<td>
						<c:out value="${item.DISTRIBUTOR_CODE}" />
						</td>
						<td>
						<c:out value="${item.FILE_NAME}" />
						</td>
						<td align="right">
						<c:out value="${item.RACCEPT}" />
						</td>
						<td align="right">
						<c:out value="${item.RREJECT}" />
						</td>
						<td align="right">
						<c:out value="${item.RQTY}" />
						</td>
						<td align="center">
						<c:out value="${item.FLAG_ADJUSTMENT}" />
						</td>
					</tr>
				</c:forEach>
				</table>
				</td>
			</tr>
		<tr><td>
			<table border="0" align="right" cellpadding="0" cellspacing="0" class="tkd_button">
				<tr>
					<td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='UploadDistributorFiles.tkd'">SALES&nbsp;UPLOAD&nbsp;PAGE</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
					<td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='UploadDistributorAdjustmentFiles.tkd'">ADJUSTMENT&nbsp;UPLOAD&nbsp;PAGE</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
					<td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="submitForm()">DELETE</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
					<td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="Export()">EXPORT</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td></tr>
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