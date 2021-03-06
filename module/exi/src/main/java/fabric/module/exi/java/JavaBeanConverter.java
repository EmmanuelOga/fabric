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
/** 16.11.2011 00:41 */
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

import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

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

  /** Name of the package in which the bean class resides */
  private String packageName;

  /** Fully qualified name of the Java bean class (incl. package) */
  private String qualifiedBeanClassName;

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

    this.packageName = this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY);
    if (this.properties.containsKey(FabricEXIModule.PACKAGE_NAME_ALT_KEY))
    {
      this.packageName = this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_ALT_KEY);
    }

    this.qualifiedBeanClassName = String.format("%s.%s",
            this.packageName,
            this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY));

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
   * @param fixLists XML lists, where value-tags need to be fixed
   * 
   * @throws Exception Source file was null or error during code generation
   */
  public void generateConverterClass(final JSourceFile sourceFile,
                                     final ArrayList<ElementData> fixElements,
                                     final ArrayList<ArrayData> fixArrays,
                                     final ArrayList<ListData> fixLists) throws Exception
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
      
      sourceFile.add(xmlLibrary.init(fixElements, fixArrays, fixLists));
      
      // Add required imports AFTER initialization
      for (String requiredImport: xmlLibrary.getRequiredImports())
      {
        sourceFile.addImport(requiredImport);
      }

      // Import bean class, if it resides in a different package
      if (!this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY).equals(this.packageName))
      {
        sourceFile.addImport(this.qualifiedBeanClassName);
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
