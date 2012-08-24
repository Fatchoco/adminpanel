package struts.tkd.act;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.tkd.actfrm.ActionFormValidated;

public class ProcessToReportResultInitial extends ActionFormValidated {
	  public ActionForward doExecute( ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)throws Exception{
			ActionForward forward = null;
			forward = mapping.findForward("forward");
			
			String process = request.getParameter("process")==null?"":request.getParameter("process");
			
			// Create DB Connection
			javax.sql.DataSource	dataSource;
			java.sql.Connection		myConnection;
			Statement stmt	= null;
			ResultSet rset	= null;
			
			//Get Unmapped count
			int umcust = 0;
			int umprd  = 0;
			
			dataSource		= getDataSource(request,"TKD_DIST_STG");
			myConnection	= dataSource.getConnection();
			stmt = myConnection.createStatement();
			rset = stmt.executeQuery("SELECT   (SELECT NVL(COUNT(*),0)   FROM     (SELECT a.DISTRIBUTOR_CD,   a.CUSTOMER_CD,   a.CUSTOMER_NM,   a.CUSTOMER_CITY,   a.CUSTOMER_PROVINCE,   b.FUNCTION FROM   (SELECT DISTINCT DISTRIBUTOR_CD,     CUSTOMER_CD,     CUSTOMER_NM,     CUSTOMER_CITY,     CUSTOMER_PROVINCE   FROM EXCP_MAPPING_CUSTOMER   WHERE TO_DATE(PERIOD, 'YYYYMM') <= (SELECT TO_DATE(MAX(CURRENT_PERIOD),'YYYYMM') FROM PAR_SYSTEM)   AND TO_DATE(PERIOD, 'YYYYMM') >= (SELECT ADD_MONTHS(TO_DATE(MAX(CURRENT_PERIOD),'YYYYMM'),-12) FROM PAR_SYSTEM)   ) a LEFT OUTER JOIN   (SELECT DISTINCT DISTRIBUTOR_CD,     CUSTOMER_CD,     CUSTOMER_NM,     CUSTOMER_CITY,     CUSTOMER_PROVINCE,     'New' FUNCTION   FROM EXCP_MAPPING_CUSTOMER   WHERE PERIOD =     (SELECT MAX(CURRENT_PERIOD) FROM PAR_SYSTEM     )   ) b ON NVL(a.DISTRIBUTOR_CD,'xnullnullx') = NVL(b.DISTRIBUTOR_CD,'xnullnullx') AND NVL(a.CUSTOMER_CD,'xnullnullx')   = NVL(b.CUSTOMER_CD,'xnullnullx') AND NVL(a.CUSTOMER_NM,'xnullnullx')   = NVL(b.CUSTOMER_NM,'xnullnullx') ORDER BY FUNCTION,   DISTRIBUTOR_CD,   CUSTOMER_CD,   CUSTOMER_NM     )   ) UMCUST,   (SELECT NVL(COUNT(*),0)   FROM     (SELECT a.DISTRIBUTOR_CD,   a.PRODUCT_CD,   a.PRODUCT_NM,   b.FUNCTION FROM   (SELECT DISTINCT DISTRIBUTOR_CD,     PRODUCT_CD,     PRODUCT_NM   FROM EXCP_MAPPING_PRODUCT   WHERE TO_DATE(PERIOD, 'YYYYMM') <=     (SELECT TO_DATE(MAX(CURRENT_PERIOD),'YYYYMM') FROM PAR_SYSTEM     )   AND TO_DATE(PERIOD, 'YYYYMM') >=     (SELECT ADD_MONTHS(TO_DATE(MAX(CURRENT_PERIOD),'YYYYMM'),-12) FROM PAR_SYSTEM     )   ) a LEFT OUTER JOIN   (SELECT DISTINCT DISTRIBUTOR_CD,     PRODUCT_CD,     PRODUCT_NM,     'New' FUNCTION   FROM EXCP_MAPPING_PRODUCT   WHERE PERIOD =     (SELECT MAX(CURRENT_PERIOD) FROM PAR_SYSTEM     )   ) b ON NVL(a.DISTRIBUTOR_CD,'xnullnullx') = NVL(b.DISTRIBUTOR_CD,'xnullnullx') AND NVL(a.PRODUCT_CD,'xnullnullx')    = NVL(b.PRODUCT_CD,'xnullnullx') AND NVL(a.PRODUCT_NM,'xnullnullx')    = NVL(b.PRODUCT_NM,'xnullnullx') ORDER BY FUNCTION,   DISTRIBUTOR_CD,   PRODUCT_CD,   PRODUCT_NM     )   ) UMPRD FROM DUAL");
			
			rset.next();
			
			umcust = rset.getInt("umcust");
			umprd  = rset.getInt("umprd");
			
			stmt.close();
			myConnection.close();
			
			request.setAttribute("umcust", umcust);
			request.setAttribute("umprd", umprd);
			request.setAttribute("process", process);
			
			return new ActionForward(forward.getPath()+"?message=Process Data to Report is successful");
	  }
}
