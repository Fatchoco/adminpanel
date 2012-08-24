package struts.tkd.function;

public class cleanStringNumeric {
	public static String execute(String colvalue)
	{
		String colresult = "";
		
		if(colvalue == null)
			return null;
		if(colvalue.equals(""))
			return new String("");
		if(colvalue.indexOf(".") > 0)
		{
			colresult = colvalue.substring(0, colvalue.indexOf("."));
		}
		else
		{	
			colresult = colvalue.trim();
		}
		
		return colresult;
	}
	public static String remove00Precision(String numstr)
	{
		if(numstr.matches("((-|\\+)?[0-9]+(\\.[0]+)+)+"))
			return numstr.substring(0, numstr.lastIndexOf('.'));
		else
			return numstr;
	}
}
