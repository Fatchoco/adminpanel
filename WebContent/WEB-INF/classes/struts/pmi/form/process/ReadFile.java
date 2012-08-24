package struts.pmi.form.process;


import org.apache.struts.upload.FormFile;
import java.io.*;


//levin17lacustre 24092009
public class ReadFile  {

	public String readStat(FormFile myFile,String path,String fileName)
	throws Exception {


        // Process the FormFile
        String contentType = myFile.getContentType();
        int fileSize       = myFile.getFileSize();
        byte[] fileData    = myFile.getFileData();
	  //  System.out.println("contentType: " + contentType);
	   // System.out.println("File Size: " + fileSize);
	if(!(contentType.equals("text/plain")))
	{
		return "Incorrect File Type";
	}
	if(fileName==null||fileName.equals(""))
	{
		return "You are not uploading any file";
	}
    try
    {
    OutputStream fileOutput = new FileOutputStream(path);
    fileOutput.write(fileData);
    fileOutput.flush();
    fileOutput.close(); 
    }
    catch (FileNotFoundException e) {
    	e.printStackTrace();
    	} catch (IOException e) {
    	e.printStackTrace();
    	} 


		return "";
	}

}