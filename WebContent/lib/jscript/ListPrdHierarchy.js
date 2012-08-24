// JavaScript Document

//document.onkeyup = CheckSubmission;
	//-----------VARIABLE LIST EXISTS IN EVERY PAGE--------------------
	var opt;
	var summaryView;
	var detailView;
	var recordPerPage=20;
	var totalPage;
	var currentPage;


	//-------------VARIABLE LIST EXIST ONLY IN THIS PAGE---------------
	var listProduct,isAccessible,hasAccess;
	
	//------------------------------- AJax Config ---------------------------------------

	function getHTTPObject() 
	{
    	var xmlhttp;
    	if (window.XMLHttpRequest) //mozilla
        	xmlhttp = new XMLHttpRequest();
    	else if (window.ActiveXObject) //IE
        	xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    
    	return xmlhttp;
	}
	
	//link to code behind
	var url = "ListPrdHierarchyQuery.scb"; 
	var http = getHTTPObject();
	
	//------------------------------- End of Ajax Config ------------------------------------		
	
	function OnLoad()
	{
		document.getElementById("progressBar").style.display = '';
		count=1;
		DrawProgressBar();
		var parameter = "FormLoad=1";
	
		http.open("POST", url, true); //true -> asynchronus ajax, false -> postback biasa
		http.onreadystatechange = HttpOnLoad; //onreadystatechange -> function yang menghandle response dari server
		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		http.setRequestHeader("Content-length", "FormLoad=1".length);
		http.setRequestHeader("Connection", "close");

		http.send(parameter);

	}
	
	function Search()
	{
		document.getElementById("progressBar").style.display = '';
		count=1;
		var parameter="Search=1&Filter="+document.getElementById("txtSearch").value;

		DrawProgressBar();
		//document.writeln(parameter);
		http.open("POST", url, true);
		http.onreadystatechange = HttpOnLoad;
		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      	http.setRequestHeader("Content-length", parameter.length);
		http.setRequestHeader("Connection", "close");
		http.send(parameter);
	}
	
	function HttpOnLoad() 
	{		
    	if (http.readyState == 4) 
		{		
    		alert(4);
			if (http.status == 200)//complete
			{
				alert(200);
				//check session
				var loginChecked = http.responseXML.getElementsByTagName("Login");
				var isLogin = loginChecked[0].childNodes[0].firstChild.nodeValue.replace("_jandj_","&");
				var uid;
				if(isLogin == 0)
				{
					window.location.href ="Login.jsp";
				}
			
				//get total row per page
				
				var tempTotal,tempTotal2;
				
				tempTotal = http.responseXML.getElementsByTagName("jTotalPage");

				tempTotal2=tempTotal[0].childNodes[0].firstChild.nodeValue.replace("_jandj_","&");

				if(tempTotal2 >0)
				{
					recordPerPage = tempTotal2;
				}
				
					listProduct = http.responseXML.getElementsByTagName("Mapping");//get level2 xml			
				
					//counting page
					totalPage=Math.floor(listProduct.length/recordPerPage);
					if(listProduct.length%recordPerPage>0)totalPage+=1;
					document.getElementById("lblTotalPage").innerHTML="of " +totalPage+" for total row count : "+ listProduct.length;
					currentPage=1;
					
					//clear the cmbPage
					while(document.getElementById("cmbPage").options.length)
						document.getElementById("cmbPage").remove(0);
						
					//fill the page combobox
					for(i=1;i<=totalPage;i++)
					{
						opt=document.createElement("OPTION");					
						opt.text=i;
						opt.value=i;
						document.getElementById("cmbPage").options.add(opt);
					}
					
					//constructing detail table
					if(listProduct.length==0)
					{
					document.getElementById("dgvList").innerHTML="<table width=100% class='TableText'><tr><td bgcolor='#000000' align='center' class='norecord'>There Is No Record Available</td></tr></table>";
					}
					else
					{
					ConstructTable();			
				//assigning detail table to html
					document.getElementById("dgvList").innerHTML=detailView;	
					}					
				
				
				document.getElementById("progressBar").style.display = 'none';
				StopProgressBar();
				
			} 
			else 		
				alert (http.responseText);
		}							
	}	
	
	function CheckSubmission()
	{
		var keyId = event.keyCode;
		if(keyId == 13)
		{
			Search();
			alert("asd");
		}
	}
	
	
	
	function ConstructTable()

	{		
		detailView="<table width='100%' cellpadding='1' cellspacing='1' border='1' class='TableText'>";				
		detailView+="<tr bgcolor='blueocean'>";
		detailView+="<td>Standard Product Code</td>";
		detailView+="<td>Standard Product Desc</td>";
		detailView+="<td>Product Family Code</td>";
		detailView+="<td>Product Family Desc</td>";
		detailView+="<td>Product Segment Code</td>";
		detailView+="<td>Product Segment Desc</td>";
		detailView+="<td>Product Area Code</td>";
		detailView+="<td>Product Area Desc</td>";
		detailView+="</tr>";
		if(listProduct.length<recordPerPage)
		{																
			for(i=0;i<listProduct.length;i++)
			{
				PrintDetails(i);
			}

		}
		else
		{					
			for(i=0;i<recordPerPage;i++)
			{			
				PrintDetails(i);
			}
		}
		detailView+="</table>";
		
		
	}
	
	function PrintDetails(i)
	{
		//get <id>				
		tempSPC=listProduct[i].childNodes[0].firstChild.nodeValue.replace("_jandj_","&");	
		tempSPD = listProduct[i].childNodes[1].firstChild.nodeValue.replace("_jandj_","&");														
		tempPFC = listProduct[i].childNodes[2].firstChild.nodeValue.replace("_jandj_","&");	
		tempPFD = listProduct[i].childNodes[3].firstChild.nodeValue.replace("_jandj_","&");
		tempPSC = listProduct[i].childNodes[4].firstChild.nodeValue.replace("_jandj_","&");
		tempPSD = listProduct[i].childNodes[5].firstChild.nodeValue.replace("_jandj_","&");
		tempPAC = listProduct[i].childNodes[6].firstChild.nodeValue.replace("_jandj_","&");
		tempPAD = listProduct[i].childNodes[7].firstChild.nodeValue.replace("_jandj_","&");

									
										
		
		detailView+="<tr >";	


		detailView+="<td>";
		detailView+="	<span class='TableText' >"+tempSPC+"&nbsp;</span>";
		detailView+="</td>";

		detailView+="<td>";				
		detailView+="	<span class='TableText' >"+tempSPD+"&nbsp;</span>";
		detailView+="</td>";


		detailView+="<td>";
		detailView+="	<span class='TableText' >"+tempPFC+"&nbsp;</span>";
		detailView+="</td>";
		detailView+="<td>";
		detailView+="	<span class='TableText' >"+tempPFD+"&nbsp;</span>";
		detailView+="</td>";
		detailView+="<td>";
		detailView+="	<span class='TableText' >"+tempPSC+"&nbsp;</span>";
		detailView+="</td>";
		detailView+="<td>";
		detailView+="	<span class='TableText' >"+tempPSD+"&nbsp;</span>";
		detailView+="</td>";
		detailView+="<td>";
		detailView+="	<span class='TableText' >"+tempPAC+"&nbsp;</span>";
		detailView+="</td>";
		detailView+="<td>";
		detailView+="	<span class='TableText' >"+tempPAD+"&nbsp;</span>";
		detailView+="</td>";


					
		detailView+="</tr>";		
	}
	
	function DrawPage()
	{
		currentPage=parseInt(document.getElementById("cmbPage").options[document.getElementById("cmbPage").selectedIndex].value);	
		detailView="<table width='100%' cellpadding='1' cellspacing='1' border='1' class='TableText'>";				
		detailView+="<tr bgcolor='blueocean'>";
		detailView+="<td>Standard Product Code</td>";
		detailView+="<td>Standard Product Desc</td>";
		detailView+="<td>Product Family Code</td>";
		detailView+="<td>Product Family Desc</td>";
		detailView+="<td>Product Segment Code</td>";
		detailView+="<td>Product Segment Desc</td>";
		detailView+="<td>Product Area Code</td>";
		detailView+="<td>Product Area Desc</td>";
		detailView+="</tr>";

		var start,end;
		start=recordPerPage*(currentPage-1);
		end=recordPerPage*currentPage;
		
		if(end>listProduct.length)
			end=listProduct.length-1;
		else
			end-=1;			
		
		var tempProductId;

		for(i=start;i<=end;i++)
		{
			PrintDetails(i);	
		}
				
		detailView+="</table>";	
		
		document.getElementById("dgvList").innerHTML=detailView;																
	}
	
	function NavigatePage()
	{	
		DrawPage();
	}



