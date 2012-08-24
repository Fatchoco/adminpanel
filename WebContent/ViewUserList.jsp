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
	 var searchQ=document.getElementsByName("searchq")[0].value;
	 window.location.href="ViewUserList.tkd?Page="+currentPage+"&Search="+searchQ;
}
function Search()
{
	 var searchQ=document.getElementById("txtSearch").value;
	 var periodQ=document.getElementsByName("cur_period")[0].value;
	 window.location.href="ViewUserList.tkd?Search="+searchQ;
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
function editData(userName)
{
	 window.location.href="EditUser.tkd?UserName="+userName;
}

function deleteData(userName)
{
	
	 if (!confirm('Are your sure want to delete user '+ userName +' ?'))
		 {
		 	return
		 }
	 window.location.href="DelUserProcess.tkd?UserName="+userName;
}

function newData(userName)
{
	 window.location.href="AddUser.tkd";
}
</script>

<title>User Management - Takeda</title>

<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
		<c:choose>
		<c:when test="${LOGIN_APP == 'IMS' }">
		<c:import url="Header2.tkd"></c:import>
		</c:when>
		<c:otherwise>
		<c:import url="Header.tkd"></c:import>
		</c:otherwise>
	</c:choose>
	
	<p class="txtTitleContent">
	<b><u>User List</u></b>
	</p>
	<html-el:hidden property="searchq" value="${param.Search}"></html-el:hidden>
		
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
					<th scope="col">USER NAME</th>
					<th scope="col">USER FULL NAME</th>
					<th scope="col">ADMIN</th>
					<th scope="col">DELETE</th>
				</tr>
				<c:forEach var="item" items="${result.rows}">
					<c:set var="i" value="${i+1}" />
					<tr id='tr_<c:out value="${item.USER_NAME}" />' <c:if test="${i % 2 == 1}"> class="oddRow" </c:if>
						<c:if test="${i % 2 == 0}"> class="evenRow" </c:if>>
						<td>
						<a href="#"  onclick="editData('<c:out value="${item.USER_NAME}"/>')"><c:out value="${item.USER_NAME}" /></a>
						
						<c:if test="${item.USER_NAME == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.FULL_NAME}" />
						<c:if test="${item.FULL_NAME == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.IS_ADMIN}" />
						<c:if test="${item.IS_ADMIN == null}">
							&nbsp;
						</c:if>
						</td>
						
						<td align="center" >
						<img src='images/DeleteRow.png' class="HandCursor" onClick="deleteData('<c:out value="${item.USER_NAME}"/>');" />
							</td>
					</tr>
				</c:forEach>
			</table>
		</td></tr>
	</table>
	<table border="0" align="left" cellpadding="0" cellspacing="0"
			class="tkd_button">
			<tr>
				<td class="tkd_button_left">&nbsp;</td>
				<td align="center" class="tkd_button_middle"><a href="#"
					onclick="newData()"> Add New User</a></td>
				<td class="tkd_button_right">&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
	
	
	
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