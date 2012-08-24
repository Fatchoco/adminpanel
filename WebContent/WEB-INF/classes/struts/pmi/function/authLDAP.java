package struts.pmi.function;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class authLDAP {

	public int authenticate(String ldap_host, int ldap_port, String ldap_dn, 
			String ldap_uid, String strUname, String strPwd, String strOU ) 
		throws Exception {
		
		int errCode = 0;
		
		try 
		{
			ldap_dn = ldap_uid + "=" + strUname + "," + strOU + "," + ldap_dn;
			Hashtable<String, String> env = new Hashtable<String, String>(11);
			env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, "ldap://"+ldap_host+":"+ldap_port+"/");
		
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, ldap_dn);
			
			env.put(Context.SECURITY_CREDENTIALS, strPwd);
			DirContext ctx = new InitialDirContext(env);
			ctx.close();
			errCode=0;
		}  
		catch (Exception e) 
		{						
			try{
				String[] checkErr = e.toString().split("LDAP: error code");
				if(checkErr[1] != null)
				{
					String[] checkErr2 = checkErr[1].split("-");
					if(checkErr2[0] != null)
					{
						int checkErr3 = Integer.parseInt(checkErr2[0].trim());
						errCode = checkErr3;
					}
				}
			}
			catch(Exception er)
			{
				errCode = 32;
			}
		}
		
		return errCode;
	};
	
	public String errMessage(int errCode) 
		throws Exception {
		String msg = "";
		
		switch (errCode) {
        	case 0:  msg = "success"; break;
        	case 2:	 msg = "protocol error"; break;
        	case 3:  msg = "time limit exceeded"; break;
        	case 19: msg = "user account locked"; break;
        	case 32: msg = "user not registered on ldap"; break;
        	case 34: msg = "invalid DN syntax"; break;
        	case 48: msg = "inappropriate authentication"; break;
        	case 49: msg = "invalid credentials"; break;
        	case 50: msg = "insufficient access rights"; break;
        	case 51: msg = "service busy"; break;
        	case 52: msg = "service unavailable"; break;
        	case 53: msg = "inactive user"; break;
        	default: msg = "general error"; break;
		};
		
		System.out.println (errCode);

		return msg;
	}
	

}