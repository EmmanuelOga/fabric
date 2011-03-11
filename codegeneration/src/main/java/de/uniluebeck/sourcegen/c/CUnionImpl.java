/**
 * Copyright (c) 2010, Institute of Telematics, University of Luebeck
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

package de.uniluebeck.sourcegen.c;

import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;



class CUnionImpl extends CStructBaseImpl implements CUnion {

	/**
	 * Constructs a new <code>structUnion</code>. Given the parameters
	 * <code>name="foo"</code>, <code>body="int bar;"</code>,
	 * <code>varName=""</code> and <code>isTypeDef=false</code>, the
	 * following code will be generated:<br>
	 * <br>
	 * <code>
	 * structUnion foo<br>
	 * {<br>
	 * &nbsp;&nbsp;int bar;<br>
	 * };<br>
	 * </code><br>
	 * <br>
	 * Given the parameter <code>varName="hello"</code> it will generate:<br>
	 * <br>
	 * <code>
	 * structUnion foo<br>
	 * {<br>
	 * &nbsp;&nbsp;int bar;<br>
	 * } hello;<br>
	 * </code><br>
	 * <br>
	 * Given <code>isTypeDef=true<code> it will generate a <code>typedef</code>
	 * named hello:<br>
	 * <br>
	 * <code>
	 * typedef structUnion foo<br>
	 * {<br>
	 * &nbsp;&nbsp;int bar;<br>
	 * } hello;<br>
	 * </code>
	 * 
	 * @param name the name of the <code>structUnion</code>
	 * @param vars the unions variables
	 * @param varname the variable name the <code>structUnion</code> or the name
	 * of the <code>typedef structUnion</code>
	 * @param typedef true if a <code>typedef</code> should be generated,
	 * false for a <code>structUnion</code>.
	 * @throws CDuplicateException if the variable list contains duplicates
	 * @throws CCodeValidationException if code and identifier validation fails
	 */
	public CUnionImpl(String name, CParam[] vars, String varname,
			boolean typedef) throws CCodeValidationException,
			CDuplicateException {

		super("union", name, vars, varname, typedef);

	}

	@Override
	public void validateFields() throws CCodeValidationException {
		// TODO: implement validation
		// remove SuppressWarnings (!)
	}

}
