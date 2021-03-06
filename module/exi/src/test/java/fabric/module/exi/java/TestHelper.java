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
package fabric.module.exi.java;

import java.util.List;

import static org.junit.Assert.*;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

/**
 * Helper class for unit tests in the Fabric EXI module.
 *
 * @author seidel
 */
public class TestHelper
{
  /**
   * Protected helper method to check the existence of a method with the
   * given properties. Visibility modifiers must be 'public' and 'static'
   * on default. Method must throw exception of type 'Exception'.
   *
   * @param classObject JClass object for checking
   * @param methodName Name of method that the class must contain
   * @param returnValueType Expected type of return value
   * @param argumentType Expected type of method argument
   */
  protected static void checkMethodExistence(final JClass classObject, final String methodName, final String returnValueType, final String argumentType)
  {
    TestHelper.checkMethodExistence(classObject, JModifier.PUBLIC | JModifier.STATIC,
            methodName, returnValueType, argumentType, new String[] { "Exception" });
  }

  /**
   * Protected helper method to check the existence of a method with
   * the given properties.
   *
   * @param classObject JClass object for checking
   * @param modifiers Expected visibility modifiers of method
   * @param methodName Name of method that the class must contain
   * @param returnValueType Expected type of return value
   * @param argumentType Expected type of method argument
   * @param exceptions Field of expected exceptions that method
   * throws or null if method does not throw exceptions at all
   */
  protected static void checkMethodExistence(final JClass classObject, final int modifiers, final String methodName, final String returnValueType, final String argumentType, final String[] exceptions)
  {
    // Check if method exists at all
    List<JMethod> methods = classObject.getJMethodsByName(methodName);
    assertNotNull(String.format("Class must contain method with name '%s'.", methodName), methods);
    assertTrue(String.format("Class must contain at least one method named '%s'.", methodName), methods.size() >= 1);

    // Check type of method parameter
    JMethod method = methods.get(0);
    JMethodSignature signature = method.getSignature();
    assertNotNull("Method signature must not be null.", signature);
    JParameter parameter = signature.getParameters().get(0);
    assertNotNull("Method parameter must not be null.", parameter);
    assertTrue(String.format("Argument must have type '%s'.", argumentType), parameter.typeEquals(argumentType));

    // Check modifiers, return value and throws-statement
    assertEquals("Modifiers of method must be as expected.", modifiers, method.getModifiers());
    assertEquals(String.format("Type of return value must be '%s'.", returnValueType), returnValueType, method.getReturnType());
    if (null != exceptions)
    {
      for (String exception: exceptions)
      {
        assertTrue(String.format("Method must throw exception of type '%s'.", exception), method.containsException(exception));
      }
    }
  }
}
