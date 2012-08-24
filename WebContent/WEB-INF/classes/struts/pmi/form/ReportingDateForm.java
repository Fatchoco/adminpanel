package struts.pmi.form;


import org.apache.struts.action.ActionForm;


//levin17lacustre 07102009
public class ReportingDateForm extends ActionForm  {


	 private String brd;
	 private String ord;
	 private String prd;
	 private String mr;
	 
	 //getter
	  public String getBrd() {
		    return brd;
	  }

	  public String getOrd() {
	    return ord;
	  }
	  
	  public String getPrd(){
		  return prd;
	  }

	  public String getMr(){
		  return mr;
	  }
	  
	  //setter
	  public void setBrd(String brd) {
	    this.brd = brd;
	  }

	  public void setOrd(String ord){
		  this.ord=ord;
	  }

	  public void setPrd(String prd){
		  this.prd=prd;
	  }
	  
	  public void setMr(String mr){
		  this.mr=mr;
	  }
}