<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<html:html xhtml="true">

<link rel="stylesheet" type="text/css" href="style.css" />



<script language="javascript">

	function Cancel() {
		window.location.href="ViewUserList.tkd";
	}
	
	
	function SaveData() {
		
		
		if (document.forms[0].userFullName.value =='' )
		{
		   alert('User Full Name must be filled!');
		   return;
		}
		
			
		
		var checkbox_choices = 0;
		var selected = false;
		for (counter = 0; counter < document.forms[0].userAppAccess.length; counter++)
		{

			if (document.forms[0].userAppAccess[counter].checked)
					{
						selected = true;
					}
		}		
		
		if (selected == false)
			{
			   alert('Please select Application to be accessed');
			   return;
			}
		
		selected = false;
		for (counter = 0; counter < document.forms[0].userFormAccess.length; counter++)
		{
				if (document.forms[0].userFormAccess[counter].checked)
					{
						selected = true; 		
					}
		}		
		
		if (selected == false)
			{
			   alert('Please select modules to be accessed');
			   return;
			}
		
		document.forms[0].submit();
		
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
	
	<html:form  action="/EditUserProcess" method="post">
		<table class="txtTitleContent" border=0>
			<tr>
				<td><b><u>User Management - Edit User</u>
				</b>
				</td>
			</tr>
		</table>
		<table class="txtSubSubTitleContent" border="0">
			<tr>
				<td>User Name</td>
				<td><html-el:hidden property="useridName" value="${FormAddEditUser.useridName}" /> <c:out value="${FormAddEditUser.useridName}"/></td>
				<td><html:checkbox property="userAdmin" value="1" /> Admin</td>
			</tr>
			<tr>
				<td>Full Name</td>
				<td><html-el:text property="userFullName" value="${FormAddEditUser.userFullName}" size="20" /></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>Password</td>
				<td><html-el:password property="userPassword" value="${FormAddEditUser.userPassword}" size="21" /></td>
				<td>* Enter new password to change password</td>
			</tr>
			<!--<tr>
				<td>Admin User</td>
				<td><html:checkbox property="userAdmin" value="1" /></td>
			</tr>-->
		</table>
		<br/>
		<table width="320" border="0">
			<tr>
				<td>
					<fieldset>
						<legend class="txtSubTitleContent">Access Type</legend>
						<!--<div class="txtSubTitleContent" align="center">
							Access Type
						</div>-->
						<div class="txtSubSubTitleContent">
							<span><input type="hidden" name="AppAccess" value="Admin" /> <html:multibox
									property="userAppAccess" value="DIST" /> DISTRIBUTOR DATA MART</span>
							<span><html:multibox property="userAppAccess" value="IMS" /> IMS DATA MART</span>
						</div>
					</fieldset>
				</td>
			</tr>
		</table>
		<br/>	
		<table border="0">
			<tr>
				<td>
					<fieldset>
						<legend class="txtSubTitleContent">Distributor Data Mart Module Access</legend>
						<table border="0">
							<tr valign="top">
								<td>
									<table class="txtSubSubTitleContent" border="0">
										<tr>
											<td><b><u>Master File</u></b></td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="PARMMST" />REPORTING PERIOD</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="PRDMST" />PRODUCT MASTER</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="DISTPRICEMST" />DISTRIBUTOR PRICE MASTER</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="DISTMST" />DISTRIBUTOR MASTER</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="CUSTMST" />CUSTOMER MASTER</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="GEOMST" />GEOGRAPHIC MASTER</td>
										</tr>
									</table>
								</td>
								<td>
									<table class="txtSubSubTitleContent" border="0">
										<tr>
											<td><b><u>Mapping File</u></b></td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="PRDMAP" />PRODUCT MAPPING</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="CUSTMAP" />CUSTOMER MAPPING</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="MRMAP" />MR MAPPING</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="HPTARGET" />HOSPITAL TARGET MAPPING</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="EFTARGET" />EX-FACTORY TARGET MAPPING</td>
										</tr>
									</table>
								</td>
								<td>
									<table class="txtSubSubTitleContent" border="0">
										<tr>
											<td><b><u>Distributor File</u></b></td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="COLMAP" />DISTRIBUTOR FILE SETUP</td>
										</tr>
										<tr>
											<td><input type="hidden" name="formAccess" value="Admin" /> 
											<html:multibox property="userFormAccess" value="DISTFILE" />DISTRIBUTOR FILE UPLOAD</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="DISTADJFILE" />DISTRIBUTOR ADJUSTMENT FILE UPLOAD</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="INVTFILE" />INVENTORY UPLOAD</td>
										</tr>
										<!--<tr>
											<td><html:multibox property="userFormAccess" value="COLMAP" />COLUMN MAPPING INPUT</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="COLMAP" />COLUMN MAPPING UPLOAD</td>
										</tr>-->
									</table>
								</td>
								<td>
									<table class="txtSubSubTitleContent" border="0">
										<tr>
											<td><b><u>Exception</u></b></td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="UNMPPRD" />UNMAPPED PRODUCT</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="UNMPCUST" />UNMAPPED CUSTOMER</td>
										</tr>
										<tr>
											<TD><html:multibox property="userFormAccess" value="DISTFILEEXCP" />DISTRIBUTOR FILE EXCEPTION</td>
										</tr>
										<tr>
											<TD><html:multibox property="userFormAccess" value="DISTADJFILEEXCP" />DISTRIBUTOR ADJUSTMENT FILE EXCEPTION</td>
										</tr>
									</table>
								</td>
								<td>
									<table class="txtSubSubTitleContent" border="0">
										<tr>
											<td><b><u>Run Process</u></b></td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="DISTRUN" />RUN MONTHLY PROCESS</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="DISTADJRUN" />RUN ADJUSTMENT PROCESS</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td>
					<fieldset>
						<legend class="txtSubTitleContent">Market Analyzer Module Access</legend>
						<table border="0">
							<tr valign="top">
								<td>
									<table class="txtSubSubTitleContent" border="0">
										<tr>
											<td><b><u>Upload File</u></b></td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="IUPDAT" />LOAD MARKET ANALYZER DATA</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="IUPMAP" />UPLOAD MAPPING TAKEDA CATEGORY</td>
										</tr>
									</table>
								</td>
								<td>
									<table class="txtSubSubTitleContent" border="0">
										<tr>
											<td><b><u>SETTING</u></b></td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="ISETPER" />REPORTING PERIOD</td>
										</tr>
										<tr>
											<td><html:multibox property="userFormAccess" value="ISETDIR" />SOURCE DIRECTORY</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>
		<table border="0" align="left" cellpadding="0" cellspacing="0"
			class="tkd_button">
				<tr>
				<td class="tkd_button_left">&nbsp;</td>
				<td align="center" class="tkd_button_middle">
					<a href="#" onclick="SaveData()"> Save</a>
				</td>
				<td align="center" class="tkd_button_right">&nbsp;</td>
				
				
				<td class="tkd_button_left">&nbsp;</td>
				<td align="center" class="tkd_button_middle">
					<a href="#" onclick="Cancel()"> Cancel</a>
				</td>
				<td align="center" class="tkd_button_right">&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				
			</tr>
		</table>
		<c:if test="${param.message != null and param.message != ''}">
			<script language="JavaScript">
				alert('<c:out value='${param.message}'/>');
			</script>
		</c:if>

	</html:form>
	</div>
<!-- <c:import url="Footer.jsp"></c:import> -->

</body>
</html:html>


