package struts.pmi.form.process;



import java.io.*;
import java.util.*;

//levin17lacustre 06102009
public class ReadFCTxt  {
	ArrayList lines = new ArrayList();

	public String readFCStat(String filePath)
	throws Exception {

		int flagErrorRead=0;

    			

						
				BufferedReader input=new BufferedReader(new FileReader(filePath));
	
						
				String line="";				
				int i=0;
				while ((line = input.readLine()) != null) {
				   if(line.length()!=0)
				   {
				   		if(line.length()<438||line.length()>438)
						{
							flagErrorRead=1;
							break;
						}
				   		if(!(line.trim().equals("")))
				   		{
				   		lines.add(i,line);
				   		i++;
				   		}
				   }
				}
				input.close();


				if(flagErrorRead==1)
				{
					//delete temp file
					DeleteFile del=new DeleteFile();
					del.deleteStat(filePath);
					return "Error : Invalid file format";
				}
				else if(i==0)
				{
					//delete temp file
					DeleteFile del=new DeleteFile();
					del.deleteStat(filePath);
					return "Error : File contains no line";
				}
				else
				{
					return "";
				}
	}
	public ArrayList getLines() {
		return lines;
	}


}