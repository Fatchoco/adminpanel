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
	 window.location.href="ViewColumnMapping.tkd?Page="+currentPage+"&Search="+searchQ;
}
function Search()
{
	 var searchQ=document.getElementById("txtSearch").value;
	 window.location.href="ViewColumnMapping.tkd?Search="+searchQ;
}
function Export()
{
	var searchQ=document.getElementById("txtSearch").value; 
	window.location.href="ExportColumnMapping.tkd?Search="+searchQ;
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
function save(total)
{
	var start=1;
	var end=total;
	var selectedRec=0;		
	
	var thisid;
	var tempParameter="";
	var idList="";
	var deleteList="";

	var parameter="";		

	for(i=start;i<=end;i++)
	{
		thisid=document.getElementById("thisid_"+i).value;
		var flag = document.getElementById("txtFlag_"+thisid).value;
	
			if(flag == 1)//if delete
			{
				if(deleteList == "")
				{
					deleteList = thisid;
					
				}
				else
				{
					deleteList += "_xDelimitx_"+thisid;
					selectedRec += 1;
				}
			}
	}	

	if (deleteList != "") {
		if (!confirm("Are you sure want to delete " + eval(selectedRec+1) + " record(s)?")) 
		{
			return;	
		}
	};


	//alert(total);
	parameter="";
	if( deleteList!="")
	{		
		parameter += "DeleteList="+deleteList;
	}
	if(parameter != "")
	{
		window.location.href="DeleteColumnMapping.tkd?"+parameter;
	}
}
</script>

<title>Distributor File Setup - Takeda</title>

<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	
	<p class="txtTitleContent">
	<b><u>Distributor File Setup - Result Page</u></b>
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
					<th scope="col">Distributor Code</th>
					<th scope="col">File Name</th>
					<th scope="col">Distributor Code in Consolidated File</th>
					<th scope="col">Transaction Date</th>
					<th scope="col">Product Code</th>
					<th scope="col">Product Name</th>
					<th scope="col">Specification</th>
					<th scope="col">Lot/Batch No</th>
					<th scope="col">Unit</th>
					<th scope="col">Invoice No</th>
					<th scope="col">Expiry Date</th>
					<th scope="col">Customer Code</th>
					<th scope="col">Customer Name</th>
					<th scope="col">Customer City</th>
					<th scope="col">Customer Province</th>
					<th scope="col">Qty</th>
					<th scope="col">Total Sales</th>
					<th scope="col">Transaction Date Format</th>
					<th scope="col">Expiry Date Format</th>
					<th scope="col">Delete</th>
					<th scope="col">Edit</th>
					</tr>
				<c:forEach var="item" items="${result.rows}">
					<c:set var="i" value="${i+1}" />
					<tr id='tr_<c:out value="${item.file_name}" />' <c:if test="${i % 2 == 1}"> class="oddRow" </c:if>
						<c:if test="${i % 2 == 0}"> class="evenRow" </c:if>>
						<td>
						<c:out value="${item.distributor_code}" />
						<c:if test="${item.distributor_code == null}">
							&nbsp;
						</c:if>
						</td>
						<td width="100%">
						<c:out value="${item.file_name}" />
						</td>
						<td>
						<c:out value="${item.distributor_code_infile}" />
						<c:if test="${item.distributor_code_infile == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.transaction_date}" />
						<c:if test="${item.transaction_date == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.product_code}" />
						<c:if test="${item.product_code == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.product_name}" />
						<c:if test="${item.product_name == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.specification_name}" />
						<c:if test="${item.specification_name == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.lot_number}" />
						<c:if test="${item.lot_number == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.unit_name}" />
						<c:if test="${item.unit_name == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.invoice_number}" />
						<c:if test="${item.invoice_number == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.expiry_date}" />
						<c:if test="${item.expiry_date == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.customer_code}" />
						<c:if test="${item.customer_code == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.customer_name}" />
						<c:if test="${item.customer_name == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.customer_city}" />
						<c:if test="${item.customer_city == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.customer_province}" />
						<c:if test="${item.customer_province == null}">
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
						<c:out value="${item.total_sales}" />
						<c:if test="${item.total_sales == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.transaction_date_fmt}" />
						<c:if test="${item.transaction_date_fmt == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<c:out value="${item.expiry_date_fmt}" />
						<c:if test="${item.expiry_date_fmt == null}">
							&nbsp;
						</c:if>
						</td>
						<td>
						<img src='images/DeleteRow.png' class='HandCursor' onclick="MarkDelete('<c:out value="${item.file_name}"/>')" />
						<input type='hidden' name='txtFlag_<c:out value="${item.file_name}"/>' id='txtFlag_<c:out value="${item.file_name}"/>' value=0 />
						<input type='hidden' name='thisid_<c:out value="${i}"/>' id='thisid_<c:out value="${i}"/>' value='<c:out value="${item.file_name}"/>' />
						</td>
						<td>
						<img src='images/EditRow.png' class='HandCursor' onclick="javascript:window.location.href='EditColumnMapping.tkd?EditFile=<c:out value="${item.file_name}" />'" />
						</td>
					</tr>
				</c:forEach>
			</table>
		</td></tr>
		<tr><td>
			<table border="0" align="right" cellpadding="0" cellspacing="0" class="tkd_button">
				<tr>
					<td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='UploadColumnMapping.tkd'">UPLOAD PAGE</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
					<td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='AddColumnMapping.tkd'">ADD</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
					<td class="tkd_button_left">&nbsp;</td>
		            <td align="center" class="tkd_button_middle"><a href="#" onclick="save('<c:out value="${i}"/>')">DELETE</a></td>
		            <td class="tkd_button_right">&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
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