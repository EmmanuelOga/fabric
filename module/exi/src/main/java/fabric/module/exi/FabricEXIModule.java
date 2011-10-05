package fabric.module.exi;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricModule;
import fabric.module.api.FabricSchemaTreeItemHandler;
import fabric.module.exi.exceptions.FabricEXIException;

/**
 * Fabric module for the generation of an XSD-specific EXI serializer
 * and deserializer class. This module uses the module options from the
 * properties file and the name of the XML schema document to create
 * a class, that allows the serialization and deserialization of the
 * corresponding type objects.
 * 
 * @author seidel
 */
public class FabricEXIModule implements FabricModule
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(FabricEXIModule.class);

  /** Key for target language in properties object */
  private static final String TARGET_LANGUAGE_KEY = "typegen.target_language";

  /** Key for main class name in properties object */
  private static final String MAIN_CLASS_NAME_KEY = "typegen.main_class_name";

  /** Key for XML framework name in properties object */
  private static final String XML_FRAMEWORK_KEY = "typegen.java.xml_framework";

  /** Key for main package name in properties object */
  private static final String PACKAGE_NAME_KEY = "typegen.java.package_name";

  /** Key for the EXI library name in properties object */
  private static final String EXI_LIBRARY_KEY = "exi.java.exi_library";
  
  /** Key for EXICodeGen factory name in properties object */
  private static final String GENERATOR_NAME_KEY = "exi.generator_name";

  /** Properties object for module configuration */
  private Properties properties = null;

  /**
   * Constructor initializes the internal properties object.
   * 
   * @param properties Properties object with module options
   */
  public FabricEXIModule(Properties properties)
  {
    this.properties = properties;
  }

  /**
   * Helper method to return module name.
   * 
   * @return Module name
   */
  @Override
  public String getName()
  {
    return "exi";
  }

  /**
   * Helper method to return module description.
   * 
   * @return Module description
   */
  @Override
  public String getDescription()
  {
    return String.format("Module to generate EXI serializer and deserializer class. "
            + "Valid options are '%s', '%s', '%s', '%s', '%s' and '%s'.",
            TARGET_LANGUAGE_KEY, MAIN_CLASS_NAME_KEY, XML_FRAMEWORK_KEY,
            PACKAGE_NAME_KEY, EXI_LIBRARY_KEY, GENERATOR_NAME_KEY);
  }

  /**
   * This method returns a Fabric handler object for the EXI generator
   * module. It is instanziated with the current workspace and module
   * options.
   * 
   * @param workspace Workspace object for EXI class output
   * 
   * @return FabricEXIHandler object
   *
   * @throws Exception Error during handler instantiation 
   */
  @Override
  public FabricSchemaTreeItemHandler getHandler(Workspace workspace) throws Exception
  {
    this.validateProperties();

    return new FabricEXIHandler(workspace, this.properties);
  }

  /**
   * This method validates all module-specific options and translates
   * them where needed (e.g. class names for factory methods). The
   * constructor of this class is called during Fabric setup. At that
   * time, however, the Java properties file from the command line is
   * not yet processed. This is why this method must not be called in
   * the module's constructor, but in the getHandler() method.
   *
   * @throws Exception Error during validation
   */
  private void validateProperties() throws Exception
  {
    // Early exit, if properties object is null
    if (null == this.properties)
    {
      throw new IllegalStateException("Properties object is null. Maybe it was not initialized properly?");
    }

    // Check properties
    this.checkTargetLanguage();
    this.checkMainClassName();
    this.checkXMLFramework();
    this.checkPackageName();
    this.checkEXILibrary();

    // Print exi module properties for debug purposes
    for (String key: this.properties.stringPropertyNames())
    {
      if (key.startsWith("exi."))
      {
        LOGGER.debug(String.format("Property '%s' has value '%s'.", key, this.properties.getProperty(key)));
      }
    }
  }

  /**
   * Check parameter for target language. This property is mandatory
   * and must either be "java" or "cpp". Further target languages
   * might be added later. In case of an illegal argument, an exception
   * is thrown.
   *
   * @throws Exception No or invalid properties passed to module
   */
  private void checkTargetLanguage() throws Exception
  {
    String targetLanguage = this.properties.getProperty(TARGET_LANGUAGE_KEY);

    // No target language defined
    if (null == targetLanguage)
    {
      throw new FabricEXIException("No target language specified. Please provide a valid option.");
    }
    // Use Java EXI code generator
    else if (targetLanguage.toLowerCase().equals("java"))
    {
      this.properties.setProperty(GENERATOR_NAME_KEY, "fabric.module.exi.java.JavaEXICodeGen");
    }
    // Use C++ EXI code generator
    else if (targetLanguage.toLowerCase().equals("cpp"))
    {
      this.properties.setProperty(GENERATOR_NAME_KEY, "fabric.module.exi.cpp.CppEXICodeGen");
    }
    // Invalid target language provided
    else
    {
      throw new FabricEXIException(String.format("Invalid target language '%s'. Use one of [java, cpp].", targetLanguage));
    }

    this.properties.remove(TARGET_LANGUAGE_KEY);
  }

  /**
   * Check parameter for the main class name. This property is optional.
   * However, it is strongly recommended to provide a value, because
   * otherwise "Main" is used as default.
   */
  private void checkMainClassName()
  {
    String className = this.properties.getProperty(MAIN_CLASS_NAME_KEY, "Main");

    // Capitalize first letter of class name
    if (null != className)
    {
      this.properties.setProperty(MAIN_CLASS_NAME_KEY,
              className.substring(0, 1).toUpperCase() + className.substring(1, className.length()));
    }
  }

  /**
   * Check parameter for XML framework. This property is optional,
   * because it only applies to the Java type generator. In case
   * that no framework name is provided, the Simple XML library is
   * used as default.
   */
  private void checkXMLFramework()
  {
    String xmlFramework = this.properties.getProperty(XML_FRAMEWORK_KEY);

    // Use Simple as default XML library
    if (!xmlFramework.equals("Simple") && !xmlFramework.equals("XStream") && !xmlFramework.equals("JAXB"))
    {
      this.properties.setProperty(XML_FRAMEWORK_KEY, "Simple");
    }
  }

  /**
   * Check parameter for the package name. This property is optional.
   * However, it is strongly recommended to provide a value, because
   * otherwise "fabric.package.default" is used as default.
   */
  private void checkPackageName()
  {
    String packageName = this.properties.getProperty(PACKAGE_NAME_KEY, "fabric.package.default");

    // Convert package name to lower case
    if (null != packageName)
    {
      this.properties.setProperty(PACKAGE_NAME_KEY, packageName.toLowerCase());
    }
  }

  /**
   * Check parameter for EXI library. This property is optional,
   * because it only applies to the EXI code generator for Java.
   * In case that no library name is provided, the EXIficient
   * framework is used as default.
   */
  private void checkEXILibrary()
  {
    String xmlFramework = this.properties.getProperty(EXI_LIBRARY_KEY);

    // Use EXIficient as default EXI library
    if (!xmlFramework.equals("EXIficient") && !xmlFramework.equals("OpenEXI"))
    {
      this.properties.setProperty(EXI_LIBRARY_KEY, "EXIficient");
    }
  }
}
