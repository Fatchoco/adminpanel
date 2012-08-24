<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<script language="javascript">
function MarkDelete(thisid)
{
	if(parseInt(document.getElementById("txtFlag_"+thisid).value)==1)
	{
		document.getElementById("tr_"+thisid).style.background="";
		document.getElementById("txtFlag_"+thisid).value=0;
	}
	else
	{
		document.getElementById("tr_"+thisid).style.background="#FFAAB3";
		document.getElementById("txtFlag_"+thisid).value=1;
	}
}
function NavigatePage()
{
	 var currentPage=document.getElementsByName("cmbPage")[0].value;
	 var searchQ1=document.getElementsByName("searchq1")[0].value;
	 var periodQ=document.getElementsByName("country_cod")[0].value;
	 window.location.href="ViewMappingTakedaCategory.tkd?Page="+currentPage+"&Search1="+searchQ1+"&Period="+periodQ;
}
function Search()
{
	 var searchQ1=document.getElementById("txtSearchDist").value;
	 var periodQ=document.getElementsByName("country_cod")[0].value;
	 window.location.href="ViewMappingTakedaCategory.tkd?Search1="+searchQ1+"&Period="+periodQ;
}
function Export()
{
	 var searchQ1=document.getElementById("txtSearchDist").value;
	 var periodQ=document.getElementsByName("country_cod")[0].value;
	 window.location.href="ExportMappingTakedaCategory.tkd?Search1="+searchQ1+"&Period="+periodQ;
}
function Delete()
{
	 var periodQ=document.getElementsByName("country_cod")[0].value;
	 var country_des=document.getElementsByName("country_des")[0].value;
	 var r=confirm("Are you sure you want to delete all Data for country "+country_des+"?");
	 if (r==true)
	 {
	 	window.location.href="DeleteMappingTakedaCategory.tkd?Period="+periodQ;
	 }
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

<title>View Mapping Takeda Category - Takeda</title>

<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header2.tkd"></c:import>
	
	<p class="txtTitleContent">
	<b><u>Takeda Category Mapping - Result Page</u></b>
	</p>
	<table border="0" style="font-size: 15px;">
		<tr>
			<td class="txtSubSubTitleContent">Country</td>
			<td class="txtSubSubTitleContent" colspan="2">
				<html-el:select property="country_cod" value="${current_period}" onchange="Search();">
					<c:forEach var="item" items="${cn_result.rows}">
						<html-el:option value="${item.country_cod}">
							<c:out value="${item.country_des}"/>
						</html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<tr>
			<td class="txtSubSubTitleContent">Pack Code </td>
			<td class="txtSubSubTitleContent"><input name="txtSearchDist" type="text"
			id="txtSearchDist" onKeyPress="return readenter2(event,'Search()');"
			value='<c:out value="${param.Search1}"/>' /></td>
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
		<tr>
			<td class="txtSubSubTitleContent" colspan="3"><c:out value="${deletecomment}" />
				<c:if test="${deletecomment == null}">
					&nbsp;
				</c:if>
			</td>
		</tr>
		<html-el:hidden property="searchq1" value="${param.Search1}"></html-el:hidden>
		<html-el:hidden property="country_des" value="${country_des}"></html-el:hidden>
	</table>
	
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
				<th scope="col">PERIOD</th>
				<th scope="col">DISTRIBUTOR CODE</th>
				<th scope="col">PRODUCT CODE</th>
				<th scope="col">UNIT PRICE</th>
			</tr>
			<c:forEach var="item" items="${result.rows}">
				<c:set var="i" value="${i+1}" />
				<tr id='tr_<c:out value="${item.distributor_cd}" />' <c:if test="${i % 2 == 1}"> class="oddRow" </c:if>
					<c:if test="${i % 2 == 0}"> class="evenRow" </c:if>>
					<td>
					<c:out value="${item.takeda_category_cod}" />
					<c:if test="${item.takeda_category_cod == null}">
						&nbsp;
					</c:if>
					</td>
					<td>
					<c:out value="${item.takeda_category_des}" />
					</td>
					<td>
					<c:out value="${item.pack_cod}" />
					<c:if test="${item.pack_cod == null}">
						&nbsp;
					</c:if>
					</td>
					<td>
					<c:out value="${item.pack_des}" />
					<c:if test="${item.pack_des == null}">
						&nbsp;
					</c:if>
					</td>
					</tr>
			</c:forEach>
		</table>
		</td></tr>
		<tr><td>
			<table border="0" align="right" cellpadding="0" cellspacing="0" class="tkd_button">
				<tr>
					<td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="Delete()">DELETE</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
		            <td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='UploadMappingTakedaCategory.tkd'">UPLOAD PAGE</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
		            <td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="Export()">EXPORT</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td></tr>
	</table>
	<table border="0"><tr><td></td></tr></table>
</div>
</body>
</html:html>

<c:if test="${param.message != null}">
	<script language="JavaScript">
		alert("<c:out value='${param.message}'/>"); 
	</script>
</c:if>