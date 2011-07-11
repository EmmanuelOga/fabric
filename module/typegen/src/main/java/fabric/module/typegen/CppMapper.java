package fabric.module.typegen;

import fabric.wsdlschemaparser.schema.FAnyURI;
import fabric.wsdlschemaparser.schema.FBase64Binary;
import fabric.wsdlschemaparser.schema.FBoolean;
import fabric.wsdlschemaparser.schema.FByte;
import fabric.wsdlschemaparser.schema.FDate;
import fabric.wsdlschemaparser.schema.FDateTime;
import fabric.wsdlschemaparser.schema.FDay;
import fabric.wsdlschemaparser.schema.FDecimal;
import fabric.wsdlschemaparser.schema.FDouble;
import fabric.wsdlschemaparser.schema.FDuration;
import fabric.wsdlschemaparser.schema.FFloat;
import fabric.wsdlschemaparser.schema.FHexBinary;
import fabric.wsdlschemaparser.schema.FInt;
import fabric.wsdlschemaparser.schema.FLong;
import fabric.wsdlschemaparser.schema.FMonth;
import fabric.wsdlschemaparser.schema.FMonthDay;
import fabric.wsdlschemaparser.schema.FPositiveInteger;
import fabric.wsdlschemaparser.schema.FQName;
import fabric.wsdlschemaparser.schema.FShort;
import fabric.wsdlschemaparser.schema.FString;
import fabric.wsdlschemaparser.schema.FTime;
import fabric.wsdlschemaparser.schema.FUnsignedByte;
import fabric.wsdlschemaparser.schema.FUnsignedInt;
import fabric.wsdlschemaparser.schema.FUnsignedLong;
import fabric.wsdlschemaparser.schema.FUnsignedShort;
import fabric.wsdlschemaparser.schema.FYear;
import fabric.wsdlschemaparser.schema.FYearMonth;

public class CppMapper extends LanguageMapper {
	
	public void createMapping() {
		types.put(new FBoolean(),			"bool");
		types.put(new FFloat(),				"float");
		types.put(new FDouble(),			"double");
		types.put(new FByte(),				"byte");
		types.put(new FUnsignedByte(),		"short");
		types.put(new FShort(),				"short");
		types.put(new FUnsignedShort(),		"int");
		types.put(new FInt(),				"int");
		// types.put(new FPositiveInteger(),	"java.math.BigInteger");
		types.put(new FUnsignedInt(),		"long");
		types.put(new FLong(),				"long");
		// types.put(new FUnsignedLong(),		"java.math.BigDecimal");
		// types.put(new FDecimal(),			"java.math.BigDecimal");
		types.put(new FString(),			"String");
		types.put(new FHexBinary(),			"byte[]");
		types.put(new FBase64Binary(),		"byte[]");

		/*
		types.put(new FDateTime(),			"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FTime(),				"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FDate(),				"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FDay(),				"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FMonth(),				"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FMonthDay(),			"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FYear(),				"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FYearMonth(),			"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FDuration(),			"javax.xml.datatype.Duration");
		//TODO: NOTATION! types.put(new FNOTATION(), "");
		types.put(new FQName(),				"javax.xml.namespace.QName");
		types.put(new FAnyURI(),			"java.net.URI" );
		*/
	}

}
