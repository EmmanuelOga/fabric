/*
 * Test case for enumerations and the corresponding "Simple" XSD data types
 */

package XSD2JavaTestCases.JavaFiles;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import javax.xml.datatype.XMLGregorianCalendar;

/*
 *	This is a class description
 */
@Root
public class Waehrung4{
	
	@Element
	public XMLGregorianCalendar Datum;
	@Element
	public Waehrungscodes Waehrungscode;
	@Element
	public String Waehrungsname; 
	@Element
	public BigDecimal Wert;
	
	/*
	 * This is a currency code type enumeration
	 */
	@Element
	public enum Waehrungscodes{
		AUD,
		BRL,
		CAD,
		CNY,
		EUR,
		GBP,
		INR,
		JPY,
		RUR,
		USD
	}
		
	public Waehrung4() {
	      super();
	} 
	
	public XMLgregorianCalendar getDatum(){
		return this.Datum;
	}
	
	public Waehrungscodes getWaehrungscode(){
		return this.Waehrungscode;
	}
	
	public String getWaehrungsnamen(){
		return this.Waehrungsname;
	}
	
	public BigDecimal getWert(){
		return this.Wert;
	}
		
}