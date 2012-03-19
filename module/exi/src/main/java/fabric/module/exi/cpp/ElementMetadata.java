/** 19.03.2012 11:48 */
package fabric.module.exi.cpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FList;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;

/**
 * This class is a container for XML element metadata. While
 * the treewalker processes an XML Schema file, information
 * about XML elements is collected in a Queue of ElementMetadata
 * objects. The data is later used to generate the serialization
 * and deserialization methods in EXIConverter class dynamically.
 * 
 * @author seidel
 */
public class ElementMetadata
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(ElementMetadata.class);
  
  /** Mapping from Fabric type names (FInt, FString etc.) to EXI built-in type names */
  private static HashMap<String, String> typesFabricToEXI = new HashMap<String, String>();
  
  /** Mapping from EXI built-in type names to C++ types */
  private static HashMap<String, String> typesEXIToCpp = new HashMap<String, String>();
  
  static
  {
    // Initialize type mappings
    ElementMetadata.createMappingFabricToEXI();
    ElementMetadata.createMappingEXIToCpp();
  }
  
  /** XML element is a single value */
  public static final int XML_ATOMIC_VALUE = 0;
  
  /** XML element is a list that may contain multiple values */
  public static final int XML_LIST = 1;
  
  /** XML element is an array that may contain multiple values */
  public static final int XML_ARRAY = 2;
  
  /** Name of the XML element */
  private String elementName;

    /** Name of the parent element */
  private String parentName;
  
  /** EXI type of XML element content (e.g. Boolean, Integer or String) */
  private String elementEXIType;
  
  /** C++ type of element content (e.g. boolean, int or char*) */
  private String elementCppType;
  
  /** Type of the XML element (e.g. atomic value, list or array) */
  private int type;
  
  /** EXI event code within the XML Schema document structure */
  private int exiEventCode;
  
  /**
   * Parameterized constructor.
   * 
   * @param elementName XML element name
   * @param elementEXIType EXI type of element content (e.g. Boolean,
   * Integer or String)
   * @param elementCppType C++ type of element content (e.g. bool,
   * int or char*)
   * @param type XML element type (atomic value, list or array)
   * @param exiEventCode EXI event code
   */
  public ElementMetadata(final String elementName, final String elementEXIType, final String elementCppType, final int type, final int exiEventCode)
  {
    this.elementName = elementName;
    this.elementEXIType = elementEXIType;
    this.elementCppType = elementCppType;
    this.type = type;
    this.exiEventCode = exiEventCode;
  }
  
  /**
   * Parameterized constructor creates ElementMetadata object
   * from an FElement object passed through from the Fabric
   * treewalker.
   * 
   * @param element FElement object passed through from treewalker
   */
  public ElementMetadata(final FElement element)
  {            
    // Set XML element name
    this.elementName = element.getName();

    // Set EXI and XML element type
    if (FSchemaTypeHelper.isList(element))
    {
      FList listType = (FList)element.getSchemaType();

      this.elementEXIType = this.getEXITypeName(listType.getItemType().getClass().getSimpleName());
      this.type = ElementMetadata.XML_LIST;
    }

    else if (FSchemaTypeHelper.isArray(element))
    {
      this.elementEXIType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName()); // TODO: Set type of array elements
        LOGGER.debug(">>> " + elementEXIType);
      this.type = ElementMetadata.XML_ARRAY;
    }

    else
    {
      this.elementEXIType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());
      this.type = ElementMetadata.XML_ATOMIC_VALUE;
    }
    
    // Set C++ element type
    this.elementCppType = this.getCppTypeName(this.elementEXIType);
    
    // Set EXI event code
    this.exiEventCode = 0;
  }
    
  public ElementMetadata(FElement element, String parentName) {
    this(element);
    this.parentName = parentName;
      LOGGER.debug(">>> " + this.parentName);
  }

    
    

  /**
   * Setter for XML element name.
   * 
   * @param elementName XML element name
   */
  public void setElementName(final String elementName)
  {
    this.elementName = elementName;
  }

  /**
   * Getter for XML element name.
   * 
   * @return XML element name
   */
  public String getElementName()
  {
    return this.elementName;
  }

    /**
     * Getter for parent element name.
     *
     * @return Parent element name
     */
    public String getParentName() {
        return this.parentName;
    }

    /**
     * Setter for parent element name.
     *
     * @param parentName Parent element name
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

  /**
   * Setter for EXI element content type.
   * 
   * @param elementEXIType EXI element content type
   */
  public void setElementEXIType(final String elementEXIType)
  {
    this.elementEXIType = elementEXIType;
  }

  /**
   * Getter for EXI element content type.
   * 
   * @return EXI element content type
   */
  public String getElementEXIType()
  {
    return this.elementEXIType;
  }

  /**
   * Setter for C++ element content type.
   * 
   * @param elementCppType C++ element content type
   */
  public void setElementCppType(final String elementCppType)
  {
    this.elementCppType = elementCppType;
  }

  /**
   * Getter for C++ element content type.
   * 
   * @return C++ element content type
   */
  public String getElementCppType()
  {
    return this.elementCppType;
  }

  /**
   * Setter for XML element type (e.g. atomic value, list or array).
   * 
   * @param type XML element type
   */
  public void setType(final int type)
  {
    this.type = type;
  }

  /**
   * Getter for XML element type.
   * 
   * @return XML element type
   */
  public int getType()
  {
    return this.type;
  }

  /**
   * Setter for EXI event code.
   * 
   * @param exiEventCode EXI event code
   */
  public void setEXIEventCode(final int exiEventCode)
  {
    this.exiEventCode = exiEventCode;
  }

  /**
   * Getter for EXI event code.
   * 
   * @return EXI event code
   */
  public int getEXIEventCode()
  {
    return this.exiEventCode;
  }

  /**
   * Clone ElementMetadata object and return a deep copy.
   * 
   * @return Cloned ElementMetadata object
   */
  @Override
  public ElementMetadata clone()
  {
    ElementMetadata metadata = new ElementMetadata(this.elementName, this.elementEXIType, this.elementCppType, this.type, this.exiEventCode);
    metadata.setParentName(this.parentName);
    return metadata;
  }

  /**
   * Private helper method to get the EXI built-in type name
   * (e.g. Boolean, Integer or String) for one of Fabric's
   * XML Schema type names (e.g. FBoolean, FInt or FString).
   * 
   * @param schemaTypeName Fabric type name
   * 
   * @return Corresponding EXI built-in type name
   * 
   * @throws IllegalArgumentException No matching mapping found
   */
  private String getEXITypeName(final String schemaTypeName) throws IllegalArgumentException
  {
    // Return mapping if available
    if (typesFabricToEXI.containsKey(schemaTypeName))
    {
      LOGGER.debug(String.format("Mapped Fabric data type '%s' to EXI built-in type '%s'.", schemaTypeName, typesFabricToEXI.get(schemaTypeName)));

      return typesFabricToEXI.get(schemaTypeName);
    }

    throw new IllegalArgumentException(String.format("No mapping found for XML datatype '%s'.", schemaTypeName));
  }

  /**
   * Private helper method to get the C++ type name (e.g.
   * bool, int or char) for one of the EXI built-in type
   * names (e.g. Boolean, Integer or String).
   *
   * @param exiTypeName EXI built-in type name
   * 
   * @return Corresponding C++ type name
   * 
   * @throws IllegalArgumentException No matching mapping found
   */
  private String getCppTypeName(final String exiTypeName) throws IllegalArgumentException
  {
    // Return mapping if available
    if (typesEXIToCpp.containsKey(exiTypeName))
    {
      LOGGER.debug(String.format("Mapped EXI built-in type '%s' to C++ type '%s'.", exiTypeName, typesEXIToCpp.get(exiTypeName)));

      return typesEXIToCpp.get(exiTypeName);
    }

    throw new IllegalArgumentException(String.format("No mapping found for EXI datatype '%s'.", exiTypeName));
  }

  /**
   * Private helper method to populate the mapping of Fabric's
   * XML Schema type names to EXI built-in type names.
   */
  private static void createMappingFabricToEXI()
  {
    typesFabricToEXI.put("FBoolean", "Boolean");
    typesFabricToEXI.put("FFloat", "Float");
    typesFabricToEXI.put("FDouble", "Float");
    typesFabricToEXI.put("FByte", "NBitUnsignedInteger");
    typesFabricToEXI.put("FUnsignedByte", "NBitUnsignedInteger");
    typesFabricToEXI.put("FShort", "Integer");
    typesFabricToEXI.put("FUnsignedShort", "Integer");
    typesFabricToEXI.put("FInt", "Integer");
    typesFabricToEXI.put("FInteger", "Integer");
    typesFabricToEXI.put("FPositiveInteger", "UnsignedInteger");
    typesFabricToEXI.put("FUnsignedInt", "UnsignedInteger");
    typesFabricToEXI.put("FLong", "Integer");
    typesFabricToEXI.put("FUnsignedLong", "UnsignedInteger");
    typesFabricToEXI.put("FDecimal", "Decimal");
    typesFabricToEXI.put("FString", "String");
    typesFabricToEXI.put("FHexBinary", "Binary");
    typesFabricToEXI.put("FBase64Binary", "Binary");
    typesFabricToEXI.put("FDateTime", "DateTime");
    typesFabricToEXI.put("FTime", "DateTime");
    typesFabricToEXI.put("FDate", "DateTime");
    typesFabricToEXI.put("FDay", "DateTime");
    typesFabricToEXI.put("FMonth", "DateTime");
    typesFabricToEXI.put("FMonthDay", "DateTime");
    typesFabricToEXI.put("FYear", "DateTime");
    typesFabricToEXI.put("FYearMonth", "DateTime");
    typesFabricToEXI.put("FDuration", "String");
    typesFabricToEXI.put("FNOTATION", "String");
    typesFabricToEXI.put("FQName", "String");
    typesFabricToEXI.put("FName", "String");
    typesFabricToEXI.put("FNCName", "String");
    typesFabricToEXI.put("FNegativeInteger", "Integer");
    typesFabricToEXI.put("FNMTOKEN", "String");
    typesFabricToEXI.put("FNonNegativeInteger", "UnsignedInteger");
    typesFabricToEXI.put("FNonPositiveInteger", "Integer");
    typesFabricToEXI.put("FNormalizedString", "String");
    typesFabricToEXI.put("FToken", "String");
    typesFabricToEXI.put("FAnyURI", "String");
    typesFabricToEXI.put("FAny", "String");
  }

  /**
   * Private helper method to populate the mapping of EXI
   * built-in type names to C++ type names.
   */
  private static void createMappingEXIToCpp()
  {
    typesEXIToCpp.put("Boolean", "bool");
    typesEXIToCpp.put("Float", "float");
    typesEXIToCpp.put("String", "const char*");
    typesEXIToCpp.put("Decimal", "char*");
    typesEXIToCpp.put("Integer", "int32");
    typesEXIToCpp.put("UnsignedInteger", "uint32");
    typesEXIToCpp.put("NBitUnsignedInteger", "unsigned int");
  }
}
