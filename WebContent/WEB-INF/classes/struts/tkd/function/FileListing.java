package struts.tkd.function;

import java.io.*;
import java.util.*;

/**
* Recursive file listing under a specified directory.
*  
* @author javapractices.com
* @author Alex Wong
* @author anonymous user
*/

public class FileListing {
	static public List<File> getFileListing(File aStartingDir) throws FileNotFoundException
	{
		validateDirectory(aStartingDir);
		List<File> result = getFileListingNoSort(aStartingDir);
		Collections.sort(result);
		return result;
	}

	// PRIVATE //
	static private List<File> getFileListingNoSort(File aStartingDir) throws FileNotFoundException
	{
		List<File> result = new ArrayList<File>();
		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		for(File file : filesDirs)
		{
			result.add(file); //always add, even if directory
			if ( ! file.isFile() )
			{
				//must be a directory
				//recursive call!
				List<File> deeperList = getFileListingNoSort(file);
				result.addAll(deeperList);
			}
		}
		return result;
	}

	/**
	* Directory is valid if it exists, does not represent a file, and can be read.
	*/
	static private void validateDirectory (File aDirectory) throws FileNotFoundException {
		if (aDirectory == null) {
		  throw new IllegalArgumentException("Directory should not be null.");
		}
		if (!aDirectory.exists()) {
		  throw new FileNotFoundException("Directory does not exist: " + aDirectory);
		}
		if (!aDirectory.isDirectory()) {
		  throw new IllegalArgumentException("Is not a directory: " + aDirectory);
		}
		if (!aDirectory.canRead()) {
		  throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
		}
	}
	public static boolean removeDirectory(File directory) {
		if (directory == null)
			return false;
		if (!directory.exists())
			return true;
		if (!directory.isDirectory())
			return false;

		String[] list = directory.list();

		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				File entry = new File(directory, list[i]);
				if (entry.isDirectory())
				{
					if (!removeDirectory(entry))
						return false;
				}
				else
				{
					if (!entry.delete())
						return false;
				}
			}
		}
		return directory.delete();
	}
}
