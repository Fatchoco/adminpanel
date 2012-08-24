package struts.tkd.function;

import javax.servlet.http.HttpServletRequest;
import java.security.*;



public class userSessionCheck {
	public static int isvalid(HttpServletRequest request,String formName, String AppName)
	{
		int result=0;
		if (request.getSession().getAttribute("APP_ACCESS") != null && request.getSession().getAttribute("FORM_ACCESS")!= null  )
		{
			if ( request.getSession().getAttribute("IS_ADMIN").toString().equals("1"))
			{
				result = 1;
			} else
			{
				String  appAccess ="";
				String  formAccess ="";
				appAccess = request.getSession().getAttribute("APP_ACCESS").toString() ;
				formAccess= request.getSession().getAttribute("FORM_ACCESS").toString() ;

				if (appAccess.contains(AppName) &&	formAccess.contains(formName))
				{
					return 1;
				}


			}

		}
		return result;	

	}

	public static int isLogin(HttpServletRequest request)
	{

		if (request.getSession().getAttribute("USER_NAME") == null   )
		{
			return 0;

		}
		return 1;

	}

	public static String EncryptMD5(String plainText)
	{
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");

			md.reset();
			md.update(plainText.getBytes());

			byte[] digest = md.digest();
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < digest.length; i++) 
			{
				plainText = Integer.toHexString(0xFF & digest[i]);
				if (plainText.length() < 2) 
				{
					plainText = '0' + plainText;
				}
				hexString.append(plainText);	
			}	
			return hexString.toString(); 
		} catch (Exception e) {
			return "";
		}
	}
}
