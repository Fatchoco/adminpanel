package struts.tkd.act;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.actfrm.FormColumnMapping;
import struts.tkd.function.excelFunc;

import struts.tkd.actfrm.ActionFormValidated;

public class AddColumnMappingProcess extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception  {
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			request.setCharacterEncoding("UTF-8");
			
			try
			{
				// Get variable from Form
				FormColumnMapping myForm = (FormColumnMapping) form;
				String distCode = myForm.getDistCode();
				String distCodeInfile = myForm.getDistCodeInfile();
				String fileName = myForm.getFileName();
				String transDate = myForm.getTransDate();
				String productCode = myForm.getProductCode();
				String productName = myForm.getProductName();
				String specification = myForm.getSpecification();
				String batchNo = myForm.getBatchNo();
				String unitName = myForm.getUnitName();
				String invoiceNo = myForm.getInvoiceNo();
				String expiryDate = myForm.getExpiryDate();
				String customerCode = myForm.getCustomerCode();
				String customerName = myForm.getCustomerName();
				String customerCity = myForm.getCustomerCity();
				String customerProvince = myForm.getCustomerProvince();
				String qty = myForm.getQty();
				String totalsales = myForm.getTotalsales();
				String transDateFormat = myForm.getTransDateFormat();
				String expiryDateFormat = myForm.getExpiryDateFormat();
				String actionType = myForm.getActionType();
				String message = "";
				
				if(distCode==null && distCodeInfile==null)
					throw new Exception("Please select Distributor Code");
				else if(!distCode.equals("") && !distCodeInfile.equals(""))
					throw new Exception("Please choose only one Distributor Code source (either from file or dropdown input)");
				/*else if(!fileName.substring(0,8).equals(distCode+'_'))
					throw new Exception("Please insert filename in correct format");*/
				else if(transDate==null || transDateFormat==null)
					throw new Exception("Please choose column for Transaction Date and Transaction Date Format");
				else if(productCode==null && productName==null)
					throw new Exception("Please choose column for Product Code or Product Name");
				else if(expiryDate!=null && expiryDateFormat==null)
					throw new Exception("Please choose column for Expiry Date Format");
				else if(customerCode==null && customerName==null)
					throw new Exception("Please choose column for Customer Code or Customer name");
				else if(qty==null && totalsales==null)
					throw new Exception("Please choose column for Quantity or Totalsales");
				
				String query;
				
				if(actionType==null)
				{
					query = "INSERT INTO MAP_FILE_COLUMN(DISTRIBUTOR_CODE,TRANSACTION_DATE,PRODUCT_CODE,PRODUCT_NAME,SPECIFICATION_NAME,CUSTOMER_CODE,CUSTOMER_NAME,LOT_NUMBER,UNIT_NAME,EXPIRY_DATE,INVOICE_NUMBER,QUANTITY,TRANSACTION_DATE_FMT,EXPIRY_DATE_FMT,DISTRIBUTOR_CODE_INFILE,CUSTOMER_CITY,CUSTOMER_PROVINCE,TOTAL_SALES,FILE_NAME,CREATED_USER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'TKD_ADM')";
					message = "Column Mapping has been added";
				}
				else
				{
					forward = mapping.findForward("edit");
					query = "UPDATE MAP_FILE_COLUMN SET DISTRIBUTOR_CODE=?,TRANSACTION_DATE=?,PRODUCT_CODE=?,PRODUCT_NAME=?,SPECIFICATION_NAME=?,CUSTOMER_CODE=?,CUSTOMER_NAME=?,LOT_NUMBER=?,UNIT_NAME=?,EXPIRY_DATE=?,INVOICE_NUMBER=?,QUANTITY=?,TRANSACTION_DATE_FMT=?,EXPIRY_DATE_FMT=?,DISTRIBUTOR_CODE_INFILE=?,CUSTOMER_CITY=?,CUSTOMER_PROVINCE=?,TOTAL_SALES=?,UPDATED_USER='TAKEDA_ADM' WHERE FILE_NAME=?";
					message = "Column Mapping has been updated";
				}
				
					// Create DB Connection
					javax.sql.DataSource	dataSource;
					java.sql.Connection		myConnection;
					dataSource		= getDataSource(request,"TKD_DIST_STG");
					myConnection	= dataSource.getConnection();
					PreparedStatement pstmt = myConnection.prepareStatement(query);
					
					pstmt.setString(1, distCode);
					pstmt.setString(2, transDate);
					pstmt.setString(3, productCode);
					pstmt.setString(4, productName);
					pstmt.setString(5, specification);
					pstmt.setString(6, customerCode);
					pstmt.setString(7, customerName);
					pstmt.setString(8, batchNo);
					pstmt.setString(9, unitName);
					pstmt.setString(10, expiryDate);
					pstmt.setString(11, invoiceNo);
					pstmt.setString(12, qty);
					pstmt.setString(13, transDateFormat);
					pstmt.setString(14, expiryDateFormat);
					pstmt.setString(15, distCodeInfile);
					pstmt.setString(16, customerCity);
					pstmt.setString(17, customerProvince);
					pstmt.setString(18, totalsales);
					pstmt.setString(19, fileName);
					
					pstmt.executeUpdate();
					
					pstmt.close();
					myConnection.close();
					
				return new ActionForward(forward.getPath()+"?message="+message);
			}
			catch(SQLException se)
			{
				return new ActionForward(forward.getPath()+"?message=Unsuccessful. That filename is already in column mapping");
			}
			catch(Exception e)
			{
				return new ActionForward(forward.getPath()+"?message="+e.getMessage().trim());
			}
	  }
}