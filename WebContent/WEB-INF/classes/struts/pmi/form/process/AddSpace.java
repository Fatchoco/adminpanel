package struts.pmi.form.process;

public class AddSpace {

	public String execute(String key, int start, int end)
	throws Exception {
    	String row = "";
    	for (int j= 1;j <= end-start-key.length(); j++){
    		row = row + " ";
    	};
		return row;
	}

}