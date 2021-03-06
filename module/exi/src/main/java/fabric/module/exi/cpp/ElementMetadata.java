/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/** 14.04.2012 00:47 */
package fabric.module.exi.cpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
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
 * @author seidel, reichart
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
  
  /** XML element is a single global value */
  public static final int XML_ATOMIC_VALUE = 0;
  
  /** XML element is a local element within a complex type */
  public static final int XML_LOCAL_ELEMENT = 1;
  
  /** XML element is an array that may contain multiple values */
  public static final int XML_ARRAY = 2;
  
  /** XML element is a list that may contain multiple values */
  public static final int XML_LIST = 3;
  
  /** Name of the XML element */
  private String elementName;

  /** Name of the parent XML element */
  private String parentName;

  /** EXI type of XML element content (e.g. Boolean, Integer or String) */
  private String elementEXIType;
  
  /** Type of the XML element (e.g. atomic value, list or array) */
  private int type;

  /**
   * Parameterized constructor.
   * 
   * @param elementName XML element name
   * @param elementEXIType EXI type of element content (e.g. Boolean,
   * Integer or String)
   * @param type XML element type (atomic value, list or array)
   */
  public ElementMetadata(final String elementName, final String elementEXIType, final int type)
  {
    this.elementName = elementName;
    this.elementEXIType = elementEXIType;
    this.type = type;

    // Validate support for EXI type
    ElementMetadata.checkEXITypeSupport(this.elementEXIType);
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

    // Element is an XML array
    if (FSchemaTypeHelper.isArray(element))
    {
      this.elementEXIType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());
      this.type = ElementMetadata.XML_ARRAY;
    }
    // Element is an XML list
    else if(FSchemaTypeHelper.isList(element))
    {
      FList listType = (FList)element.getSchemaType();

      this.elementEXIType = this.getEXITypeName(listType.getItemType().getClass().getSimpleName());
      this.type = ElementMetadata.XML_LIST;
    }
    // Element is a local element
    else if (!element.isTopLevel())
    {
      this.elementEXIType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());
      this.type = ElementMetadata.XML_LOCAL_ELEMENT;
    }
    // Element is an atomic value
    else
    {
      this.elementEXIType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());
      this.type = ElementMetadata.XML_ATOMIC_VALUE;
    }

    // Validate support for EXI type
    ElementMetadata.checkEXITypeSupport(this.elementEXIType);
  }

  /**
   * Parameterized constructor creates ElementMetadata object
   * from an FElement object and stores the name of the parent
   * XML element.
   *
   * @param element FElement object passed through from treewalker
   * @param parentName Name of the parent XML element
   */
  public ElementMetadata(final FElement element, final String parentName)
  {
    // Set element metadata
    this(element);
    
    // Set name of parent XML element
    this.parentName = parentName;
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
   * Setter for parent XML element name.
   *
   * @param parentName Parent XML element name
   */
  public void setParentName(final String parentName)
  {
    this.parentName = parentName;
  }

  /**
   * Getter for parent XML element name.
   *
   * @return Parent XML element name
   */
  public String getParentName()
  {
    return this.parentName;
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
   * Getter for C++ element content type.
   * 
   * @return C++ element content type
   */
  public String getElementCppType()
  {
    return this.getCppTypeName(this.elementEXIType);
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
   * Clone ElementMetadata object and return a deep copy.
   * 
   * @return Cloned ElementMetadata object
   */
  @Override
  public ElementMetadata clone()
  {
    ElementMetadata result;
    
    result = new ElementMetadata(this.elementName, this.elementEXIType, this.type);
    result.setParentName(this.parentName);
    
    return result;
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
    typesEXIToCpp.put("Float", "xsd_float_t");
    typesEXIToCpp.put("String", "const char*");
    typesEXIToCpp.put("Decimal", "char*");
    typesEXIToCpp.put("Integer", "int32");
    typesEXIToCpp.put("UnsignedInteger", "uint32");
    typesEXIToCpp.put("NBitUnsignedInteger", "unsigned int");
  }

  /**
   * Private helper method to check whether a desired EXI type
   * is supported by our C++ EXI implementation or not. We do
   * currently not support all EXI types, e.g. there is no
   * implementation for EXI string tables yet.
   * 
   * In case of an unsupported EXI type an exception is raised.
   * 
   * @param exiTypeName EXI type name
   * 
   * @throws UnsupportedOperationException EXI type not supported
   */
  private static void checkEXITypeSupport(final String exiTypeName)
  {
    // Create a list of supported EXI types
    List<String> supportedEXITypes = new ArrayList<String>();
    supportedEXITypes.add("Boolean");
    supportedEXITypes.add("Float");
    // supportedEXITypes.add("String"); // TODO: Add support for String
    // supportedEXITypes.add("Decimal"); // TODO: Add support for Decimal
    supportedEXITypes.add("Integer");
    supportedEXITypes.add("UnsignedInteger");
    supportedEXITypes.add("NBitUnsignedInteger");

    // Validate desired EXI type
    if (!supportedEXITypes.contains(exiTypeName))
    {
      throw new UnsupportedOperationException(String.format("EXI data type '%s' is not supported yet.", exiTypeName));
    }
  }
}
