package struts.tkd.function;

import java.io.*;

public class getPath {
	public static String execute(String oriPath)
	{
		String resultPath = oriPath;
		if(resultPath.indexOf(resultPath.length()-1)!='/' && resultPath.indexOf(resultPath.length()-1)!='\\')
		{
			File dummy = new File(oriPath);
			resultPath = resultPath+dummy.separator;
		}
		
		return resultPath;
	}
}
