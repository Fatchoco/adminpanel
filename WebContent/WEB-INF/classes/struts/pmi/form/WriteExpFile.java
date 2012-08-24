package struts.pmi.form;



import java.io.*;
import java.util.*;



public class WriteExpFile  {

	public void writeExp(String expPath,ArrayList messageCode,String Header)
	throws Exception {


		String writeFilePath=expPath;
		PrintWriter pw = new PrintWriter(new FileOutputStream(writeFilePath));
		pw.println(Header);
		//write message code to file
		for(int j=0;j<messageCode.size();j++)
		{

		    pw.println((String)(messageCode.get(j)));
		}
		    pw.close();
		
	}

}