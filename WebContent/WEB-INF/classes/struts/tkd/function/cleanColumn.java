package struts.tkd.function;

import org.apache.commons.lang.StringUtils;

public class cleanColumn {
	public static String execute(String colvalue)
	{
		String colresult = "";
		
		if(colvalue == null)
			return null;
		if(colvalue.equals(""))
			return new String("");
		
		colresult = colvalue.trim();
		colresult = StringUtils.strip(colresult, "\u3000");
		return colresult;
	}
}
