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
	 var periodQ=document.getElementsByName("cur_period")[0].value;
	 var periodQ2=document.getElementsByName("cur_period2")[0].value;
	 window.location.href="ViewMappingHospitalTarget.tkd?Page="+currentPage+"&Search="+searchQ+"&Search2="+searchQ2+"&Period="+periodQ+"&Period2="+periodQ2;
}
function Search()
{
	 var searchQ=document.getElementById("txtSearch").value;
	 var searchQ2=document.getElementById("txtSearch2").value;
	 var periodQ=document.getElementsByName("cur_period")[0].value;
	 var periodQ2=document.getElementsByName("cur_period2")[0].value;
	 window.location.href="ViewMappingHospitalTarget.tkd?Search="+searchQ+"&Search2="+searchQ2+"&Period="+periodQ+"&Period2="+periodQ2;
}
function Export()
{
	 var searchQ=document.getElementsByName("searchq")[0].value;
	 var searchQ2=document.getElementsByName("searchq2")[0].value;
	 var periodQ=document.getElementsByName("cur_period")[0].value;
	 var periodQ2=document.getElementsByName("cur_period2")[0].value;
	 window.location.href="ExportMappingHospitalTarget.tkd?Search="+searchQ+"&Search2="+searchQ2+"&Period="+periodQ+"&Period2="+periodQ2;
}
function Delete()
{
	 var periodQ=document.getElementsByName("cur_period")[0].value;
	 var periodQ2=document.getElementsByName("cur_period2")[0].value;
	 var r=confirm("Are you sure you want to delete all Data from period "+periodQ+" to "+periodQ2+"?");
	 if (r==true)
	 {
		 window.location.href="DeleteMappingHospitalTarget.tkd?Period="+periodQ+"&Period2="+periodQ2;
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

<title>View Mapping Hospital Target - Takeda</title>

<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	
	<p class="txtTitleContent">
	<b><u>Hospital Target Mapping - Result Page</u></b>
	</p>
	<table border="0" style="font-size: 15px;">
		<tr>
			<td colspan="4">
				<table width="70%" border="0">
				<tr>
				<td class="txtSubSubTitleContent">From</td>
				<td class="txtSubSubTitleContent">
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
				<td class="txtSubSubTitleContent">To</td>
				<td class="txtSubSubTitleContent">
					<html-el:select property="cur_period2" value="${param.Period2}" onchange="Search()">
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
				</table>
			</td>
		</tr>
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
					<th scope="col">PERIOD</th>
					<th scope="col">PRODUCT ID</th>
					<th scope="col">PRODUCT NAME</th>
					<th scope="col">HOSPITAL ID</th>
					<th scope="col">HOSPITAL NAME</th>
					<th scope="col">CITY ID</th>
					<th scope="col">CITY NAME</th>
					<th scope="col">PROVINCE ID</th>
					<th scope="col">PROVINCE NAME</th>
					<th scope="col">REGION ID</th>
					<th scope="col">REGION NAME</th>
					<th scope="col">TARGET QUANTITY</th>
				</tr>
				<c:forEach var="item" items="${result.rows}">
					<c:set var="i" value="${i+1}" />
					<tr id='tr_<c:out value="${item.product_cd}" />' <c:if test="${i % 2 == 1}"> class="oddRow" </c:if>
						<c:if test="${i % 2 == 0}"> class="evenRow" </c:if>>
						<td>
						<c:out value="${item.period}" />
						<c:if test="${item.period == null}">
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
						<c:out value="${item.product_nm}" />
						<c:if test="${item.product_nm == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.hospital_cd}" />
						<c:if test="${item.hospital_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.hospital_nm}" />
						<c:if test="${item.hospital_nm == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.city_cd}" />
						<c:if test="${item.city_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.city_nm}" />
						<c:if test="${item.city_nm == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.province_cd}" />
						<c:if test="${item.province_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.province_nm}" />
						<c:if test="${item.province_nm == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.region_cd}" />
						<c:if test="${item.region_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.region_nm}" />
						<c:if test="${item.region_nm == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.target_quantity}" />
						<c:if test="${item.target_quantity == null}">
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
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='UploadMappingHospitalTarget.tkd'">UPLOAD PAGE</a></td>
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