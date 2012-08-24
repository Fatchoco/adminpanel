<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el"%>

<script type="text/javascript">function flyIn(obj){document.getElementById(obj.id).style.backgroundColor = "#5F0101";};function flyOut(obj) {document.getElementById(obj.id).style.backgroundColor = "#9B0101";};</script> 
    <div id="header"><img src="images/header.jpg" width="100%" /></div>
    <div id="mainMenuLeft">
        <span class="mainMenuText" onmouseover="flyIn(this)" onmouseout="flyOut(this)" id="menu2" onclick="javascript:window.location.href='MenuMaster.jsp'">Master File</span>
        <span class="mainMenuText" onmouseover="flyIn(this)" onmouseout="flyOut(this)" id="menu3" onclick="javascript:window.location.href='MenuMapping.jsp'">Mapping File</span>
		<span class="mainMenuText" onmouseover="flyIn(this)" onmouseout="flyOut(this)" id="menu1" onclick="javascript:window.location.href='MenuDistributorFiles.jsp'">Distributor File</span>
        <span class="mainMenuText" onmouseover="flyIn(this)" onmouseout="flyOut(this)" id="menu4" onclick="javascript:window.location.href='MenuException.jsp'">Exception</span>
        <span class="mainMenuText" onmouseover="flyIn(this)" onmouseout="flyOut(this)" id="menu5" onclick="javascript:window.location.href='MenuProcess.jsp'">Run Process</span>
        <span class="mainMenuText" onmouseover="flyIn(this)" onmouseout="flyOut(this)" id="menu6" onclick="javascript:window.location.href='MenuUserMgt.jsp'">User Management</span>
        <span class="mainMenuText" onmouseover="flyIn(this)" onmouseout="flyOut(this)" id="menu7" onclick="javascript:window.location.href='LogoutUser.tkd'">Logout</span>
    </div>
    <div id="mainMenuRight">
    <span class="mainMenuText"><i>Welcome <c:out value="${LoginName}"/></i></span>
    </div>