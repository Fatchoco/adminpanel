package struts.pmi.function;


//levin17lacustre 06102009
public class IsDouble {

	public boolean execute(String num)
			throws Exception {

	      	try{

	          Double.parseDouble(num);

	          } catch(NumberFormatException nfe) {

	          return true;

	          }

	          return false;
	}

}