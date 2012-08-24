package struts.pmi.form;


import org.apache.struts.action.ActionForm;

public class TaxRateForm extends ActionForm  {


	 private String txttaxrate;
	 
	 //getter
	  public String gettxtTaxRate() {
		    return txttaxrate;
	  }

	  
	  //setter
	  public void settxtTaxRate(String txTax) {
	    this.txttaxrate = txTax;
	  }


}