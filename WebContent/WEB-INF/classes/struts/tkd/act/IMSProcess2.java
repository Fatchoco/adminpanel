package struts.tkd.act;


import java.io.*;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sun.java.swing.plaf.windows.resources.windows;

import struts.tkd.function.FileListing;
import struts.tkd.function.UISummaryFuncIMS;
import struts.tkd.function.getPath;
import struts.tkd.actfrm.ActionFormValidated;

public class IMSProcess2 implements Runnable {
    long minPrime;
    DataSource pDataSource;
    DataSource pDataSource2;
    String pzipFileTrg;
    String pusr;
    IMSProcess2(long minPrime,DataSource pDataSource,DataSource pDataSource2,String pzipFileTrg,String pusr) {
        this.minPrime = minPrime;
        this.pDataSource = pDataSource;
        this.pDataSource2 = pDataSource2;
        this.pzipFileTrg = pzipFileTrg;
        this.pusr = pusr;
    }

    public void run() {
        // compute primes larger than minPrime
    	String current_period;
    	String sfp;
    	String fcountry;
		String ncountry;
		String process=null;
    	try
		{
    		// Create DB Connection
			java.sql.Connection		myConnection;
			myConnection	= pDataSource.getConnection();
			Statement stmt	= myConnection.createStatement();
			ResultSet rset;
			
			rset = stmt.executeQuery("SELECT COUNTRY_CODE, COUNTRY_DES, PERIOD, SOURCE_FILE_PATH from PRO_STATUS where STATUS='INITIAL'");
			rset.next();
			current_period=rset.getString("PERIOD");
			sfp=rset.getString("SOURCE_FILE_PATH");
			fcountry=rset.getString("COUNTRY_CODE");
			ncountry=rset.getString("COUNTRY_DES");
			// Close DB Connection
			rset.close();
			stmt.close();
			myConnection.close();
			
			//Get Server Realpath
			String filePath = sfp;
			String temp    = "";
			String r_input = "";
			String r_error = "";
			String zipFileSrc = filePath + "INPUTFILE_STG_SET_" + ncountry + "_" + current_period;
			String zipFileTrg = pzipFileTrg;
			try
			{
				try{
				//Process p = Runtime.getRuntime().exec("C:\\Program Files\\7-Zip\\7z.exe x \""+zipFileSrc+"\" -o\""+zipFileTrg+"\" -y");
				//Process p = Runtime.getRuntime().exec("/home/oracle/Erico/7za x "+zipFileSrc+" -o"+zipFileTrg+" -y");
				Process p = Runtime.getRuntime().exec("unzip -o "+zipFileSrc+" -d "+zipFileTrg+"");
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				while((temp = stdInput.readLine())!=null)
					r_input += temp;
				while((temp = stdError.readLine())!=null)
					r_error += temp;
				p.waitFor();
				}catch(Exception ext){
					System.out.println(ext.getMessage());
					myConnection	= pDataSource.getConnection();
					Statement stmt2	= myConnection.createStatement();
					stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('FAILED','Extracting Error')");
					stmt2.execute("COMMIT");
					stmt2.close();
					myConnection.close();
				}
				String check_atc="";
				String check_city="";
				String check_manu="";
				String check_mole="";
				String check_newf="";
				String check_pack="";
				String check_packmole="";
				String check_tran="";
				String check_catc="";
				String check_cmanu="";
				String check_cmole="";
				String check_cnewf="";
				String check_cpack="";
				String check_cpackmole="";
				String check_ctran="";
				
				try
	    		{
					File startingDirectory= new File(zipFileTrg);
				    List<File> files = FileListing.getFileListing(startingDirectory);
				    for(File file:files)
				    {
				    	//Only process File
				    	if(file.isFile())
				    	{
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"CITIES_STG_ATC_"+ncountry+"_"+current_period+".TXT"))	check_atc=file.toString();	
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"CITIES_STG_CITY_"+ncountry+"_"+current_period+".TXT"))	check_city=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"CITIES_STG_MANUFACTURER_"+ncountry+"_"+current_period+".TXT"))	check_manu=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"CITIES_STG_MOLECULE_"+ncountry+"_"+current_period+".TXT"))	check_mole=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"CITIES_STG_NEWFORM_"+ncountry+"_"+current_period+".TXT"))	check_newf=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"CITIES_STG_PACK_"+ncountry+"_"+current_period+".TXT"))	check_pack=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"CITIES_STG_PACKMOLE_"+ncountry+"_"+current_period+".TXT"))	check_packmole=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"CITIES_STG_TRANSACTION_"+ncountry+"_"+current_period+".TXT"))	check_tran=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"COUNTRY_STG_ATC_"+ncountry+"_"+current_period+".TXT"))	check_catc=file.toString().substring(74);	
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"COUNTRY_STG_MANUFACTURER_"+ncountry+"_"+current_period+".TXT"))	check_cmanu=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"COUNTRY_STG_MOLECULE_"+ncountry+"_"+current_period+".TXT"))	check_cmole=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"COUNTRY_STG_NEWFORM_"+ncountry+"_"+current_period+".TXT"))	check_cnewf=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"COUNTRY_STG_PACK_"+ncountry+"_"+current_period+".TXT"))	check_cpack=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"COUNTRY_STG_PACKMOLE_"+ncountry+"_"+current_period+".TXT"))	check_cpackmole=file.toString().substring(74);
				    			if(file.toString().equals(zipFileTrg+File.separatorChar+"COUNTRY_STG_TRANSACTION_"+ncountry+"_"+current_period+".TXT"))	check_ctran=file.toString().substring(74);
				    		}
				    	}
				    
				    String prog="yes";
				    if(check_catc.equals("")||check_cmanu.equals("")||check_cmole.equals("")||check_cnewf.equals("")||check_cpack.equals("")||check_cpackmole.equals("")||check_ctran.equals("")){
			    		prog="no";
			    		if(!(check_atc.equals("") || check_city.equals("") || check_manu.equals("") || check_mole.equals("") || check_newf.equals("") || check_pack.equals("") || check_packmole.equals("") || check_tran.equals(""))){
					    	prog="yes";
					    }
			    	}
				    if(check_atc.equals("") || check_city.equals("") || check_manu.equals("") || check_mole.equals("") || check_newf.equals("") || check_pack.equals("") || check_packmole.equals("") || check_tran.equals("")){
				    	prog="no";
				    }
				    
				    if(prog.equals("yes")){
				    	myConnection	= pDataSource.getConnection();
						CallableStatement cs = null;
				    	try
						{
				    		cs	= myConnection.prepareCall("{call pkg_tkd_ims_stg.clear_table}");
							cs.execute();
						}
						catch(Exception e_del_job)
						{
							myConnection	= pDataSource.getConnection();
							Statement stmt2	= myConnection.createStatement();
							stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('FAILED','Error Clearing Staging Database')");
							stmt2.execute("COMMIT");
							stmt2.close();
							myConnection.close();
							System.out.print(e_del_job.getMessage());
						}
						finally
						{
							if(cs!=null)cs.close();
							if(myConnection!=null)myConnection.close();
						}
						String fname;
						files = FileListing.getFileListing(startingDirectory);
					    for(File file:files)
					    {
					    	//Only process File
					    	if(file.isFile())
					    	{
					    		try
					    		{
					    			String[] nama=file.toString().split(File.separator); //linux
					    			//String[] nama=file.toString().split(File.separator+File.separator); //windows
					    			PreparedStatement pstmt = null;
					    			fname=nama[nama.length-1];
					    			if(nama[nama.length-1].substring(0,fname.length()-11).equals("CITIES_STG_CITY_"+ncountry)){
					    				FileInputStream fstream = new FileInputStream(file);
					    				DataInputStream in = new DataInputStream(fstream);
					    				BufferedReader br = new BufferedReader(new InputStreamReader(in));
					    			    String strLine;
					    			    String cod;
					    			    String des;
										myConnection	= pDataSource.getConnection();
										 while ((strLine = br.readLine()) != null)   {
											 if(strLine.charAt(0)!=''){
										    	if(strLine.charAt(1)==' '){
										String query = "insert into stg_city(city_cod,city_des,country_cod, created_user, source_file_name) " +
							            "values(?,?,?,?,?)";
										pstmt = myConnection.prepareStatement(query);
										String[] nums = strLine.split(",");
									    cod=nums[0].replace('"',' ').trim();
									    des=nums[1].replace('"',' ').trim();
									    pstmt.setObject(1, cod);
										 pstmt.setObject(2, des);
										 pstmt.setObject(3, fcountry);
										 pstmt.setObject(4, pusr);
										 pstmt.setObject(5, nama[nama.length-1]);
										 pstmt.executeUpdate();
										 pstmt.close();	 
										    	}}
										 }in.close();
										 br.close();
										 fstream.close(); 
										 myConnection.close();
										 myConnection	= pDataSource.getConnection();
										 Statement stmt2	= myConnection.createStatement();
										 stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('RUNNING','"+nama[nama.length-1]+"')");
										 stmt2.execute("COMMIT");
										 stmt2.close();
										 myConnection.close();
					    			}
					    			if(nama[nama.length-1].substring(0,fname.length()-11).equals("CITIES_STG_MOLECULE_"+ncountry)||nama[nama.length-1].substring(0,fname.length()-11).equals("COUNTRY_STG_MOLECULE_"+ncountry)){
					    				FileInputStream fstream = new FileInputStream(file);
					    				DataInputStream in = new DataInputStream(fstream);
					    				BufferedReader br = new BufferedReader(new InputStreamReader(in));
					    			    String strLine;
					    			    String cod;
					    			    String des;
										myConnection	= pDataSource.getConnection();
										 while ((strLine = br.readLine()) != null)   {
											 if(strLine.charAt(0)!=''){
										    	if(strLine.charAt(1)==' '){
										String query = "insert into stg_molecule(molecule_cod,molecule_des,country_cod, created_user, source_file_name) " +
							            "values(?,?,?,?,?)";
										pstmt = myConnection.prepareStatement(query);
										String[] nums = strLine.split("\",");
									    cod=nums[0].replace('"',' ').trim();
									    des=nums[1].replace('"',' ').trim();
									    pstmt.setObject(1, cod);
										 pstmt.setObject(2, des);
										 pstmt.setObject(3, fcountry);
										 pstmt.setObject(4, pusr);
										 pstmt.setObject(5, nama[nama.length-1]);
										 pstmt.executeUpdate();
										 pstmt.close();
					    			}}
										 }in.close(); br.close(); fstream.close();
										 myConnection.close();
										 myConnection	= pDataSource.getConnection();
										 Statement stmt2	= myConnection.createStatement();
										 stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('RUNNING','"+nama[nama.length-1]+"')");
										 stmt2.execute("COMMIT");
										 stmt2.close();
										 myConnection.close();
					    			}
					    			if(nama[nama.length-1].substring(0,fname.length()-11).equals("CITIES_STG_PACKMOLE_"+ncountry)||nama[nama.length-1].substring(0,fname.length()-11).equals("COUNTRY_STG_PACKMOLE_"+ncountry)){
					    				FileInputStream fstream = new FileInputStream(file);
					    				DataInputStream in = new DataInputStream(fstream);
					    				BufferedReader br = new BufferedReader(new InputStreamReader(in));
					    				String strLine;
					    			    String pack_cod=null;
					    			    String pack_des=null;
					    			    String mole_cod=null;
					    			    String mole_des=null;
										myConnection	= pDataSource.getConnection();
										 while ((strLine = br.readLine()) != null)   {
											 if(strLine.charAt(0)!=''){
												 String[] nums3 = strLine.split("\",");
												 if(nums3[1].charAt(1)==' ' && nums3[1].charAt(2)!=' '){
														String[] nums = strLine.split("\",");
													    pack_cod=nums[2].replace('"',' ').trim();
													    pack_des=nums[1].replace('"',' ').trim();
														}
												if(nums3[1].charAt(1)==' ' && nums3[1].charAt(2)==' '){
													String query = "insert into stg_brg_packmole(pack_cod,pack_des,molecule_cod,molecule_des,country_cod, created_user, source_file_name) " +
										            "values(?,?,?,?,?,?,?)";
													pstmt = myConnection.prepareStatement(query);
														String[] nums2 = strLine.split("\",");
														mole_cod=nums2[2].replace('"',' ').trim();
														mole_des=nums2[1].replace('"',' ').trim();
													    pstmt.setObject(1, pack_cod);
														pstmt.setObject(2, pack_des);
														pstmt.setObject(3, mole_cod);
														pstmt.setObject(4, mole_des);
														pstmt.setObject(5, fcountry);
														pstmt.setObject(6, pusr);
														 pstmt.setObject(7, nama[nama.length-1]);
														pstmt.executeUpdate();
														pstmt.close();
														}}
					    			}in.close(); br.close();fstream.close();
									 myConnection.close();
									 myConnection	= pDataSource.getConnection();
									 Statement stmt2	= myConnection.createStatement();
									 stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('RUNNING','"+nama[nama.length-1]+"')");
									 stmt2.execute("COMMIT");
									 stmt2.close();
									 myConnection.close();
					    			}
					    			if(nama[nama.length-1].substring(0,fname.length()-11).equals("CITIES_STG_MANUFACTURER_"+ncountry)||nama[nama.length-1].substring(0,fname.length()-11).equals("COUNTRY_STG_MANUFACTURER_"+ncountry)){
					    				FileInputStream fstream = new FileInputStream(file);
					    				DataInputStream in = new DataInputStream(fstream);
					    				BufferedReader br = new BufferedReader(new InputStreamReader(in));
					    				String strLine;
					    			    String manu_cod=null;
					    			    String manu_des=null;
					    			    String manut_cod=null;
					    			    String manut_des=null;
					    			    String corp_cod=null;
					    			    String corp_des=null;
										myConnection	= pDataSource.getConnection();
										 while ((strLine = br.readLine()) != null)   {
											 if(strLine.charAt(0)!=''){
											 	if(strLine.charAt(1)==' ' && strLine.charAt(2)!=' '){
													String[] nums = strLine.split("\",");
												    corp_des=nums[1].replace('"',' ').trim();
													}
												 if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)!=' '){
														String[] nums2 = strLine.split("\",");
													    manut_des=nums2[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' '){
													String query = "insert into stg_manufacturer(manufacturer_cod,manufacturer_des,manufacturer_type_cod,manufacturer_type_des,corporation_cod,corporation_des,country_cod, created_user, source_file_name) " +
										            "values(?,?,?,?,?,?,?,?,?)";
													pstmt = myConnection.prepareStatement(query);
														String[] nums3 = strLine.split("\",");
														manut_cod=nums3[3].replace('"',' ').trim();
														manu_des=nums3[1].replace('"',' ').trim();
													    manu_cod=nums3[4].replace('"',' ').trim();
													    corp_cod=nums3[2].replace('"',' ').trim();
													    pstmt.setObject(1, manu_cod);
														pstmt.setObject(2, manu_des);
														pstmt.setObject(3, manut_cod);
														pstmt.setObject(4, manut_des);
														pstmt.setObject(5, corp_cod);
														pstmt.setObject(6, corp_des);
														pstmt.setObject(7, fcountry);
														pstmt.setObject(8, pusr);
														 pstmt.setObject(9, nama[nama.length-1]);
														pstmt.executeUpdate();
														pstmt.close();
														}}
					    			}in.close(); br.close();fstream.close();myConnection.close();
					    			myConnection	= pDataSource.getConnection();
									 Statement stmt2	= myConnection.createStatement();
									 stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('RUNNING','"+nama[nama.length-1]+"')");
									 stmt2.execute("COMMIT");
									 stmt2.close();
									 myConnection.close();
					    		}
					    			if(nama[nama.length-1].substring(0,fname.length()-11).equals("CITIES_STG_NEWFORM_"+ncountry)||nama[nama.length-1].substring(0,fname.length()-11).equals("COUNTRY_STG_NEWFORM_"+ncountry)){
					    				FileInputStream fstream = new FileInputStream(file);
					    				DataInputStream in = new DataInputStream(fstream);
					    				BufferedReader br = new BufferedReader(new InputStreamReader(in));
					    				String strLine;
					    			    String app1_cod=null;
					    			    String app1_des=null;
					    			    String app2_cod=null;
					    			    String app2_des=null;
					    			    String app3_cod=null;
					    			    String app3_des=null;
										myConnection	= pDataSource.getConnection();
										 while ((strLine = br.readLine()) != null)   {
											 if(strLine.charAt(0)!=''){
											 	if(strLine.charAt(1)==' ' && strLine.charAt(2)!=' '){
													String[] nums = strLine.split("\",");
												    app1_des=nums[1].replace('"',' ').trim();
													}
												 if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)!=' '){
														String[] nums2 = strLine.split("\",");
													    app2_des=nums2[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' '){
													String query = "insert into stg_newform(app3_cod,app3_des,app2_cod,app2_des,app1_cod,app1_des,country_cod, created_user, source_file_name) " +
										            "values(?,?,?,?,?,?,?,?,?)";
													pstmt = myConnection.prepareStatement(query);
														String[] nums3 = strLine.split("\",");
														app1_cod=nums3[2].replace('"',' ').trim();
														app3_des=nums3[1].replace('"',' ').trim();
														app2_cod=nums3[3].replace('"',' ').trim();
														app3_cod=nums3[4].replace('"',' ').trim();
													    pstmt.setObject(1, app3_cod);
														pstmt.setObject(2, app3_des);
														pstmt.setObject(3, app2_cod);
														pstmt.setObject(4, app2_des);
														pstmt.setObject(5, app1_cod);
														pstmt.setObject(6, app1_des);
														pstmt.setObject(7, fcountry);
														pstmt.setObject(8, pusr);
														 pstmt.setObject(9, nama[nama.length-1]);
														pstmt.executeUpdate();
														pstmt.close();
														}}
					    			}in.close(); br.close();fstream.close();myConnection.close();
					    			myConnection	= pDataSource.getConnection();
									 Statement stmt2	= myConnection.createStatement();
									 stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('RUNNING','"+nama[nama.length-1]+"')");
									 stmt2.execute("COMMIT");
									 stmt2.close();
									 myConnection.close();
					    			}
					    			if(nama[nama.length-1].substring(0,fname.length()-11).equals("CITIES_STG_PACK_"+ncountry)||nama[nama.length-1].substring(0,fname.length()-11).equals("COUNTRY_STG_PACK_"+ncountry)){
					    				FileInputStream fstream = new FileInputStream(file);
					    				DataInputStream in = new DataInputStream(fstream);
					    				BufferedReader br = new BufferedReader(new InputStreamReader(in));
					    				String strLine;
					    			    String pack_cod=null;
					    			    String pack_des=null;
					    			    String molec_cod=null;
					    			    String molec_des=null;
					    			    String nmole=null;
					    			    int inmole=0;
					    			    String prod_cod=null;
					    			    String prod_des=null;
					    			    String prod_atc=null;
					    			    String gene_cod=null;
					    			    String gene_des=null;
					    			    String mark_cod=null;
					    			    String mark_des=null;
										myConnection	= pDataSource.getConnection();
										 while ((strLine = br.readLine()) != null)   {
											 if(strLine.charAt(0)!=''){
											 	if(strLine.charAt(1)==' ' && strLine.charAt(2)!=' '){
													String[] nums = strLine.split(",");
												    mark_des=nums[1].replace('"',' ').trim();
													}
												 if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)!=' '){
														String[] nums2 = strLine.split(",");
													    gene_des=nums2[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' ' && strLine.charAt(4)!=' '){
														String[] nums3 = strLine.split("\",");
														prod_atc=nums3[2].replace('"',' ').trim();
														prod_des=nums3[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' ' && strLine.charAt(4)==' ' && strLine.charAt(5)!=' '){
														String[] nums4 = strLine.split("\",");
														nmole=nums4[2].replace('"',' ').trim();
														inmole= Integer.parseInt(nmole);
														molec_des=nums4[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' ' && strLine.charAt(4)==' ' && strLine.charAt(5)==' '){
													String query = "insert into stg_pack(pack_cod,pack_des,molecomp_cod,molecomp_des,nmolecomp,product_cod,product_des,product_atc,generic_cod,generic_des,market_cod,market_des,country_cod, created_user, source_file_name) " +
										            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
													pstmt = myConnection.prepareStatement(query);
														String[] nums5 = strLine.split("\",");
														mark_cod=nums5[2].replace('"',' ').trim();
														pack_des=nums5[1].replace('"',' ').trim();
														gene_cod=nums5[3].replace('"',' ').trim();
														prod_cod=nums5[4].replace('"',' ').trim();
														molec_cod=nums5[5].replace('"',' ').trim();
														pack_cod=nums5[6].replace('"',' ').trim();
													    pstmt.setObject(1, pack_cod);
														pstmt.setObject(2, pack_des);
														pstmt.setObject(3, molec_cod);
														pstmt.setObject(4, molec_des);
														pstmt.setObject(5, inmole);
														pstmt.setObject(6, prod_cod);
														pstmt.setObject(7, prod_des);
														pstmt.setObject(8, prod_atc);
														pstmt.setObject(9, gene_cod);
														pstmt.setObject(10, gene_des);
														pstmt.setObject(11, mark_cod);
														pstmt.setObject(12, mark_des);
														pstmt.setObject(13, fcountry);
														pstmt.setObject(14, pusr);
														 pstmt.setObject(15, nama[nama.length-1]);
														pstmt.executeUpdate();
														pstmt.close();
														}}
					    			}in.close(); br.close();fstream.close();myConnection.close();
					    			myConnection	= pDataSource.getConnection();
									 Statement stmt2	= myConnection.createStatement();
									 stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('RUNNING','"+nama[nama.length-1]+"')");
									 stmt2.execute("COMMIT");
									 stmt2.close();
									 myConnection.close();
					    			}
					    			if(nama[nama.length-1].substring(0,fname.length()-11).equals("CITIES_STG_ATC_"+ncountry)||nama[nama.length-1].substring(0,fname.length()-11).equals("COUNTRY_STG_ATC_"+ncountry)){
					    				FileInputStream fstream = new FileInputStream(file);
					    				DataInputStream in = new DataInputStream(fstream);
					    				BufferedReader br = new BufferedReader(new InputStreamReader(in));
					    				String strLine;
					    			    String atc1_cod=null;
					    			    String atc1_des=null;
					    			    String atc2_cod=null;
					    			    String atc2_des=null;
					    			    String atc3_cod=null;
					    			    String atc3_des=null;
					    			    String atc4_cod=null;
					    			    String atc4_des=null;
										myConnection	= pDataSource.getConnection();
										 while ((strLine = br.readLine()) != null)   {
											 if(strLine.charAt(0)!=''){
											 	if(strLine.charAt(1)==' ' && strLine.charAt(2)!=' '){
													String[] nums = strLine.split("\",");
												    atc1_des=nums[1].replace('"',' ').trim();
													}
												 if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)!=' '){
														String[] nums2 = strLine.split("\",");
													    atc2_des=nums2[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' ' && strLine.charAt(4)!=' '){
														String[] nums3 = strLine.split("\",");
														atc3_des=nums3[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' ' && strLine.charAt(4)==' '){
													String query = "insert into stg_atc(atc4_cod,atc4_des,atc3_cod,atc3_des,atc2_cod,atc2_des,atc1_cod,atc1_des,country_cod, created_user, source_file_name) " +
										            "values(?,?,?,?,?,?,?,?,?,?,?)";
													pstmt = myConnection.prepareStatement(query);
														String[] nums4 = strLine.split("\",");
														atc1_cod=nums4[2].replace('"',' ').trim();
														atc4_des=nums4[1].replace('"',' ').trim();
														atc2_cod=nums4[3].replace('"',' ').trim();
														atc3_cod=nums4[4].replace('"',' ').trim();
														atc4_cod=nums4[5].replace('"',' ').trim();
													    pstmt.setObject(1, atc4_cod);
														pstmt.setObject(2, atc4_des);
														pstmt.setObject(3, atc3_cod);
														pstmt.setObject(4, atc3_des);
														pstmt.setObject(5, atc2_cod);
														pstmt.setObject(6, atc2_des);
														pstmt.setObject(7, atc1_cod);
														pstmt.setObject(8, atc1_des);
														pstmt.setObject(9, fcountry);
														pstmt.setObject(10, pusr);
														 pstmt.setObject(11, nama[nama.length-1]);
														pstmt.executeUpdate();
														pstmt.close();
														}}
					    			}in.close(); br.close();fstream.close();myConnection.close();
					    			myConnection	= pDataSource.getConnection();
									 Statement stmt2	= myConnection.createStatement();
									 stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('RUNNING','"+nama[nama.length-1]+"')");
									 stmt2.execute("COMMIT");
									 stmt2.close();
									 myConnection.close();
					    			}
					    			if(nama[nama.length-1].substring(0,fname.length()-11).equals("CITIES_STG_TRANSACTION_"+ncountry)){
					    				FileInputStream fstream = new FileInputStream(file);
					    				DataInputStream in = new DataInputStream(fstream);
					    				BufferedReader br = new BufferedReader(new InputStreamReader(in));
					    				String strLine;
					    			    String city_cod=null;
					    			    String city_des=null;
					    			    String manu_cod=null;
					    			    String manu_des=null;
					    			    String atc4_cod=null;
					    			    String atc4_des=null;
					    			    String app3_cod=null;
					    			    String app3_des=null;
					    			    String pack_cod=null;
					    			    String pack_des=null;
					    			    Long q00du;Long q00lc;Long q00cu;Long q00un;Long q00us; Long q01du;Long q01lc;Long q01cu;Long q01un;Long q01us;
					    			    Long q02du;Long q02lc;Long q02cu;Long q02un;Long q02us; Long q03du;Long q03lc;Long q03cu;Long q03un;Long q03us;
					    			    Long q04du;Long q04lc;Long q04cu;Long q04un;Long q04us; Long q05du;Long q05lc;Long q05cu;Long q05un;Long q05us;
					    			    Long q06du;Long q06lc;Long q06cu;Long q06un;Long q06us; Long q07du;Long q07lc;Long q07cu;Long q07un;Long q07us;
					    			    Long q08du;Long q08lc;Long q08cu;Long q08un;Long q08us; Long q09du;Long q09lc;Long q09cu;Long q09un;Long q09us;
					    			    Long q10du;Long q10lc;Long q10cu;Long q10un;Long q10us; Long q11du;Long q11lc;Long q11cu;Long q11un;Long q11us;
					    			    Long q12du;Long q12lc;Long q12cu;Long q12un;Long q12us; Long q13du;Long q13lc;Long q13cu;Long q13un;Long q13us;
					    			    Long q14du;Long q14lc;Long q14cu;Long q14un;Long q14us; Long q15du;Long q15lc;Long q15cu;Long q15un;Long q15us;
					    			    Long q16du;Long q16lc;Long q16cu;Long q16un;Long q16us; Long q17du;Long q17lc;Long q17cu;Long q17un;Long q17us;
					    			    Long q18du;Long q18lc;Long q18cu;Long q18un;Long q18us; Long q19du;Long q19lc;Long q19cu;Long q19un;Long q19us;
										myConnection	= pDataSource.getConnection();
										 while ((strLine = br.readLine()) != null)   {
											 if(strLine.charAt(0)!=''){
											 	if(strLine.charAt(1)==' ' && strLine.charAt(2)!=' '){
													String[] nums = strLine.split(",");
												    city_des=nums[1].replace('"',' ').trim();
												    city_cod=nums[2].replace('"',' ').trim();
													}
												 if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)!=' '){
														String[] nums2 = strLine.split(",");
													    manu_des=nums2[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' ' && strLine.charAt(4)!=' '){
														String[] nums3 = strLine.split(",");
														atc4_des=nums3[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' ' && strLine.charAt(4)==' ' && strLine.charAt(5)!=' '){
														String[] nums4 = strLine.split(",");
														app3_des=nums4[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' ' && strLine.charAt(4)==' ' && strLine.charAt(5)==' '){
													String query = "insert into stg_transaction(city_cod,city_des,manufacturer_cod,manufacturer_des,atc4_cod,atc4_des,app3_cod,app3_des,pack_cod,pack_des," +
															"qtr19du,qtr19lc,qtr19cu,qtr19un,qtr19us,qtr18du,qtr18lc,qtr18cu,qtr18un,qtr18us,qtr17du,qtr17lc,qtr17cu,qtr17un,qtr17us,qtr16du,qtr16lc,qtr16cu,qtr16un,qtr16us,qtr15du,qtr15lc,qtr15cu,qtr15un,qtr15us," +
															"qtr14du,qtr14lc,qtr14cu,qtr14un,qtr14us,qtr13du,qtr13lc,qtr13cu,qtr13un,qtr13us,qtr12du,qtr12lc,qtr12cu,qtr12un,qtr12us,qtr11du,qtr11lc,qtr11cu,qtr11un,qtr11us,qtr10du,qtr10lc,qtr10cu,qtr10un,qtr10us," +
															"qtr09du,qtr09lc,qtr09cu,qtr09un,qtr09us,qtr08du,qtr08lc,qtr08cu,qtr08un,qtr08us,qtr07du,qtr07lc,qtr07cu,qtr07un,qtr07us,qtr06du,qtr06lc,qtr06cu,qtr06un,qtr06us,qtr05du,qtr05lc,qtr05cu,qtr05un,qtr05us," +
															"qtr04du,qtr04lc,qtr04cu,qtr04un,qtr04us,qtr03du,qtr03lc,qtr03cu,qtr03un,qtr03us,qtr02du,qtr02lc,qtr02cu,qtr02un,qtr02us,qtr01du,qtr01lc,qtr01cu,qtr01un,qtr01us,qtr00du,qtr00lc,qtr00cu,qtr00un,qtr00us,country_cod, created_user, source_file_name) " +
										            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
													pstmt = myConnection.prepareStatement(query);
														String[] nums5 = strLine.split(",");
														if(nums5[2].charAt(0)!='"'){
															manu_cod=nums5[3].replace('"',' ').trim();
															pack_des=nums5[1].replace('"',' ').trim()+", "+nums5[2].replace('"',' ').trim();
															atc4_cod=nums5[4].replace('"',' ').trim();
															app3_cod=nums5[5].replace('"',' ').trim();
															pack_cod=nums5[6].replace('"',' ').trim();
															q00du=Long.parseLong(nums5[7]);q00lc=Long.parseLong(nums5[8]);q00cu=Long.parseLong(nums5[9]);q00un=Long.parseLong(nums5[10]);q00us=Long.parseLong(nums5[11]);
															q01du=Long.parseLong(nums5[12]);q01lc=Long.parseLong(nums5[13]);q01cu=Long.parseLong(nums5[14]);q01un=Long.parseLong(nums5[15]);q01us=Long.parseLong(nums5[16]);
															q02du=Long.parseLong(nums5[17]);q02lc=Long.parseLong(nums5[18]);q02cu=Long.parseLong(nums5[19]);q02un=Long.parseLong(nums5[20]);q02us=Long.parseLong(nums5[21]);
															q03du=Long.parseLong(nums5[22]);q03lc=Long.parseLong(nums5[23]);q03cu=Long.parseLong(nums5[24]);q03un=Long.parseLong(nums5[25]);q03us=Long.parseLong(nums5[26]);
															q04du=Long.parseLong(nums5[27]);q04lc=Long.parseLong(nums5[28]);q04cu=Long.parseLong(nums5[29]);q04un=Long.parseLong(nums5[30]);q04us=Long.parseLong(nums5[31]);
															q05du=Long.parseLong(nums5[32]);q05lc=Long.parseLong(nums5[33]);q05cu=Long.parseLong(nums5[34]);q05un=Long.parseLong(nums5[35]);q05us=Long.parseLong(nums5[36]);
															q06du=Long.parseLong(nums5[37]);q06lc=Long.parseLong(nums5[38]);q06cu=Long.parseLong(nums5[39]);q06un=Long.parseLong(nums5[40]);q06us=Long.parseLong(nums5[41]);
															q07du=Long.parseLong(nums5[42]);q07lc=Long.parseLong(nums5[43]);q07cu=Long.parseLong(nums5[44]);q07un=Long.parseLong(nums5[45]);q07us=Long.parseLong(nums5[46]);
															q08du=Long.parseLong(nums5[47]);q08lc=Long.parseLong(nums5[48]);q08cu=Long.parseLong(nums5[49]);q08un=Long.parseLong(nums5[50]);q08us=Long.parseLong(nums5[51]);
															q09du=Long.parseLong(nums5[52]);q09lc=Long.parseLong(nums5[53]);q09cu=Long.parseLong(nums5[54]);q09un=Long.parseLong(nums5[55]);q09us=Long.parseLong(nums5[56]);
															q10du=Long.parseLong(nums5[57]);q10lc=Long.parseLong(nums5[58]);q10cu=Long.parseLong(nums5[59]);q10un=Long.parseLong(nums5[60]);q10us=Long.parseLong(nums5[61]);
															q11du=Long.parseLong(nums5[62]);q11lc=Long.parseLong(nums5[63]);q11cu=Long.parseLong(nums5[64]);q11un=Long.parseLong(nums5[65]);q11us=Long.parseLong(nums5[66]);
															q12du=Long.parseLong(nums5[67]);q12lc=Long.parseLong(nums5[68]);q12cu=Long.parseLong(nums5[69]);q12un=Long.parseLong(nums5[70]);q12us=Long.parseLong(nums5[71]);
															q13du=Long.parseLong(nums5[72]);q13lc=Long.parseLong(nums5[73]);q13cu=Long.parseLong(nums5[74]);q13un=Long.parseLong(nums5[75]);q13us=Long.parseLong(nums5[76]);
															q14du=Long.parseLong(nums5[77]);q14lc=Long.parseLong(nums5[78]);q14cu=Long.parseLong(nums5[79]);q14un=Long.parseLong(nums5[80]);q14us=Long.parseLong(nums5[81]);
															q15du=Long.parseLong(nums5[82]);q15lc=Long.parseLong(nums5[83]);q15cu=Long.parseLong(nums5[84]);q15un=Long.parseLong(nums5[85]);q15us=Long.parseLong(nums5[86]);
															q16du=Long.parseLong(nums5[87]);q16lc=Long.parseLong(nums5[88]);q16cu=Long.parseLong(nums5[89]);q16un=Long.parseLong(nums5[90]);q16us=Long.parseLong(nums5[91]);
															q17du=Long.parseLong(nums5[92]);q17lc=Long.parseLong(nums5[93]);q17cu=Long.parseLong(nums5[94]);q17un=Long.parseLong(nums5[95]);q17us=Long.parseLong(nums5[96]);
															q18du=Long.parseLong(nums5[97]);q18lc=Long.parseLong(nums5[98]);q18cu=Long.parseLong(nums5[99]);q18un=Long.parseLong(nums5[100]);q18us=Long.parseLong(nums5[101]);
															q19du=Long.parseLong(nums5[102]);q19lc=Long.parseLong(nums5[103]);q19cu=Long.parseLong(nums5[104]);q19un=Long.parseLong(nums5[105]);q19us=Long.parseLong(nums5[106]);	
														}
														else{
														manu_cod=nums5[2].replace('"',' ').trim();
														pack_des=nums5[1].replace('"',' ').trim();
														atc4_cod=nums5[3].replace('"',' ').trim();
														app3_cod=nums5[4].replace('"',' ').trim();
														pack_cod=nums5[5].replace('"',' ').trim();
														q00du=Long.parseLong(nums5[6]);q00lc=Long.parseLong(nums5[7]);q00cu=Long.parseLong(nums5[8]);q00un=Long.parseLong(nums5[9]);q00us=Long.parseLong(nums5[10]);
														q01du=Long.parseLong(nums5[11]);q01lc=Long.parseLong(nums5[12]);q01cu=Long.parseLong(nums5[13]);q01un=Long.parseLong(nums5[14]);q01us=Long.parseLong(nums5[15]);
														q02du=Long.parseLong(nums5[16]);q02lc=Long.parseLong(nums5[17]);q02cu=Long.parseLong(nums5[18]);q02un=Long.parseLong(nums5[19]);q02us=Long.parseLong(nums5[20]);
														q03du=Long.parseLong(nums5[21]);q03lc=Long.parseLong(nums5[22]);q03cu=Long.parseLong(nums5[23]);q03un=Long.parseLong(nums5[24]);q03us=Long.parseLong(nums5[25]);
														q04du=Long.parseLong(nums5[26]);q04lc=Long.parseLong(nums5[27]);q04cu=Long.parseLong(nums5[28]);q04un=Long.parseLong(nums5[29]);q04us=Long.parseLong(nums5[30]);
														q05du=Long.parseLong(nums5[31]);q05lc=Long.parseLong(nums5[32]);q05cu=Long.parseLong(nums5[33]);q05un=Long.parseLong(nums5[34]);q05us=Long.parseLong(nums5[35]);
														q06du=Long.parseLong(nums5[36]);q06lc=Long.parseLong(nums5[37]);q06cu=Long.parseLong(nums5[38]);q06un=Long.parseLong(nums5[39]);q06us=Long.parseLong(nums5[40]);
														q07du=Long.parseLong(nums5[41]);q07lc=Long.parseLong(nums5[42]);q07cu=Long.parseLong(nums5[43]);q07un=Long.parseLong(nums5[44]);q07us=Long.parseLong(nums5[45]);
														q08du=Long.parseLong(nums5[46]);q08lc=Long.parseLong(nums5[47]);q08cu=Long.parseLong(nums5[48]);q08un=Long.parseLong(nums5[49]);q08us=Long.parseLong(nums5[50]);
														q09du=Long.parseLong(nums5[51]);q09lc=Long.parseLong(nums5[52]);q09cu=Long.parseLong(nums5[53]);q09un=Long.parseLong(nums5[54]);q09us=Long.parseLong(nums5[55]);
														q10du=Long.parseLong(nums5[56]);q10lc=Long.parseLong(nums5[57]);q10cu=Long.parseLong(nums5[58]);q10un=Long.parseLong(nums5[59]);q10us=Long.parseLong(nums5[60]);
														q11du=Long.parseLong(nums5[61]);q11lc=Long.parseLong(nums5[62]);q11cu=Long.parseLong(nums5[63]);q11un=Long.parseLong(nums5[64]);q11us=Long.parseLong(nums5[65]);
														q12du=Long.parseLong(nums5[66]);q12lc=Long.parseLong(nums5[67]);q12cu=Long.parseLong(nums5[68]);q12un=Long.parseLong(nums5[69]);q12us=Long.parseLong(nums5[70]);
														q13du=Long.parseLong(nums5[71]);q13lc=Long.parseLong(nums5[72]);q13cu=Long.parseLong(nums5[73]);q13un=Long.parseLong(nums5[74]);q13us=Long.parseLong(nums5[75]);
														q14du=Long.parseLong(nums5[76]);q14lc=Long.parseLong(nums5[77]);q14cu=Long.parseLong(nums5[78]);q14un=Long.parseLong(nums5[79]);q14us=Long.parseLong(nums5[80]);
														q15du=Long.parseLong(nums5[81]);q15lc=Long.parseLong(nums5[82]);q15cu=Long.parseLong(nums5[83]);q15un=Long.parseLong(nums5[84]);q15us=Long.parseLong(nums5[85]);
														q16du=Long.parseLong(nums5[86]);q16lc=Long.parseLong(nums5[87]);q16cu=Long.parseLong(nums5[88]);q16un=Long.parseLong(nums5[89]);q16us=Long.parseLong(nums5[90]);
														q17du=Long.parseLong(nums5[91]);q17lc=Long.parseLong(nums5[92]);q17cu=Long.parseLong(nums5[93]);q17un=Long.parseLong(nums5[94]);q17us=Long.parseLong(nums5[95]);
														q18du=Long.parseLong(nums5[96]);q18lc=Long.parseLong(nums5[97]);q18cu=Long.parseLong(nums5[98]);q18un=Long.parseLong(nums5[99]);q18us=Long.parseLong(nums5[100]);
														q19du=Long.parseLong(nums5[101]);q19lc=Long.parseLong(nums5[102]);q19cu=Long.parseLong(nums5[103]);q19un=Long.parseLong(nums5[104]);q19us=Long.parseLong(nums5[105]);
														}
													    pstmt.setObject(1, city_cod);
														pstmt.setObject(2, city_des);
														pstmt.setObject(3, manu_cod);
														pstmt.setObject(4, manu_des);
														pstmt.setObject(5, atc4_cod);
														pstmt.setObject(6, atc4_des);
														pstmt.setObject(7, app3_cod);
														pstmt.setObject(8, app3_des);
														pstmt.setObject(9, pack_cod);
														pstmt.setObject(10, pack_des);
														pstmt.setObject(11, q00du);pstmt.setObject(12, q00lc);pstmt.setObject(13, q00cu);pstmt.setObject(14, q00un);pstmt.setObject(15, q00us);
														pstmt.setObject(16, q01du);pstmt.setObject(17, q01lc);pstmt.setObject(18, q01cu);pstmt.setObject(19, q01un);pstmt.setObject(20, q01us);
														pstmt.setObject(21, q02du);pstmt.setObject(22, q02lc);pstmt.setObject(23, q02cu);pstmt.setObject(24, q02un);pstmt.setObject(25, q02us);
														pstmt.setObject(26, q03du);pstmt.setObject(27, q03lc);pstmt.setObject(28, q03cu);pstmt.setObject(29, q03un);pstmt.setObject(30, q03us);
														pstmt.setObject(31, q04du);pstmt.setObject(32, q04lc);pstmt.setObject(33, q04cu);pstmt.setObject(34, q04un);pstmt.setObject(35, q04us);
														pstmt.setObject(36, q05du);pstmt.setObject(37, q05lc);pstmt.setObject(38, q05cu);pstmt.setObject(39, q05un);pstmt.setObject(40, q05us);
														pstmt.setObject(41, q06du);pstmt.setObject(42, q06lc);pstmt.setObject(43, q06cu);pstmt.setObject(44, q06un);pstmt.setObject(45, q06us);
														pstmt.setObject(46, q07du);pstmt.setObject(47, q07lc);pstmt.setObject(48, q07cu);pstmt.setObject(49, q07un);pstmt.setObject(50, q07us);
														pstmt.setObject(51, q08du);pstmt.setObject(52, q08lc);pstmt.setObject(53, q08cu);pstmt.setObject(54, q08un);pstmt.setObject(55, q08us);
														pstmt.setObject(56, q09du);pstmt.setObject(57, q09lc);pstmt.setObject(58, q09cu);pstmt.setObject(59, q09un);pstmt.setObject(60, q09us);
														pstmt.setObject(61, q10du);pstmt.setObject(62, q10lc);pstmt.setObject(63, q10cu);pstmt.setObject(64, q10un);pstmt.setObject(65, q10us);
														pstmt.setObject(66, q11du);pstmt.setObject(67, q11lc);pstmt.setObject(68, q11cu);pstmt.setObject(69, q11un);pstmt.setObject(70, q11us);
														pstmt.setObject(71, q12du);pstmt.setObject(72, q12lc);pstmt.setObject(73, q12cu);pstmt.setObject(74, q12un);pstmt.setObject(75, q12us);
														pstmt.setObject(76, q13du);pstmt.setObject(77, q13lc);pstmt.setObject(78, q13cu);pstmt.setObject(79, q13un);pstmt.setObject(80, q13us);
														pstmt.setObject(81, q14du);pstmt.setObject(82, q14lc);pstmt.setObject(83, q14cu);pstmt.setObject(84, q14un);pstmt.setObject(85, q14us);
														pstmt.setObject(86, q15du);pstmt.setObject(87, q15lc);pstmt.setObject(88, q15cu);pstmt.setObject(89, q15un);pstmt.setObject(90, q15us);
														pstmt.setObject(91, q16du);pstmt.setObject(92, q16lc);pstmt.setObject(93, q16cu);pstmt.setObject(94, q16un);pstmt.setObject(95, q16us);
														pstmt.setObject(96, q17du);pstmt.setObject(97, q17lc);pstmt.setObject(98, q17cu);pstmt.setObject(99, q17un);pstmt.setObject(100, q17us);
														pstmt.setObject(101, q18du);pstmt.setObject(102, q18lc);pstmt.setObject(103, q18cu);pstmt.setObject(104, q18un);pstmt.setObject(105, q18us);
														pstmt.setObject(106, q19du);pstmt.setObject(107, q19lc);pstmt.setObject(108, q19cu);pstmt.setObject(109, q19un);pstmt.setObject(110, q19us);
														pstmt.setObject(111, fcountry);
														pstmt.setObject(112, pusr);
														 pstmt.setObject(113, nama[nama.length-1]);
														pstmt.executeUpdate();
														pstmt.close();
														}}
					    			}in.close(); br.close();fstream.close();myConnection.close();
					    			myConnection	= pDataSource.getConnection();
									 Statement stmt2	= myConnection.createStatement();
									 stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('RUNNING','"+nama[nama.length-1]+"')");
									 stmt2.execute("COMMIT");
									 stmt2.close();
									 myConnection.close();
					    			}
					    			if(nama[nama.length-1].substring(0,fname.length()-11).equals("COUNTRY_STG_TRANSACTION_"+ncountry)){
					    				FileInputStream fstream = new FileInputStream(file);
					    				DataInputStream in = new DataInputStream(fstream);
					    				BufferedReader br = new BufferedReader(new InputStreamReader(in));
					    				String strLine;
					    			    String city_cod=null;
					    			    String city_des=null;
					    			    String manu_cod=null;
					    			    String manu_des=null;
					    			    String atc4_cod=null;
					    			    String atc4_des=null;
					    			    String app3_cod=null;
					    			    String app3_des=null;
					    			    String pack_cod=null;
					    			    String pack_des=null;
					    			    Long q00du;Long q00lc;Long q00cu;Long q00un;Long q00us; Long q01du;Long q01lc;Long q01cu;Long q01un;Long q01us;
					    			    Long q02du;Long q02lc;Long q02cu;Long q02un;Long q02us; Long q03du;Long q03lc;Long q03cu;Long q03un;Long q03us;
					    			    Long q04du;Long q04lc;Long q04cu;Long q04un;Long q04us; Long q05du;Long q05lc;Long q05cu;Long q05un;Long q05us;
					    			    Long q06du;Long q06lc;Long q06cu;Long q06un;Long q06us; Long q07du;Long q07lc;Long q07cu;Long q07un;Long q07us;
					    			    Long q08du;Long q08lc;Long q08cu;Long q08un;Long q08us; Long q09du;Long q09lc;Long q09cu;Long q09un;Long q09us;
					    			    Long q10du;Long q10lc;Long q10cu;Long q10un;Long q10us; Long q11du;Long q11lc;Long q11cu;Long q11un;Long q11us;
					    			    Long q12du;Long q12lc;Long q12cu;Long q12un;Long q12us; Long q13du;Long q13lc;Long q13cu;Long q13un;Long q13us;
					    			    Long q14du;Long q14lc;Long q14cu;Long q14un;Long q14us; Long q15du;Long q15lc;Long q15cu;Long q15un;Long q15us;
					    			    Long q16du;Long q16lc;Long q16cu;Long q16un;Long q16us; Long q17du;Long q17lc;Long q17cu;Long q17un;Long q17us;
					    			    Long q18du;Long q18lc;Long q18cu;Long q18un;Long q18us; Long q19du;Long q19lc;Long q19cu;Long q19un;Long q19us;
										myConnection	= pDataSource.getConnection();
										 while ((strLine = br.readLine()) != null)   {
											 if(strLine.charAt(0)!=''){
											 	if(strLine.charAt(1)==' ' && strLine.charAt(2)!=' '){
													String[] nums = strLine.split(",");
													manu_des=nums[1].replace('"',' ').trim();
													}
												 if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)!=' '){
														String[] nums2 = strLine.split(",");
														atc4_des=nums2[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' ' && strLine.charAt(4)!=' '){
														String[] nums3 = strLine.split(",");
														app3_des=nums3[1].replace('"',' ').trim();
														}
												if(strLine.charAt(1)==' ' && strLine.charAt(2)==' ' && strLine.charAt(3)==' ' && strLine.charAt(4)==' '){
													String query = "insert into stg_transaction(city_cod,city_des,manufacturer_cod,manufacturer_des,atc4_cod,atc4_des,app3_cod,app3_des,pack_cod,pack_des," +
															"qtr19du,qtr19lc,qtr19cu,qtr19un,qtr19us,qtr18du,qtr18lc,qtr18cu,qtr18un,qtr18us,qtr17du,qtr17lc,qtr17cu,qtr17un,qtr17us,qtr16du,qtr16lc,qtr16cu,qtr16un,qtr16us,qtr15du,qtr15lc,qtr15cu,qtr15un,qtr15us," +
															"qtr14du,qtr14lc,qtr14cu,qtr14un,qtr14us,qtr13du,qtr13lc,qtr13cu,qtr13un,qtr13us,qtr12du,qtr12lc,qtr12cu,qtr12un,qtr12us,qtr11du,qtr11lc,qtr11cu,qtr11un,qtr11us,qtr10du,qtr10lc,qtr10cu,qtr10un,qtr10us," +
															"qtr09du,qtr09lc,qtr09cu,qtr09un,qtr09us,qtr08du,qtr08lc,qtr08cu,qtr08un,qtr08us,qtr07du,qtr07lc,qtr07cu,qtr07un,qtr07us,qtr06du,qtr06lc,qtr06cu,qtr06un,qtr06us,qtr05du,qtr05lc,qtr05cu,qtr05un,qtr05us," +
															"qtr04du,qtr04lc,qtr04cu,qtr04un,qtr04us,qtr03du,qtr03lc,qtr03cu,qtr03un,qtr03us,qtr02du,qtr02lc,qtr02cu,qtr02un,qtr02us,qtr01du,qtr01lc,qtr01cu,qtr01un,qtr01us,qtr00du,qtr00lc,qtr00cu,qtr00un,qtr00us,country_cod, created_user, source_file_name) " +
								            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
											pstmt = myConnection.prepareStatement(query);
												String[] nums5 = strLine.split(",");
												if(nums5[2].charAt(0)!='"'){
													manu_cod=nums5[3].replace('"',' ').trim();
													pack_des=nums5[1].replace('"',' ').trim()+", "+nums5[2].replace('"',' ').trim();
													atc4_cod=nums5[4].replace('"',' ').trim();
													app3_cod=nums5[5].replace('"',' ').trim();
													pack_cod=nums5[6].replace('"',' ').trim();
													q00du=Long.parseLong(nums5[7]);q00lc=Long.parseLong(nums5[8]);q00cu=Long.parseLong(nums5[9]);q00un=Long.parseLong(nums5[10]);q00us=Long.parseLong(nums5[11]);
													q01du=Long.parseLong(nums5[12]);q01lc=Long.parseLong(nums5[13]);q01cu=Long.parseLong(nums5[14]);q01un=Long.parseLong(nums5[15]);q01us=Long.parseLong(nums5[16]);
													q02du=Long.parseLong(nums5[17]);q02lc=Long.parseLong(nums5[18]);q02cu=Long.parseLong(nums5[19]);q02un=Long.parseLong(nums5[20]);q02us=Long.parseLong(nums5[21]);
													q03du=Long.parseLong(nums5[22]);q03lc=Long.parseLong(nums5[23]);q03cu=Long.parseLong(nums5[24]);q03un=Long.parseLong(nums5[25]);q03us=Long.parseLong(nums5[26]);
													q04du=Long.parseLong(nums5[27]);q04lc=Long.parseLong(nums5[28]);q04cu=Long.parseLong(nums5[29]);q04un=Long.parseLong(nums5[30]);q04us=Long.parseLong(nums5[31]);
													q05du=Long.parseLong(nums5[32]);q05lc=Long.parseLong(nums5[33]);q05cu=Long.parseLong(nums5[34]);q05un=Long.parseLong(nums5[35]);q05us=Long.parseLong(nums5[36]);
													q06du=Long.parseLong(nums5[37]);q06lc=Long.parseLong(nums5[38]);q06cu=Long.parseLong(nums5[39]);q06un=Long.parseLong(nums5[40]);q06us=Long.parseLong(nums5[41]);
													q07du=Long.parseLong(nums5[42]);q07lc=Long.parseLong(nums5[43]);q07cu=Long.parseLong(nums5[44]);q07un=Long.parseLong(nums5[45]);q07us=Long.parseLong(nums5[46]);
													q08du=Long.parseLong(nums5[47]);q08lc=Long.parseLong(nums5[48]);q08cu=Long.parseLong(nums5[49]);q08un=Long.parseLong(nums5[50]);q08us=Long.parseLong(nums5[51]);
													q09du=Long.parseLong(nums5[52]);q09lc=Long.parseLong(nums5[53]);q09cu=Long.parseLong(nums5[54]);q09un=Long.parseLong(nums5[55]);q09us=Long.parseLong(nums5[56]);
													q10du=Long.parseLong(nums5[57]);q10lc=Long.parseLong(nums5[58]);q10cu=Long.parseLong(nums5[59]);q10un=Long.parseLong(nums5[60]);q10us=Long.parseLong(nums5[61]);
													q11du=Long.parseLong(nums5[62]);q11lc=Long.parseLong(nums5[63]);q11cu=Long.parseLong(nums5[64]);q11un=Long.parseLong(nums5[65]);q11us=Long.parseLong(nums5[66]);
													q12du=Long.parseLong(nums5[67]);q12lc=Long.parseLong(nums5[68]);q12cu=Long.parseLong(nums5[69]);q12un=Long.parseLong(nums5[70]);q12us=Long.parseLong(nums5[71]);
													q13du=Long.parseLong(nums5[72]);q13lc=Long.parseLong(nums5[73]);q13cu=Long.parseLong(nums5[74]);q13un=Long.parseLong(nums5[75]);q13us=Long.parseLong(nums5[76]);
													q14du=Long.parseLong(nums5[77]);q14lc=Long.parseLong(nums5[78]);q14cu=Long.parseLong(nums5[79]);q14un=Long.parseLong(nums5[80]);q14us=Long.parseLong(nums5[81]);
													q15du=Long.parseLong(nums5[82]);q15lc=Long.parseLong(nums5[83]);q15cu=Long.parseLong(nums5[84]);q15un=Long.parseLong(nums5[85]);q15us=Long.parseLong(nums5[86]);
													q16du=Long.parseLong(nums5[87]);q16lc=Long.parseLong(nums5[88]);q16cu=Long.parseLong(nums5[89]);q16un=Long.parseLong(nums5[90]);q16us=Long.parseLong(nums5[91]);
													q17du=Long.parseLong(nums5[92]);q17lc=Long.parseLong(nums5[93]);q17cu=Long.parseLong(nums5[94]);q17un=Long.parseLong(nums5[95]);q17us=Long.parseLong(nums5[96]);
													q18du=Long.parseLong(nums5[97]);q18lc=Long.parseLong(nums5[98]);q18cu=Long.parseLong(nums5[99]);q18un=Long.parseLong(nums5[100]);q18us=Long.parseLong(nums5[101]);
													q19du=Long.parseLong(nums5[102]);q19lc=Long.parseLong(nums5[103]);q19cu=Long.parseLong(nums5[104]);q19un=Long.parseLong(nums5[105]);q19us=Long.parseLong(nums5[106]);	
												}
												else{
												manu_cod=nums5[2].replace('"',' ').trim();
												pack_des=nums5[1].replace('"',' ').trim();
												atc4_cod=nums5[3].replace('"',' ').trim();
												app3_cod=nums5[4].replace('"',' ').trim();
												pack_cod=nums5[5].replace('"',' ').trim();
												q00du=Long.parseLong(nums5[6]);q00lc=Long.parseLong(nums5[7]);q00cu=Long.parseLong(nums5[8]);q00un=Long.parseLong(nums5[9]);q00us=Long.parseLong(nums5[10]);
												q01du=Long.parseLong(nums5[11]);q01lc=Long.parseLong(nums5[12]);q01cu=Long.parseLong(nums5[13]);q01un=Long.parseLong(nums5[14]);q01us=Long.parseLong(nums5[15]);
												q02du=Long.parseLong(nums5[16]);q02lc=Long.parseLong(nums5[17]);q02cu=Long.parseLong(nums5[18]);q02un=Long.parseLong(nums5[19]);q02us=Long.parseLong(nums5[20]);
												q03du=Long.parseLong(nums5[21]);q03lc=Long.parseLong(nums5[22]);q03cu=Long.parseLong(nums5[23]);q03un=Long.parseLong(nums5[24]);q03us=Long.parseLong(nums5[25]);
												q04du=Long.parseLong(nums5[26]);q04lc=Long.parseLong(nums5[27]);q04cu=Long.parseLong(nums5[28]);q04un=Long.parseLong(nums5[29]);q04us=Long.parseLong(nums5[30]);
												q05du=Long.parseLong(nums5[31]);q05lc=Long.parseLong(nums5[32]);q05cu=Long.parseLong(nums5[33]);q05un=Long.parseLong(nums5[34]);q05us=Long.parseLong(nums5[35]);
												q06du=Long.parseLong(nums5[36]);q06lc=Long.parseLong(nums5[37]);q06cu=Long.parseLong(nums5[38]);q06un=Long.parseLong(nums5[39]);q06us=Long.parseLong(nums5[40]);
												q07du=Long.parseLong(nums5[41]);q07lc=Long.parseLong(nums5[42]);q07cu=Long.parseLong(nums5[43]);q07un=Long.parseLong(nums5[44]);q07us=Long.parseLong(nums5[45]);
												q08du=Long.parseLong(nums5[46]);q08lc=Long.parseLong(nums5[47]);q08cu=Long.parseLong(nums5[48]);q08un=Long.parseLong(nums5[49]);q08us=Long.parseLong(nums5[50]);
												q09du=Long.parseLong(nums5[51]);q09lc=Long.parseLong(nums5[52]);q09cu=Long.parseLong(nums5[53]);q09un=Long.parseLong(nums5[54]);q09us=Long.parseLong(nums5[55]);
												q10du=Long.parseLong(nums5[56]);q10lc=Long.parseLong(nums5[57]);q10cu=Long.parseLong(nums5[58]);q10un=Long.parseLong(nums5[59]);q10us=Long.parseLong(nums5[60]);
												q11du=Long.parseLong(nums5[61]);q11lc=Long.parseLong(nums5[62]);q11cu=Long.parseLong(nums5[63]);q11un=Long.parseLong(nums5[64]);q11us=Long.parseLong(nums5[65]);
												q12du=Long.parseLong(nums5[66]);q12lc=Long.parseLong(nums5[67]);q12cu=Long.parseLong(nums5[68]);q12un=Long.parseLong(nums5[69]);q12us=Long.parseLong(nums5[70]);
												q13du=Long.parseLong(nums5[71]);q13lc=Long.parseLong(nums5[72]);q13cu=Long.parseLong(nums5[73]);q13un=Long.parseLong(nums5[74]);q13us=Long.parseLong(nums5[75]);
												q14du=Long.parseLong(nums5[76]);q14lc=Long.parseLong(nums5[77]);q14cu=Long.parseLong(nums5[78]);q14un=Long.parseLong(nums5[79]);q14us=Long.parseLong(nums5[80]);
												q15du=Long.parseLong(nums5[81]);q15lc=Long.parseLong(nums5[82]);q15cu=Long.parseLong(nums5[83]);q15un=Long.parseLong(nums5[84]);q15us=Long.parseLong(nums5[85]);
												q16du=Long.parseLong(nums5[86]);q16lc=Long.parseLong(nums5[87]);q16cu=Long.parseLong(nums5[88]);q16un=Long.parseLong(nums5[89]);q16us=Long.parseLong(nums5[90]);
												q17du=Long.parseLong(nums5[91]);q17lc=Long.parseLong(nums5[92]);q17cu=Long.parseLong(nums5[93]);q17un=Long.parseLong(nums5[94]);q17us=Long.parseLong(nums5[95]);
												q18du=Long.parseLong(nums5[96]);q18lc=Long.parseLong(nums5[97]);q18cu=Long.parseLong(nums5[98]);q18un=Long.parseLong(nums5[99]);q18us=Long.parseLong(nums5[100]);
												q19du=Long.parseLong(nums5[101]);q19lc=Long.parseLong(nums5[102]);q19cu=Long.parseLong(nums5[103]);q19un=Long.parseLong(nums5[104]);q19us=Long.parseLong(nums5[105]);
												}
											    pstmt.setObject(1, city_cod);
												pstmt.setObject(2, city_des);
												pstmt.setObject(3, manu_cod);
												pstmt.setObject(4, manu_des);
												pstmt.setObject(5, atc4_cod);
												pstmt.setObject(6, atc4_des);
												pstmt.setObject(7, app3_cod);
												pstmt.setObject(8, app3_des);
												pstmt.setObject(9, pack_cod);
												pstmt.setObject(10, pack_des);
												pstmt.setObject(11, q00du);pstmt.setObject(12, q00lc);pstmt.setObject(13, q00cu);pstmt.setObject(14, q00un);pstmt.setObject(15, q00us);
												pstmt.setObject(16, q01du);pstmt.setObject(17, q01lc);pstmt.setObject(18, q01cu);pstmt.setObject(19, q01un);pstmt.setObject(20, q01us);
												pstmt.setObject(21, q02du);pstmt.setObject(22, q02lc);pstmt.setObject(23, q02cu);pstmt.setObject(24, q02un);pstmt.setObject(25, q02us);
												pstmt.setObject(26, q03du);pstmt.setObject(27, q03lc);pstmt.setObject(28, q03cu);pstmt.setObject(29, q03un);pstmt.setObject(30, q03us);
												pstmt.setObject(31, q04du);pstmt.setObject(32, q04lc);pstmt.setObject(33, q04cu);pstmt.setObject(34, q04un);pstmt.setObject(35, q04us);
												pstmt.setObject(36, q05du);pstmt.setObject(37, q05lc);pstmt.setObject(38, q05cu);pstmt.setObject(39, q05un);pstmt.setObject(40, q05us);
												pstmt.setObject(41, q06du);pstmt.setObject(42, q06lc);pstmt.setObject(43, q06cu);pstmt.setObject(44, q06un);pstmt.setObject(45, q06us);
												pstmt.setObject(46, q07du);pstmt.setObject(47, q07lc);pstmt.setObject(48, q07cu);pstmt.setObject(49, q07un);pstmt.setObject(50, q07us);
												pstmt.setObject(51, q08du);pstmt.setObject(52, q08lc);pstmt.setObject(53, q08cu);pstmt.setObject(54, q08un);pstmt.setObject(55, q08us);
												pstmt.setObject(56, q09du);pstmt.setObject(57, q09lc);pstmt.setObject(58, q09cu);pstmt.setObject(59, q09un);pstmt.setObject(60, q09us);
												pstmt.setObject(61, q10du);pstmt.setObject(62, q10lc);pstmt.setObject(63, q10cu);pstmt.setObject(64, q10un);pstmt.setObject(65, q10us);
												pstmt.setObject(66, q11du);pstmt.setObject(67, q11lc);pstmt.setObject(68, q11cu);pstmt.setObject(69, q11un);pstmt.setObject(70, q11us);
												pstmt.setObject(71, q12du);pstmt.setObject(72, q12lc);pstmt.setObject(73, q12cu);pstmt.setObject(74, q12un);pstmt.setObject(75, q12us);
												pstmt.setObject(76, q13du);pstmt.setObject(77, q13lc);pstmt.setObject(78, q13cu);pstmt.setObject(79, q13un);pstmt.setObject(80, q13us);
												pstmt.setObject(81, q14du);pstmt.setObject(82, q14lc);pstmt.setObject(83, q14cu);pstmt.setObject(84, q14un);pstmt.setObject(85, q14us);
												pstmt.setObject(86, q15du);pstmt.setObject(87, q15lc);pstmt.setObject(88, q15cu);pstmt.setObject(89, q15un);pstmt.setObject(90, q15us);
												pstmt.setObject(91, q16du);pstmt.setObject(92, q16lc);pstmt.setObject(93, q16cu);pstmt.setObject(94, q16un);pstmt.setObject(95, q16us);
												pstmt.setObject(96, q17du);pstmt.setObject(97, q17lc);pstmt.setObject(98, q17cu);pstmt.setObject(99, q17un);pstmt.setObject(100, q17us);
												pstmt.setObject(101, q18du);pstmt.setObject(102, q18lc);pstmt.setObject(103, q18cu);pstmt.setObject(104, q18un);pstmt.setObject(105, q18us);
												pstmt.setObject(106, q19du);pstmt.setObject(107, q19lc);pstmt.setObject(108, q19cu);pstmt.setObject(109, q19un);pstmt.setObject(110, q19us);
												pstmt.setObject(111, fcountry);
												pstmt.setObject(112, pusr);
												 pstmt.setObject(113, nama[nama.length-1]);
												pstmt.executeUpdate();
												pstmt.close();
												}	
														}
					    			}in.close(); br.close();fstream.close();myConnection.close();
					    			myConnection	= pDataSource.getConnection();
									 Statement stmt2	= myConnection.createStatement();
									 stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('RUNNING','"+nama[nama.length-1]+"')");
									 stmt2.execute("COMMIT");
									 stmt2.close();
					    			}
					    		}
					    		catch(Exception esf)
					    		{
					    			System.out.println(esf.getMessage());
					    			myConnection	= pDataSource.getConnection();
									Statement stmt2	= myConnection.createStatement();
									stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('FAILED','Error Loading Files "+file.toString().substring(74)+"')");
									stmt2.execute("COMMIT");
									stmt2.close();
									myConnection.close();
					    		}process="Continue";
					    	}
					    }
				    }else if(prog.equals("no")){
				    myConnection	= pDataSource.getConnection();
					Statement stmt2	= myConnection.createStatement();
					stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('FAILED','File not completed, cannot process')");
					stmt2.execute("COMMIT");
					stmt2.close();
					myConnection.close();
				    }
				    FileListing.removeDirectory(startingDirectory);
				    if(process.equals("Continue")){
						myConnection	= pDataSource2.getConnection();
						stmt	= myConnection.createStatement();
						CallableStatement cs = null;
				    	try
						{
				    		cs	= myConnection.prepareCall("{call pkg_tkd_ims_rpt.startprocess(?,?)}");
				    		cs.setString(1, fcountry);
				    		cs.setString(2, pusr);
							cs.execute();
						}
						catch(Exception e_del_job)
						{
							myConnection	= pDataSource.getConnection();
							Statement stmt2	= myConnection.createStatement();
							stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('FAILED','Error Starting Procedure')");
							stmt2.execute("COMMIT");
							stmt2.close();
							myConnection.close();
							System.out.print(e_del_job.getMessage());
						}
						finally
						{
							if(cs!=null)cs.close();
							if(myConnection!=null)myConnection.close();
						}
						myConnection	= pDataSource.getConnection();
						Statement stmt2	= myConnection.createStatement();
						stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('SUCCEEDED','All Files Loaded')");
						stmt2.execute("COMMIT");
						stmt2.close();
						myConnection.close();
			    		}
			    		else {
			    		myConnection	= pDataSource.getConnection();
						Statement stmt2	= myConnection.createStatement();
						stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('FAILED','Error Data not procceded')");
						stmt2.execute("COMMIT");
						stmt2.close();
						myConnection.close();
			    		}
	    		}catch(Exception ff)
	    		{
	    			System.out.println(ff.getMessage());
	    		}
			}catch(Exception ezip)
			{
				System.out.println(ezip.getMessage());
				myConnection	= pDataSource.getConnection();
				Statement stmt2	= myConnection.createStatement();
				stmt2.execute("INSERT INTO PRO_STATUS(STATUS,JOB) VALUES ('FAILED','Process Error')");
				stmt2.execute("COMMIT");
				stmt2.close();
				myConnection.close();
			}
		}catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
    }
}
