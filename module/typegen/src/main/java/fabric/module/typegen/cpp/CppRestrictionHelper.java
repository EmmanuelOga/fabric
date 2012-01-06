/** 06.01.2012 17:56 */
package fabric.module.typegen.cpp;

import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.exceptions.FabricTypeGenException;

/**
 * This helper class contains various static methods to create
 * C++ code that checks XML schema restrictions. These functions
 * were externalized, simply to increase maintainability of the
 * CppTypeGen class.
 *
 * @author reichart, seidel
 */
public class CppRestrictionHelper
{
  /**
   * Private constructor, because all methods of this class are static.
   */
  private CppRestrictionHelper()
  {
    // Empty implementation
  }

  /**
   * Helper method to create a string that checks a boolean expression
   * and throws an exception with predefined message on failure.
   *
   * This function is used to add parameter and restriction checks
   * to setter methods.
   *
   * @param expression Boolean expression for if-statement
   * @param message Error message to show in exception
   * @param comment Text to add as comment
   *
   * @return String with parameter check code
   */
  public static String createCheckCode(final String expression, final String message, final String comment)
  {
    String result = "";

    // Add comment, if any text is set
    if (!("").equals(comment))
    {
      result += String.format("// %s\n", comment);
    }

    result += String.format("if (%s)", expression);
    result += "\n{";
    result += String.format("\n\tthrow \"%s\";\n", message);
    result += "\n}\n\n";

    return result;
  }

  /**
   * Helper method for check code creation. This function is provided
   * for convenience. A call to it is equivalent to calling
   * createCheckCode(expression, message, "").
   *
   * @param expression Boolean expression for if-statement
   * @param message Error message to show in exception
   *
   * @return String with parameter check code
   */
  public static String createCheckCode(final String expression, final String message)
  {
    return CppRestrictionHelper.createCheckCode(expression, message, "");
  }

  /**
   * Helper method to create a string that matches a member variable
   * against a predefined regular expression. If the check fails or
   * the XSD pattern is not compatible with the utilized regular
   * expression language, an exception will be raised.
   *
   * @param memberName Name of member variable to check
   * @param pattern XSD regular expression for pattern matching
   * @param message Error message to show in exception
   *
   * @return String with pattern check code
   */
  public static String createPatternCheckCode(final String memberName, final String pattern, final String message)
  {
    // TODO: Add support for Boost.Regex library: www.boost.org/doc/libs/release/libs/regex

    throw new UnsupportedOperationException("Regular expressions are not supported yet.");
  }

  /**
   * Helper method to create a string that enforces the 'whiteSpace'
   * restriction on the given member variable. Possible values for
   * 'whiteSpace' are 'preserve', 'replace' and 'collapse'.
   *
   * Value 'preserve' means that the value of the member variable
   * remains unchanged. Value 'replace' means that all whitespace
   * characters (line feeds, tabs, spaces and carriage returns)
   * will be replaced by spaces. Value 'collapse' means that multiple
   * whitespace characters will be replaced with a single space
   * plus leading and tailing whitespace characters will be trimmed.
   *
   * @param memberName Name of member variable to check
   * @param whiteSpace Either 'preserve', 'replace' or 'collapse'
   *
   * @return String that enforces 'whiteSpace' restriction
   *
   * @throws Exception Invalid value for 'whiteSpace' provided
   */
  public static String createWhiteSpaceCheckCode(final String memberName, final String whiteSpace) throws Exception
  {
    String result = "";
    String message = "// Enforce '%s' for the 'whiteSpace' restriction\n";

    // Preserve whitespace characters, i.e. leave value unchanged
    if (("preserve").equals(whiteSpace))
    {
      result += String.format(message, "preserve");
      result += String.format("// Nothing to do: %s = %s;", memberName, memberName);
    }
    // Replace all whitespace characters with spaces
    else if (("replace").equals(whiteSpace))
    {
      result += String.format(message, "replace");
      result += String.format("\n%s::replace(%s);", CppUtilHelper.FILE_NAME, memberName);
    }
    // Replace multiple whitespace characters with single space and trim
    else if (("collapse").equals(whiteSpace))
    {
      result += String.format(message, "collapse");
      result += String.format("\n%s::collapse(%s);", CppUtilHelper.FILE_NAME, memberName);
    }
    // Value of 'whiteSpace' was invalid
    else
    {
      throw new FabricTypeGenException(String.format("Unknown value '%s' for the 'whiteSpace' " +
              "restriction on member variable '%s'.", whiteSpace, memberName));
    }

    result += "\n\n";

    return result;
  }

  /**
   * Helper method to create code that checks the 'totalDigits' and/or
   * 'fractionDigits' restriction.
   *
   * @param member Member variable for digits checking
   *
   * @return String with digits check code
   */
  public static String createDigitsCheckCode(final AttributeContainer.RestrictedElementBase member)
  {
    String result = "";
    String message = "Restriction '%s' violated for member variable '%s'.";
    String comment = "// Check the '%s' restriction\n";

    // Member variable is of type char*
    if (("char*").equals(member.type))
    {
      // Member variable is fraction digits restricted
      if (member.isFractionDigitsRestricted())
      {
        result += String.format(comment, "fractionDigits");
        result += CppRestrictionHelper.createCheckCode(
                String.format("%s::fraction_digits(%s) > %d", CppUtilHelper.FILE_NAME, member.name, Long.parseLong(member.restrictions.fractionDigits)),
                String.format(message, "fractionDigits", member.name));
      }

      // Member variable is total digits restricted
      if (member.isTotalDigitsRestricted())
      {
        result += String.format(comment, "totalDigits");
        result += CppRestrictionHelper.createCheckCode(
                String.format("%s::total_digits(%s) > %d", CppUtilHelper.FILE_NAME, member.name, Long.parseLong(member.restrictions.totalDigits)),
                String.format(message, "totalDigits", member.name));
      }
    }
    // Member variable is of integer type (e.g. int64, uint64, int32, uint32, int16, uint16, int8 or uint8)
    else
    {
      // Member variable is total digits restricted
      if (member.isTotalDigitsRestricted())
      {
        result += String.format(comment, "totalDigits");
        result += "unsigned int totalDigits = 0;";
        result += "\ndo";
        result += "\n\t++totalDigits;";
        result += String.format("\nwhile(%s /= 10);\n\n", member.name);

        result += CppRestrictionHelper.createCheckCode(
                String.format("totalDigits > %d", Long.parseLong(member.restrictions.totalDigits)),
                String.format(message, "totalDigits", member.name));
      }
    }

    return result;
  }

  /**
   * Build expression to check the 'length' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String lengthExpression(final AttributeContainer.RestrictedElementBase member)
  {
    String result = "";

    // Member variable is of type char*
    if (("char*").equals(member.type))
    {
      result = String.format("%s::my_strlen(%s) != %d",
              CppUtilHelper.FILE_NAME, member.name, Long.parseLong(member.restrictions.length));
    }
    // Member variable is of type hexBinary or base64Binary
    else if (("xs_hexBinary_t").equals(member.type) || ("xs_base64Binary_t").equals(member.type))
    {
      result = String.format("%s.length != %d",
              member.name, Long.parseLong(member.restrictions.length));
    }
    // Member variable is of type qName or NOTATION
    else if (("xs_qName_t").equals(member.type) || ("xs_notation_t").equals(member.type))
    {
      result = String.format("%s::my_strlen(%s.localPart) + %s::my_strlen(%s.namespaceURI) != %d",
              CppUtilHelper.FILE_NAME, member.name,
              CppUtilHelper.FILE_NAME, member.name,
              Long.parseLong(member.restrictions.length));
    }

    return result;
  }

  /**
   * Build expression to check the 'minLength' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String minLengthExpression(final AttributeContainer.RestrictedElementBase member)
  {
    String result = "";

    // Member variable is of type char*
    if (("char*").equals(member.type))
    {
      result = String.format("%s::my_strlen(%s) < %d",
              CppUtilHelper.FILE_NAME, member.name, Long.parseLong(member.restrictions.minLength));
    }
    // Member variable is of type hexBinary or base64Binary
    else if (("xs_hexBinary_t").equals(member.type) || ("xs_base64Binary_t").equals(member.type))
    {
      result = String.format("%s.length < %d",
              member.name, Long.parseLong(member.restrictions.length));
    }
    // Member variable is of type qName or NOTATION
    else if (("xs_qName_t").equals(member.type) || ("xs_notation_t").equals(member.type))
    {
      result = String.format("%s::my_strlen(%s.localPart) + %s::my_strlen(%s.namespaceURI) < %d",
              CppUtilHelper.FILE_NAME, member.name,
              CppUtilHelper.FILE_NAME, member.name,
              Long.parseLong(member.restrictions.length));
    }

    return result;
  }

  /**
   * Build expression to check the 'maxLength' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String maxLengthExpression(final AttributeContainer.RestrictedElementBase member)
  {
    String result = "";

    // Member variable is of type char*
    if (("char*").equals(member.type))
    {
      result = String.format("%s::my_strlen(%s) > %d",
              CppUtilHelper.FILE_NAME, member.name, Long.parseLong(member.restrictions.maxLength));
    }
    // Member variable is of type hexBinary or base64Binary
    else if (("xs_hexBinary_t").equals(member.type) || ("xs_base64Binary_t").equals(member.type))
    {
      result = String.format("%s.length > %d",
              member.name, Long.parseLong(member.restrictions.length));
    }
    // Member variable is of type qName or NOTATION
    else if (("xs_qName_t").equals(member.type) || ("xs_notation_t").equals(member.type))
    {
      result = String.format("%s::my_strlen(%s.localPart) + %s::my_strlen(%s.namespaceURI) > %d",
              CppUtilHelper.FILE_NAME, member.name,
              CppUtilHelper.FILE_NAME, member.name,
              Long.parseLong(member.restrictions.length));
    }

    return result;
  }

  /**
   * Build expression to check the 'minInclusive' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String minInclusiveExpression(final AttributeContainer.RestrictedElementBase member)
  {
    String result = "";

    // Member variable is of type char*
    if (("char*").equals(member.type))
    {
      result = String.format("%s::compare(%s, \"%s\") < 0",
              CppUtilHelper.FILE_NAME, member.name, member.restrictions.minInclusive);
    }
    else
    {
      result = String.format("%s < %d", member.name, Long.parseLong(member.restrictions.minInclusive));
    }

    return result;
  }

  /**
   * Build expression to check the 'maxInclusive' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String maxInclusiveExpression(final AttributeContainer.RestrictedElementBase member)
  {
    String result = "";

    // Member variable is of type char*
    if (("char*").equals(member.type))
    {
      result = String.format("%s::compare(%s, \"%s\") > 0",
              CppUtilHelper.FILE_NAME, member.name, member.restrictions.maxInclusive);
    }
    else
    {
      result = String.format("%s > %d", member.name, Long.parseLong(member.restrictions.maxInclusive));
    }

    return result;
  }

  /**
   * Build expression to check the 'minExclusive' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String minExclusiveExpression(final AttributeContainer.RestrictedElementBase member)
  {
    String result = "";

    // Member variable is of type char*
    if (("char*").equals(member.type))
    {
      result = String.format("%s::compare(%s, \"%s\") <= 0",
              CppUtilHelper.FILE_NAME, member.name, member.restrictions.minExclusive);
    }
    else
    {
      result = String.format("%s <= %d", member.name, Long.parseLong(member.restrictions.minExclusive));
    }

    return result;
  }

  /**
   * Build expression to check the 'maxExclusive' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String maxExclusiveExpression(final AttributeContainer.RestrictedElementBase member)
  {
    String result = "";

    // Member variable is of type char*
    if (("char*").equals(member.type))
    {
      result = String.format("%s::compare(%s, \"%s\") >= 0",
              CppUtilHelper.FILE_NAME, member.name, member.restrictions.maxExclusive);
    }
    else
    {
      result = String.format("%s >= %d", member.name, Long.parseLong(member.restrictions.maxExclusive));
    }

    return result;
  }
}
