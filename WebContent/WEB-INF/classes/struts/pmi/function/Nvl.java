package struts.pmi.function;


//levin17lacustre 06102009
public class Nvl {

	public String execute(String value, String nullValue)
			throws Exception {


			    return (value == null) ? nullValue : value;

	}

}