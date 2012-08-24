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
	 var searchQ2=document.getElementsByName("searchq2")[0].value;
	 window.location.href="ViewMappingMR.tkd?Page="+currentPage+"&Search="+searchQ+"&Search2="+searchQ2;
	 //window.location.href="ViewMappingHospitalTarget.tkd?Page="+currentPage+"&Period="+periodQ;
}
function Search()
{
	 var searchQ=document.getElementById("txtSearch").value;
	 var searchQ2=document.getElementById("txtSearch2").value;
	 window.location.href="ViewMappingMR.tkd?Search="+searchQ+"&Search2="+searchQ2;
}
function Export()
{
	 var searchQ=document.getElementsByName("searchq")[0].value;
	 var searchQ2=document.getElementsByName("searchq2")[0].value;
	 window.location.href="ExportMappingMR.tkd?Search="+searchQ+"&Search2="+searchQ2;
}
function Delete()
{
	 var r=confirm("Are you sure you want to delete all data?");
	 if (r==true)
	 {
		 window.location.href="DeleteMappingMR.tkd";
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

<title>View Mapping MR - Takeda</title>

<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	
	<p class="txtTitleContent">
	<b><u>MR Mapping - Result Page</u></b>
	</p>
	<table border="0" style="font-size: 15px;">
	<!--
		<tr>
			<td>Period</td>
			<td colspan="2">
				<html-el:select property="cur_period" value="${param.Period}" onchange="Search()">
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
		-->
		<tr>
			<td class="txtSubSubTitleContent">Product ID </td>
			<td class="txtSubSubTitleContent"><input name="txtSearch" type="text"
			id="txtSearch" onKeyPress="return readenter2(event,'Search()');"
			value='<c:out value="${param.Search}"/>' /></td>
			<td class="txtSubSubTitleContent">Hospital ID </td>
			<td class="txtSubSubTitleContent"><input name="txtSearch2" type="text"
			id="txtSearch2" onKeyPress="return readenter2(event,'Search()');"
			value='<c:out value="${param.Search2}"/>' /></td>
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
		<html-el:hidden property="searchq2" value="${param.Search2}"></html-el:hidden>
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
					<th scope="col">Hospital ID</th>
					<th scope="col">Product ID</th>
					<th scope="col">MR ID</th>
					<th scope="col">MR Employee ID</th>
					<th scope="col">MR Name (EN)</th>
					<th scope="col">MR Name (CN)</th>
					<th scope="col">DSM ID</th>
					<th scope="col">DSM Employee ID</th>
					<th scope="col">DSM NAME (EN)</th>
					<th scope="col">DSM NAME (CN)</th>
					<th scope="col">RSM ID</th>
					<th scope="col">RSM Employee ID</th>
					<th scope="col">RSM NAME (EN)</th>
					<th scope="col">RSM NAME (CN)</th>
					<th scope="col">RSD ID</th>
					<th scope="col">RSD Employee ID</th>
					<th scope="col">RSD NAME (EN)</th>
					<th scope="col">RSD NAME (CN)</th>
					<th scope="col">Region ID</th>
					<th scope="col">Region Name (EN)</th>
					<th scope="col">Region Name (CN)</th>
				</tr>
				<c:forEach var="item" items="${result.rows}">
					<c:set var="i" value="${i+1}" />
					<tr id='tr_<c:out value="${item.product_cd}" />' <c:if test="${i % 2 == 1}"> class="oddRow" </c:if>
						<c:if test="${i % 2 == 0}"> class="evenRow" </c:if>>
						<td>
						<c:out value="${item.hospital_cd}" />
						<c:if test="${item.hospital_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.product_cd}" />
						<c:if test="${item.product_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.mr_cd}" />
						<c:if test="${item.mr_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.MR_EMPLOYEE_CD}" />
						<c:if test="${item.MR_EMPLOYEE_CD == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.MR_NM}" />
						<c:if test="${item.MR_NM== null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.MR_NM_CN}" />
						<c:if test="${item.MR_NM_CN == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.DSM_CD}" />
						<c:if test="${item.DSM_CD == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.DSM_EMPLOYEE_CD}" />
						<c:if test="${item.DSM_EMPLOYEE_CD == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.DSM_NM}" />
						<c:if test="${item.DSM_NM == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.DSM_NM_CN}" />
						<c:if test="${item.DSM_NM_CN == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.RSM_CD}" />
						<c:if test="${item.RSM_CD == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.RSM_EMPLOYEE_CD}" />
						<c:if test="${item.RSM_EMPLOYEE_CD == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.RSM_NM}" />
						<c:if test="${item.RSM_NM == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.RSM_NM_CN}" />
						<c:if test="${item.RSM_NM_CN == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.RSD_CD}" />
						<c:if test="${item.RSD_CD == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.RSD_EMPLOYEE_CD}" />
						<c:if test="${item.RSD_EMPLOYEE_CD == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.RSD_NM}" />
						<c:if test="${item.RSD_NM == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.RSD_NM_CN}" />
						<c:if test="${item.RSD_NM_CN == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.REGION_CD}" />
						<c:if test="${item.REGION_CD == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.REGION_NM}" />
						<c:if test="${item.REGION_NM == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.REGION_NM_CN}" />
						<c:if test="${item.REGION_NM_CN == null}">
							&nbsp;
						</c:if>
						</td>
						<!--<td>
						<img src='images/DeleteRow.png' class='HandCursor' onclick="MarkDelete('<c:out value="${item.product_cd}"/>')" />
						<input type='hidden' name='txtFlag_<c:out value="${item.product_cd}"/>' id='txtFlag_<c:out value="${item.product_cd}"/>' value=0 />
						<input type='hidden' name='thisid_<c:out value="${i}"/>' id='thisid_<c:out value="${i}"/>' value='<c:out value="${item.product_cd}"/>' />
						</td>-->
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
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='UploadMappingMR.tkd'">UPLOAD PAGE</a></td>
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
	
	
	
	<!--<button type="button" class="tkd_button_middle" name="btnEdit"
		onclick="window.open('ExportMasterProduct.tkd')"
		id="btnEdit"><span><span>Export</span></span></button>
	<button type="button" class="btn" name="btnEdit"
		onclick="javascript:window.location.href='EditMasterPage.tkd?Search=<c:out value='${param.Search}'/>&Page=<c:out value='${param.Page}'/>'"
		id="btnEdit"><span><span>Edit Page</span></span></button>
		--><!-- <c:import url="Footer.jsp"></c:import> -->
</div>
</body>
</html:html>

<c:if test="${param.message != null}">
	<script language="JavaScript">
		alert("<c:out value='${param.message}'/>"); 
	</script>
</c:if>