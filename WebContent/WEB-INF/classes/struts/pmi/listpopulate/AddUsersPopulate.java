package struts.pmi.listpopulate;

import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.sql.*;
import java.util.*;


public class AddUsersPopulate extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward nextPage = null;
		nextPage = mapping.findForward("forward");
		String query="";
		String queryCount="";
		int totalRowPerPage = 20;
		String search="";
		String message="";


		int page=1;
		int start=0;
		int end=0;
		int count=-1;
		int tot_page=0;
		
		String ldap_host = "";
		int ldap_port = 0;
		String ldap_dn = "";
		String ldap_uid = "";
		String ldap_fullname = "";
		String strUname = "";
		String strOU = "";
		boolean isExists = false;
		String fullName = "";
		
		//initiate db connection
		 javax.sql.DataSource dataSource;
		 java.sql.Connection myConnection;
		 dataSource = getDataSource(request,"userDB");
		 myConnection = dataSource.getConnection();
		 Statement stmt = myConnection.createStatement();
		 ResultSet r;

	     	//check login stat
		
			if(!(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("username") != "" && !request.getSession().getAttribute("isAdmin").equals("0")))
			{
				ActionForward login=mapping.findForward("loginpage");
				return login;
				
			}			

				try
				{
					 if((request.getParameter("message") != null && request.getParameter("message") != ""))
					{
							message=(request.getParameter("message"));
							
						
					}

					 if((request.getParameter("Page") != null && request.getParameter("Page") != ""))
					{
						page=Integer.parseInt(request.getParameter("Page"));
						
					
					}
					 start=totalRowPerPage*(page-1);
					 end=totalRowPerPage*(page);
					 if((request.getParameter("Search") != null && request.getParameter("Search") != ""))
					{

						 //Get LDAP server settings
						 r = stmt.executeQuery ("select VARNAME, VALUE_ from pmi_appvariables where varname like 'LDAP_%'");

						 while(r.next())
						{
								//out.println(r.getString(1));
								if(r.getString(1).equals("LDAP_SERVER"))
								{
									ldap_host = r.getString(2);
								}
								else if(r.getString(1).equals("LDAP_PORT"))
								{
									ldap_port = Integer.parseInt(r.getString(2));
								}
								else if(r.getString(1).equals("LDAP_BASEDN"))
								{
									ldap_dn = r.getString(2);
								}	
								else if(r.getString(1).equals("LDAP_USER_IDENTIFIER"))
								{
									ldap_uid = r.getString(2);
								}	
								else if (r.getString(1).equals("LDAP_FULLNAME_IDENTIFIER"))
								{
									ldap_fullname = r.getString(2);
								}
						};
						 
						 
						if (!ldap_host.equals("") && ldap_port!=0 && !ldap_dn.equals("") && !ldap_uid.equals("") && !ldap_fullname.equals("")) {
							//TODO: add logic to search LDAP
							strUname = request.getParameter("Search"); 
							strOU = request.getParameter("ou");
							
							ldap_dn = ldap_uid + "=" + strUname + "," + strOU + "," + ldap_dn;
							

							// The search base is the level in the hierarchy
							// that our search will start at. Here was use ""
							// which indicates the very root of the directory.
							String base = ldap_dn;
							
							// LDAP filters are sort of like a WHERE clause. It
							// is constructed in a standard way based on LDAP
							// standards. The search here is a simple one that
							// says to return any entry with an objectclass value.
							// Since all entries must contain an objectclass, all
							// entries will be returned.
							String filter = "(objectclass=*)";
							// Here we set some connection properties for JNDI.
							Properties env = new Properties();
							// The Sun provider is the most widely used JNDI
							// provider and comes with Java 1.3+
							env.put(DirContext.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
							// The provider URL is an LDAP URL that tells JNDI
							// where it will need to connect to.
							//env.put(DirContext.PROVIDER_URL,"ldap://demo1:8088/");
							env.put(DirContext.PROVIDER_URL,"ldap://"+ldap_host+":"+ldap_port+"/");
							//env.put(Context.PROVIDER_URL, "ldap://10.20.195.58:389/");
							try {
								// Here we create a DirContext object using
								// the environment we setup above. This
								// object will be used to communicate with
								// the server.
								DirContext dc = new InitialDirContext(env);
								// Above we mentioned the filter and base.
								// Another important part of the search criteria
								// is the scope. There are three scopes: base (this
								// entry only), onelevel (the direct children of this
								// entry), and subtree (this entry and all of its
								// decendents in the tree). In JNDI, OBJECT_SCOPE
								// indicates a base search.
								SearchControls sc = new SearchControls();
								sc.setSearchScope(SearchControls.OBJECT_SCOPE);
								NamingEnumeration ne = null;
								// Here we actually perform the search.
								//System.out.println("start searching");
								ne = dc.search(base, filter, sc);
								// We cycle through the NamingEnumeration
								// that is returned by the search.
				
								while (ne.hasMore()) {
									//System.out.println("resullt return");
									// Retrieve the result as a SearchResult
									// and print it (not very pretty). There are
									// methods for extracting the attributes and
									// values without printing, as well.
									isExists = true;
									SearchResult sr = (SearchResult) ne.next();
									Attributes attr =sr.getAttributes();
									
									//out.println(sr.toString()+"\n");
									//out.print("asd"+attr.get("fullName").get());

									fullName = attr.get(ldap_fullname).get().toString();
									//fullName = attr.get("fullName").get().toString();

								}
								// Here we unbind from the LDAP server.
								dc.close();
								
								request.setAttribute("fullname",fullName);
								request.setAttribute("uid",strUname);
								request.setAttribute("ou",strOU);
								count = 1;
							} 
							catch (Exception nex) {
								// A number of exceptions are subclassed from
								// NamingException. In a real application you'd
								// probably want to handle many of them
								// differently.
								//out.println("Error: " + nex.toString());
								//message = "error LDAP: " + nex.getMessage();
								count = 0;
							}
							
						} else {
							message = "missing LDAP configuration";
						}
						
					}
					 
					
					//System.out.println(query);
//					r=stmt.executeQuery(queryCount);
//					r.next();
//					count=r.getInt(1);
//					tot_page=(int)Math.floor(count/totalRowPerPage);
//					if(count%totalRowPerPage>0)tot_page+=1;
					request.setAttribute("searchq",search);
					request.setAttribute("count",count );
//					request.setAttribute("tot_page",tot_page );	
//					request.setAttribute("page",page );	
//					r.close();
//					r = stmt.executeQuery(query);
//					Result result = ResultSupport.toResult(r);
//					request.setAttribute("listResult",result );	
					
					//r.close();
					stmt.close();
					myConnection.close();
				}
				catch(SQLException e)
				{
					System.out.println("ERROR : " + e );
					e.printStackTrace(System.out);
				}

				
				return new ActionForward(nextPage.getPath() + "?message=" + message ); 
	}
	

}
