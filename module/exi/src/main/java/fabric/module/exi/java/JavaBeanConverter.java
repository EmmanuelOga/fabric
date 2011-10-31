/** 31.10.2011 19:29 */
package fabric.module.exi.java;

import java.util.Properties;
import java.util.ArrayList;

import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;
import de.uniluebeck.sourcegen.java.JSourceFile;

import fabric.module.exi.FabricEXIModule;
import fabric.module.exi.exceptions.FabricEXIException;

import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.NonSimpleListData;
import fabric.module.exi.java.FixValueContainer.SimpleListData;

import fabric.module.exi.java.lib.xml.XMLLibrary;
import fabric.module.exi.java.lib.xml.XMLLibraryFactory;

/**
 * JavaBeanConverter class creates the XML converter class and
 * generates methods for the application's main function to
 * demonstrate the usage of the converter.
 * 
 * @author seidel
 */
public class JavaBeanConverter
{
  /** Properties object with module configuration */
  private Properties properties;
  
  /** Name of the Java bean class */
  private String beanClassName;
  
  /** Name of the XML converter class */
  private String converterClassName;
  
  /**
   * Parameterized constructor initializes properties object
   * and various other member variables.
   * 
   * @param properties Properties object with module options
   */
  public JavaBeanConverter(Properties properties)
  {
    this.properties = properties;
    
    this.beanClassName = this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY);
    
    this.converterClassName = this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY) + "Converter";
  }
  
  /**
   * Public callback method that generates the XML converter class
   * and adds it to the provided Java source file.
   * 
   * @param sourceFile Java source file for code write-out
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixSimpleLists XML lists with simple-typed items,
   * where value-tags need to be fixed
   * @param fixNonSimpleLists XML lists with non-simple-typed items,
   * where value-tags need to be fixed
   * 
   * @throws Exception Source file was null or error during code generation
   */
  public void generateConverterClass(final JSourceFile sourceFile,
                                     final ArrayList<ElementData> fixElements,
                                     final ArrayList<ArrayData> fixArrays,
                                     final ArrayList<SimpleListData> fixSimpleLists,
                                     final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception
  {    
    if (null == sourceFile)
    {
      throw new FabricEXIException("Cannot create XML converter class. Source file is null.");
    }
    else
    {
      // Create instance of desired XML library
      XMLLibrary xmlLibrary = XMLLibraryFactory.getInstance().createXMLLibrary(
              this.properties.getProperty(FabricEXIModule.XMLLIBRARY_NAME_KEY),
              this.beanClassName);
      
      sourceFile.add(xmlLibrary.init(fixElements, fixArrays, fixSimpleLists, fixNonSimpleLists));
      
      // Add required imports AFTER initialization
      for (String requiredImport: xmlLibrary.getRequiredImports())
      {
        sourceFile.addImport(requiredImport);
      }
    }
  }
  
  /**
   * Public callback method that creates code to operate the
   * XML converter class. The generated code demonstrates how
   * to serialize an instance of the Java bean class to an
   * XML document.
   * 
   * @return JMethod object with serialization code
   * 
   * @throws Exception Error during code generation
   */
  public JMethod generateSerializeCall() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, beanClassName, "beanObject"));
    
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC, "String", "toXML", jms, new String[] { "Exception" });
    
    String methodBody = String.format("return %s.instanceToXML(beanObject);", this.converterClassName);
    
    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Convert Java bean object to XML document."));

    return jm;
  }
  
  /**
   * Public callback method that creates code to operate the
   * XML converter class. The generated code demonstrates how
   * to deserialize an XML document to an instance of the Java
   * bean class.
   * 
   * @return JMethod object with deserialization code
   * 
   * @throws Exception Error during code generation
   */
  public JMethod generateDeserializeCall() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC, beanClassName, "toInstance", jms, new String[] { "Exception" });

    String methodBody = String.format("return %s.xmlToInstance(xmlDocument);", this.converterClassName);

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Convert XML document to Java bean object."));

    return jm;
  }
}
