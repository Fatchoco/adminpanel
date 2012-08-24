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
	 var periodQ=document.getElementsByName("cur_period")[0].value;
	 window.location.href="ViewMasterProduct.tkd?Page="+currentPage+"&Search="+searchQ+"&Period="+periodQ;
}
function Search()
{
	 var searchQ=document.getElementById("txtSearch").value;
	 var periodQ=document.getElementsByName("cur_period")[0].value;
	 window.location.href="ViewMasterProduct.tkd?Search="+searchQ+"&Period="+periodQ;
}
function Export()
{
	 var searchQ=document.getElementById("txtSearch").value;
	 var periodQ=document.getElementsByName("cur_period")[0].value;
	 window.location.href="ExportMasterProduct.tkd?Search="+searchQ+"&Period="+periodQ;
}
function Delete()
{
	 var periodQ=document.getElementsByName("cur_period")[0].value;
	 var r=confirm("Are you sure you want to delete all Data for period "+periodQ+"?");
	 if (r==true)
	 {
		 window.location.href="DeleteMasterProduct.tkd?Period="+periodQ;
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
function CopyPreviousPeriodData(a)
{
	var r=confirm("This will replace all period <c:out value='${active_period}'/> data with previous period data. Do you want to continue?");
	if (r==true)
	{
	 window.location.href="CopyPreviousPeriodData.tkd?m=masterproduct";
	}
}
</script>

<title>View Master Product - Takeda</title>

<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	
	<p class="txtTitleContent">
	<b><u>Product Master - Result Page</u></b>
	</p>
	<table border="0" style="font-size: 15px;">
		<tr>
			<td class="txtSubSubTitleContent">Period</td>
			<td class="txtSubSubTitleContent"colspan="2">
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
		<tr>
			<td class="txtSubSubTitleContent">Product Code </td>
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
	
	<!--<fieldset>
		<legend>Search</legend>
		<span>Product Code</span>
		<input name="txtSearch" type="text"
			id="txtSearch" onKeyPress="return readenter2(event,'Search()');"
			value='<c:out value="${param.Search}"/>' />
		<button type="button" class="btn" name="btnResult" onclick="Search()" id="btnSearch">
			<span><span>Search</span></span>
		</button>
		<html-el:hidden property="searchq" value="${param.Search}"></html-el:hidden>
	</fieldset> -->
	<table border="0">
		<tr><td class="txtSubSubTitleContent" align="right">
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
					<th scope="col">PRODUCT CODE</th>
					<th scope="col">PRODUCT NAME (EN)</th>
					<th scope="col">PRODUCT NAME (CN)</th>
					<th scope="col">PRODUCT FAMILY CODE</th>
					<th scope="col">PRODUCT FAMILY NAME (EN)</th>
					<th scope="col">PRODUCT FAMILY NAME (CN)</th>
					<th scope="col">PRODUCT GROUP CODE</th>
					<th scope="col">PRODUCT GROUP NAME (EN)</th>
					<th scope="col">PRODUCT GROUP NAME (CN)</th>
					<th scope="col">USAGE</th>
					<th scope="col">PACKAGING</th>
					<th scope="col">AVERAGE PRICE</th>
					<!--<td>
					<c:out value="PERIOD" />
					</td>
					<td>
					<c:out value="PRODUCT CODE" />
					</td>
					<td>
					<c:out value="PRODUCT NAME (EN)" />
					</td>
					<td>
					<c:out value="PRODUCT NAME (CN)" />
					</td>
					<td>
					<c:out value="PRODUCT FAMILY CODE" />
					</td>
					<td>
					<c:out value="PRODUCT FAMILY NAME (EN)" />
					</td>
					<td>
					<c:out value="PRODUCT FAMILY NAME (CN)" />
					</td>
					<td>
					<c:out value="PRODUCT GROUP CODE" />
					</td>
					<td>
					<c:out value="PRODUCT GROUP NAME (EN)" />
					</td>
					<td>
					<c:out value="PRODUCT GROUP NAME (CN)" />
					</td>
					<td>
					<c:out value="USAGE" />
					</td>
					<td>
					<c:out value="PACKAGING" />
					</td>
					<td>
					<c:out value="AVERAGE PRICE" />
					</td>
					<td>
					&nbsp;
					</td>
					--></tr>
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
						<c:out value="${item.product_family_cd}" />
						<c:if test="${item.product_family_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.product_family_nm}" />
						<c:if test="${item.product_family_nm == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.product_family_nm_cn}" />
						<c:if test="${item.product_family_nm_cn == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.product_group_cd}" />
						<c:if test="${item.product_group_cd == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.product_group}" />
						<c:if test="${item.product_group == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.product_group_cn}" />
						<c:if test="${item.product_group_cn == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.usage}" />
						<c:if test="${item.usage == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.packaging}" />
						<c:if test="${item.packaging == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.avg_unit_price}" />
						<c:if test="${item.avg_unit_price == null}">
							&nbsp;
						</c:if>
						</td>
						<!--<td>
						<img src='images/DeleteRow.png' class='HandCursor' onclick="MarkDelete('<c:out value="${item.product_cd}"/>')" />
						<input type='hidden' name='txtFlag_<c:out value="${item.product_cd}"/>' id='txtFlag_<c:out value="${item.product_cd}"/>' value=0 />
						<input type='hidden' name='thisid_<c:out value="${i}"/>' id='thisid_<c:out value="${i}"/>' value='<c:out value="${item.product_cd}"/>' />
						</td>
					--></tr>
				</c:forEach>
			</table>
		</td></tr>
		<tr><td>
			<table border="0" align="right" cellpadding="0" cellspacing="0" class="tkd_button">
				<tr>
					<td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="CopyPreviousPeriodData()">COPY&nbsp;PREVIOUS&nbsp;PERIOD&nbsp;DATA</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
					<td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="Delete()">DELETE</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
		            <td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='UploadMasterProduct.tkd'">UPLOAD PAGE</a></td>
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