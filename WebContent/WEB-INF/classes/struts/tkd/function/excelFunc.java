package struts.tkd.function;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

import org.apache.struts.upload.FormFile;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class excelFunc {
	public static Vector getExcelContent(String filePath, String fileName) throws Exception
	{
		Vector dataHolder = null;
		Vector cellVectorHolder = new Vector();
		
		InputStream myInput = new FileInputStream(filePath+fileName);
		char xlsTypeFlag = 'X';
		XSSFWorkbook myWorkBookX = null;
		HSSFWorkbook myWorkBookH = null;
		
		try
		{
			myWorkBookX = new XSSFWorkbook(myInput);
		}
		catch(Exception wbex)
		{
			try
			{
				myInput = new FileInputStream(filePath+fileName);
				myWorkBookH = new HSSFWorkbook(myInput);
				xlsTypeFlag = 'H';
			}
			catch(Exception wbeh)
			{
				throw new Exception("Invalid file type : "+wbeh.getMessage()+"::"+filePath+fileName);
			}
			//throw new Exception(wbex.getMessage());
		}
		Sheet mySheet;
		if(xlsTypeFlag == 'X')
			mySheet = myWorkBookX.getSheetAt(0);
		else
			mySheet = myWorkBookH.getSheetAt(0);
		
        Iterator rowIter = mySheet.rowIterator();
        
        while(rowIter.hasNext())
        {
        	Row myRow = (Row) rowIter.next();
        	Iterator cellIter = myRow.cellIterator();
        	Vector cellStoreVector = new Vector();
        	int last_row = 0;
        	int curr_row = 0;
        	while (cellIter.hasNext())
        	{

        		Cell myCell = (Cell) cellIter.next();
        		last_row = curr_row;
        		curr_row = myCell.getColumnIndex();
        		if(cellStoreVector.size()==0 && curr_row != 0)
        		{
            		for(int i_add = 0; i_add < (curr_row); i_add++)
            		{
            			Cell dummyCell = null;
            			cellStoreVector.addElement(dummyCell);
            		}
        		}
        		for(int i_add = 0; i_add < (curr_row-last_row-1); i_add++)
        		{
        			Cell dummyCell = null;
        			cellStoreVector.addElement(dummyCell);
        		}
        		cellStoreVector.addElement(myCell);
        	}
        	cellVectorHolder.addElement(cellStoreVector);
        }
        dataHolder = cellVectorHolder;
    
        return dataHolder;
	}
	public static void saveExcelToServer(FormFile myFile, String filePath, String fileName) throws Exception
	{
		File fileToCreate = new File(filePath, fileName);
		FileOutputStream fileOutStr = new FileOutputStream(fileToCreate);
		fileOutStr.write(myFile.getFileData());
		fileOutStr.flush();
		fileOutStr.close();
	}
	public static String removeDistFileNamePeriod(String fileName)
	{
		String result = "";
		
		if(fileName.matches("(.*)_......\\.(.*)"))
			result = fileName.substring(0, fileName.lastIndexOf("_"));
		else
			result = fileName;
		
		return result;
	}
	public static boolean checkDistFileNameFormat(File file)
	{
		if(file.getName().matches("(.*)_......\\.(.*)"))
			return true;
		else
			return false;
	}
	public static boolean checkDistFileNameFormat(String file)
	{
		if(file.matches("(.*)_......\\.(.*)"))
			return true;
		else
			return false;
	}
	public static String allToString(Cell myCell)
	{
		String result = "";
		switch(myCell.getCellType())
		{
			case Cell.CELL_TYPE_NUMERIC:
				if(DateUtil.isCellDateFormatted(myCell))
					result = myCell.toString();
				else
				{
					Double tempNum = myCell.getNumericCellValue();
					NumberFormat f = NumberFormat.getInstance();
					f.setGroupingUsed(false);
					result = f.format(tempNum);
				}
				break;
			default:
				result = myCell.toString();
				break;
		}
		return result;
	}
}
