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
	 var searchQ=document.getElementsByName("searchq")[0].value;
	 window.location.href="ViewMappingCustomer.tkd?Page="+currentPage+"&Search="+searchQ;
}
function Search()
{
	 var searchQ=document.getElementById("txtSearch").value;
	 window.location.href="ViewMappingCustomer.tkd?Search="+searchQ;
}
function Export()
{
	 var searchQ=document.getElementById("txtSearch").value;
	 window.location.href="ExportMappingCustomer.tkd?Search="+searchQ;
}

function Delete()
{
	 var r=confirm("Are you sure you want to delete all data?");
	 if (r==true)
	 {
		 window.location.href="DeleteMappingCustomer.tkd";
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

<title>View Mapping Customer - Takeda</title>

<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	
	<p class="txtTitleContent">
	<b><u>Customer Mapping - Result Page</u></b>
	</p>
	<table border="0" style="font-size: 15px;">
		<tr>
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
		<tr>
			<td class="txtSubSubTitleContent" colspan="3"><c:out value="${deletecomment}" />
				<c:if test="${deletecomment == null}">
					&nbsp;
				</c:if>
			</td>
		</tr>
		<html-el:hidden property="searchq" value="${param.Search}"></html-el:hidden>
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
					<th scope="col">DISTRIBUTOR CODE</th>
					<th scope="col">DISTRIBUTOR CUSTOMER CODE</th>
					<th scope="col">DISTRIBUTOR CUSTOMER NAME (CN)</th>
					<th scope="col">DISTRIBUTOR CUSTOMER CITY</th>
					<th scope="col">DISTRIBUTOR CUSTOMER PROVINCE</th>
					<th scope="col">TAKEDA CUSTOMER CODE</th>
					<th scope="col">TAKEDA CUSTOMER NAME (CN)</th>
				</tr>
				<c:forEach var="item" items="${result.rows}">
					<c:set var="i" value="${i+1}" />
					<tr id='tr_<c:out value="${item.distributor_customer_cd}" />' <c:if test="${i % 2 == 1}"> class="oddRow" </c:if>
						<c:if test="${i % 2 == 0}"> class="evenRow" </c:if>>
						<td>
						<c:out value="${item.distributor_id}" />
						<c:if test="${item.distributor_id == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.distributor_customer_cd}" />
						<c:if test="${item.distributor_customer_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.distributor_customer_nm}" />
						<c:if test="${item.distributor_customer_nm == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.distributor_customer_city}" />
						<c:if test="${item.distributor_customer_city == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.distributor_customer_province}" />
						<c:if test="${item.distributor_customer_province == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.customer_cd}" />
						<c:if test="${item.customer_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.customer_nm}" />
						<c:if test="${item.customer_nm == null}">
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
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='UploadMappingCustomer.tkd'">UPLOAD PAGE</a></td>
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
	
	<!-- <c:import url="Footer.jsp"></c:import> -->
</div>
</body>
</html:html>

<c:if test="${param.message != null}">
	<script language="JavaScript">
		alert("<c:out value='${param.message}'/>"); 
	</script>
</c:if>