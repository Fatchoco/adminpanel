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
	 var periodQ1=document.getElementsByName("cur_period1")[0].value;
	 var periodQ2=document.getElementsByName("cur_period2")[0].value;
	 window.location.href="ViewMappingExfactoryTarget.tkd?Page="+currentPage+"&Search="+searchQ+"&Period1="+periodQ1+"&Period2="+periodQ2;
	 //window.location.href="ViewMappingHospitalTarget.tkd?Page="+currentPage+"&Period="+periodQ;
}
function Search()
{
	 var searchQ=document.getElementById("txtSearch").value;
	 var periodQ1=document.getElementsByName("cur_period1")[0].value;
	 var periodQ2=document.getElementsByName("cur_period2")[0].value;
	 window.location.href="ViewMappingExfactoryTarget.tkd?Search="+searchQ+"&Period1="+periodQ1+"&Period2="+periodQ2;
}
function Export()
{
	 var searchQ=document.getElementsByName("searchq")[0].value;
	 var periodQ1=document.getElementsByName("cur_period1")[0].value;
	 var periodQ2=document.getElementsByName("cur_period2")[0].value;
	 window.location.href="ExportMappingExfactoryTarget.tkd?Search="+searchQ+"&Period1="+periodQ1+"&Period2="+periodQ2;
}
function Delete()
{
	 var periodQ1=document.getElementsByName("cur_period1")[0].value;
	 var periodQ2=document.getElementsByName("cur_period2")[0].value;
	 var r=confirm("Are you sure you want to delete all Data for period from "+periodQ1+" to "+periodQ2+"?");
	 if (r==true)
	 {
		 window.location.href="DeleteMappingExfactoryTarget.tkd?Period1="+periodQ1+"&Period2="+periodQ2;
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

<title>View Mapping Ex-factory Target - Takeda</title>

<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	
	<p class="txtTitleContent">
	<b><u>Ex-factory Target Mapping - Result Page</u></b>
	</p>
	<table border="0">
		<tr>
			<td colspan="4">
				<table width="70%" border="0">
					<tr>
						<td class="txtSubSubTitleContent">From</td>
						<td class="txtSubSubTitleContent">
							<html-el:select property="cur_period1" value="${param.Period1}" onchange="Search()">
								<c:forEach var="i" begin="${i_year-2}" end="${i_year+2}" step="1">
									<c:forEach var="j" begin="1" end="12" step="1">
										<c:choose>
											<c:when test="${j<10}"><c:set var="opt_period1" value="${i}0${j}" /></c:when>
											<c:otherwise><c:set var="opt_period1" value="${i}${j}" /></c:otherwise>
										</c:choose>
										<html-el:option value="${opt_period1}">
											<c:out value="${opt_period1}"/>
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
											<c:when test="${j<10}"><c:set var="opt_period2" value="${i}0${j}" /></c:when>
											<c:otherwise><c:set var="opt_period2" value="${i}${j}" /></c:otherwise>
										</c:choose>
										<html-el:option value="${opt_period2}">
											<c:out value="${opt_period2}"/>
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
			<td class="txtSubSubTitleContent">Product Code </td>
			<td colspan ="2" class="txtSubSubTitleContent"><input name="txtSearch" type="text"
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
			<td class="txtSubSubTitleContent" colspan="4"><c:out value="${deletecomment}" />
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
					<th scope="col">YEAR</th>
					<th scope="col">MONTH</th>
					<th scope="col">PRODUCT CODE</th>
					<th scope="col">PRODUCT NAME (EN)</th>
					<th scope="col">PRODUCT NAME (CN)</th>
					<th scope="col">PRODUCT FAMILY</th>
					<th scope="col">BRAND NAME</th>
					<th scope="col">PRODUCT CATEOGRY</th>
					<th scope="col">PACK SIZE</th>
					<th scope="col">QUANTITY</th>
					<th scope="col">NET SALES</th>
				</tr>
				<c:forEach var="item" items="${result.rows}">
					<c:set var="i" value="${i+1}" />
					<tr id='tr_<c:out value="${item.product_cd}" />' <c:if test="${i % 2 == 1}"> class="oddRow" </c:if>
						<c:if test="${i % 2 == 0}"> class="evenRow" </c:if>>
						<td>
						<c:out value="${item.year}" />
						<c:if test="${item.year == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.month}" />
						<c:if test="${item.month == null}">
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
						<c:out value="${item.product_nm_cn}" />
						<c:if test="${item.product_nm_cn == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.product_family}" />
						<c:if test="${item.product_family == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.brand_nm}" />
						<c:if test="${item.brand_nm == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.product_category}" />
						<c:if test="${item.product_category == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.pack_size}" />
						<c:if test="${item.pack_size == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.quantity}" />
						<c:if test="${item.quantity == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.net_sales}" />
						<c:if test="${item.net_sales == null}">
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
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='UploadMappingExfactoryTarget.tkd'">UPLOAD PAGE</a></td>
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