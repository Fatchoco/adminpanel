package struts.pmi.form.process;


import org.apache.struts.upload.FormFile;
import java.io.*;

//levin17lacustre 24092009

public class DeleteFile  {

	public void deleteStat(String path)
	throws Exception {

		String fileTobeDeleted=path;
		File f=new File(fileTobeDeleted);
		//delete file in the path
		boolean flagDelete=f.delete();
	}

}