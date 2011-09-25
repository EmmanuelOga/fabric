package fabric.module.typegen.java;

import org.junit.Test;
import static org.junit.Assert.*;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JSourceFileImpl;

import fabric.module.typegen.AttributeContainer;

/**
 * Unit test for AttributeContainer class and Builder.
 *
 * @author seidel
 */
public class AttributeContainerTest
{
  /** Constant with initial container name */
  private static final String CONTAINER_NAME = "Car";

  /** Constant with new container name */
  private static final String NEW_CONTAINER_NAME = "NewCar";

  /**
   * Test outer AttributeContainer class.
   */
  @Test(timeout = 1000)
  public void testAttributeContainer()
  {
    // Create attribute container
    AttributeContainer carContainer = AttributeContainerTest.createAttributeContainer();

    // Check getDefaultInstance()
    assertNotNull("DefaultInstance must not be null.", AttributeContainer.getDefaultInstance());

    // Check parameterless constructor
    assertNotNull("Attribute container from constructor must not be null.", AttributeContainer.newBuilder());

    // Check prototyped constructor
    assertNotNull("Attribute container created from prototype must not be null.", AttributeContainer.newBuilder(carContainer));

    // Check toBuilder()
    assertNotNull("Builder created from container must not be null.", carContainer.toBuilder());

    // Check getName() from container
    assertEquals("Container name must match initial value.", CONTAINER_NAME, carContainer.getName());
  }

  /**
   * Test inner Builder class.
   */
  @Test(timeout = 1000)
  public void testBuilder()
  {
    // Create attribute container
    AttributeContainer carContainer = AttributeContainerTest.createAttributeContainer();

    // Check clear()
    AttributeContainer carContainerClear = AttributeContainer.newBuilder(carContainer).setName(NEW_CONTAINER_NAME).build();
    assertEquals("Container name must match new value.", NEW_CONTAINER_NAME, carContainerClear.getName());
    assertEquals("Cleared builder name must match initial value.", AttributeContainer.getDefaultContainerName(), AttributeContainer.newBuilder(carContainerClear).clear().build().getName());

    // Check clone()
    AttributeContainer.Builder clonedBuilder = carContainer.toBuilder().clone();
    assertNotNull("Cloned builder must not be null.", clonedBuilder);
    assertEquals("Clone name must match initial value.", CONTAINER_NAME, clonedBuilder.build().getName());

    // Check build()
    assertNotNull("Built attribute container must not be null.", clonedBuilder.build());

    // Check mergeWith()
    AttributeContainer carContainerOtherName = AttributeContainer.newBuilder(carContainer).setName(NEW_CONTAINER_NAME).build();
    AttributeContainer carContainerMerged = AttributeContainer.newBuilder(carContainer).mergeWith(carContainerOtherName).build();
    assertNotNull("Merged builder must not be null.", carContainerMerged);
    assertEquals("Merged container name must match new value.", NEW_CONTAINER_NAME, carContainerMerged.toBuilder().getName());

    // Check getName()
    assertEquals("Builder name must match initial value.", CONTAINER_NAME, carContainer.toBuilder().getName());

    // Check setName()
    AttributeContainer carContainerNewName = AttributeContainer.newBuilder(carContainer).setName(NEW_CONTAINER_NAME).build();
    assertEquals("Builder name must match new value.", NEW_CONTAINER_NAME, carContainerNewName.getName());
  }

  /**
   * Test adding XML attributes, XML elements and XML element arrays
   * to AttributeContainer class (through inner Builder class).
   */
  @Test(timeout = 1000)
  public void testMemberVariables()
  {
    // Create builder
    AttributeContainer.Builder testBuilder = AttributeContainer.newBuilder();

    // Check addElement() with two and three parameters
    testBuilder = testBuilder.clear().addElement("int", "foo");
    assertTrue("Container must contain one attribute.", testBuilder.build().getMembers().size() == 1);
    assertTrue("Element 'foo' must be instance of AttributeContainer.Element.", testBuilder.build().getMembers().get("foo") instanceof AttributeContainer.Element);

    testBuilder = testBuilder.clear().addElement("int", "bar", "5");
    assertTrue("Container must contain one attribute.", testBuilder.build().getMembers().size() == 1);
    assertTrue("Element 'bar' must be instance of AttributeContainer.Element.", testBuilder.build().getMembers().get("bar") instanceof AttributeContainer.Element);

    // Check duplicate addElement()
    testBuilder = testBuilder.clear().addElement("int", "foo").addElement("String", "foo");
    assertTrue("Container must not allow duplicate attribute names.", testBuilder.build().getMembers().size() == 1);

    // Check unbounded addElementArray()
    testBuilder = testBuilder.clear().addElementArray("int", "numbers");
    assertTrue("Container must contain one attribute array.", testBuilder.build().getMembers().size() == 1);
    assertTrue("Element 'numbers' must be instance of AttributeContainer.ElementArray.", testBuilder.build().getMembers().get("numbers") instanceof AttributeContainer.ElementArray);
    assertTrue("Attribute array must be unbounded.", ((AttributeContainer.ElementArray)(testBuilder.build().getMembers().get("numbers"))).maxSize == Integer.MAX_VALUE);

    // Check max bounded addElementArray()
    testBuilder = testBuilder.clear().addElementArray("String", "names", 5);
    assertTrue("Container must contain one attribute array.", testBuilder.build().getMembers().size() == 1);
    assertTrue("Element 'names' must be instance of AttributeContainer.ElementArray.", testBuilder.build().getMembers().get("names") instanceof AttributeContainer.ElementArray);
    assertTrue("Attribute array must be bounded to a size of 5.", ((AttributeContainer.ElementArray)(testBuilder.build().getMembers().get("names"))).maxSize == 5);

    // Check min and max bounded addElementArray()
    testBuilder = testBuilder.clear().addElementArray("String", "addresses", 1, 5);
    assertTrue("Container must contain one attribute array.", testBuilder.build().getMembers().size() == 1);
    assertTrue("Element 'addresses' must be instance of AttributeContainer.ElementArray.", testBuilder.build().getMembers().get("addresses") instanceof AttributeContainer.ElementArray);
    assertTrue("Attribute array must be bound to a size of [1..5].",
            ((AttributeContainer.ElementArray)(testBuilder.build().getMembers().get("addresses"))).minSize == 1 &&
            ((AttributeContainer.ElementArray)(testBuilder.build().getMembers().get("addresses"))).maxSize == 5);

    // Check deleteMember()
    testBuilder = testBuilder.clear().addElement("int", "foo").addElement("boolean", "bar");
    assertNotNull("Container must contain an attribute named 'foo'.", testBuilder.build().getMembers().get("foo"));
    assertNotNull("Container must contain an attribute named 'bar'.", testBuilder.build().getMembers().get("bar"));
    testBuilder = testBuilder.deleteMember("foo");
    assertNull("Container must not contain an attribute named 'foo' anymore.", testBuilder.build().getMembers().get("foo"));
    assertNotNull("Container must still contain an attribute named 'bar'.", testBuilder.build().getMembers().get("bar"));

    // Check duplicate deleteMember()
    testBuilder = testBuilder.clear().addElement("int", "foobar");
    assertNotNull("Container must contain an attribute named 'foobar'.", testBuilder.build().getMembers().get("foobar"));
    testBuilder = testBuilder.deleteMember("foobar");
    assertNull("Container must not contain an attribute named 'foobar' anymore.", testBuilder.build().getMembers().get("foobar"));
    testBuilder = testBuilder.deleteMember("foobar");
    assertNull("Container must still not contain an attribute named 'foobar'.", testBuilder.build().getMembers().get("foobar"));
  }

  /**
   * Test adding, reading and cloning restrictions on elements
   * in the AttributeContainer class.
   */
  @Test(timeout = 1000)
  public void testRestrictions()
  {
    // Create builder
    AttributeContainer.Builder testBuilder = AttributeContainer.newBuilder();
    
    // Check addElement() with empty restrictions
    testBuilder = testBuilder.clear().addElement("String", "foo", new AttributeContainer.Restriction());
    AttributeContainer.Restriction restrictions = ((AttributeContainer.Element)testBuilder.getMembers().get("foo")).restrictions;
    assertNull("Restriction on 'length' must not be set.", restrictions.length);
    assertNull("Restriction on 'minLength' must not be set.", restrictions.minLength);
    assertNull("Restriction on 'maxLength' must not be set.", restrictions.maxLength);
    assertNull("Restriction on 'minInclusive' must not be set.", restrictions.minInclusive);
    assertNull("Restriction on 'maxInclusive' must not be set.", restrictions.maxInclusive);
    assertNull("Restriction on 'minExclusive' must not be set.", restrictions.minExclusive);
    assertNull("Restriction on 'maxExclusive' must not be set.", restrictions.maxExclusive);

    // Check addElement() with restrictions on content length
    testBuilder = testBuilder.clear().addElement("String", "bar", new AttributeContainer.Restriction("1", "2", "3"));
    restrictions = ((AttributeContainer.Element)testBuilder.getMembers().get("bar")).restrictions;
    assertEquals("Restriction on 'length' must be '1'.", "1", restrictions.length);
    assertEquals("Restriction on 'minLength' must be '2'.", "2", restrictions.minLength);
    assertEquals("Restriction on 'maxLength' must be '3'.", "3", restrictions.maxLength);
    assertNull("Restrictions on value boundary must not be set.", restrictions.minInclusive);

    // Check addElement() with restrictions on element value (including boundary)
    testBuilder = testBuilder.clear().addElement("String", "inclusive", new AttributeContainer.Restriction("-5", "5", true));
    restrictions = ((AttributeContainer.Element)testBuilder.getMembers().get("inclusive")).restrictions;
    assertNull("Restriction on 'length' must not be set.", restrictions.length);
    assertEquals("Restriction on 'minInclusive' must be '-5'.", "-5", restrictions.minInclusive);
    assertEquals("Restriction on 'maxInclusive' must be '5'.", "5", restrictions.maxInclusive);
    assertNull("Restriction on 'minExclusive' must not be set.", restrictions.minExclusive);
    assertNull("Restriction on 'maxExclusive' must not be set.", restrictions.maxExclusive);
    
    // Check addElement() with restrictions on element value (excluding boundary)
    testBuilder = testBuilder.clear().addElement("String", "exclusive", new AttributeContainer.Restriction("-3", "7", false));
    restrictions = ((AttributeContainer.Element)testBuilder.getMembers().get("exclusive")).restrictions;
    assertNull("Restriction on 'length' must not be set.", restrictions.length);
    assertNull("Restriction on 'minInclusive' must not be set.", restrictions.minInclusive);
    assertNull("Restriction on 'maxInclusive' must not be set.", restrictions.maxInclusive);
    assertEquals("Restriction on 'minExclusive' must be '-3'.", "-3", restrictions.minExclusive);
    assertEquals("Restriction on 'maxExclusive' must be '7'.", "7", restrictions.maxExclusive);

    // Check cloning of Restriction objects
    restrictions = new AttributeContainer.Restriction("5", null, null);
    AttributeContainer.Restriction clonedRestrictions = restrictions.clone();
    assertEquals("Restrictions in cloned object must match original.", restrictions.length, clonedRestrictions.length);
    assertFalse("Cloned object reference must not equal original object reference.", clonedRestrictions == restrictions);
    clonedRestrictions.length = "10";
    assertFalse("Restrictions in original and cloned object must not match anymore.", restrictions.length.equals(clonedRestrictions.length));
  }

  /**
   * Test class generation with ClassGenerationStrategy interface
   * and AnnotationMapper implementation for Java.
   */
  @Test(timeout = 1000)
  public void testClassGeneration() throws Exception
  {
    // Create attribute container
    AttributeContainer carContainer = AttributeContainerTest.createAttributeContainer();

    // Create JClass object from AttributeContainer
    AnnotationMapper xmlMapper = new AnnotationMapper("Simple");
    JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(xmlMapper);
    JClass jClassObject = (JClass)carContainer.asClassObject(strategy);

    // Check asClassObject()
    assertTrue("Method must return instance of 'java.lang.String'.", jClassObject.toString() instanceof String);
    assertFalse("Class content must not be empty string.", ("").equals(jClassObject.toString()));

    // Add JClass to JSourceFile
    JSourceFileImpl jsf = new JSourceFileImpl("test.de", "testFile.java");
    jsf.add(jClassObject);

    // Add required Java imports to JSourceFile
    for (String requiredImport: strategy.getRequiredDependencies())
    {
      jsf.addImport(requiredImport);
    }

    // Output JSourceFile for debug reasons
    System.out.println(jsf.toString());
  }

  /**
   * Helper method for test-container creation.
   *
   * @return AttributeContainer object
   */
  private static AttributeContainer createAttributeContainer()
  {
    AttributeContainer result = AttributeContainer.newBuilder()
                                  .setName(CONTAINER_NAME)
                                  .addAttribute("String", "manufacturer", "Audi")
                                  .addAttribute("String", "model", "TT")
                                  .addElement("String", "color", "red")
                                  .addConstantElement("int", "maxSpeed", "220")
                                  .addElementArray("TrunkItem", "trunkItems")
                                  .addElementArray("String", "passengers", 1, 2)
                                  .build();

    return result;
  }
}
