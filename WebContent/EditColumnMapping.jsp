<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />

<script language="javascript">
	function checkForm()
	{
		var distCode = document.getElementsByName("distCode")[0].value;
		var distCodeInfile = document.getElementsByName("distCodeInfile")[0].value;
		var fileName = document.getElementsByName("fileName")[0].value;
		var transDate = document.getElementsByName("transDate")[0].value;
		var productCode = document.getElementsByName("productCode")[0].value;
		var productName = document.getElementsByName("productName")[0].value;
		var specification = document.getElementsByName("specification")[0].value;
		var batchNo = document.getElementsByName("batchNo")[0].value;
		var unitName = document.getElementsByName("unitName")[0].value;
		var invoiceNo = document.getElementsByName("invoiceNo")[0].value;
		var expiryDate = document.getElementsByName("expiryDate")[0].value;
		var customerCode = document.getElementsByName("customerCode")[0].value;
		var customerName = document.getElementsByName("customerName")[0].value;
		var customerCity = document.getElementsByName("customerCity")[0].value;
		var customerProvince = document.getElementsByName("customerProvince")[0].value;
		var qty = document.getElementsByName("qty")[0].value;
		var totalsales = document.getElementsByName("totalsales")[0].value;
		var transDateFormat = document.getElementsByName("transDateFormat")[0].value;
		var expiryDateFormat = document.getElementsByName("expiryDateFormat")[0].value;
		
		if(distCode=="" && distCodeInfile=="")
		{
			alert("Please select either Distributor Code or Distributor Code in Consolidated file");
			return false;
		}
		else if(distCode!="" && distCodeInfile!="")
		{
			alert("Please choose only one Distributor Code source (either from file or dropdown input)");
			return false;
		}
		else if(fileName=="")
		{
			alert("Please fill the file name");
			return false;
		}
		else if(transDate=="" || transDateFormat=="")
		{
			alert("Please choose column for Transaction Date and Transaction Date Format");
			return false;
		}
		else if(productCode=="" && productName=="")
		{
			alert("Please choose column for Product Code or Product Name");
			return false;
		}
		else if((expiryDate!="" && expiryDateFormat=="") || (expiryDate=="" && expiryDateFormat!=""))
		{
			alert("Please choose column for Expiry Date and Expiry Date Format");
			return false;
		}
		else if(customerCode=="" && customerName=="")
		{
			alert("Please choose column for Customer Code or Customer name");
			return false;
		}
		else if(qty=="" && totalsales=="")
		{
			alert("Please choose column for Quantity or Totalsales");
			return false;
		}
		else
		{return true;}
	}
	function frSubmit()
	{
		if(checkForm())
			document.forms[0].submit();
	}
	function docReset()
	{
		document.forms[0].reset();
	}
	function distFileNameAdd()
	{
		document.getElementsByName("fileName")[0].value = document.getElementsByName("distCode")[0].value;
	}
	function checkFileNameFormat(fileName)
	{
		var distCode = document.getElementsByName("distCode")[0].value;
		var fileName = document.getElementsByName("fileName")[0].value;
		if(fileName.substring(0,8) == distCode)
			return true;
		else
			return false;
	}
</script>

<title>Edit Distributor File Setup - Takeda</title>
<body>
<div class="content">
	<script type="text/javascript" src="wz_tooltip.js"></script>
	<c:import url="Header.tkd"></c:import>
	<p class="txtTitleContent">
		<b><u>Edit Distributor File Setup</u></b>
	</p>
	<html:form action="/AddColumnMappingProcess" method="post" enctype="multipart/form-data">
	<c:set var="items_edit" value="${items_data.rows[0]}" />
	<html-el:hidden property="actionType" value="edit"></html-el:hidden>
	<html-el:hidden property="distCode" value="${items_edit.distributor_code}"></html-el:hidden>
	<html-el:hidden property="fileName" value="${items_edit.file_name}"></html-el:hidden>
	<table class="txtSubSubTitleContent">
		<!-- Distributor Code -->
		<tr>
			<td colspan="3" align="right">
				<span>Distributor Code :</span>
			</td>
			<td>
				<html-el:select property="distCodeD" value="${items_edit.distributor_code}" onchange="distFileNameAdd()" disabled="true">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="item" items="${items_distributorcode.rows}">
						<html-el:option value="${item.DISTRIBUTOR_CD}"><c:out value="${item.DISTRIBUTOR_CD}"/></html-el:option>
					</c:forEach>
				</html-el:select> *Please choose '-' for consolidated file
			</td>
		</tr>
		<!-- FileName -->
		<tr>
			<td colspan="3" align="right">
				<span>File Name :</span>
			</td>
			<td>
				<html-el:text property="fileNameD" value="${items_edit.file_name}" disabled="true"/>
			</td>
		</tr>
		<!-- Distributor Code InFile -->
		<tr>
			<td colspan="3" align="right">
				<span>Distributor Code in Consolidated File :</span>
			</td>
			<td>
				<html-el:select property="distCodeInfile" value="${items_edit.distributor_code_infile}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select> *Mandatory for consolidated file
			</td>
		</tr>
		<!-- Transaction Date -->
		<tr>
			<td colspan="3" align="right">
				<span>Transaction Date :</span>
			</td>
			<td>
				<html-el:select property="transDate" value="${items_edit.transaction_date}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Product Code -->
		<tr>
			<td colspan="3" align="right">
				<span>Product Code :</span>
			</td>
			<td>
				<html-el:select property="productCode" value="${items_edit.product_code}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Product Name -->
		<tr>
			<td colspan="3" align="right">
				<span>Product Name :</span>
			</td>
			<td>
				<html-el:select property="productName" value="${items_edit.product_name}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Specification -->
		<tr>
			<td colspan="3" align="right">
				<span>Specification :</span>
			</td>
			<td>
				<html-el:select property="specification" value="${items_edit.specification_name}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Lot/Batch Number -->
		<tr>
			<td colspan="3" align="right">
				<span>Lot/Batch Number :</span>
			</td>
			<td>
				<html-el:select property="batchNo" value="${items_edit.lot_number}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Unit Name -->
		<tr>
			<td colspan="3" align="right">
				<span>Unit Name :</span>
			</td>
			<td>
				<html-el:select property="unitName" value="${items_edit.unit_name}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Invoice Number -->
		<tr>
			<td colspan="3" align="right">
				<span>Invoice Number :</span>
			</td>
			<td>
				<html-el:select property="invoiceNo" value="${items_edit.invoice_number}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Expiry Date -->
		<tr>
			<td colspan="3" align="right">
				<span>Expiry Date :</span>
			</td>
			<td>
				<html-el:select property="expiryDate" value="${items_edit.expiry_date}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Customer Code -->
		<tr>
			<td colspan="3" align="right">
				<span>Customer Code :</span>
			</td>
			<td>
				<html-el:select property="customerCode" value="${items_edit.customer_code}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Customer Name -->
		<tr>
			<td colspan="3" align="right">
				<span>Customer Name :</span>
			</td>
			<td>
				<html-el:select property="customerName" value="${items_edit.customer_name}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Customer City -->
		<tr>
			<td colspan="3" align="right">
				<span>Customer City :</span>
			</td>
			<td>
				<html-el:select property="customerCity" value="${items_edit.customer_city}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Customer Province -->
		<tr>
			<td colspan="3" align="right">
				<span>Customer Province :</span>
			</td>
			<td>
				<html-el:select property="customerProvince" value="${items_edit.customer_province}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Quantity -->
		<tr>
			<td colspan="3" align="right">
				<span>Quantity :</span>
			</td>
			<td>
				<html-el:select property="qty" value="${items_edit.quantity}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Total Sales -->
		<tr>
			<td colspan="3" align="right">
				<span>Total Sales :</span>
			</td>
			<td>
				<html-el:select property="totalsales" value="${items_edit.total_sales}">
					<html-el:option value="">-</html-el:option>
					<c:forEach var="i" begin="1" end="50" step="1">
						<html-el:option value="C${i}"><c:out value="C${i}"/></html-el:option>
					</c:forEach>
				</html-el:select>
			</td>
		</tr>
		<!-- Transaction Date Format -->
		<tr>
			<td colspan="3" align="right">
				<span>Transaction Date Format :</span>
			</td>
			<td>
				<html-el:select property="transDateFormat" value="${items_edit.transaction_date_fmt}">
					<html-el:option value="">-</html-el:option>
					<html-el:option value="MM/DD/YYYY">MM/DD/YYYY</html-el:option>
					<html-el:option value="YYYY-MM-DD">YYYY-MM-DD</html-el:option>
					<html-el:option value="YYYY/MM/DD">YYYY/MM/DD</html-el:option>
					<html-el:option value="YYYY.MM.DD">YYYY.MM.DD</html-el:option>
					<html-el:option value="YYYYMMDD">YYYYMMDD</html-el:option>
				</html-el:select>
			</td>
		</tr>
		<!-- Expired Date Format -->
		<tr>
			<td colspan="3" align="right">
				<span>Expired Date Format :</span>
			</td>
			<td>
				<html-el:select property="expiryDateFormat" value="${items_edit.expiry_date_fmt}">
					<html-el:option value="">-</html-el:option>
					<html-el:option value="MM/DD/YYYY">MM/DD/YYYY</html-el:option>
					<html-el:option value="YYYY-MM-DD">YYYY-MM-DD</html-el:option>
					<html-el:option value="YYYY/MM/DD">YYYY/MM/DD</html-el:option>
					<html-el:option value="YYYY.MM.DD">YYYY.MM.DD</html-el:option>
					<html-el:option value="YYYYMMDD">YYYYMMDD</html-el:option>
				</html-el:select>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
					<tr>
						<td class="tkd_button_left">&nbsp;</td>
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="frSubmit()">SAVE</a></td>
			            <td class="tkd_button_right">&nbsp;</td>
					</tr>
				</table>
			</td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
					<tr>
						<td class="tkd_button_left">&nbsp;</td>
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="docReset()">RESET</a></td>
			            <td class="tkd_button_right">&nbsp;</td>
					</tr>
				</table>
			</td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" class="tkd_button">
					<tr>
						<td class="tkd_button_left">&nbsp;</td>
			            <td align="center" class="tkd_button_middle"><a href="#" onclick="javascript:window.location.href='ViewColumnMapping.tkd'">CANCEL</a></td>
			            <td class="tkd_button_right">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</html:form>
	<!-- <c:import url="Footer.jsp"></c:import> -->
</div>
</body>
</html:html>

<c:if test="${param.message != null}">
	<script language="JavaScript">
		alert("<c:out value='${param.message}'/>"); 
	</script>
</c:if>