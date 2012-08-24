package struts.pmi.form.process;



import java.io.*;
import java.util.*;

//levin17lacustre 24092009
public class ReadTabTxt  {
	ArrayList lines = new ArrayList();
	String Header="";
	public String readTabStat(String filePath,int fieldNum, String HeaderValid)
	throws Exception {
		int addTab=0;
		String setTab="";
		String partition="";
		int flagErrorRead=0;

    			

						
				BufferedReader input=new BufferedReader(new FileReader(filePath));
				BufferedReader checkNewLine=new BufferedReader(new FileReader(filePath));
				BufferedReader noLine=new BufferedReader(new FileReader(filePath));
				
				int thereIsNewLine=0;
						
				String line="";		
				long lineCounter=0;
				
				//check new line(blank line)
				while((line=checkNewLine.readLine())!=null)
				{

					if(line.length()<1||(line.trim()).length()==0)
					{
						thereIsNewLine=1;
						break;
					}
					else
						thereIsNewLine=0;
					
					
				}
				checkNewLine.close();
				if(thereIsNewLine==0)
				{
					while((line=noLine.readLine())!=null)
					{
						lineCounter++;
						
						//for header
						if(lineCounter==1)
						{
							if(line.split("\t",fieldNum).length!=fieldNum)
							{
							    //System.out.println("number field " + fieldNum);
							    //System.out.println("split length " + line.split("\t",fieldNum).length);
								flagErrorRead=1;
								break;
							}
							else if(!((line.trim().toUpperCase()).equals(HeaderValid.toUpperCase())))
							{
								flagErrorRead=2;
								break;
							}
							Header=line;
						}
						//for content
						else
						{
							String[] temp=new String[fieldNum];
							temp=line.split("\t",fieldNum);
							//if tab count not match field num
							if(temp.length!=fieldNum)
							{
								setTab="";
								addTab=fieldNum-temp.length;
								for(int i=0;i<addTab;i++)
								{
								setTab+="\t ";
								}
								lines.add(line+setTab);							
								//out.println("Error : Invalid file, unmatch between header and body1.<br>");
								//out.println("Click <a href='"+address+"'>here</a> to try again");
								//return;
							}
							else
							{												
							lines.add(line);
							}

						}							
					}
		
				}
				else if(thereIsNewLine==1)
				{			
					while((line=input.readLine())!=null)
					{
						lineCounter++;
			
						if((line.trim()).length()>0)
						{
							//for header
							if(lineCounter==1)
							{
								if(line.split("\t",fieldNum).length!=fieldNum)
								{
									flagErrorRead=1;
									break;
								}
								else if(!((line.trim().toUpperCase()).equals(HeaderValid.toUpperCase())))
								{
									flagErrorRead=2;
									break;
								}
								Header=line;
							}
							//for content
							else
							{
								if(lineCounter>=2)
								{
									String[] temp=new String[fieldNum];
									temp=line.split("\t",fieldNum);
									//if tab count not match field num
									if(temp.length!=fieldNum)
									{

										//out.println("Error : Invalid file, unmatch between header and body.<br>");
										//out.println("Click <a href='"+address+"'>here</a> to try again");
										//return;
										setTab="";
										addTab=fieldNum-temp.length;
										for(int i=0;i<addTab;i++)
										{
										setTab+="\t ";
										}
										lines.add(line+setTab);	
									}
									else
									{																				
									lines.add(line);
									}
								}					
							}
						}
					}				
				}
				noLine.close();
				input.close();
				//append header line with error column
				Header+="\t:Error";
				if(flagErrorRead==1)
				{
					//delete temp file
					DeleteFile del=new DeleteFile();
					del.deleteStat(filePath);
					return "Error : Invalid file format";
				}
				else if(flagErrorRead==2)
				{
					//delete temp file
					DeleteFile del=new DeleteFile();
					del.deleteStat(filePath);
					return "Error : Invalid Header Line";
				}
				else
				{
					return "";
				}
	}
	public ArrayList getLines() {
		return lines;
	}
	public String getHeader() {
		return Header;
	}

}