
package struts.pmi.function;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
 
public class IsDate
{

	String validDate="";
    public boolean  execute(String date,int flag)throws Exception 
    {
    	DateFormat df;
    	 DateFormat df2;
    	if(flag==1)
    	{
       df = new SimpleDateFormat("dd/MM/yyyy");
       df2 = new SimpleDateFormat("yyyyMMdd");
    	}
    	else
    	{
           df = new SimpleDateFormat("yyyyMM");
           df2 = new SimpleDateFormat("yyyyMM");   		
    	}
  
        try
        {
            Date today = df.parse(date); 
            validDate=df2.format(today);
            return true;
        } catch (ParseException e)
        {
            return false;
        }
    }
    public String getDate()
    {
    	return validDate;
    }
    public void setDate()
    {
    	validDate="";
    }
}