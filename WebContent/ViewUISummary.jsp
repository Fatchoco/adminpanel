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
		 var searchQ2=document.getElementsByName("searchq2")[0].value;
		 window.location.href="ViewUISummary.tkd?Period="+periodQ+"&Page="+currentPage+"&Search2="+searchQ2;
	}
	function Search()
	{
		 var searchQ2=document.getElementsByName("txtSearch2")[0].value;
		 var periodQ=document.getElementsByName("cur_period")[0].value;
		 window.location.href="ViewUISummary.tkd?Search2="+searchQ2+"&Period="+periodQ;
	}
	function NavigatePeriod()
	{
		 var periodQ=document.getElementsByName("cur_period")[0].value;
		 var searchQ2=document.getElementsByName("txtSearch2")[0].value;
		 window.location.href="ViewUISummary.tkd?Period="+periodQ+"&Search2="+searchQ2;
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
		var searchQ2=document.getElementsByName("txtSearch2")[0].value;
		window.location.href="ExportUISummary.tkd?Search2="+searchQ2+"&Period="+periodQ;
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

<title>Upload & Process Summary - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<p class="txtTitleContent">
		<b><u>Upload & Process Summary</u></b>
	</p>
	<!--<html:form action="/DeleteDistributorFiles" method="post">-->
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
							<html-el:option value="summary">Summary</html-el:option>
							<html-el:option value="details">Details</html-el:option>
						</html-el:select>
			</td>
		</tr>
	</table>
	<!--</html:form>-->
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
					<c:choose>
						<c:when test="${param.Search2 == 'details'}">
							<th scope="col">PERIOD</th>
							<th scope="col">ACTION</th>
							<th scope="col">ACTION CODE</th>
							<th scope="col">FILE NAME</th>
							<th scope="col">NOTES</th>
							<th scope="col">USER</th>
							<th scope="col">DATE</th>
						</c:when>
						<c:otherwise>
							<th scope="col">PERIOD</th>
							<th scope="col">ACTION</th>
							<th scope="col">ACTION CODE</th>
							<th scope="col">COUNT</th>
						</c:otherwise>
					</c:choose>
				</tr>
				<c:forEach var="item" items="${result.rows}">
					<c:set var="i" value="${i+1}" />
					<c:choose>
						<c:when test="${param.Search2 == 'details'}">
							<!-- Details -->
							<tr id='tr_<c:out value="${item.period}" />' <c:if test="${i % 2 == 1}"> class="oddRow" </c:if><c:if test="${i % 2 == 0}"> class="evenRow" </c:if>>
								<td>
								<c:out value="${item.period}" />
								<c:if test="${item.period == null}">
									&nbsp;
								</c:if>
								</td>
								<td>
								<c:out value="${item.action}" />
								<c:if test="${item.action == null}">
									&nbsp;
								</c:if>
								</td>
								<td>
								<c:out value="${item.action_code}" />
								<c:if test="${item.action_code == null}">
									&nbsp;
								</c:if>
								</td>
								<td>
								<c:out value="${item.file_name}" />
								<c:if test="${item.file_name == null}">
									&nbsp;
								</c:if>
								</td>
								<td>
								<c:out value="${item.notes}" />
								<c:if test="${item.notes == null}">
									&nbsp;
								</c:if>
								</td>
								<td>
								<c:out value="${item.created_user}" />
								<c:if test="${item.created_user == null}">
									&nbsp;
								</c:if>
								</td>
								<td>
								<c:out value="${item.created_date}" />
								<c:if test="${item.created_date == null}">
									&nbsp;
								</c:if>
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<!-- Summary -->
							<tr id='tr_<c:out value="${item.period}" />' <c:if test="${i % 2 == 1}"> class="oddRow" </c:if><c:if test="${i % 2 == 0}"> class="evenRow" </c:if>>
								<td>
								<c:out value="${item.period}" />
								<c:if test="${item.period == null}">
									&nbsp;
								</c:if>
								</td>
								<td>
								<c:out value="${item.action}" />
								<c:if test="${item.action == null}">
									&nbsp;
								</c:if>
								</td>
								<td>
								<c:out value="${item.action_code}" />
								<c:if test="${item.action_code == null}">
									&nbsp;
								</c:if>
								</td>
								<td align = "right">
								<c:out value="${item.count}" />
								<c:if test="${item.count == null}">
									&nbsp;
								</c:if>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</table>
				</td>
			</tr>
		<tr><td>
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