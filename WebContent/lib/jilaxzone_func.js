	var count=1;
	
	function LoopProgressBar()
	{
		var temps="<table cellpadding='0' cellspacing='0' width='100%'>";
        temps+="<tr style='height:1px;' bgcolor='#00cc33'>";
        temps+="<td></td><td></td><td></td>";
        temps+="</tr>";
        temps+="<tr>";
        temps+="<td style='width:1px; background-color:#00cc33'></td>";
        temps+="<td style='background-color:#97ff97'>";

		var loading = new Array(".","l","o","a","d","i","n","g",".",".",".",".",".",".",".","." )
		var color = new Array("9999FF","00CC00","00CC00","00CC00","006600","000066","000066","0099FF","0099FF","0066FF","0066FF","0066FF","0066FF","0066FF","0066FF","0066FF","0066FF");
		var table;
		table="<table border='0' cellpadding='0' cellspacing='0'>";
		table+="<tr>";
		for(i=1;i<=count;i++)
			table+="<td bgcolor='#"+color[i]+"' class='progressBar' width='5px'> "+loading[i]+" </td>";
		table+="</tr>";
		table+="</table>";

		temps+=table;

		temps+="</td>";
        temps+="<td style='width:1px; background-color:#00cc33'></td>";
        temps+="</tr>";
        temps+="<tr style='height:1px;' bgcolor='#00cc33'>";
        temps+="<td></td><td></td><td></td></tr></table><br>";

		if(count==11)
			count=1
		else
			count=count+1;

		document.getElementById("progressBar").innerHTML=table;
	}
	
	function DrawProgressBar()
	{
		if(count>0)
		{
			LoopProgressBar();
			setTimeout("DrawProgressBar()",200);
		}
	}	
	function StopProgressBar()
	{
		count=0;
		document.getElementById("progressBar").innerHTML="";
	}
	